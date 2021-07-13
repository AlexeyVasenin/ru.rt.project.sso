package ru.rt.sso.configs;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                //.antMatcher("/**")
                .authorizeRequests()
                    .antMatchers("/")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                .oauth2Login();
    }

    //@Bean
    //WebClient webClient(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository) {
    //    ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
    //            new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
    //                    authorizedClientRepository);
    //    oauth2.setDefaultOAuth2AuthorizedClient(true);
    //    return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
    //}
}
