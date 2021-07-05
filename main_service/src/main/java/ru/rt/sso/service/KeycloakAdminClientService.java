package ru.rt.sso.service;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.rt.sso.config.KeycloakAdminClientUtils;
import ru.rt.sso.config.KeycloakPropertyReader;
import ru.rt.sso.domain.KeycloakAdminClientConfig;
import ru.rt.sso.domain.User;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakAdminClientService {
    @Value("${keycloak.resource}")
    private String keycloakClient;

    private final KeycloakPropertyReader keycloakPropertyReader;

    @Autowired
    public KeycloakAdminClientService(KeycloakPropertyReader keycloakPropertyReader) {
        this.keycloakPropertyReader = keycloakPropertyReader;
    }

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

    //Получить всех пользователей
    public List<UserRepresentation> getUsers() {

        List<UserRepresentation> allUsers = getBuildKeycloak().users().list();
        return allUsers;
    }

    //Создание нового Client
    public void createClient() {
        ClientRepresentation client = new ClientRepresentation();
        client.setId("testClientId01");
        client.setBearerOnly(false);
        client.setPublicClient(false);
        client.setSecret("******");
        client.setProtocol("openid-connect");
    }

    //Создание Role in Client
    public void createRoleInClient(String name, String title) {
        RoleRepresentation clientRole = new RoleRepresentation();
        clientRole.setName(name);
        getBuildKeycloak().clients().get(title).roles().create(clientRole);

    }

    // Поиск пользователя по имени
    public UserRepresentation getUser(String name) {
        UserRepresentation getUser = getBuildKeycloak()
                .users()
                .search(name)
                .get(0);
        return getUser;
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private RealmResource getBuildKeycloak() {
        KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal =
                (KeycloakPrincipal<RefreshableKeycloakSecurityContext>)
                        SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getPrincipal();

        KeycloakAdminClientConfig keycloakAdminClientConfig =
                KeycloakAdminClientUtils.loadConfig(keycloakPropertyReader);

        Keycloak keycloak = KeycloakAdminClientUtils.getKeycloakClient
                (principal.getKeycloakSecurityContext(),
                        keycloakAdminClientConfig);

        RealmResource realmResource = keycloak.realm(keycloakAdminClientConfig.getRealm());
        return realmResource;
    }


}
