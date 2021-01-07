package com.yufuid.idaas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: yunzhang
 * Date: 2019/9/17,8:23 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {
    @JsonProperty(value = "id_token")
    String idToken;

    @JsonProperty(value = "access_token")
    String accessToken;

    @JsonProperty(value = "refresh_token")
    String refreshToken;

    @JsonProperty(value = "expires_in")
    int expireIn;

    /**
     * @since 20.9.4, YuFu specific. the refresh_token expires_in
     * for TENCENT_IEG use.
     */
    @JsonProperty(value = "refresh_token_expires_in")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer refreshTokenExpireIn;

    @JsonProperty(value = "token_type")
    String tokenType;

    @JsonProperty(value = "scope")
    String scope;
}
