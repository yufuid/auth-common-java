package com.yufuid.idaas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

/**
 * User: yunzhang
 * Date: 2020/12/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceAccount {
    private String clientId;
    private JSONObject privateKey;

    public String parseKeyId() {
        return privateKey.getAsString("kid");
    }
}
