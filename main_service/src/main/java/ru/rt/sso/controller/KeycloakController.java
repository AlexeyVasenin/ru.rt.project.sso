package ru.rt.sso.controller;

import io.swagger.annotations.ApiOperation;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.rt.sso.clients.KeycloakAdminClient;
import ru.rt.sso.service.KeycloakAdminClientService;

import java.util.List;

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

    @ApiOperation(value = "Создание нового пользователя")
    @PostMapping(path = "/user")
    @PreAuthorize("hasRole('ROLE_REALM-ADMIN')")
    @ResponseBody
    public UserRepresentation createUser(@RequestParam(value = "username") String username,
                                         @RequestParam(value = "pass") String pass) {
        return keycloakAdminClientService.addUser(username, pass);
    }

    @PutMapping(path = "/u/user")
    @ResponseBody
    public void updateUser(@RequestParam String name,
                           @RequestParam String description) {
        keycloakAdminClientService.updateUser(name, description);
    }

    @ApiOperation(value = "Удаление пользователя по username")
    @PostMapping(path = "/user/del")
    @PreAuthorize("hasRole('ROLE_REALM-ADMIN')")
    @ResponseBody
    public String deleteUser(@RequestParam(value = "username") String username) {
        keycloakAdminClientService.deleteUser(username);
        return "пользователь с именем " + username + " удален";
    }

    @ApiOperation(value = "Получить список всех пользователей")
    @GetMapping(path = "/users")
    @PreAuthorize("hasRole('ROLE_REALM-ADMIN')")
    @ResponseBody
    public List<UserRepresentation> getAllUsers() {
        return keycloakAdminClientService.getUsers();
    }

    @ApiOperation(value = "Получить список всех сервисов(клиентов)")
    @GetMapping(path = "/clients")
    @ResponseBody
    public List<ClientRepresentation> getAllClient(){
        return keycloakAdminClientService.getAllClient();
    }

    @ApiOperation(value = "Получить список ролей в Клиенте")
    @GetMapping(path = "/client/roles")
    @ResponseBody
    public List<RoleRepresentation> getRoles(@RequestParam(value = "clientId") String clientId) {
        return keycloakAdminClientService.getTheClientRoles(clientId);
    }

    @ApiOperation(value = "Получение ролей пользователя в Клиенте")
    @GetMapping(path = "/user/roles")
    @ResponseBody
    public List<RoleRepresentation> getRolesByUserName(@RequestParam(value = "username") String username,
                                                       @RequestParam(value = "clientId") String clientId) {
        return keycloakAdminClientService.getRolesByUsername(username, clientId);
    }

    @ApiOperation(value = "Создание нового клиента в реалме")
    @PostMapping(path = "/client")
    @ResponseBody
    public String createClient(@RequestParam(value = "clientId") String clientId) {
       keycloakAdminClientService.createClient(clientId);
       return "Создан клиент " + clientId;
    }

    @ApiOperation(value = "Создание роли в Клиенте")
    @PostMapping(path = "/client/role/new")
    @ResponseBody
    public String createRole(@RequestParam(value = "roleName") String roleName,
                           @RequestParam(value = "clientId") String clientId) {
        keycloakAdminClientService.createRoleInClient(roleName, clientId);
        return "Создана роль " + roleName;
    }

    @ApiOperation(value = "Назначение роли клиента пользователю")
    @PostMapping(path = "/user/role")
    @ResponseBody
    public String assingAClientRoleToTheUser(@RequestParam(value = "userName") String userName,
                                           @RequestParam(value = "clientId") String clientId,
                                           @RequestParam(value = "role") String role){
        keycloakAdminClientService.assingAClientRoleToTheUser(userName, clientId, role);
        return "Роль " + role + " назначена пользователю " + userName;
    }


   /* @PostMapping(path = "/user")
    @ResponseBody
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
*/

}
