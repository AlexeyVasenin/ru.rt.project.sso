package ru.rt.library.controllers;

import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpHeaders;

@RestController
@RequestMapping("/library")
public class LibraryController {

    @GetMapping
    public String index() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Jwt principalJwt = (Jwt) authentication.getPrincipal();
        String username = (String) principalJwt.getClaims().get("preferred_username");
        String email = (String) principalJwt.getClaims().get("email");
        String authorizedParty = (String) principalJwt.getClaims().get("azp");
        String scopeValues = (String) principalJwt.getClaims().get("scope");
        JSONObject realmAccess = (JSONObject) principalJwt.getClaims().get("realm_access");
        return String.format("authorized party: %s;\nprincipal username: %s;\nprincipal email: %s;\nprincipal scopes: %s;\nprincipal realm access: %s",
                authorizedParty, username, email, scopeValues, realmAccess);
    }
}
