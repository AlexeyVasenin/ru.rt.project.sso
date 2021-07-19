package ru.rt.sso.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.rt.sso.clients.KeycloakAdminClient;

import java.util.*;

/**
 * Сервис для работы из Администраторского клиента <br>
 * Получение базовой информации из системы SSO Keycloak <br>
 * Внесение изменений в систему. Например добавление нового пользователя, клиента(сервиса) и тд.
 *
 * @author Алексей Васенин
 */

@Service
public class KeycloakAdminClientService {

    private final KeycloakAdminClient keycloakAdminClient;

    public KeycloakAdminClientService(KeycloakAdminClient keycloakAdminClient) {
        this.keycloakAdminClient = keycloakAdminClient;
    }

    /**
     * Создание нового пользователя в системе. Принимает два параметра
     *
     * @param userName логин пользователя
     * @param pass     пароль
     * @return объект в виде нового пользователя со всеми параметрами системы Keycloak
     */
    public Object addUser(String userName, String pass) {

        if (!findUser(userName).isPresent()) {
            UsersResource usersResource = getBuildKeycloak().users();
            CredentialRepresentation credentialRepresentation = createPasswordCredentials(pass);
            UserRepresentation kcUser = new UserRepresentation();

            kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
            kcUser.setUsername(userName);
            kcUser.setEnabled(true);
            kcUser.setEmailVerified(false);
            usersResource.create(kcUser);

            return kcUser;
        }
        return "Пользователь с логином " + userName + " уже существует";
    }

    /**
     * Обновление данных пользователя
     */
    public String updateUser(String userName, String description) {

        if (findUser(userName).isPresent()) {
            UserRepresentation userRepresentation = findUser(userName).get();
            UserResource userResource = getBuildKeycloak().users().get(userRepresentation.getId());
            Map<String, List<String>> attributes = new HashMap<>();

            attributes.put("description", Arrays.asList(description));
            userRepresentation.setAttributes(attributes);
            userResource.update(userRepresentation);

            return "User updated";

        } else {
            return "User not found";
        }
    }

    /**
     * Удаление пользователя из системы
     *
     * @param userName логин пользователя
     */
    public String deleteUser(String userName) {

        if (findUser(userName).isPresent()) {
            UsersResource users = getBuildKeycloak().users();

            users.search(userName).stream()
                    .forEach(u -> getBuildKeycloak().users().delete(u.getId()));

            return "Пользователь с логином " + userName + " удален";

        } else return "Пользователя с таким именем не существует";
    }

    /**
     * Получение всех пользователей в системе
     *
     * @return Список всех пользователей из система
     */
    public List<UserRepresentation> getUsers() {
        List<UserRepresentation> allUsers = getBuildKeycloak().users().list();
        return allUsers;
    }

    /**
     * Получение всех клиентов(сервисов)
     *
     * @return Список клиентов(сервисов) в системе
     */
    public List<ClientRepresentation> getAllClient() {
        List<ClientRepresentation> allClient = getBuildKeycloak().clients().findAll();
        return allClient;
    }

    /**
     * Создание нового клиента(сервиса) в системе
     *
     * @param clientId название клиента(сервиса)
     */
    public Object createClient(String clientId) {
        Optional<ClientRepresentation> clietns = getBuildKeycloak().clients().findByClientId(clientId).stream().
                filter(c -> c.getClientId().equals(clientId)).findFirst();

        if (!clietns.isPresent()) {
            ClientRepresentation client = new ClientRepresentation();
            client.setClientId(clientId);
            client.setName(clientId);
            client.setBearerOnly(false);
            client.setPublicClient(false);
            client.setEnabled(true);
            client.setProtocol("openid-connect");

            getBuildKeycloak().clients().create(client);

            return client;

        } else return "Client c именем " + clientId + " уже существует";
    }

    /**
     * Создание Роли в клиенте(сервисе)
     *
     * @param roleName название роли
     * @param clientId Id клиента(сервиса)
     */
    public void createRoleInClient(String roleName, String clientId) {
        RoleRepresentation clientRole = new RoleRepresentation();
        clientRole.setName(roleName);
        getBuildKeycloak().clients().get(clientId).roles().create(clientRole);
    }

