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
 * Date: 2020/6/10
 * https://openid.net/specs/openid-connect-core-1_0.html#StandardClaims
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

    @JsonProperty(value = "user_id")
    String userId;

    @JsonProperty(value = "sub")
    String sub;

    @JsonProperty(value = "name")
    String name;

    @JsonProperty(value = "preferred_username")
    String preferredUsername;

    @JsonProperty(value = "email")
    String email;

    @JsonProperty(value = "email_verified")
    boolean emailVerified;

    @JsonProperty(value = "phone_number")
    String phoneNumber;

    @JsonProperty(value = "phone_number_verified")
    boolean phoneNumberVerified;
}
