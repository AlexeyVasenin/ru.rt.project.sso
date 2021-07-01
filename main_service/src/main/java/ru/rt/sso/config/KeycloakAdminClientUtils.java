package ru.rt.sso.config;


import org.apache.commons.lang3.StringUtils;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rt.sso.domain.KeycloakAdminClientConfig;

public class KeycloakAdminClientUtils {

    private static Logger log = LoggerFactory.getLogger(KeycloakAdminClientUtils.class);

    /**
     * Loads the keycloak configuration from system property.
     *
     * @return keycloak configuration
     * @see KeycloakAdminClientConfig
     */
    public static KeycloakAdminClientConfig loadConfig(KeycloakPropertyReader keycloakPropertyReader) {

        KeycloakAdminClientConfig.KeycloakAdminClientConfigBuilder builder = KeycloakAdminClientConfig.builder();

        try {
            String keycloakServer = System.getProperty("keycloak.url");
            if (!StringUtils.isBlank(keycloakServer)) {
                builder = builder.serverUrl(keycloakServer);

            } else {
                builder = builder.serverUrl(keycloakPropertyReader.getProperty("keycloak.auth-server-url"));
            }

            String realm = System.getProperty("keycloak.realm");
            if (!StringUtils.isBlank(realm)) {
                builder = builder.realm(realm);

            } else {
                builder = builder.realm(keycloakPropertyReader.getProperty("keycloak.realm"));
            }

            String userName = System.getProperty("keycloak.userName");
            if (!StringUtils.isBlank(userName)) {
                builder = builder.userName(userName);

            } else {
                builder = builder.userName(keycloakPropertyReader.getProperty("keycloak.secret"));
            }

            String clientId = System.getProperty("keycloak.clientId");
            if (!StringUtils.isBlank(clientId)) {
                builder = builder.clientId(clientId);

            } else {
                builder = builder.clientId(keycloakPropertyReader.getProperty("keycloak.resource"));
            }

            String clientSecret = System.getProperty("keycloak.secret");
            if (!StringUtils.isBlank(clientSecret)) {
                builder = builder.clientSecret(clientSecret);

            } else {
                builder = builder.clientSecret(keycloakPropertyReader.getProperty("keycloak.secret"));
            }

        } catch (Exception e) {
            log.error("Error: Loading keycloak admin configuration => {}", e.getMessage());
        }

        KeycloakAdminClientConfig config = builder.build();
        log.debug("Found keycloak configuration: {}", config);

        return config;
    }

    /**
     * It builds a {@link Keycloak} client from a given configuration. This client
     * is used to communicate with the Keycloak instance via REST API.
     *
     * @param session the security context
     * @param config  keycloak configuration
     * @return Keycloak instance
     * @see Keycloak
     * @see KeycloakAdminClientConfig
     */
    public static Keycloak getKeycloakClient(KeycloakSecurityContext session, KeycloakAdminClientConfig config) {

        return KeycloakBuilder.builder() //
                .serverUrl(config.getServerUrl()) //
                .realm(config.getRealm()) //
                .grantType(OAuth2Constants.PASSWORD) //
                .username(config.getUserName())
                .clientId(config.getClientId()) //
                .clientSecret(config.getClientSecret()) //
                .authorization(session.getTokenString()) //
                .build();
    }



}
