package ru.rt.sso.clients;

import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KeycloakAdminClient {

    //todo на ленивый синглтон
    private Keycloak keycloak = null;

    @Value("${admin.auth-server-url}")
    private String authServerUrl;

    @Value("${admin.keycloak.realm}")
    private String childRealm;

    @Value("${admin.keycloak.clientId}")
    private String clientId;

    @Value("${admin.keycloak.username}")
    private String username;

    @Value("${admin.keycloak.password}")
    private String password;

    @Value("${admin.keycloak.secret}")
    private String secret;

    public String getChildRealm() {
        return childRealm;
    }

    public String getClientId() {
        return clientId;
    }

    public Keycloak getAdminClient() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(authServerUrl)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .realm(childRealm)
                    .clientId(clientId)
                    .clientSecret(secret)
                    .username(username)
                    .password(password)
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build())
                    .build();

            log.info("[REALM] : " + childRealm);
        }
        return keycloak;
    }
}
