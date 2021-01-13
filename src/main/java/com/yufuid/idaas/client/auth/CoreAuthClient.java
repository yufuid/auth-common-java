package com.yufuid.idaas.client.auth;

import com.yufuid.idaas.AuthClient;
import com.yufuid.idaas.domain.Token;
import com.yufuid.idaas.domain.UserInfo;
import com.yufuid.idaas.domain.WellKnown;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * User: yunzhang
 * Date: 2020/12/21
 */
public class CoreAuthClient extends AuthClient {

    private String clientSecret;
    private String redirectUri;

    private Set<String> scope;

    CoreAuthClient(
        final String clientId,
        final String wellKnownUrl,
        final String clientSecret,
        final String redirectUri,
        final Set<String> scope
    ) {
        super(clientId, wellKnownUrl);
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = scope;
    }

    CoreAuthClient(
        final String clientId,
        final WellKnown wellKnown,
        final String clientSecret,
        final String redirectUri,
        final Set<String> scope
    ) {
        super(clientId, wellKnown);
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = scope;
    }

    void setScope(final Set<String> scope) {
        this.scope = scope;
    }

    void addScope(final String scope) {
        this.scope.add(scope);
    }

    void setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
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

        String basicString = DatatypeConverter.printBase64Binary(
            (clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)
        );

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
