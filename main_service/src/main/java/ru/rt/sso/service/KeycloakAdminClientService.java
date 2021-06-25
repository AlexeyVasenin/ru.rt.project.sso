package ru.rt.sso.service;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.rt.sso.config.KeycloakAdminClientUtils;
import ru.rt.sso.config.KeycloakPropertyReader;
import ru.rt.sso.config.UserProvider;
import ru.rt.sso.controller.KeycloakController;
import ru.rt.sso.domain.KeycloakAdminClientConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KeycloakAdminClientService {
    @Value("${keycloak.resource}")
    private String keycloakClient;

    private final UserProvider userProvider;
    private final KeycloakPropertyReader keycloakPropertyReader;

    @Autowired
    public KeycloakAdminClientService(UserProvider userProvider, KeycloakPropertyReader keycloakPropertyReader) {
        this.userProvider = userProvider;
        this.keycloakPropertyReader = keycloakPropertyReader;
    }


   /*
   public void AddRealm() {
        RealmRepresentation realm = new RealmRepresentation();
    }
*/
    public void addUser(KeycloakController.SaveNewUsers newUsers) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(newUsers.getUserName());
        user.setEmail(newUsers.getEmail());

        CredentialRepresentation cr = new CredentialRepresentation();
        cr.setType(CredentialRepresentation.PASSWORD);
        cr.setValue(newUsers.getPassword());
        user.setCredentials(Arrays.asList(cr));

        KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal = (KeycloakPrincipal<RefreshableKeycloakSecurityContext>) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        KeycloakAdminClientConfig keycloakAdminClientConfig = KeycloakAdminClientUtils.loadConfig(keycloakPropertyReader);
        Keycloak keycloak = KeycloakAdminClientUtils.getKeycloakClient(principal.getKeycloakSecurityContext(), keycloakAdminClientConfig);
        keycloak.realm(keycloakAdminClientConfig.getRealm()).users().create(user);
    }

    public List<String> getUserRoles() {
        return userProvider.getCurrentUser().getRoles();
    }


    public Object getUserProfileOfLoggedUser() {
        @SuppressWarnings("unchecked")
        KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal = (KeycloakPrincipal<RefreshableKeycloakSecurityContext>) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        KeycloakAdminClientConfig keycloakAdminClientConfig = KeycloakAdminClientUtils.loadConfig(keycloakPropertyReader);
        Keycloak keycloak = KeycloakAdminClientUtils.getKeycloakClient(principal.getKeycloakSecurityContext(), keycloakAdminClientConfig);

        // Get realm
        RealmResource realmResource = keycloak.realm(keycloakAdminClientConfig.getRealm());
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(userProvider.getCurrentUser().getUserName());
        UserRepresentation userRepresentation = userResource.toRepresentation();

        return userRepresentation;
    }
}
