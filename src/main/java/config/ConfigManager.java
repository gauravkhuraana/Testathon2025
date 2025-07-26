package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration manager to load and manage framework configuration properties
 */
public class ConfigManager {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties: " + e.getMessage());
        }
    }
    
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            // Replace environment variables in format ${ENV_VAR}
            value = replaceEnvironmentVariables(value);
        }
        return value;
    }
    
    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }
    
    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
    
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    private static String replaceEnvironmentVariables(String value) {
        if (value.contains("${") && value.contains("}")) {
            int startIndex = value.indexOf("${");
            int endIndex = value.indexOf("}", startIndex);
            if (startIndex != -1 && endIndex != -1) {
                String envVar = value.substring(startIndex + 2, endIndex);
                String envValue = System.getenv(envVar);
                if (envValue != null) {
                    value = value.replace("${" + envVar + "}", envValue);
                }
            }
        }
        return value;
    }
    
    // Convenience methods for common properties
    public static String getBrowserStackUsername() {
        // Try multiple sources for credentials with debugging
        String username = null;
        
        // 1. System property
        username = System.getProperty("browserstack.username");
        if (username != null && !username.isEmpty()) {
            System.out.println("✓ BrowserStack username found in system property");
            return username;
        }
        
        // 2. Direct environment variable
        username = System.getenv("BROWSERSTACK_USERNAME");
        if (username != null && !username.isEmpty()) {
            System.out.println("✓ BrowserStack username found in environment variable");
            return username;
        }
        
        // 3. Config file (which may contain environment variable placeholder)
        username = getProperty("browserstack.username");
        if (username != null && !username.isEmpty()) {
            System.out.println("✓ BrowserStack username found in config file: " + 
                (username.startsWith("${") ? "environment variable placeholder" : "direct value"));
            return username;
        }
        
        // 4. Hardcoded fallback (for testing only)
        username = "gauravkhurana_yShwDZ";
        System.out.println("⚠ Using hardcoded BrowserStack username as fallback");
        return username;
    }
    
    public static String getBrowserStackAccessKey() {
        // Try multiple sources for credentials with debugging
        String accessKey = null;
        
        // 1. System property
        accessKey = System.getProperty("browserstack.accesskey");
        if (accessKey != null && !accessKey.isEmpty()) {
            System.out.println("✓ BrowserStack access key found in system property");
            return accessKey;
        }
        
        // 2. Direct environment variable
        accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (accessKey != null && !accessKey.isEmpty()) {
            System.out.println("✓ BrowserStack access key found in environment variable");
            return accessKey;
        }
        
        // 3. Config file (which may contain environment variable placeholder)
        accessKey = getProperty("browserstack.accesskey");
        if (accessKey != null && !accessKey.isEmpty()) {
            System.out.println("✓ BrowserStack access key found in config file: " + 
                (accessKey.startsWith("${") ? "environment variable placeholder" : "direct value"));
            return accessKey;
        }
        
        // 4. Hardcoded fallback (for testing only)
        accessKey = "VGzb8uFGpPF7XWjXzcsG";
        System.out.println("⚠ Using hardcoded BrowserStack access key as fallback");
        return accessKey;
    }
    
    public static String getBrowserStackHubUrl() {
        return getProperty("browserstack.hub.url");
    }
    
    public static String getAppUrl() {
        String environment = System.getProperty("environment", "staging");
        return getProperty("app.url." + environment);
    }
    
    public static int getDefaultTimeout() {
        return getIntProperty("default.timeout", 30);
    }
    
    public static int getImplicitWait() {
        return getIntProperty("implicit.wait", 10);
    }
    
    public static int getExplicitWait() {
        return getIntProperty("explicit.wait", 30);
    }
    
    public static boolean isScreenshotOnFailureEnabled() {
        return getBooleanProperty("screenshot.on.failure", true);
    }
}
