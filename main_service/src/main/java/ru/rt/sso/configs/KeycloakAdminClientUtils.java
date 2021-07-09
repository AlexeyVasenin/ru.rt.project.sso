package ru.rt.sso.configs;


import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeycloakAdminClientUtils {



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
    public static Keycloak getKeycloakClient(KeycloakSecurityContext session) {

        return KeycloakBuilder.builder() //
                .serverUrl("${keycloak-client.server-url}") //
                .realm("${keycloak-client.realm}") //
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS) //
                .username("admin")
                .password("password")
                .clientId("${keycloak.resource}") //
                .authorization(session.getTokenString()) //
                .build();
    }


}
