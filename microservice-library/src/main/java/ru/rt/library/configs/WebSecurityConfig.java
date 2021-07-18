package ru.rt.library.configs;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.library.handlers.KeycloakLogoutHandler;
import ru.rt.library.services.KeycloakOauth2UserService;

/**
 * @author Alexey Baidin
 * @author Vyacheslav Tretyakov
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurer(KeycloakLogoutHandler keycloakLogoutHandler, KeycloakOauth2UserService keycloakOidcUserService) {
        return new WebSecurityConfigurerAdapter() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                http
                        .authorizeRequests()
                        .antMatchers("/", "/back-channel-logout", "/static/**").permitAll()
                        .antMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                        .and()
                        .logout().addLogoutHandler(keycloakLogoutHandler)
                        .logoutUrl("/logout").logoutSuccessUrl("/")
                        .invalidateHttpSession(true).clearAuthentication(true)
                        //.deleteCookies("JSESSIONID")
                        .and().oauth2Login().userInfoEndpoint().oidcUserService(keycloakOidcUserService);
            }
        };
    }

    //todo описание
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

    /**
     * Бин службы получения пользовательских атрибутов, регистрируемой в конфигурации oidcUserService().
     * <p>
     *
     * @param oauth2ClientProperties свойства OAuth клиента
     * @return объект {@link KeycloakOauth2UserService}, расширяющий {@link OidcUserService}
     */
    @Bean
    KeycloakOauth2UserService keycloakOidcUserService(OAuth2ClientProperties oauth2ClientProperties) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(oauth2ClientProperties.getProvider().get("keycloak").getJwkSetUri()).build();
        SimpleAuthorityMapper authoritiesMapper = new SimpleAuthorityMapper();
        authoritiesMapper.setConvertToUpperCase(true);
        return new KeycloakOauth2UserService(jwtDecoder, authoritiesMapper);
    }

    //todo описание
    @Bean
    KeycloakLogoutHandler keycloakLogoutHandler() {
        return new KeycloakLogoutHandler("JSESSIONID");
    }
}


