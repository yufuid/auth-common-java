package com.yufuid.idaas.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.yufuid.idaas.exception.InvalidTokenTimeException;
import com.yufuid.idaas.exception.TokenParseException;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * User: yunzhang
 * Date: 2021/1/12
 */
public class TokenUtils {

    public static void verify(SignedJWT jwt, Map<String, RSAPublicKey> publicKeys) throws
        TokenParseException,
        InvalidTokenTimeException {
        String kid = jwt.getHeader().getKeyID();
        RSAPublicKey rsaPublicKey = publicKeys.get(kid);
        if (rsaPublicKey == null) {
            throw new TokenParseException();
        }
        verify(jwt, rsaPublicKey);
    }

    public static void verify(SignedJWT jwt, RSAPublicKey publicKey) throws
        TokenParseException,
        InvalidTokenTimeException {
        try {
            RSASSAVerifier verifier = new RSASSAVerifier(publicKey);
            if (!jwt.verify(verifier)) {
                throw new TokenParseException();
            }
            Date now = new Date();
            if (jwt.getJWTClaimsSet().getExpirationTime().before(now)) {
                throw new InvalidTokenTimeException();
            }
        } catch (JOSEException | ParseException e) {
            throw new TokenParseException();
        }
    }
}
