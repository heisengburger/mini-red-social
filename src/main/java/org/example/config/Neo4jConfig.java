package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Neo4jConfig {
    private static final String DEFAULT_PROPERTIES_FILE = "application.properties";

    private final String uri;
    private final String user;
    private final String password;

    private Neo4jConfig(String uri, String user, String password) {
        this.uri = uri;
        this.user = user;
        this.password = password;
    }

    public static Neo4jConfig load() {
        Properties properties = new Properties();

        try (InputStream input = Neo4jConfig.class.getClassLoader()
                .getResourceAsStream(DEFAULT_PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + DEFAULT_PROPERTIES_FILE, e);
        }

        return new Neo4jConfig(
                readValue(properties, "NEO4J_URI", "neo4j.uri"),
                readValue(properties, "NEO4J_USER", "neo4j.user"),
                readValue(properties, "NEO4J_PASSWORD", "neo4j.password")
        );
    }

    public String uri() {
        return uri;
    }

    public String user() {
        return user;
    }

    public String password() {
        return password;
    }

    private static String readValue(Properties properties, String environmentKey, String propertyKey) {
        String environmentValue = System.getenv(environmentKey);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }

        String propertyValue = properties.getProperty(propertyKey);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        throw new IllegalStateException("Falta configurar " + propertyKey + " o " + environmentKey);
    }
}
