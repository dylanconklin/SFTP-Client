package sftpClient.Intent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import sftpClient.Client.Client;

public class LocalListIntent extends Intent {

    @Override
    void parse(ArrayList<String> args) {
        // lls command can accept an optional directory path
        // args[0] = "lls"
        // args[1] = optional directory path
        if (args.size() > 2) {
            System.out.println("Warning: lls command accepts at most one directory path argument");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        
        try {
            // Determine the directory to list
            String directoryPath;
            if (args.size() > 1 && !args.get(1).trim().isEmpty()) {
                // Use provided directory path
                directoryPath = args.get(1);
            } else {
                // Use current working directory
                directoryPath = System.getProperty("user.dir");
            }
            
            File directory = new File(directoryPath);
            
            // Check if directory exists and is actually a directory
            if (!directory.exists()) {
                output.add("Error: Directory '" + directoryPath + "' does not exist");
                return output;
            }
            
            if (!directory.isDirectory()) {
                output.add("Error: '" + directoryPath + "' is not a directory");
                return output;
            }
            
            // Get list of files and directories
            File[] files = directory.listFiles();
            
            if (files == null) {
                output.add("Error: Unable to read directory '" + directoryPath + "'");
                return output;
            }
            
            if (files.length == 0) {
                output.add("Directory is empty: " + directoryPath);
                return output;
            }
            
            // Add header
            output.add("Listing directory: " + directory.getAbsolutePath());
            output.add("Type    Size         Modified                 Name");
            output.add("----    ----         --------                 ----");
            
            // Format date for display
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            // List files and directories
            for (File file : files) {
                StringBuilder line = new StringBuilder();
                
                // Type (DIR or FILE)
                if (file.isDirectory()) {
                    line.append("<DIR>   ");
                } else {
                    line.append("<FILE>  ");
                }
                
                // Size (show size for files, empty for directories)
                if (file.isFile()) {
                    long size = file.length();
                    line.append(String.format("%10d   ", size));
                } else {
                    line.append("           ");  // 11 spaces for alignment
                }
                
                // Last modified date
                Date lastModified = new Date(file.lastModified());
                line.append(dateFormat.format(lastModified));
                line.append("   ");
                
                // Name
                line.append(file.getName());
                
                output.add(line.toString());
            }
            
            // Add summary
            int fileCount = 0;
            int dirCount = 0;
            for (File file : files) {
                if (file.isFile()) {
                    fileCount++;
                } else if (file.isDirectory()) {
                    dirCount++;
                }
            }
            
            output.add("");
            output.add(String.format("Total: %d files, %d directories", fileCount, dirCount));
            
        } catch (Exception e) {
            output.add("Error listing directory: " + e.getMessage());
        }
        
        return output;
    }
}
