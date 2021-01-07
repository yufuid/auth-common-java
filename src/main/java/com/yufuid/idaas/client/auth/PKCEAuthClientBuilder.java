package com.yufuid.idaas.client.auth;

import com.yufuid.idaas.domain.WellKnown;

import java.util.HashSet;
import java.util.Set;

/**
 * User: yunzhang
 * Date: 2020/12/23
 */
public class PKCEAuthClientBuilder {

    PKCEAuthClient authClient;

    public PKCEAuthClientBuilder(
        final String clientId,
        final WellKnown wellKnown,
        final String redirectUri
    ) {
        Set<String> scope = new HashSet<>();
        scope.add("openid");
        scope.add("offline_access");
        authClient = new PKCEAuthClient(
            clientId,
            wellKnown,
            null,
            redirectUri,
            scope
        );
    }

    public PKCEAuthClientBuilder(
        final String clientId,
        final String wellKnownUrl,
        final String redirectUri
    ) {
        Set<String> scope = new HashSet<>();
        scope.add("openid");
        scope.add("offline_access");
        authClient = new PKCEAuthClient(
            clientId,
            wellKnownUrl,
            null,
            redirectUri,
            scope
        );
    }

    public PKCEAuthClientBuilder redirectUri(String redirectUri) {
        authClient.setRedirectUri(redirectUri);
        return this;
    }

    public PKCEAuthClientBuilder codeVerifier(String codeVerifier) {
        authClient.setCodeVerifier(codeVerifier);
        return this;
    }

    public PKCEAuthClientBuilder scope(final Set<String> scope) {
        authClient.setScope(scope);
        return this;
    }

    public PKCEAuthClientBuilder scope(final String scope) {
        authClient.addScope(scope);
        return this;
    }

    public PKCEAuthClient build() {
        return this.authClient;
    }
}