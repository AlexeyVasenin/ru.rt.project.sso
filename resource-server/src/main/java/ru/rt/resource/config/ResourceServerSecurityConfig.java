package ru.rt.resource.config;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
//todo A. Baidin описание класса, методов
public class ResourceServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    @Bean
    //todo нужен вообще? при permitall?
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();

        return jwt -> {
            Collection<GrantedAuthority> grantedAuthorities = delegate.convert(jwt);

            if (jwt.getClaim("realm_access") == null) {
                return grantedAuthorities;
            }
            JSONObject realmAccess = jwt.getClaim("realm_access");
            if (realmAccess.get("roles") == null) {
                return grantedAuthorities;
            }
            JSONArray roles = (JSONArray) realmAccess.get("roles");

            final List<SimpleGrantedAuthority> keycloakAuthorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("TEST_" + role))
                    .collect(Collectors.toList());

            grantedAuthorities.addAll(keycloakAuthorities);

            return grantedAuthorities;
        };
    }
}
