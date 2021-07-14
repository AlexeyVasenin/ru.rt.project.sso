package ru.rt.sso.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.rt.sso.clients.KeycloakAdminClient;
import ru.rt.sso.domain.User;

import java.util.*;

@Service
public class KeycloakAdminClientService {

    private final KeycloakAdminClient keycloakAdminClient;

    public KeycloakAdminClientService(KeycloakAdminClient keycloakAdminClient) {
        this.keycloakAdminClient = keycloakAdminClient;
    }

    //todo ЮЗЕРА заменить на OidcUser !!!!!!!
    //Создать нового пользователя
    public UserRepresentation addUser(User user) {

        UsersResource usersResource = getBuildKeycloak().users();

        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();


        kcUser.setUsername(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstName());
        kcUser.setLastName(user.getLastName());
        kcUser.setEmail(user.getEmail());
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


    //Создание Role in Client
    public void createRoleInClient(String name, String title) {
        RoleRepresentation clientRole = new RoleRepresentation();
        clientRole.setName(name);
        getBuildKeycloak().clients().get(title).roles().create(clientRole);

    }


    //Получение списка ролей пользователя в определенном клиенте
    public Object getRolesByUsername(String username, String clientId) {
        Optional<UserRepresentation> user = getBuildKeycloak().users().search(username).stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();

        if (user.isPresent()) {
            UserRepresentation userRepresentation = user.get();
            UserResource userResource = getBuildKeycloak().users().get(userRepresentation.getId());
            ClientRepresentation clientRepresentation = getBuildKeycloak().clients().findByClientId(clientId).get(0);
            List<RoleRepresentation> roles = userResource.roles().clientLevel(clientRepresentation.getId()).listAll();

            return roles;
        } else {
            return "Roles not found";
        }
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
