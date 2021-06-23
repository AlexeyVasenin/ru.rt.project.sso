package ru.rt.sso.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rt.sso.service.KeycloakAdminClientService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/keycloak", produces = MediaType.APPLICATION_JSON_VALUE)
public class KeycloakController {

    private final KeycloakAdminClientService keycloakAdminClientService;

    public KeycloakController(KeycloakAdminClientService keycloakAdminClientService) {
        this.keycloakAdminClientService = keycloakAdminClientService;
    }

    @GetMapping(path = "/hello")
    public String hello(){
        return "Hello";
    }

    @GetMapping(path = "/roles")
    public Collection<String> rolesOfUser(){
        return keycloakAdminClientService.getUserRoles();
    }

    @GetMapping(path = "/profile")
    public Object profileOfUser(){
        return keycloakAdminClientService.getUserProfileOfLoggedUser();
    }
}
