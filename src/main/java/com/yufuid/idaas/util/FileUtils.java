package com.yufuid.idaas.util;

import java.io.*;

/**
 * User: yunzhang
 * Date: 2021/1/7
 */
public class FileUtils {

    public static String getFileAsString(String relativeResourcePath) throws IOException {
        InputStream is = FileUtils.class.getResourceAsStream("/" + relativeResourcePath);
        if (is == null) {
            throw new FileNotFoundException(relativeResourcePath);
        }

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            copyBytes(new BufferedInputStream(is), bytes);

            return bytes.toString("utf-8");
        } finally {
            is.close();
        }
    }

    private static void copyBytes(InputStream is, OutputStream bytes) throws IOException {
        int res = is.read();
        while (res != -1) {
            bytes.write(res);
            res = is.read();
        }
    }
}
