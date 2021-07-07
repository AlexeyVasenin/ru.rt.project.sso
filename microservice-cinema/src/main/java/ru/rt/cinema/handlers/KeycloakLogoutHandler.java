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
 * Хендлер для логаута кейклока и локально
 */
@RequiredArgsConstructor
public class KeycloakLogoutHandler extends SecurityContextLogoutHandler {

    private final RestTemplate restTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //локальный логаут
        super.logout(request, response, authentication);

        propagateLogoutToKeycloak((OidcUser) authentication.getPrincipal());
    }

    private void propagateLogoutToKeycloak(OidcUser user) {

        String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";

        UriComponentsBuilder builder = UriComponentsBuilder //
                .fromUriString(endSessionEndpoint) //
                .queryParam("id_token_hint", user.getIdToken().getTokenValue());

        ResponseEntity<String> logoutResponse = restTemplate.getForEntity(builder.toUriString(), String.class);
        if (logoutResponse.getStatusCode().is2xxSuccessful()) {
            System.out.println("Successfulley logged out in Keycloak");
        } else {
            System.out.println("Could not propagate logout to Keycloak");
        }
    }
}

/*class KeycloakLogoutHandler extends SecurityContextLogoutHandler {

    //private Logger logger = LoggerFactory.getLogger(ru.rt.cinema.KeycloakLogoutHandler.class);
    @Autowired
    private final WebClient webClient;

    KeycloakLogoutHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);
        logoutFromKeyCloak(authentication);
    }

    private void logoutFromKeyCloak(Authentication authentication) {
        OidcUser oidcUser = (OidcUser)authentication.getPrincipal();
        URI logoutUri = UriComponentsBuilder
                .fromUriString(oidcUser.getIssuer()+"/protocol/openid-connect/logout")
                .queryParam("id_token_hint", oidcUser.getIdToken().getTokenValue()).build().toUri();
        ClientResponse response = this.webClient.get().uri(logoutUri).exchange().doOnError(clientResponse -> clientResponse.printStackTrace()).block();
    //    logger.info("Log out response: "+response.statusCode());
    }
}*/
