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

@Service
public class KeycloakAdminClientService {

    private final KeycloakAdminClient keycloakAdminClient;

    public KeycloakAdminClientService(KeycloakAdminClient keycloakAdminClient) {
        this.keycloakAdminClient = keycloakAdminClient;
    }

    //todo ЮЗЕРА заменить на OidcUser !!!!!!!
    //Создать нового пользователя
    public UserRepresentation addUser(String username, String pass) {

        UsersResource usersResource = getBuildKeycloak().users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(pass);
        UserRepresentation kcUser = new UserRepresentation();

        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setUsername(username);
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        usersResource.create(kcUser);

        return kcUser;
    }

    //Обновление данных пользователя
    public void updateUser(String username, String description) {
        Optional<UserRepresentation> user = getBuildKeycloak().users().search(username).stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();

        if (user.isPresent()) {
            UserRepresentation userRepresentation = user.get();
            UserResource userResource = getBuildKeycloak().users().get(userRepresentation.getId());
            Map<String, List<String>> attributes = new HashMap<>();

            attributes.put("description", Arrays.asList(description));
            userRepresentation.setAttributes(attributes);
            userResource.update(userRepresentation);

            System.out.println("Ок");
        } else {
            System.out.println("User not found");
        }
    }

    //Удаление пользователя
    public void deleteUser(String username) {
        UsersResource users = getBuildKeycloak().users();
        users.search(username).stream()
                .forEach(user -> getBuildKeycloak().users().delete(user.getId()));
    }

    //Получить всех пользователей
    public List<UserRepresentation> getUsers() {
        List<UserRepresentation> allUsers = getBuildKeycloak().users().list();
        return allUsers;
    }

    public List<ClientRepresentation> getAllClient(){
        List<ClientRepresentation> allClient = getBuildKeycloak().clients().findAll();
        return allClient;
    }

    //Создание нового клиента в реалме
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

    //Создание Role in Client
    public void createRoleInClient(String roleName, String clientId) {
        RoleRepresentation clientRole = new RoleRepresentation();
        clientRole.setName(roleName);
        getBuildKeycloak().clients().get(clientId).roles().create(clientRole);

    }

    //Назначить роль клиента -> юзеру
    public void assingAClientRoleToTheUser(String username, String clientId, String role) {

        Optional<UserRepresentation> user = getBuildKeycloak().users().search(username).stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();

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

    //Получение ролей Клиента
    public List<RoleRepresentation> getTheClientRoles(String clientId) {
        ClientRepresentation clientRepresentation = getBuildKeycloak().clients().findByClientId(clientId).get(0);
        List<RoleRepresentation> roles = getBuildKeycloak().clients().get(clientRepresentation.getId()).roles().list();
        return roles;
    }

    //Получение списка ролей пользователя в определенном клиенте
    public List<RoleRepresentation> getRolesByUsername(String username, String clientId) {
        Optional<UserRepresentation> user = getBuildKeycloak().users().search(username).stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();

        UserRepresentation userRepresentation = user.get();
        UserResource userResource = getBuildKeycloak().users().get(userRepresentation.getId());
        ClientRepresentation clientRepresentation = getBuildKeycloak().clients().findByClientId(clientId).get(0);
        List<RoleRepresentation> roles = userResource.roles().clientLevel(clientRepresentation.getId()).listAll();
        return roles;
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private RealmResource getBuildKeycloak() {
        Keycloak keycloak = keycloakAdminClient.getAdminClient();
        keycloak.tokenManager().getAccessToken();
        return keycloak.realm(keycloakAdminClient.getRealm());
    }
}
