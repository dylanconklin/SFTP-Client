package sftpClient.Intent;

import java.io.File;
import java.util.ArrayList;
import sftpClient.Client.Client;

/**
 * LocalChangeDirectoryIntent implements the lcd (Local Change Directory) command.
 * Changes the current working directory for local file system operations.
 */
public class LocalChangeDirectoryIntent extends Intent {
    // Static variable to track current local working directory
    public static String currentLocalDirectory;

    @Override
    void parse(ArrayList<String> args) {
        this.args = args;
    }
    
    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        currentLocalDirectory = client.sftp.lpwd();
        ArrayList<String> result = new ArrayList<>();

        try {
            // Check if we have the right number of arguments
            if (args.size() < 2) {
                result.add("Error: lcd requires a directory path");
                result.add("Usage: lcd <directory_path>");
                return result;
            }

            // Get the target directory path
            String targetPath = args.get(1);

            // Handle relative paths by resolving against current local directory
            File targetFile;
            if (new File(targetPath).isAbsolute()) {
                targetFile = new File(targetPath);
            } else {
                targetFile = new File(client.sftp.lpwd(), targetPath);
            }

            // Get the canonical path to resolve any .. or . references
            String canonicalPath = targetFile.getCanonicalPath();
            File canonicalFile = new File(canonicalPath);

            // Check if the target exists
            if (!canonicalFile.exists()) {
                result.add("Error: Directory '" + targetPath + "' does not exist");
                return result;
            }

            // Check if the target is a directory
            if (!canonicalFile.isDirectory()) {
                result.add("Error: '" + targetPath + "' is not a directory");
                return result;
            }

            // Check if we have read permission
            if (!canonicalFile.canRead()) {
                result.add("Error: No read permission for directory '" + targetPath + "'");
                return result;
            }

            // Change the current local directory
            currentLocalDirectory = canonicalPath;
            client.sftp.lcd(currentLocalDirectory);

            return result;
        } catch (Exception e) {
            result.add("Error: Failed to change directory - " + e.getMessage());
            return result;
        }
    }
}
