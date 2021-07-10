package ru.rt.sso.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeycloakAdminClientConfig {

    private String serverUrl;
    private String realm;
    private String clientId;
}
