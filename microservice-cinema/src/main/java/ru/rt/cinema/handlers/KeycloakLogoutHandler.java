package ru.rt.cinema.handlers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.rt.cinema.domain.Token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Пользовательский класс для обработки события локального logout и отправки запроса на завершение текущей сессии в Keycloak.
 * <p>
 *
 * @author Alexey Baidin
 */
@Slf4j
public class KeycloakLogoutHandler extends SecurityContextLogoutHandler {

    @Value("${application.url}")
    private String applicationURL;

    private final Logger logger = LoggerFactory.getLogger(KeycloakLogoutHandler.class);

    private final List<String> cookies;

    @Autowired
    private WebClient webClient;

    public KeycloakLogoutHandler(String... cookiesToClear) {
        this.cookies = Arrays.asList(cookiesToClear);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // удаление cookies
        response.setContentType("text/html");
        for (String cookieName : cookies) {
            Cookie cookie = new Cookie(cookieName, "");
            String cookiePath = request.getContextPath();
            if (!StringUtils.hasLength(cookiePath)) {
                cookiePath = "/";
            }
            cookie.setPath(cookiePath);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        // нативный logout
        super.logout(request, response, authentication);

        // попытка сделать свой back-channel logout
        if (request.getAttribute("back-channel-logout") == null) {
            logoutFromKeyCloak(authentication);
        } else {
            String issuer = (String) request.getAttribute("issuer");
            String token = (String) request.getAttribute("token");
            backChannelLogoutFromKeycloak(issuer, token);
        }
    }

    /**
     * Метод отправки запроса на завершение сессии текущего пользователя и итеративной отправки запросов к другим
     * зарегистрированным в системе микросервисам для завершения своих клиентских сессий.
     * <p>
     *
     * @param authentication объект токена аутентификации, который хранится в {@link SecurityContext}
     */
    private void logoutFromKeyCloak(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        URI logoutUri = UriComponentsBuilder
                .fromUriString(oidcUser.getIssuer() + "/protocol/openid-connect/logout")
                .queryParam("id_token_hint", oidcUser.getIdToken().getTokenValue()).build().toUri();

        ResponseEntity<Void> response = this.webClient
                .get()
                .uri(logoutUri)
                .retrieve()
                .toBodilessEntity()
                .block();

        if (response != null) {
            logger.info("OIDC log out response: " + response.getStatusCode());
        }
        URI servicesURLsUri = UriComponentsBuilder
                .fromUriString("http://localhost:8761/api/services/urls")
                .build().toUri();

        List<String> servicesURLs = this.webClient
                .get()
                .uri(servicesURLsUri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block();

        if (servicesURLs != null) {
            // удаляем url текущего сервиса
            servicesURLs.remove(this.applicationURL);

            servicesURLs.forEach(URL -> {
                StringBuilder str = new StringBuilder(URL);
                str.append("/back-channel-logout");
                str.append("?issuer=").append(oidcUser.getIssuer());
                str.append("&token=").append(oidcUser.getIdToken().getTokenValue());

                URI serviceLogoutUri = UriComponentsBuilder
                        .fromUriString(str.toString())
                        .build().toUri();

                ResponseEntity<Void> serviceLogoutResponse = this.webClient
                        .get()
                        .uri(serviceLogoutUri)
                        .retrieve()
                        .toBodilessEntity()
                        .block();

                if (serviceLogoutResponse != null) {
                    logger.info("Service logout request to " + URL + " is successful");
                }
            });
        }
    }

    /**
     * Метод, инициализирующийся при получении внешнего запроса, который завершает процесс пользовательского back-channel
     * logout-а: отправляет запрос на завершение свой сессии в Keycloak.
     * <p>
     *
     * @param issuer уникальный идентификатор стороны, которая сгенерировала токен (URL сервера авторизации)
     * @param token валидный access-токен, переданный из сервиса, инициализирующего logout
     */
    private void backChannelLogoutFromKeycloak(String issuer, String token) {
        URI logoutUri = UriComponentsBuilder
                .fromUriString(issuer + "/protocol/openid-connect/logout")
                .queryParam("id_token_hint", token).build().toUri();

        ResponseEntity<Void> response = this.webClient
                .get()
                .uri(logoutUri)
                .retrieve()
                .toBodilessEntity()
                .block();

        if (response != null) {
            logger.info("OIDC back channel log out response: " + response.getStatusCode());
        }
    }

    /**
     * Тестовый метод попытки логаута через запрос к Admin REST API. Не рекоммендуется использовать!
     * <p>
     *
     * @param authentication объект токена аутентификации, который хранится в {@link SecurityContext}
     */
    @Deprecated
    private void logoutFromKeyCloakREST(Authentication authentication) {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String s = oidcUser.getIssuer().toString();
        String sessionId = ((WebAuthenticationDetails) authentication.getDetails()).getSessionId();

        URI accessTokenUri = UriComponentsBuilder
                .fromUriString(s + "/protocol/openid-connect/token")
                .queryParam("client_session_state", sessionId)
                .build().toUri();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        // пока для тестов
        formData.add("username", "tester");
        formData.add("password", "123456");
        formData.add("grant_type", "password");
        formData.add("client_id", "microservice-cinema");
        formData.add("client_secret", "192f9bc9-7617-4b97-a549-92dc40d091be");

        // получаем новый access-токен
        ResponseEntity<Token> respToken = this.webClient
                .post()
                .uri(accessTokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .flatMap(response -> response.toEntity(Token.class))
                .block();

        if (respToken != null) {
            String beginUri = s.substring(0, s.indexOf("/realms")) + "/admin" + s.substring(s.indexOf("/realms"));
            String userId = oidcUser.getClaim("sub");

            URI logoutURI = UriComponentsBuilder
                    .fromUriString(beginUri + "/users/" + userId + "/logout")
                    .build().toUri();

            // user logout
            ResponseEntity<Void> response = this.webClient
                    .post()
                    .uri(logoutURI)
                    .header("Authorization", "Bearer " + Objects.requireNonNull(respToken.getBody()).accessToken)
                    .header("Content-Type", "application/json")
                    .header("Cache-Control", "no-cache")
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            if (response != null) {
                logger.info("Log out response: " + response.getStatusCode());
            }
        }
    }
}
