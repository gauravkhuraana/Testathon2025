package config;

/**
 * BrowserStack SDK Configuration class
 * This class helps ensure the BrowserStack SDK properly detects TestNG framework
 * and enables Test Observability features
 */
public class BrowserStackSDKConfig {
    
    static {
        // Set framework detection for BrowserStack SDK
        System.setProperty("browserstack.framework", "testng");
        System.setProperty("browserstack.testObservability", "true");
        System.setProperty("browserstack.debug", "true");
        System.setProperty("browserstack.video", "true");
        System.setProperty("browserstack.networkLogs", "true");
        System.setProperty("browserstack.consoleLogs", "verbose");
        System.setProperty("browserstack.seleniumLogs", "true");
        
        // Auto-capture features
        System.setProperty("browserstack.autoCapture.screenshots", "true");
        System.setProperty("browserstack.autoCapture.logs", "true");
        System.setProperty("browserstack.autoCapture.network", "true");
        System.setProperty("browserstack.autoCapture.performance", "true");
        
        // Smart reporting and analytics
        System.setProperty("browserstack.smartReporting", "true");
        System.setProperty("browserstack.testAnalytics", "true");
        System.setProperty("browserstack.aiDebugging", "true");
    }
    
    /**
     * Initialize BrowserStack SDK configuration
     * Call this method at the start of your test suite
     */
    public static void initialize() {
        System.out.println("BrowserStack SDK Configuration initialized");
        System.out.println("Framework: " + System.getProperty("browserstack.framework"));
        System.out.println("Test Observability: " + System.getProperty("browserstack.testObservability"));
        System.out.println("Debug Mode: " + System.getProperty("browserstack.debug"));
    }
    
    /**
     * Verify that BrowserStack credentials are available
     */
    public static boolean verifyCredentials() {
        String username = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        
        if (username == null || username.isEmpty() || accessKey == null || accessKey.isEmpty()) {
            System.err.println("BrowserStack credentials not found in environment variables");
            System.err.println("Please set BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY");
            return false;
        }
        
        System.out.println("BrowserStack credentials verified");
        return true;
    }
}
