package com.yufuid.idaas;

import com.yufuid.idaas.client.auth.CoreAuthClient;
import com.yufuid.idaas.client.auth.CoreAuthClientBuilder;
import com.yufuid.idaas.domain.ClientKeyPair;
import com.yufuid.idaas.domain.JWKResult;
import com.yufuid.idaas.domain.WellKnown;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * User: yunzhang
 * Date: 2021/1/7
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ClientBuilder.class, CoreAuthClient.class, AuthClient.class})
public class CoreAuthClientTest {

    @Mock
    Client client;

    @Mock
    Response response;

    @Mock
    Invocation.Builder builder;

    @Mock
    WebTarget webTarget;

    CoreAuthClient coreAuthClient;

    @Before
    public void init() throws IOException {
        Mockito.when(webTarget.path(Matchers.anyString())).thenReturn(webTarget);
        Mockito.when(webTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(
            builder);

        Mockito.when(client.target(Matchers.anyString())).thenReturn(webTarget);

        PowerMockito.mockStatic(ClientBuilder.class);
        PowerMockito.when(ClientBuilder.newClient()).thenReturn(client);

        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(builder.get()).thenReturn(response);
        Mockito.when(builder.get(WellKnown.class)).thenReturn(TestUtils.getDefaultWellKnown());
        Mockito.when(builder.get(JWKResult.class)).thenReturn(TestUtils.getDefaultJWKResult());
    }

    @Test
    public void authorize() {
        ClientKeyPair clientKeyPair = new ClientKeyPair("a", "b");
        String redirectUri = "https://www.client.com/callback";
        coreAuthClient = new CoreAuthClientBuilder(
            clientKeyPair,
            "",
            redirectUri
        ).build();

        String authorize = coreAuthClient.authorize();
        assert
            "https://idaas.yufuid.com/authorize?response_type=code&client_id=a&redirect_uri=https://www.client.com/callback&scope=openid%20offline_access"
                .equals(authorize);

        String state = "state";
        String authorizeWithState = coreAuthClient.authorize(state);
        assert
            "https://idaas.yufuid.com/authorize?response_type=code&client_id=a&redirect_uri=https://www.client.com/callback&scope=openid%20offline_access&state=state"
                .equals(authorizeWithState);
    }
}
