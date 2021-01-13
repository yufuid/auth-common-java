package com.yufuid.idaas.domain;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Builder;
import lombok.Getter;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Builder
@Getter
public class JWT {
    /**
     * id of key used to sign token
     */
    private String keyId;

    /**
     * issuer of token, commonly referring to ID of Id/Service Provider
     */
    private String issuer;
    private String audience;
    private String subject;
    private String jwtId;
    private Date expiration;
    private Date issueAt;
    private Date notBefore;

    public Date getExpiration() {
        return (Date) expiration.clone();
    }

    public Date getIssueAt() {
        return (Date) issueAt.clone();
    }

    public Date getNotBefore() {
        return (Date) notBefore.clone();
    }

    /**
     * All claims (registered + custom)
     */
    private Map<String, Object> claims;

    public static JWT convert(SignedJWT token) throws ParseException {
        JWTClaimsSet claims = token.getJWTClaimsSet();

        return JWT.builder()
            .keyId(token.getHeader().getKeyID())
            .audience(claims.getAudience().get(0))
            .expiration(claims.getExpirationTime())
            .issueAt(claims.getIssueTime())
            .issuer(claims.getIssuer())
            .jwtId(claims.getJWTID())
            .notBefore(claims.getNotBeforeTime())
            .subject(claims.getSubject())
            .claims(claims.getClaims())
            .build();
    }
}
