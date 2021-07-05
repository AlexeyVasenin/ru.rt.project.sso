package ru.rt.sso.controller;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.rt.sso.domain.User;
import ru.rt.sso.service.KeycloakAdminClientService;

import java.util.List;

@RestController
@RequestMapping(path = "/keycloak", produces = MediaType.APPLICATION_JSON_VALUE)
public class KeycloakController {

    private final KeycloakAdminClientService keycloakAdminClientService;

    public KeycloakController(KeycloakAdminClientService keycloakAdminClientService) {
        this.keycloakAdminClientService = keycloakAdminClientService;
    }

    @PostMapping(path = "/user")
    @ResponseBody
    public UserRepresentation createUser(@RequestBody User user) {
        return keycloakAdminClientService.addUser(user);
    }

    @GetMapping(path = "/users")
    @ResponseBody
    public List<UserRepresentation> getAllUsers() {
        return keycloakAdminClientService.getUsers();
    }

    @GetMapping(path = "users/{name}")
    @ResponseBody
    public UserRepresentation getUser(@PathVariable String name){
        return keycloakAdminClientService.getUser(name);
    }

}
