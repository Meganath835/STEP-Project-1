package bank;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

// ============================================================
// ACCOUNT RULES PROPERTIES LOADER
//
// Reads the key=value rules for one account type out of its
// .properties file under config/rules/ (physically stored next
// to this class, at bank/config/rules/<type>.properties) and
// parses it into a java.util.Properties dictionary.
//
// Because the thresholds live in these external files instead
// of in Java source, a policy can be changed by editing the
// .properties file and calling AccountRulesEngine.reloadPolicies()
// -- no recompiling, and no restarting the running application.
// ============================================================

public class AccountRulesPropertiesLoader {

    private static final String RESOURCE_BASE = "config/rules/";

    // Prevent instantiation -- every member is static.
    private AccountRulesPropertiesLoader() {
    }


    // ------------------------------------------------------------
    // Normalizes an account type into the lowercase file name the
    // properties files are saved under (e.g. "FIXEDDEPOSIT",
    // "Fixed Deposit" and "fixeddeposit" all resolve to the same
    // fixeddeposit.properties file).
    // ------------------------------------------------------------

    private static String normalizeFileName(String accountType) {

        if (accountType == null) {
            return "";
        }

        return accountType.trim().replace(" ", "").toLowerCase();
    }


    // ------------------------------------------------------------
    // Loads <accountType>.properties into a Properties dictionary.
    // Returns an empty (not null) Properties object when the file
    // does not exist, so callers can apply their own defaults.
    // ------------------------------------------------------------

    public static Properties loadProperties(String accountType) {

        String fileName = RESOURCE_BASE + normalizeFileName(accountType) + ".properties";
        Properties properties = new Properties();

        try (InputStream in = AccountRulesPropertiesLoader.class.getResourceAsStream(fileName)) {

            if (in == null) {
                return properties;
            }

            properties.load(in);

        } catch (IOException e) {
            throw new RuntimeException(
                "Failed to load rules for account type: " + accountType, e
            );
        }

        return properties;
    }


    // ------------------------------------------------------------
    // Resolves the on-disk location of an account type's
    // properties file, so it can be edited at runtime to
    // demonstrate dynamic, no-restart reloading. Returns null if
    // the file cannot be found.
    // ------------------------------------------------------------

    public static File resolvePropertiesFile(String accountType) {

        String fileName = RESOURCE_BASE + normalizeFileName(accountType) + ".properties";
        URL url = AccountRulesPropertiesLoader.class.getResource(fileName);

        if (url == null) {
            return null;
        }

        return new File(url.getFile());
    }


    // ------------------------------------------------------------
    // Parses a numeric property, falling back to defaultValue when
    // the key is missing, blank, or not a valid number.
    // ------------------------------------------------------------

    public static double getDouble(Properties properties, String key, double defaultValue) {

        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
