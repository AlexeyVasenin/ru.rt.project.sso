package ru.rt.sso.clients;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Класс для создания Keycloak объекта c параметрами администратора, для дальнейшей работы через Admin client-а.
 * @author Алексей Васенин
 * @author Вячеслав Третьяков
 *
 */

@Slf4j
@Service
@Getter
public class KeycloakAdminClient {

    private Keycloak keycloak = null;

    @Value("${keycloak-client.server-url}")
    private String authServerUrl;

    @Value("${keycloak-client.realm}")
    private String realm;

    @Value("${keycloak-client.admin.clientId}")
    private String clientId;

    @Value("${keycloak-client.admin.username}")
    private String username;

    @Value("${keycloak-client.admin.password}")
    private String password;

    @Value("${keycloak-client.admin.secret}")
    private String secret;

    /**
     * It builds a {@link Keycloak} client from a given configuration. This client
     * is used to communicate with the Keycloak instance via REST API.
     * @return Keycloak instance
     * @see Keycloak
     */
    public Keycloak getAdminClient() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(authServerUrl)
                    .grantType(OAuth2Constants.PASSWORD)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(secret)
                    .username(username)
                    .password(password)
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build())
                    .build();

            log.info("[REALM] : " + realm);
        }
        return keycloak;
    }
}
