package com.yufuid.idaas.client.auth;

import com.yufuid.idaas.domain.ClientKeyPair;
import com.yufuid.idaas.domain.WellKnown;

/**
 * User: yunzhang
 * Date: 2020/12/23
 */
public class PasswordAuthClientBuilder {

    PasswordAuthClient authClient;

    public PasswordAuthClientBuilder(
        final ClientKeyPair clientKeyPair,
        final WellKnown wellKnown
    ) {
        authClient = new PasswordAuthClient(
            clientKeyPair.getClientId(),
            clientKeyPair.getClientSecret(),
            wellKnown
        );
    }

    public PasswordAuthClientBuilder(
        final ClientKeyPair clientKeyPair,
        final String wellKnownUrl
    ) {
        authClient = new PasswordAuthClient(
            clientKeyPair.getClientId(),
            clientKeyPair.getClientSecret(),
            wellKnownUrl
        );
    }

    public PasswordAuthClient build() {
        return this.authClient;
    }
}