package ru.rt.music.services;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Расширение {@link OidcUserInfo} службы для пользовательских атрибутов Конечного пользователя из Конечной точки UserInfo.
 */
@RequiredArgsConstructor
public class KeycloakOauth2UserService extends OidcUserService {

    private final OAuth2Error INVALID_REQUEST = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST);

    private final JwtDecoder jwtDecoder;

    private final GrantedAuthoritiesMapper authoritiesMapper;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser user = super.loadUser(userRequest);

        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.addAll(user.getAuthorities());
        authorities.addAll(extractKeycloakAuthorities(userRequest));

        //OidcUserInfo userInfo = user.getUserInfo();

        return new DefaultOidcUser(authorities, userRequest.getIdToken(), user.getUserInfo(), "preferred_username");
    }

    /**
     * Извлекает пользовательские Authorities из запроса {@link OidcUserRequest}
     * @param userRequest пользовательский запрос
     * @return Коллекция {@link GrantedAuthority}
     */
    private Collection<? extends GrantedAuthority> extractKeycloakAuthorities(OidcUserRequest userRequest) {

        Jwt token = parseJwt(userRequest.getAccessToken().getTokenValue());

        String clientName = userRequest.getClientRegistration().getClientName();

        JSONObject resourceAccess = token.getClaim("resource_access");

        JSONObject clientAccess = (JSONObject) resourceAccess.get(clientName);

        JSONArray clientRoles = (JSONArray) clientAccess.get("roles");

        Collection<? extends GrantedAuthority> authorities = clientRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return authoritiesMapper.mapAuthorities(authorities);
    }

    /**
     * Декодирует AccessToken в объект {@link Jwt}
     * @param accessTokenValue токен
     * @return декодированный объект {@link Jwt}
     */
    private Jwt parseJwt(String accessTokenValue) {
        try {
            // Token is already verified by spring security infrastructure
            return jwtDecoder.decode(accessTokenValue);
        } catch (JwtException e) {
            throw new OAuth2AuthenticationException(INVALID_REQUEST, e);
        }
    }
}
