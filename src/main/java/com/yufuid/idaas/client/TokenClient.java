package com.yufuid.idaas.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.yufuid.idaas.domain.JWT;
import com.yufuid.idaas.domain.ServiceAccount;
import com.yufuid.idaas.exception.KeyParseException;
import com.yufuid.idaas.util.TokenUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import static com.yufuid.idaas.domain.JWT.convert;

/**
 * User: yunzhang
 * Date: 2020/12/23
 */
public class TokenClient {
    private final ServiceAccount serviceAccount;
    private final RSAKey rsaKey;
    protected ObjectMapper objectMapper = new ObjectMapper();

    public TokenClient(final ServiceAccount serviceAccount) throws KeyParseException {
        this.serviceAccount = serviceAccount;
        this.rsaKey = initKey();
    }

    public TokenClient(final Map<String, Object> serviceAccount) {
        this.serviceAccount = objectMapper.convertValue(serviceAccount, ServiceAccount.class);
        this.rsaKey = initKey();
    }

    private RSAKey initKey() throws KeyParseException {
        try {
            return RSAKey.parse(serviceAccount.getPrivateKey());
        } catch (ParseException e) {
            throw new KeyParseException();
        }
    }

    public String generateJWS(String audience) throws KeyParseException {
        try {
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .keyID(serviceAccount.parseKeyId())
                .build();

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issueTime(new Date(System.currentTimeMillis()))
                .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .audience(audience)
                .issuer(serviceAccount.getClientId())
                .claim("account_type", "serviceAccount")
                .build();

            SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
            JWSSigner signer = new RSASSASigner(rsaKey, true);
            jwt.sign(signer);
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new KeyParseException();
        }
    }

    public JWT verify(String token) throws KeyParseException {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            TokenUtils.verify(jwt, rsaKey.toRSAPublicKey());
            return convert(jwt);
        } catch (ParseException | JOSEException e) {
            throw new KeyParseException();
        }
    }
}
