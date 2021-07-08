package ru.rt.sso.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@EnableWebSecurity
@Configuration
public class SecurityConfig{

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
                        .logout().addLogoutHandler(keycloakLogoutHandler)
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID");
            }
        };
    }

   /* @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .and()
                .logout().addLogoutHandler(this.keycloakLogoutHandler)
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");
    }*/

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
        return new KeycloakLogoutHandler(new RestTemplate());
    }
}

@RequiredArgsConstructor
class KeycloakLogoutHandler extends SecurityContextLogoutHandler {

    private final RestTemplate restTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
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
            System.out.println("Could not pro\n" +
                    "\n" +
                    "/*class KeycloakLogoutHandler extends SecurityContextLogoutHandler {\n" +
                    "\n" +
                    "    //private Logger logger = LoggerFactory.getLogger(ru.rt.cinema.KeycloakLogoutHandler.class);" +
                    "\n" +
                    "    @Autowired\n" +
                    "    private final WebClient webClient;\n" +
                    "\n" +
                    "    KeycloakLogoutHandler(WebClient webClient) {\n" +
                    "        this.webClient = webClient;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication " +
                    "authentication) {\n" +
                    "        super.logout(request, response, authentication);\n" +
                    "        logoutFromKeyCloak(authentication);\n" +
                    "    }\n" +
                    "\n" +
                    "    private void logoutFromKeyCloak(Authentication authentication) {\n" +
                    "        OidcUser oidcUser = (OidcUser)authentication.getPrincipal();\n" +
                    "        URI logoutUri = UriComponentsBuilder\n" +
                    "                .fromUriString(oidcUser.getIssuer()+\"/protocol/openid-connect/logout\")\n" +
                    "                .queryParam(\"id_token_hint\", oidcUser.getIdToken().getTokenValue()).build()" +
                    ".toUri();\n" +
                    "        ClientResponse response = this.webClient.get().uri(logoutUri).exchange().doOnError" +
                    "(clientResponse -> clientResponse.printStackTrace()).block();\n" +
                    "    //    logger.info(\"Log out response: \"+response.statusCode());\n" +
                    "    }\n" +
                    "}*/\npagate logout to Keycloak");
        }
    }

   /* @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {

        *//**
         * Returning NullAuthenticatedSessionStrategy means app will not remember session
         *//*

        return new NullAuthenticatedSessionStrategy();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider =
                keycloakAuthenticationProvider();

        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());

        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public FilterRegistrationBean<?> keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter) {

        FilterRegistrationBean<?> registrationBean = new FilterRegistrationBean<>(filter);

        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<?> keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter) {

        FilterRegistrationBean<?> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<?> keycloakAuthenticatedActionsFilterBean(
            KeycloakAuthenticatedActionsFilter filter) {

        FilterRegistrationBean<?> registrationBean = new FilterRegistrationBean<>(filter);

        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<?> keycloakSecurityContextRequestFilterBean(
            KeycloakSecurityContextRequestFilter filter) {

        FilterRegistrationBean<?> registrationBean = new FilterRegistrationBean<>(filter);

        registrationBean.setEnabled(false);

        return registrationBean;
    }

    @Bean
    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }*/
}




