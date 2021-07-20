package ru.rt.sso.controller;

import io.swagger.annotations.ApiOperation;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.rt.sso.service.KeycloakAdminClientServiceImpl;

import java.util.List;

/**
 * Web controller для работы с системой Keycloak <br>
 * Внесение изменений и получение данных из системы от имени администратора
 *
 * @author Алексей Васенин
 */
@RestController
@RequestMapping(path = "/keycloak", produces = MediaType.APPLICATION_JSON_VALUE)
public class KeycloakController {

    private final KeycloakAdminClientServiceImpl keycloakAdminClientServiceImpl;

    public KeycloakController(KeycloakAdminClientServiceImpl keycloakAdminClientServiceImpl) {
        this.keycloakAdminClientServiceImpl = keycloakAdminClientServiceImpl;
    }

    @ApiOperation(value = "Создание нового пользователя")
    @PostMapping(path = "/user")
    @ResponseBody
    public Object createUser(@RequestParam(value = "userName") String userName,
                                         @RequestParam(value = "pass") String pass) {
        return keycloakAdminClientServiceImpl.addUser(userName, pass);
    }

    @ApiOperation(value = "Удаление пользователя по логину")
    @PostMapping(path = "/user/del")
    @ResponseBody
    public String deleteUser(@RequestParam(value = "userName") String userName) {
       return keycloakAdminClientServiceImpl.deleteUser(userName);

    }

    @ApiOperation(value = "Получить список всех пользователей")
    @GetMapping(path = "/users")
    @ResponseBody
    public List<UserRepresentation> getAllUsers() {
        return keycloakAdminClientServiceImpl.getUsers();
    }

    @ApiOperation(value = "Получить список всех сервисов(клиентов)")
    @GetMapping(path = "/clients")
    @ResponseBody
    public List<ClientRepresentation> getAllClient() {
        return keycloakAdminClientServiceImpl.getAllClient();
    }

    @ApiOperation(value = "Получить список ролей в Клиенте")
    @GetMapping(path = "/client/roles")
    @ResponseBody
    public List<RoleRepresentation> getRoles(@RequestParam(value = "clientId") String clientId) {

        return keycloakAdminClientServiceImpl.getTheClientRoles(clientId);
    }

    @ApiOperation(value = "Получение ролей пользователя в Клиенте")
    @GetMapping(path = "/user/roles")
    @ResponseBody
    public Object getRolesByUserName(@RequestParam(value = "userName") String userName,
                                                       @RequestParam(value = "clientId") String clientId) {
        return keycloakAdminClientServiceImpl.getRolesByUsername(userName, clientId);
    }

    @ApiOperation(value = "Создание нового клиента в реалме")
    @PostMapping(path = "/client")
    @ResponseBody
    public Object createClient(@RequestParam(value = "clientId") String clientId) {

        return keycloakAdminClientServiceImpl.createClient(clientId);
    }

    @ApiOperation(value = "Создание роли в Клиенте")
    @PostMapping(path = "/client/role/new")
    @ResponseBody
    public String createRole(@RequestParam(value = "roleName") String roleName,
                             @RequestParam(value = "clientId") String clientId) {
        keycloakAdminClientServiceImpl.createRoleInClient(roleName, clientId);
        return "Создана роль " + roleName;
    }

    @ApiOperation(value = "Назначение роли клиента пользователю")
    @PostMapping(path = "/user/role")
    @ResponseBody
    public String assingAClientRoleToTheUser(@RequestParam(value = "userName") String userName,
                                             @RequestParam(value = "clientId") String clientId,
                                             @RequestParam(value = "role") String role) {
       return keycloakAdminClientServiceImpl.assingAClientRoleToTheUser(userName, clientId, role);

    }

    @ApiOperation(value = "Отключить роли клиента пользователю")
    @PostMapping(path = "/user/d/role")
    @ResponseBody
    public String deleteAClientRoleToTheUser(@RequestParam(value = "userName") String userName,
                                             @RequestParam(value = "clientId") String clientId,
                                             @RequestParam(value = "role") String role) {
       return keycloakAdminClientServiceImpl.deleteAClientRoleToTheUser(userName, clientId, role);

    }
}
