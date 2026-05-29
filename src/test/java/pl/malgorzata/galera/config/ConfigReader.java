package pl.malgorzata.galera.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load configuration properties file.");
        }
    }

    public static String getBaseUri() {
        return properties.getProperty("base.uri");
    }

    public static int getBasePort() {
        return Integer.parseInt(properties.getProperty("base.port"));
    }
}
