package ru.rt.library.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.rt.library.domain.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurer(KeycloakLogoutHandler keycloakLogoutHandler) {
        return new WebSecurityConfigurerAdapter() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                http
                        .authorizeRequests()
                            .antMatchers("/")
                            .permitAll()
                            .anyRequest().authenticated()
                            .and()
                        .oauth2Login()
                            .and()
                        .logout()
                            .addLogoutHandler(keycloakLogoutHandler)
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .deleteCookies("JSESSIONID");
            }
        };
    }

    @Bean
    WebClient webClient(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                        authorizedClientRepository);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
    }

    @Bean
    KeycloakLogoutHandler keycloakLogoutHandler() {
        return new KeycloakLogoutHandler();
    }
}

class KeycloakLogoutHandler extends SecurityContextLogoutHandler {

    private final Logger logger = LoggerFactory.getLogger(KeycloakLogoutHandler.class);

    @Autowired
    private WebClient webClient;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);
//        logoutFromKeyCloak(authentication);
        logoutFromKeyCloakREST(authentication);
    }

    private void logoutFromKeyCloak(Authentication authentication) {
        OidcUser oidcUser = (OidcUser)authentication.getPrincipal();
        URI logoutUri = UriComponentsBuilder
                .fromUriString(oidcUser.getIssuer()+"/protocol/openid-connect/logout")
                .queryParam("id_token_hint", oidcUser.getIdToken().getTokenValue()).build().toUri();

        ResponseEntity<Void> response = this.webClient
                .get()
                .uri(logoutUri)
                .retrieve()
                .toBodilessEntity()
                .block();

        if (response != null) {
            logger.info("Log out response: " + response.getStatusCode());
        }
    }

    private void logoutFromKeyCloakREST(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String s = oidcUser.getIssuer().toString();
        String beginUri = s.substring(0, s.indexOf("/realms")) + "/admin" + s.substring(s.indexOf("/realms"));
        String userId = oidcUser.getClaim("sub");

        URI accessTokenUri = UriComponentsBuilder
                .fromUriString(s + "/protocol/openid-connect/token")
                .build().toUri();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "tester");
        formData.add("password", "123456");
        formData.add("grant_type", "password");
        formData.add("client_id", "microservice-cinema");
        formData.add("client_secret", "192f9bc9-7617-4b97-a549-92dc40d091be");

        ResponseEntity<Token> respToken = this.webClient
                .post()
                .uri(accessTokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .flatMap(response -> response.toEntity(Token.class))
                .block();

        if (respToken != null) {
            // REST Logout user example: http://127.0.0.1:8180/auth/admin/realms/heroes/users/83c72e88-7ac9-4fc7-a7fb-97736d67d261/logout

            URI logoutURI = UriComponentsBuilder
                    .fromUriString(beginUri + "/users/" + userId + "/logout")
                    .build().toUri();

            ResponseEntity<Void> response = this.webClient
                    .post()
                    .uri(logoutURI)
                    .header("Authorization", "Bearer " + respToken.getBody().accessToken)
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


