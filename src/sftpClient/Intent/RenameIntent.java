package sftpClient.Intent;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import sftpClient.Client.Client;

/**
 * RenameIntent implements the rename command for renaming files and directories on the remote SFTP server.
 * Supports renaming files and directories, cross-directory moves, and comprehensive error handling.
 */
public class RenameIntent extends Intent {
    private static final Logger logger = Logger.getLogger(RenameIntent.class.getName());

    @Override
    public void parse(ArrayList<String> args) {
        // Remove the "rename" command if present
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("rename")) {
            args.remove(0);
        }
        
        // Validate that we have arguments
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Rename requires source and destination arguments.");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        
        try {
            // Parse arguments
            parse(args);
            
            // Check argument count
            if (args.size() < 2) {
                output.add("Usage: rename <old_filename> <new_filename>");
                output.add("  Renames a file or directory on the remote server");
                output.add("  Examples:");
                output.add("    rename oldfile.txt newfile.txt");
                output.add("    rename documents/old.pdf documents/new.pdf");
                output.add("    rename folder1 folder2");
                logger.info("rename usage displayed: insufficient arguments");
                return output;
            }
            
            if (args.size() > 2) {
                output.add("Warning: Extra arguments ignored beyond source and destination");
            }

            String oldName = args.get(0).trim();
            String newName = args.get(1).trim();
            
            // Validate arguments
            if (oldName.isEmpty()) {
                output.add("Error: Source filename cannot be empty");
                logger.warning("rename error: empty source filename");
                return output;
            }
            
            if (newName.isEmpty()) {
                output.add("Error: Destination filename cannot be empty");
                logger.warning("rename error: empty destination filename");
                return output;
            }
            
            // Check if source and destination are the same
            if (oldName.equals(newName)) {
                output.add("Warning: Source and destination are identical");
                output.add("No operation performed: " + oldName);
                logger.info("rename skipped: source equals destination (" + oldName + ")");
                return output;
            }
            
            // If no client provided (testing mode)
            if (client == null) {
                output.add("rename command parsed successfully:");
                output.add("  Source: " + oldName);
                output.add("  Destination: " + newName);
                output.add("  Note: No SFTP connection - command parsing only");
                logger.info("rename test mode: " + oldName + " -> " + newName);
                return output;
            }

            // Perform the rename operation
            client.sftp.rename(oldName, newName);
            
            // Success message
            output.add("Successfully renamed '" + oldName + "' to '" + newName + "'");
            
            // Add helpful information about the operation
            if (oldName.contains("/") || newName.contains("/")) {
                output.add("Cross-directory rename completed");
            }
            
            logger.info("Successfully renamed " + oldName + " to " + newName);
            
        } catch (IllegalArgumentException e) {
            output.add("Error: " + e.getMessage());
            output.add("Usage: rename <old_filename> <new_filename>");
            logger.warning("rename argument error: " + e.getMessage());
        } catch (SftpException e) {
            // Provide more specific error messages based on SFTP error codes
            String errorMessage = "Failed to rename '" + args.get(0) + "' to '" + args.get(1) + "'";
            String detailedMessage = e.getMessage().toLowerCase();
            
            if (detailedMessage.contains("no such file") || detailedMessage.contains("not found")) {
                output.add(errorMessage);
                output.add("Reason: Source file or directory does not exist");
            } else if (detailedMessage.contains("permission") || detailedMessage.contains("access")) {
                output.add(errorMessage);
                output.add("Reason: Insufficient permissions");
            } else if (detailedMessage.contains("file exists") || detailedMessage.contains("already exists")) {
                output.add(errorMessage);
                output.add("Reason: Destination already exists");
            } else if (detailedMessage.contains("directory") && detailedMessage.contains("not empty")) {
                output.add(errorMessage);
                output.add("Reason: Cannot rename to non-empty directory");
            } else {
                output.add(errorMessage);
                output.add("Reason: " + e.getMessage());
            }
            
            logger.warning("Failed to rename " + args.get(0) + " to " + args.get(1) + ": " + e.getMessage());
        } catch (Exception e) {
            output.add("Error: Unexpected error during rename operation");
            output.add("Details: " + e.getMessage());
            logger.severe("Unexpected error during rename: " + e.getMessage());
        }
        
        logger.info("rename result: " + output.toString());
        return output;
    }
}
