package ru.rt.music.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

//todo A. Baidin описание класса, снести\деприкетйнуть, что не надо
public class Token {
    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("expires_in")
    public Integer expiresIn;

    @JsonProperty("refresh_expires_in")
    public Integer refreshExpiresIn;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("not_before_policy")
    public Integer notBeforePolicy;

    @JsonProperty("session_state")
    public String sessionState;

    @JsonProperty("scope")
    public String scope;
}
