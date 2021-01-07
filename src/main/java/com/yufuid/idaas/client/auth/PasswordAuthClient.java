package com.yufuid.idaas.client.auth;

import com.yufuid.idaas.AuthClient;
import com.yufuid.idaas.domain.Token;
import com.yufuid.idaas.domain.UserInfo;
import com.yufuid.idaas.domain.WellKnown;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * User: yunzhang
 * Date: 2020/12/22
 */
public class PasswordAuthClient extends AuthClient {
    private final String clientSecret;

    PasswordAuthClient(final String clientId, final String clientSecret, final String wellKnownUrl) {
        super(clientId, wellKnownUrl);
        this.clientSecret = clientSecret;
    }

    PasswordAuthClient(
        final String clientId,
        final String clientSecret,
        final WellKnown wellKnown
    ) {
        super(clientId, wellKnown);
        this.clientSecret = clientSecret;
    }

    public UserInfo getUserInfo(final String userName, final String password, final boolean needDetail) {
        MultivaluedMap<String, String> form = new MultivaluedHashMap<>();
        form.add("grant_type", "password");
        form.add("username", userName);
        form.add("password", password);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        Response
            response = client.target(wellKnown.getTokenEndpoint())
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.form(form));
        Token token = response.readEntity(Token.class);
        if (needDetail) {
            return getUserInfoByToken(token);
        } else {
            String sub = getSubject(token);
            UserInfo userInfo = new UserInfo();
            userInfo.setSub(sub);
            return userInfo;
        }
    }

    public UserInfo getUserInfo(final String userName, final String password) {
        return getUserInfo(userName, password, false);
    }
}
