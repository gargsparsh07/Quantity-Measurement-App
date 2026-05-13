package com.app.quantitymeasurement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static ApplicationConfig instance;
    private final Properties properties = new Properties();

    private ApplicationConfig() {
        loadProperties("application.properties");
    }

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    private void loadProperties(String filename) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (input != null) {
                properties.load(input);
                logger.info("Loaded configuration from {}", filename);
            } else {
                logger.warn("Could not find {}, using defaults", filename);
            }
        } catch (IOException e) {
            logger.error("Error loading properties: {}", e.getMessage());
        }
    }

    public String get(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }

    public String get(String key, String defaultValue) {
        String value = get(key);
        return (value != null) ? value : defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue) {
        try {
            return Long.parseLong(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String getRepositoryType() {
        return get("repository.type", "cache");
    }

    public String getDbUrl() {
        return get("db.url");
    }

    public String getDbUsername() {
        return get("db.username", "sa");
    }

    public String getDbPassword() {
        return get("db.password", "");
    }

    public String getDbDriver() {
        return get("db.driver", "org.h2.Driver");
    }

    public int getPoolSize() {
        return getInt("db.pool.size", 10);
    }

    public long getPoolTimeout() {
        return getLong("db.pool.timeout", 30000);
    }
}
