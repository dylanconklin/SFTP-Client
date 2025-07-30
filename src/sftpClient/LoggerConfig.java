package sftpClient;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {
    private static final String LOG_FILE = "sftp-user.log";
    private static boolean isConfigured = false;

    public static void setup() {
        if (!isConfigured) {
            Logger logger = Logger.getLogger("");
            logger.setLevel(Level.INFO);

            try {
                FileHandler fileHandler = new FileHandler(LOG_FILE, true);
                fileHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(fileHandler);
            } catch (IOException e) {
                System.err.println("Failed to set up log file handler: " + e.getMessage());
            }

            isConfigured = true;
        }
    }
}
