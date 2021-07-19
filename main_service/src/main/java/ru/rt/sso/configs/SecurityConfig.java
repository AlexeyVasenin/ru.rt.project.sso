package ru.rt.sso.configs;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.sso.handlers.KeycloakLogoutHandler;
import ru.rt.sso.service.KeycloakOauth2UserService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurer(KeycloakLogoutHandler keycloakLogoutHandler, KeycloakOauth2UserService keycloakOidcUserService) {
        return new WebSecurityConfigurerAdapter() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                http
                        .csrf().disable()
                        .authorizeRequests()
                            .antMatchers("/**").hasRole("REALM-ADMIN")
                            //.antMatchers("/keycloak/users").hasRole("VIEW-USERS") по-другому как-то там...
                            .anyRequest().authenticated()
                            .and()
                        .logout()
                            .addLogoutHandler(keycloakLogoutHandler)
                            .logoutUrl("/logout").logoutSuccessUrl("/")
                            .invalidateHttpSession(true).clearAuthentication(true)
                            .and()
                        .oauth2Login()
                            .userInfoEndpoint()
                            .oidcUserService(keycloakOidcUserService)
                            .and()
                        .defaultSuccessUrl("/", true);
            }
        };
    }

    @Bean
    WebClient webClient(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                        authorizedClientRepository);
        oauth2.setDefaultOAuth2AuthorizedClient(true);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 10)).build();

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .apply(oauth2.oauth2Configuration())
                .build();
    }

    @Bean
    KeycloakOauth2UserService keycloakOidcUserService(OAuth2ClientProperties oauth2ClientProperties) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(oauth2ClientProperties.getProvider().get("keycloak").getJwkSetUri()).build();
        SimpleAuthorityMapper authoritiesMapper = new SimpleAuthorityMapper();
        authoritiesMapper.setConvertToUpperCase(true);
        return new KeycloakOauth2UserService(jwtDecoder, authoritiesMapper);
    }

    @Bean
    KeycloakLogoutHandler keycloakLogoutHandler() {
        return new KeycloakLogoutHandler("JSESSIONID");
    }
}




