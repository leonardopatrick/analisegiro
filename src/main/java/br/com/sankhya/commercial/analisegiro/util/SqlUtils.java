package br.com.sankhya.commercial.analisegiro.util;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Enumeration;

public class SqlUtils {

    public static StringBuffer getStringBufferSQLFromResource(Class baseClass, String resourcePath) throws Exception {
        InputStream inStream = baseClass.getResourceAsStream(resourcePath);

        if (inStream == null) {
            throw new IllegalArgumentException("Arquivo de SQL não encontrado: " + baseClass.getName() + " -> " + resourcePath);
        }

        byte[] buf = new byte[1024];
        StringBuffer sqlBuf = new StringBuffer();
        while (true) {
            int readen = inStream.read(buf);

            if (readen <= 0) {
                break;
            }
            sqlBuf.append(new String(buf, 0, readen, "ISO-8859-1"));
        }
        return sqlBuf;
    }

    public static String loadSQLFromResource(Class baseClass, String resourcePath) throws Exception {
        return getStringBufferSQLFromResource(baseClass, resourcePath).toString();
    }

    public static StringBuffer loadSql(String fileName) throws IOException {

        File file = ResourceUtils.getFile("classpath:"+fileName);
        String content = new String(Files.readAllBytes(file.toPath()));

        return new StringBuffer(content);
    }

}
