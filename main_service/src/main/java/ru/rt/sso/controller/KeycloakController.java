package ru.rt.sso.controller;

import io.swagger.annotations.ApiOperation;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.rt.sso.service.KeycloakAdminClientService;

import java.util.List;

// todo описание
@RestController
@RequestMapping(path = "/keycloak")
public class KeycloakController {

    private final KeycloakAdminClientService keycloakAdminClientService;

    public KeycloakController(KeycloakAdminClientService keycloakAdminClientService) {
        this.keycloakAdminClientService = keycloakAdminClientService;
    }

    @ApiOperation(value = "Создание нового пользователя")
    @PostMapping(path = "/user")
    @PreAuthorize("hasRole('ROLE_REALM-ADMIN')")
    @ResponseBody
    public UserRepresentation createUser(@RequestParam(value = "userName") String userName,
                                         @RequestParam(value = "pass") String pass) {
        return keycloakAdminClientService.addUser(userName, pass);
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
    public String deleteUser(@RequestParam(value = "userName") String userName) {
        keycloakAdminClientService.deleteUser(userName);
        // todo при удалении несуществующего пользователя также выводится сообщение об удалении
        return "пользователь с именем " + userName + " удален";
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
    public List<ClientRepresentation> getAllClient() {
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
    public List<RoleRepresentation> getRolesByUserName(@RequestParam(value = "userName") String userName,
                                                       @RequestParam(value = "clientId") String clientId) {
        return keycloakAdminClientService.getRolesByUsername(userName, clientId);
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
                                             @RequestParam(value = "role") String role) {
        keycloakAdminClientService.assingAClientRoleToTheUser(userName, clientId, role);
        return "Роль " + role + " назначена пользователю " + userName;
    }

    @ApiOperation(value = "Отключить роли клиента пользователю")
    @PostMapping(path = "/user/d/role")
    @ResponseBody
    public String deleteAClientRoleToTheUser(@RequestParam(value = "userName") String userName,
                                             @RequestParam(value = "clientId") String clientId,
                                             @RequestParam(value = "role") String role) {
        keycloakAdminClientService.deleteAClientRoleToTheUser(userName, clientId, role);
        return "Роль " + role + " отключена пользователю " + userName;
    }
}
