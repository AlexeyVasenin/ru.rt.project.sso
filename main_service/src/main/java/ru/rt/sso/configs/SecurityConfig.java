package ru.rt.sso.configs;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.sso.handlers.KeycloakLogoutHandler;
import ru.rt.sso.service.KeycloakOauth2UserService;

/**
 * Класс конфигурации для настройки {@link WebSecurity} и {@link HttpSecurity}.
 *
 * @author Alexey Baidin
 * @author Alexey Vasenin
 * @author Vyacheslav Tretyakov
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    /**
     * Бин пользовательской настройки конфигурации Web-security и авторизации через OAuth.
     * <p>
     *
     * @param keycloakLogoutHandler autowired бин обработчика нативного и keycloak logout-ов
     * @param keycloakOidcUserService autowired бин службы получения пользовательских атрибутов
     * @return бин конфигурации web-security
     */
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
                            .logout().addLogoutHandler(keycloakLogoutHandler)
                            .logoutUrl("/logout").logoutSuccessUrl("/")
                            .invalidateHttpSession(true).clearAuthentication(true)
                            .and()
                            .oauth2Login().userInfoEndpoint().oidcUserService(keycloakOidcUserService)
                            .and().defaultSuccessUrl("/", true);
            }
        };
    }

    /**
     * Бин объекта {@link WebClient} - интерфейса модуля Spring Web Reactive, являющийся ключевой точкой входа и обработки web-запросов.
     * <p>
     *
     * @param clientRegistrationRepository autowired бин OAuth 2.0 репозитория регистрации клиентов
     * @param authorizedClientRepository autowired бин репозитория авторизации клиентов
     * @return собранный в билдере бин объекта {@link WebClient}, который расширяет максимальный размер буфера для передачи HTTP-запросов
     * за счет созданных {@link ExchangeStrategies} и применяет базовую OAuth2-конфигурацию для пробрасывания access-токена при выполнении запросов
     */
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

    /**
     * Бин пользовательского обработчика события logout на стороне приложения и Keycloak-а.
     * <p>
     *
     * @return объект {@link KeycloakLogoutHandler}, в который передается список с названиями кук на удаление
     */
    @Bean
    KeycloakLogoutHandler keycloakLogoutHandler() {
        return new KeycloakLogoutHandler("JSESSIONID");
    }
}




