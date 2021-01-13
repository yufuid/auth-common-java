package com.yufuid.idaas;

import com.yufuid.idaas.client.TokenClient;
import com.yufuid.idaas.domain.JWT;
import com.yufuid.idaas.domain.ServiceAccount;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * User: yunzhang
 * Date: 2021/1/7
 */
public class TokenClientTest {
    private static final String DEFAULT_AUDIENCE = "audience";

    @Test
    public void assertToken() throws IOException {
        ServiceAccount serviceAccount = TestUtils.getDefaultServiceAccount();
        TokenClient tokenClient = new TokenClient(serviceAccount);
        String token = tokenClient.generateJWS(DEFAULT_AUDIENCE);
        JWT jwt = tokenClient.verify(token);

        Date now = new Date(System.currentTimeMillis());
        assert now.after(jwt.getIssueAt());

        assert now.before(jwt.getExpiration());

        assert DEFAULT_AUDIENCE.equals(jwt.getAudience());

    }

}
