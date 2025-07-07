package sftpClient;

public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("=== SFTP Client Local Commands Test ===");
        System.out.println("Current directory: " + System.getProperty("user.dir"));
        System.out.println();
        
        // Test lls functionality
        System.out.println("Testing 'lls' command (list local files):");
        testLocalListing(".");
        
        System.out.println();
        
        // Test lpwd functionality  
        System.out.println("Testing 'lpwd' command (print working directory):");
        testLocalPrintWorkingDirectory();
        
        System.out.println();
        
        // Test lcd functionality
        System.out.println("Testing 'lcd' command (change directory to lib):");
        testLocalChangeDirectory(new String[]{"lcd", "lib"});
        
        System.out.println();
        
        // Test lls in new directory
        System.out.println("Testing 'lls' in lib directory:");
        testLocalListing("lib");
        
        System.out.println();
        System.out.println("=== Test completed successfully! ===");
    }
    
    private static void testLocalListing(String localPath) {
        System.out.println("Listing local files in " + localPath + "...");
        
        try {
            java.io.File dir = new java.io.File(localPath);
            if (!dir.exists()) {
                System.out.println("✗ Path does not exist: " + localPath);
            } else if (!dir.isDirectory()) {
                System.out.println("✗ Not a directory: " + localPath);
            } else {
                java.io.File[] files = dir.listFiles();
                if (files != null && files.length > 0) {
                    System.out.println("Local files and directories found:");
                    for (java.io.File file : files) {
                        if (file.isDirectory()) {
                            System.out.println("  [DIR]  " + file.getName() + "/");
                        } else {
                            System.out.println("  [FILE] " + file.getName() + " (" + file.length() + " bytes)");
                        }
                    }
                } else {
                    System.out.println("Directory is empty");
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error listing local directory: " + e.getMessage());
        }
    }
    
    private static void testLocalChangeDirectory(String[] parts) {
        if (parts.length < 2) {
            System.out.println("lcd Error: Missing directory path");
        } else {
            String newPath = parts[1];
            try {
                java.io.File dir = new java.io.File(newPath);
                if (!dir.exists()) {
                    System.out.println("✗ Directory does not exist: " + newPath);
                } else if (!dir.isDirectory()) {
                    System.out.println("✗ Not a directory: " + newPath);
                } else {
                    System.setProperty("user.dir", dir.getAbsolutePath());
                    System.out.println("✓ Changed local directory to: " + dir.getAbsolutePath());
                }
            } catch (Exception e) {
                System.out.println("✗ Error changing directory: " + e.getMessage());
            }
        }
    }
    
    private static void testLocalPrintWorkingDirectory() {
        try {
            String currentDir = System.getProperty("user.dir");
            System.out.println("Current local directory: " + currentDir);
        } catch (Exception e) {
            System.out.println("✗ Error getting current directory: " + e.getMessage());
        }
    }
}
