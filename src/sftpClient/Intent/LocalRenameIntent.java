package sftpClient.Intent;

import java.io.File;
import java.util.ArrayList;
import sftpClient.Client.Client;

/**
 * LocalRenameIntent implements the lrn (Local Rename) command.
 * Renames local files and directories.
 */
public class LocalRenameIntent extends Intent {
    
    @Override
    void parse(ArrayList<String> args) {
        this.args = args;
    }
    
    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> result = new ArrayList<>();
        
        try {
            // Check if we have the right number of arguments
            if (args.size() < 3) {
                result.add("Error: lrn requires both source and destination names");
                result.add("Usage: lrn <current_name> <new_name>");
                return result;
            }
            
            if (args.size() > 3) {
                result.add("Warning: lrn ignores extra arguments beyond source and destination");
            }
            
            // Get source and destination names
            String sourceName = args.get(1);
            String destinationName = args.get(2);
            
            // Validate arguments
            if (sourceName.trim().isEmpty()) {
                result.add("Error: Source name cannot be empty");
                return result;
            }
            
            if (destinationName.trim().isEmpty()) {
                result.add("Error: Destination name cannot be empty");
                return result;
            }
            
            // Create File objects
            File sourceFile = new File(sourceName);
            File destinationFile = new File(destinationName);
            
            // Check if source exists
            if (!sourceFile.exists()) {
                result.add("Error: Source '" + sourceName + "' does not exist");
                result.add("Full path: " + sourceFile.getAbsolutePath());
                return result;
            }
            
            // Check if destination already exists
            if (destinationFile.exists()) {
                result.add("Error: Destination '" + destinationName + "' already exists");
                result.add("Full path: " + destinationFile.getAbsolutePath());
                result.add("Cannot overwrite existing files or directories");
                return result;
            }
            
            // Check if source is readable
            if (!sourceFile.canRead()) {
                result.add("Error: No read permission for source '" + sourceName + "'");
                result.add("Full path: " + sourceFile.getAbsolutePath());
                return result;
            }
            
            // Check if parent directory of destination is writable
            File destinationParent = destinationFile.getParentFile();
            if (destinationParent != null && !destinationParent.canWrite()) {
                result.add("Error: No write permission in destination directory");
                result.add("Directory: " + destinationParent.getAbsolutePath());
                return result;
            }
            
            // Perform the rename operation
            boolean success = sourceFile.renameTo(destinationFile);
            
            if (!success) {
                result.add("Error: Failed to rename '" + sourceName + "' to '" + destinationName + "'");
                result.add("This may be due to:");
                result.add("- Files on different filesystems");
                result.add("- Insufficient permissions");
                result.add("- File is in use by another process");
                result.add("- Invalid destination path");
            }
            
            return result;
        } catch (Exception e) {
            result.add("Error: Failed to rename file - " + e.getMessage());
            return result;
        }
    }
    
    /**
     * Format file size in human-readable format
     * @param size File size in bytes
     * @return Formatted size string
     */
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " bytes";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
