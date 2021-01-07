package com.yufuid.idaas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: yunzhang
 * Date: 2020/12/21
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WellKnown {

    private String issuer;

    @JsonProperty(value = "authorization_endpoint")
    private String authorizationEndpoint;

    @JsonProperty(value = "token_endpoint")
    private String tokenEndpoint;

    @JsonProperty(value = "jwks_uri")
    private String jwksUri;

    @JsonProperty(value = "userinfo_endpoint")
    private String userinfoEndpoint;

}
