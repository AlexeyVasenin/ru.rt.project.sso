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

    @PutMapping(path = "/user")
    @ResponseBody
    public void updateUser(@RequestParam String name,
                           @RequestParam String description){
        keycloakAdminClientService.updateUser(name, description);
    }

    @DeleteMapping(path = "user/del/{username}")
    @ResponseBody
    public void deleteUser(@PathVariable String username){
        keycloakAdminClientService.deleteUser(username);
    }

    @GetMapping(path = "/users")
    @ResponseBody
    public List<UserRepresentation> getAllUsers() {
        return keycloakAdminClientService.getUsers();
    }

    @GetMapping(path = "/user/roles")
    @ResponseBody
    public Object getRolesByUserName(@RequestParam String username,
                                     @RequestParam String clientId){
        return keycloakAdminClientService.getRolesByUsername(username, clientId);
    }




}
