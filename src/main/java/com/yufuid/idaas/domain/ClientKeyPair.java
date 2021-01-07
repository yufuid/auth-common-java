package com.yufuid.idaas.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * User: yunzhang
 * Date: 2021/1/7
 */
@Data
@AllArgsConstructor
public class ClientKeyPair {
    String clientId;
    String clientSecret;
}
