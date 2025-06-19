package org.example.constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Credentials {
    private static Properties props = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/environments/credentials.properties");
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc file credentials.properties", e);
        }
    }

    public static String getUsername() {
        return props.getProperty("username");
    }

    public static String getPassword() {
        return props.getProperty("password");
    }
}
