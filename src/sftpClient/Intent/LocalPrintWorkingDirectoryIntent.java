package sftpClient.Intent;

import java.util.ArrayList;
import java.util.logging.Logger;
import sftpClient.Client.Client;

public class LocalPrintWorkingDirectoryIntent extends Intent {
    private static final Logger logger = Logger.getLogger(CreateDirectoryIntent.class.getName());
    @Override
    void parse(ArrayList<String> args) {
        // lpwd command doesn't need any arguments
        // Just validate that no extra arguments were provided
        if (args.size() > 1) {
            System.out.println("Warning: lpwd command ignores extra arguments");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        
        try {
            // Get current working directory
            String currentDir = System.getProperty("user.dir");
            output.add("Current local directory: " + currentDir);
        } catch (Exception e) {
            output.add("Error getting current directory: " + e.getMessage());
        }
        logger.info(output.toString());
        return output;
    }
}