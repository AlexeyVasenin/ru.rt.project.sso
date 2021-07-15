package ru.rt.sso.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.rt.sso.clients.KeycloakAdminClient;
import ru.rt.sso.service.KeycloakAdminClientService;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/keycloak", produces = MediaType.APPLICATION_JSON_VALUE)
public class KeycloakController {

    private final KeycloakAdminClientService keycloakAdminClientService;

    private final KeycloakAdminClient keycloakAdminClient;


    public KeycloakController(KeycloakAdminClientService keycloakAdminClientService,
                              KeycloakAdminClient keycloakAdminClient) {
        this.keycloakAdminClientService = keycloakAdminClientService;
        this.keycloakAdminClient = keycloakAdminClient;
    }

    //@PostMapping(path = "/user")
    //@ResponseBody
    //public UserRepresentation createUser(@RequestBody User user) {
    //    return keycloakAdminClientService.addUser(user);
    //}
    //
    //@PutMapping(path = "/user")
    //@ResponseBody
    //public void updateUser(@RequestParam String name,
    //                       @RequestParam String description){
    //    keycloakAdminClientService.updateUser(name, description);
    //}

    @DeleteMapping(path = "user/del")
    @PreAuthorize("hasRole('ROLE_REALM-ADMIN')")
    @ResponseBody
    public void deleteUser(@RequestParam String username, Model model){
        model.addAttribute("username", username);
        keycloakAdminClientService.deleteUser(username);
    }

    @GetMapping(path = "/users")
    @PreAuthorize("hasRole('ROLE_REALM-ADMIN')")
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


    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResponseEntity.badRequest().body("Empty username or password");
        }
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        credentials.setTemporary(false);

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);
        userRepresentation.setCredentials(Arrays.asList(credentials));
        userRepresentation.setEnabled(true);

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("description", Arrays.asList("A test user"));
        userRepresentation.setAttributes(attributes);
        Keycloak keycloak = keycloakAdminClient.getKeycloak();
        Response result = keycloak.realm("sso_realm").users().create(userRepresentation);
        return new ResponseEntity<>(HttpStatus.valueOf(result.getStatus()));
    }



}
