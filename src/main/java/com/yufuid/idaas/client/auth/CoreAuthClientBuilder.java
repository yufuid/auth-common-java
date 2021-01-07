package com.yufuid.idaas.client.auth;

import com.yufuid.idaas.domain.ClientKeyPair;
import com.yufuid.idaas.domain.WellKnown;

import javax.ws.rs.client.Client;
import java.util.HashSet;
import java.util.Set;

/**
 * User: yunzhang
 * Date: 2020/12/23
 */
public class CoreAuthClientBuilder {

    CoreAuthClient authClient;

    public CoreAuthClientBuilder(
        final ClientKeyPair clientKeyPair,
        final WellKnown wellKnown,
        final String redirectUri
    ) {
        Set<String> scope = new HashSet<>();
        scope.add("openid");
        scope.add("offline_access");
        authClient = new CoreAuthClient(
            clientKeyPair.getClientId(),
            wellKnown,
            clientKeyPair.getClientSecret(),
            redirectUri,
            scope
        );
    }

    public CoreAuthClientBuilder(
        final ClientKeyPair clientKeyPair,
        final String wellKnownUrl,
        final String redirectUri
    ) {
        Set<String> scope = new HashSet<>();
        scope.add("openid");
        scope.add("offline_access");
        authClient = new CoreAuthClient(
            clientKeyPair.getClientId(),
            wellKnownUrl,
            clientKeyPair.getClientSecret(),
            redirectUri,
            scope
        );
    }

    public CoreAuthClientBuilder redirectUri(String redirectUri) {
        authClient.setRedirectUri(redirectUri);
        return this;
    }

    public CoreAuthClientBuilder clientSecret(String clientSecret) {
        authClient.setClientSecret(clientSecret);
        return this;
    }

    public CoreAuthClientBuilder scope(final Set<String> scope) {
        authClient.setScope(scope);
        return this;
    }

    public CoreAuthClientBuilder scope(final String scope) {
        authClient.addScope(scope);
        return this;
    }

    public CoreAuthClient build() {
        return this.authClient;
    }
}