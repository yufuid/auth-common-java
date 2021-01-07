package com.yufuid.idaas;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import com.yufuid.idaas.domain.JWKResult;
import com.yufuid.idaas.domain.Token;
import com.yufuid.idaas.domain.UserInfo;
import com.yufuid.idaas.domain.WellKnown;
import com.yufuid.idaas.exception.InvalidTokenTimeException;
import com.yufuid.idaas.exception.KeyParseException;
import com.yufuid.idaas.exception.SubNotMatchException;
import com.yufuid.idaas.exception.TokenParseException;
import net.minidev.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.*;

/**
 * User: yunzhang
 * Date: 2020/12/11
 */
public abstract class AuthClient {
    protected String clientId;
    protected WellKnown wellKnown;
    protected Map<String, RSAPublicKey> rsaPublicKeys = new HashMap<>();
    protected Client client = ClientBuilder.newClient();

    public AuthClient(final String clientId, final String wellKnownUrl) {
        this.clientId = clientId;
        this.wellKnown = parseWellKnown(wellKnownUrl);
        initPubKey(wellKnown.getJwksUri());
    }

    public AuthClient(final String clientId, final WellKnown wellKnown) {
        this.clientId = clientId;
        this.wellKnown = wellKnown;
        initPubKey(wellKnown.getJwksUri());
    }

    public String parseScope(Set<String> scope) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String scopeItem : scope) {
            stringBuilder.append(scopeItem).append("%20");
        }
        return scope.size() > 0
            ? stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length()).toString()
            : "";
    }

    private WellKnown parseWellKnown(final String wellKnownUrl) {
        return client.target(wellKnownUrl)
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get(WellKnown.class);
    }

    protected UserInfo getUserInfoByToken(final Token token) {
        Response response = client.target(wellKnown.getUserinfoEndpoint())
            .request(MediaType.APPLICATION_JSON_TYPE)
            .header("Authorization", "Bearer " + token.getAccessToken())
            .get();
        UserInfo userInfo = response.readEntity(UserInfo.class);
        if (!userInfo.getSub().equals(getSubject(token))) {
            throw new SubNotMatchException();
        }
        return userInfo;
    }

    protected Token refreshToken(final String refreshToken, final String clientSecret) {
        MultivaluedMap<String, String> form = new MultivaluedHashMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("refresh_token", refreshToken);
        Response response = client.target(wellKnown.getTokenEndpoint())
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.form(form));
        return response.readEntity(Token.class);
    }

    protected Token refreshToken(final Token token, final String clientSecret) {
        return refreshToken(token.getRefreshToken(), clientSecret);
    }

    protected String getSubject(final Token token) {
        try {
            String idToken = token.getIdToken();
            SignedJWT jwt = SignedJWT.parse(idToken);

            boolean verifyResult = verify(jwt, rsaPublicKeys);
            if (!verifyResult) {
                throw new TokenParseException();
            }
            Date now = new Date();
            if (jwt.getJWTClaimsSet().getIssueTime().after(now)) {
                throw new InvalidTokenTimeException();
            }
            if (jwt.getJWTClaimsSet().getExpirationTime().before(now)) {
                throw new InvalidTokenTimeException();
            }
            return jwt.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new TokenParseException();
        }
    }

    private void initPubKey(final String jwkUri) throws KeyParseException {

        JWKResult jwkObject = client.target(jwkUri)
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get(JWKResult.class);

        // Find the RSA signing key
        List<JSONObject> keyList = jwkObject.getKeys();
        for (JSONObject k : keyList) {
            if (k.get("use").equals("sig") && k.get("kty").equals("RSA")) {
                try {
                    rsaPublicKeys.put(k.get("kid").toString(), RSAKey.parse(k).toRSAPublicKey());
                } catch (ParseException | JOSEException e) {
                    throw new KeyParseException();
                }
            }
        }
    }

    private boolean verify(SignedJWT jwt, Map<String, RSAPublicKey> publicKeys) throws TokenParseException {
        try {
            String kid = jwt.getHeader().getKeyID();
            RSAPublicKey rsaPublicKey = publicKeys.get(kid);
            if (rsaPublicKey == null) {
                return false;
            }
            RSASSAVerifier verifier = new RSASSAVerifier(rsaPublicKey);
            return jwt.verify(verifier);
        } catch (JOSEException e) {
            throw new TokenParseException();
        }
    }

}
