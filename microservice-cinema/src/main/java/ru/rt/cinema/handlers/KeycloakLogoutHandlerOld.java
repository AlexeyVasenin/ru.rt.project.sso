package ru.rt.cinema.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Хендлер, выполняющий локальный выход extends {@link SecurityContextLogoutHandler}, а также закрытие сессий в keycloak
 * <p>
 * @author Vyacheslav Tretykov
 */
@Deprecated
@RequiredArgsConstructor
public class KeycloakLogoutHandlerOld extends SecurityContextLogoutHandler {

    private final RestTemplate restTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //локальный логаут
        super.logout(request, response, authentication);
        //закрытие сессий в keycloak
        propagateLogoutToKeycloak((OidcUser) authentication.getPrincipal());
    }

    /**
     * Отправляет запрос в keycloak на эндпоинт /protocol/openid-connect/logout, завершая сессии
     * @param user текущий пользователь сессии
     */
    private void propagateLogoutToKeycloak(OidcUser user) {

        String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";

        UriComponentsBuilder builder = UriComponentsBuilder //
                .fromUriString(endSessionEndpoint) //
                .queryParam("id_token_hint", user.getIdToken().getTokenValue());

        ResponseEntity<String> logoutResponse = restTemplate.getForEntity(builder.toUriString(), String.class);
        if (logoutResponse.getStatusCode().is2xxSuccessful()) {
            System.out.println("Successfully logged out in Keycloak");
        } else {
            System.out.println("Could not propagate logout to Keycloak");
        }
    }
}
