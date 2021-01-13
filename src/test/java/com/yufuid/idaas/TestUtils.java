package com.yufuid.idaas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yufuid.idaas.domain.*;
import com.yufuid.idaas.util.FileUtils;

import java.io.IOException;

/**
 * User: yunzhang
 * Date: 2021/1/7
 */
public class TestUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static WellKnown getDefaultWellKnown() throws IOException {
        String dataStr = FileUtils.getFileAsString("data/well-known.json");
        return objectMapper.readValue(dataStr, WellKnown.class);
    }

    public static JWKResult getDefaultJWKResult() throws IOException {
        String dataStr = FileUtils.getFileAsString("data/jwk-result.json");
        return objectMapper.readValue(dataStr, JWKResult.class);
    }

    public static ServiceAccount getDefaultServiceAccount() throws IOException {
        String dataStr = FileUtils.getFileAsString("data/service-account.json");
        return objectMapper.readValue(dataStr, ServiceAccount.class);
    }

    public static Token getDefaultToken() throws IOException {
        String dataStr = FileUtils.getFileAsString("data/token.json");
        return objectMapper.readValue(dataStr, Token.class);
    }

    public static UserInfo getDefaultUserInfo() throws IOException {
        String dataStr = FileUtils.getFileAsString("data/user-info.json");
        return objectMapper.readValue(dataStr, UserInfo.class);
    }
}
