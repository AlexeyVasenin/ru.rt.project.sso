package ru.rt.sso.controller;

import lombok.Getter;
import lombok.Setter;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.rt.sso.domain.User;
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
    public String hello() {
        return "Hello";
    }


    @PostMapping(path = "/users")
    @ResponseBody
    public UserRepresentation createUser(@RequestBody User user) {
        return keycloakAdminClientService.addUser(user);
    }

}
