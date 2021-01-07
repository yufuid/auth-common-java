package com.yufuid.idaas.client.auth;

import com.yufuid.idaas.AuthClient;
import com.yufuid.idaas.domain.Token;
import com.yufuid.idaas.domain.UserInfo;
import com.yufuid.idaas.domain.WellKnown;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

/**
 * User: yunzhang
 * Date: 2020/12/22
 */
public class PKCEAuthClient extends AuthClient {

    private String redirectUri;
    private String codeVerifier;
    private Set<String> scope;

    public PKCEAuthClient(
        final String clientId,
        final String wellKnownUrl,
        final String codeVerifier,
        final String redirectUri,
        final Set<String> scope
    ) {
        super(clientId, wellKnownUrl);
        this.redirectUri = redirectUri;
        this.codeVerifier = codeVerifier;
        this.scope = scope;
    }

    public PKCEAuthClient(
        final String clientId,
        final WellKnown wellKnown,
        final String codeVerifier,
        final String redirectUri,
        final Set<String> scope
    ) {
        super(clientId, wellKnown);
        this.redirectUri = redirectUri;
        this.codeVerifier = codeVerifier;
        this.scope = scope;
    }

    void setScope(final Set<String> scope) {
        this.scope = scope;
    }

    void addScope(final String scope) {
        this.scope.add(scope);
    }

    void setCodeVerifier(final String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }

    void setRedirectUri(final String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String authorize() {
        return wellKnown.getAuthorizationEndpoint() + "?" +
            "response_type=code" +
            "&client_id=" + clientId +
            "&redirect_uri=" + redirectUri +
            "&scope=" + parseScope(scope);
    }

    public String authorize(final String state) {
        return authorize() + "&state=" + state;
    }

    public UserInfo getUserInfo(final String code) {
        return getUserInfo(code, false);
    }

    public UserInfo getUserInfo(final String code, final boolean needDetail) {
        Token token = getToken(code);
        if (needDetail) {
            return getUserInfoByToken(token);
        } else {
            String sub = getSubject(token);
            UserInfo userInfo = new UserInfo();
            userInfo.setSub(sub);
            return userInfo;
        }
    }

    private Token getToken(final String code) {
        String basicString = Base64.getEncoder().encodeToString((
            clientId + ":" + codeVerifier
        ).getBytes(StandardCharsets.UTF_8));

        Form form = new Form();
        form.param("grant_type", "authorization_code");
        form.param("redirect_uri", redirectUri);
        form.param("code", code);

        Response result = client.target(wellKnown.getTokenEndpoint())
            .request(MediaType.APPLICATION_JSON_TYPE)
            .header("Authorization", "Basic " + basicString)
            .post(Entity.form(form));
        return result.readEntity(Token.class);
    }
}
