package sftpClient;

import sftpClient.CredentialManager.CredentialManager;
import sftpClient.CredentialManager.Credentials;
import sftpClient.IO.IO;

import java.util.ArrayList;
import java.util.Objects;

public class REPL {
    Credentials credentials;
    SFTPManager sftpManager;

    REPL() {
        this.credentials = CredentialManager.getLoginCredentials(CredentialManager.credentialFile());
        this.sftpManager = new SFTPManager(credentials);

        // Attempt to connect to SFTP server
        System.out.println("Connecting to SFTP server...");
        if (sftpManager.connect()) {
            System.out.println("Connected successfully!");
        } else {
            System.out.println("Failed to connect to SFTP server. Some commands may not work.");
        }
    }

    public void repl() {
        String input = "";
        ArrayList<String> output = new ArrayList<>();
        while (!Objects.equals(input, "quit") && !Objects.equals(input, "exit")) {
            input = read();
            output = eval(input);
            print(output);
        }
        // Disconnect when exiting
        if (sftpManager != null) {
            sftpManager.disconnect();
            System.out.println("Disconnected from SFTP server.");
        }
    }

    public String read() {
        return IO.getInputFromUser(">>> ");
    }

    public ArrayList<String> eval(String input) {
        ArrayList<String> output = new ArrayList<>();
        String[] parts = input.trim().split("\\s+");
        String command = parts[0];
        switch (command) {
            case "get":
                if (parts.length < 2) {
                    output.add("get Error: Missing Parameters Like File Names");
                } else {
                    String filename = parts[1];
                    String localPath = parts.length > 2 ? parts[2] : filename; // Use local name if provided
                    output.add("Downloading " + filename + " to " + localPath + "... Please wait");

                    if (sftpManager != null && sftpManager.isConnected()) {
                        if (sftpManager.downloadFile(filename, localPath)) {
                            output.add("✓ Successfully downloaded " + filename);
                        } else {
                            output.add("✗ Failed to download " + filename);
                        }
                    } else {
                        output.add("✗ Not connected to SFTP server");
                    }
                }
                break;

            case "put":
                if (parts.length < 2) {
                    output.add("Put Error: Missing Parameters Like File Names");
                } else {
                    String filename = parts[1];
                    String remotePath = parts.length > 2 ? parts[2] : filename; // Use remote name if provided
                    output.add("Uploading " + filename + " to " + remotePath + "... Please wait");

                    if (sftpManager != null && sftpManager.isConnected()) {
                        if (sftpManager.uploadFile(filename, remotePath)) {
                            output.add("✓ Successfully uploaded " + filename);
                        } else {
                            output.add("✗ Failed to upload " + filename);
                        }
                    } else {
                        output.add("✗ Not connected to SFTP server");
                    }
                }
                break;

            case "ls":
                String remotePath = parts.length > 1 ? parts[1] : "."; // Use current directory if no path provided
                output.add("Listing files in " + remotePath + "...");

                if (sftpManager != null && sftpManager.isConnected()) {
                    String[] files = sftpManager.listFiles(remotePath);
                    if (files.length > 0) {
                        output.add("Files found:");
                        for (String file : files) {
                            output.add("  " + file);
                        }
                    } else {
                        output.add("No files found or directory doesn't exist");
                    }
                } else {
                    output.add("✗ Not connected to SFTP server");
                }
                break;

            case "rm":
                if (parts.length < 2) {
                    output.add("Rm Error: Missing Parameters Like File Names");
                } else {
                    String filename = parts[1];
                    output.add("Deleting " + filename + "... Please wait");

                    if (sftpManager != null && sftpManager.isConnected()) {
                        if (sftpManager.deleteFile(filename)) {
                            output.add("✓ Successfully deleted " + filename);
                        } else {
                            output.add("✗ Failed to delete " + filename);
                        }
                    } else {
                        output.add("✗ Not connected to SFTP server");
                    }
                }
                break;

            case "exit":
                output.add("Exiting FTP client.");
                break;

            case "help":
                output.add("Available commands:");
                output.add("Remote server commands:");
                output.add("  get <remote_file> [local_file] - Download file from server");
                output.add("  put <local_file> [remote_file] - Upload file to server");
                output.add("  ls [remote_path]               - List files in remote directory");
                output.add("  rm <remote_file>               - Delete file on server");
                output.add("");
                output.add("Local file system commands:");
                output.add("  lls [local_path]               - List local files and directories");
                output.add("  lcd <local_path>               - Change local directory");
                output.add("  lpwd                           - Show current local directory");
                output.add("");
                output.add("General commands:");
                output.add("  help                           - Show this help message");
                output.add("  exit/quit                      - Exit the application");
                break;

            case "lls":
                String localPath = parts.length > 1 ? parts[1] : "."; // Use current directory if no path provided
                output.add("Listing local files in " + localPath + "...");

                try {
                    java.io.File dir = new java.io.File(localPath);
                    if (!dir.exists()) {
                        output.add("✗ Path does not exist: " + localPath);
                    } else if (!dir.isDirectory()) {
                        output.add("✗ Not a directory: " + localPath);
                    } else {
                        java.io.File[] files = dir.listFiles();
                        if (files != null && files.length > 0) {
                            output.add("Local files and directories found:");
                            for (java.io.File file : files) {
                                if (file.isDirectory()) {
                                    output.add("  [DIR]  " + file.getName() + "/");
                                } else {
                                    output.add("  [FILE] " + file.getName() + " (" + file.length() + " bytes)");
                                }
                            }
                        } else {
                            output.add("Directory is empty");
                        }
                    }
                } catch (Exception e) {
                    output.add("✗ Error listing local directory: " + e.getMessage());
                }
                break;

            case "lcd":
                if (parts.length < 2) {
                    output.add("lcd Error: Missing directory path");
                } else {
                    String newPath = parts[1];
                    try {
                        java.io.File dir = new java.io.File(newPath);
                        if (!dir.exists()) {
                            output.add("✗ Directory does not exist: " + newPath);
                        } else if (!dir.isDirectory()) {
                            output.add("✗ Not a directory: " + newPath);
                        } else {
                            System.setProperty("user.dir", dir.getAbsolutePath());
                            output.add("✓ Changed local directory to: " + dir.getAbsolutePath());
                        }
                    } catch (Exception e) {
                        output.add("✗ Error changing directory: " + e.getMessage());
                    }
                }
                break;

            case "lpwd":
                try {
                    String currentDir = System.getProperty("user.dir");
                    output.add("Current local directory: " + currentDir);
                } catch (Exception e) {
                    output.add("✗ Error getting current directory: " + e.getMessage());
                }
                break;

            default:
                output.add("Unknown Command: " + command + " (type 'help' for available commands)");
        }

        return output;
    }

    public void print(ArrayList<String> output) {
        output.forEach(System.out::println);
    }
}
