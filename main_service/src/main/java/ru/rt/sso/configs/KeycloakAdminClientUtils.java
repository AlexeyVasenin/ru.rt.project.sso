package ru.rt.sso.configs;


import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeycloakAdminClientUtils {

    //@Value("${keycloak-client.server-url}")
    //private static String serverUrl;
    //
    //@Value("${keycloak-client.realm}")
    //private static String realm;
    //
    //@Value("${keycloak.resource}")
    //  private static String clientId;

    public static Keycloak getKeycloakClient() {

        return KeycloakBuilder.builder() //
                .serverUrl("http:\\//keycloak-java-school.apps.okd.stage.digital.rt.ru/auth") //
                .realm("sso_realm") //
                .grantType(OAuth2Constants.PASSWORD) //
                .username("admin")
                .password("password")
                .clientId("admin-cli")
                .clientSecret("589f6f4c-d006-4bf1-9978-c133ac87447a")//
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build())//
                .build();
    }


}