    /**
     * Назначить роль клиента -> юзеру
     *
     * @param userName логин пользователя
     * @param clientId Id клиента(сервиса). Пример: (cda63aba-3337-49de-b2b9-e7e04756df29)
     * @param role     название роли
     */
    public String assingAClientRoleToTheUser(String userName, String clientId, String role) {

        if (findUser(userName).isPresent()) {
            if (!findUserRole(userName, clientId, role).isPresent()) {

                getUserId(userName).roles().clientLevel(clientId).add(setRole(clientId, role));

                return "Роль " + role + " назначена пользователю " + userName;

            } else return "Роль " + role + " у пользователя " + userName + " уже назначена";

        } else return "Пользователь не существует";
    }

    /**
     * Отключить роли сервиса у пользователя
     *
     * @param userName логин пользователя
     * @param clientId Id клиента(сервиса) Пример: (cda63aba-3337-49de-b2b9-e7e04756df29)
     * @param role     название роли
     */
    public String deleteAClientRoleToTheUser(String userName, String clientId, String role) {

        if (findUser(userName).isPresent()) {
            if (findUserRole(userName, clientId, role).isPresent()) {

                getUserId(userName).roles().clientLevel(clientId).remove(setRole(clientId, role));

                return "Роль " + role + " у пользователя " + userName + " отключена";

            } else return "Роль " + role + " у пользователя " + userName + " уже отключена";

        } else return "Пользователя " + userName + " не существует";
    }

    /**
     * Получение ролей Клиента(Сервиса)
     *
     * @param clientId имя клиента(сервиса)
     * @return Список ролей в клиенте(сервисе)
     */
    public List<RoleRepresentation> getTheClientRoles(String clientId) {
        ClientRepresentation clientRepresentation = getBuildKeycloak().clients().findByClientId(clientId).get(0);
        List<RoleRepresentation> roles = getBuildKeycloak().clients().get(clientRepresentation.getId()).roles().list();
        return roles;
    }

    /**
     * Получение списка ролей пользователя в определенном клиенте
     *
     * @param userName логин пользователя
     * @param clientId имя клиента(сервиса)
     * @return Список ролей пользователя в сервисе
     * @see RoleRepresentation
     */
    public Object getRolesByUsername(String userName, String clientId) {

        if (findUser(userName).isPresent()) {
            ClientRepresentation clientRepresentation = getBuildKeycloak().clients().findByClientId(clientId).get(0);
            List<RoleRepresentation> roles =
                    getUserId(userName).roles().clientLevel(clientRepresentation.getId()).listAll();

            return roles;

        } else return "Пользователь не существует";
    }

    /**
     * @param password пароль пользователя
     */
    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    /**
     * Создаем лист ролей, которые будем присваивать пользователю;
     */
    private List<RoleRepresentation> setRole(String clientId, String role) {
        List<RoleRepresentation> clientRolesToAdd = new ArrayList<RoleRepresentation>();
        RoleRepresentation clientRole = getBuildKeycloak()
                .clients()
                .get(clientId)
                .roles()
                .get(role)
                .toRepresentation();

        clientRolesToAdd.add(clientRole);

        return clientRolesToAdd;
    }

    /**
     * Получение id пользователя по логину
     *
     * @param userName логин пользователя
     * @return возвращает Id пользователя
     */
    private UserResource getUserId(String userName) {
        UserRepresentation userRepresentation = findUser(userName).get();
        UserResource userResource = getBuildKeycloak().users().get(userRepresentation.getId());
        return userResource;
    }

    /**
     * Поиск роли у пользователя в клиенте(сервисе)
     *
     * @param userName логин пользователя
     * @param clientId Id клиента(сервиса) Пример: (cda63aba-3337-49de-b2b9-e7e04756df29)
     * @param role     название роли
     */
    private Optional<RoleRepresentation> findUserRole(String userName, String clientId, String role) {
        Optional<RoleRepresentation> roleMappingResource =
                getUserId(userName).roles().clientLevel(clientId).listAll().stream()
                        .filter(r -> r.getName().equals(role)).findFirst();
        return roleMappingResource;
    }

    /**
     * Поиск пользователя в системе с логинов
     *
     * @param userName логин пользователя
     */
    private Optional<UserRepresentation> findUser(String userName) {
        return getBuildKeycloak().users().search(userName).stream()
                .filter(u -> u.getUsername().equals(userName)).findFirst();
    }

    /**
     * Метод получает Keycloak build и возвращает наш реалм.<br>
     * Для использования в методах для получение данных и внесения изменений в систему
     *
     * @return Возвращает реалм c профилем администратора
     * @see KeycloakAdminClient
     */
    private RealmResource getBuildKeycloak() {
        Keycloak keycloak = keycloakAdminClient.getAdminClient();
        keycloak.tokenManager().getAccessToken();
        return keycloak.realm(keycloakAdminClient.getRealm());
    }
}
