package ru.rt.sso.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
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
 * @author Алексей Васенин
 */

@Service
public class KeycloakAdminClientService {

    private final KeycloakAdminClient keycloakAdminClient;

    public KeycloakAdminClientService(KeycloakAdminClient keycloakAdminClient) {
        this.keycloakAdminClient = keycloakAdminClient;
    }

    /**
     * Создание нового пользователя {@link UserRepresentation}, принимает два параметра
     * @param userName логин пользователя
     * @param pass пароль
     * @return объект
     */
    public UserRepresentation addUser(String userName, String pass) {

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

    /**
     * Обновление данных пользователя
     * */
    public String updateUser(String username, String description) {
        Optional<UserRepresentation> user = getBuildKeycloak().users().search(username).stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();

        if (user.isPresent()) {
            UserRepresentation userRepresentation = user.get();
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
     * @param userName логин пользователя
     * */
    public void deleteUser(String userName) {
        UsersResource users = getBuildKeycloak().users();
        users.search(userName).stream()
                .forEach(user -> getBuildKeycloak().users().delete(user.getId()));
    }

    /**
     * Получение всех пользователей в системе
     * @return Список всех пользователей из система
     * */
    public List<UserRepresentation> getUsers() {
        List<UserRepresentation> allUsers = getBuildKeycloak().users().list();
        return allUsers;
    }

    /**
     * Получение всех клиентов(сервисов)
     * @return Список клиентов(сервисов) в реалме
     * */
    public List<ClientRepresentation> getAllClient() {
        List<ClientRepresentation> allClient = getBuildKeycloak().clients().findAll();
        return allClient;
    }

    /**
     * Создание нового клиента(сервиса) в реалме
     * @param clientId название клиента(сервиса)
     * */
    public void createClient(String clientId) {
        //ClientRepresentation clientRepresentation = getBuildKeycloak().clients().findByClientId(clientId).get(0);
        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(clientId);
        client.setName(clientId);
        client.setBearerOnly(false);
        client.setPublicClient(false);
        client.setEnabled(true);
        client.setProtocol("openid-connect");
        getBuildKeycloak().clients().create(client);
    }

    /**
     * Создание Роли в клиенте(сервисе)
     * @param roleName название роли
     * @param clientId Id клиента(сервиса)
     * */
    public void createRoleInClient(String roleName, String clientId) {
        RoleRepresentation clientRole = new RoleRepresentation();
        clientRole.setName(roleName);
        getBuildKeycloak().clients().get(clientId).roles().create(clientRole);
    }

    /**
     * Назначить роль клиента -> юзеру
     * @param userName логин пользователя
     * @param clientId Id клиента(сервиса). Пример: (cda63aba-3337-49de-b2b9-e7e04756df29)
     * @param role название роли
     * */
    public void assingAClientRoleToTheUser(String userName, String clientId, String role) {

        Optional<UserRepresentation> user = getBuildKeycloak().users().search(userName).stream()
                .filter(u -> u.getUsername().equals(userName)).findFirst();

        UserRepresentation userRepresentation = user.get();
        UserResource userResource = getBuildKeycloak().users().get(userRepresentation.getId());

        RoleMappingResource roleMappingResource = userResource.roles();

        List<RoleRepresentation> clientRolesToAdd = new ArrayList<RoleRepresentation>();
        RoleRepresentation clientRole = getBuildKeycloak()
                .clients()
                .get(clientId)
                .roles()
                .get(role)
                .toRepresentation();
        clientRolesToAdd.add(clientRole);
        roleMappingResource.clientLevel(clientId).add(clientRolesToAdd);
    }

    /**
     * Отключить роли сервиса у пользователя
     * @param userName логин пользователя
     * @param clientId Id клиента(сервиса) Пример: (cda63aba-3337-49de-b2b9-e7e04756df29)
     * @param role название роли
     */
    public void deleteAClientRoleToTheUser(String userName, String clientId, String role) {

        Optional<UserRepresentation> user = getBuildKeycloak().users().search(userName).stream()
                .filter(u -> u.getUsername().equals(userName)).findFirst();

        UserRepresentation userRepresentation = user.get();
        UserResource userResource = getBuildKeycloak().users().get(userRepresentation.getId());

        RoleMappingResource roleMappingResource = userResource.roles();

        List<RoleRepresentation> clientRolesToAdd = new ArrayList<RoleRepresentation>();
        RoleRepresentation clientRole = getBuildKeycloak()
                .clients()
                .get(clientId)
                .roles()
                .get(role)
                .toRepresentation();
        clientRolesToAdd.add(clientRole);
        roleMappingResource.clientLevel(clientId).remove(clientRolesToAdd);
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
    public List<RoleRepresentation> getRolesByUsername(String userName, String clientId) {
        Optional<UserRepresentation> user = getBuildKeycloak().users().search(userName).stream()
                .filter(u -> u.getUsername().equals(userName)).findFirst();

        UserRepresentation userRepresentation = user.get();
        UserResource userResource = getBuildKeycloak().users().get(userRepresentation.getId());
        ClientRepresentation clientRepresentation = getBuildKeycloak().clients().findByClientId(clientId).get(0);
        List<RoleRepresentation> roles = userResource.roles().clientLevel(clientRepresentation.getId()).listAll();
        return roles;
    }

    /**
     * @param password пароль пользователя
     * */
    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    /**
     * Метод получает Keycloak build и возвращает наш реалм.
     * Для использования в методах для получение данных и внесения изменений в систему
     * @return Возвращает реалм
     * @see KeycloakAdminClient
     */
    private RealmResource getBuildKeycloak() {
        Keycloak keycloak = keycloakAdminClient.getAdminClient();
        keycloak.tokenManager().getAccessToken();
        return keycloak.realm(keycloakAdminClient.getRealm());
    }
}
