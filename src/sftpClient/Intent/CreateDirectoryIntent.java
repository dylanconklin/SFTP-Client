package sftpClient.Intent;

import java.util.ArrayList;
import java.util.logging.Logger;
import sftpClient.Client.Client;

public class CreateDirectoryIntent extends Intent {
    private static final Logger logger = Logger.getLogger(CreateDirectoryIntent.class.getName());

    private boolean createParents = false;
    private boolean verbose = false;
    private String directoryPath = "";

    @Override
    public void parse(ArrayList<String> args) {
        // Reset flags
        createParents = false;
        verbose = false;
        directoryPath = "";

        // Remove command name if present
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("mkdir")) {
            args.remove(0);
        }

        // Parse flags and directory path
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);

            if (arg.startsWith("-")) {
                // Handle flags
                if (arg.equals("-p") || arg.equals("--parents")) {
                    createParents = true;
                } else if (arg.equals("-v") || arg.equals("--verbose")) {
                    verbose = true;
                } else if (arg.equals("-pv") || arg.equals("-vp")) {
                    createParents = true;
                    verbose = true;
                } else {
                    throw new IllegalArgumentException("Unknown flag: " + arg);
                }
            } else {
                // This should be the directory path
                if (directoryPath.isEmpty()) {
                    directoryPath = arg.trim();
                } else {
                    throw new IllegalArgumentException("Multiple directory paths specified");
                }
            }
        }

        if (directoryPath.isEmpty()) {
            throw new IllegalArgumentException("Directory path is required");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> result = new ArrayList<>();

        try {
            // Parse arguments
            parse(args);

            // If no client provided (testing mode)
            if (client == null) {
                result.add("mkdir command parsed successfully:");
                result.add("  Directory: " + directoryPath);
                result.add("  Create parents: " + createParents);
                result.add("  Verbose: " + verbose);
                result.add("  Note: No SFTP connection - command parsing only");
                logger.info("mkdir test mode: " + result.toString());
                return result;
            }

            // Create directory based on flags
            if (createParents) {
                createDirectoryWithParents(client, directoryPath, result);
            } else {
                createSingleDirectory(client, directoryPath, result);
            }

        } catch (IllegalArgumentException e) {
            result.add("Error: " + e.getMessage());
            result.add("Usage: mkdir [-p] [-v] <directory>");
            result.add("  -p, --parents    Create parent directories as needed");
            result.add("  -v, --verbose    Show verbose output");
            logger.warning("mkdir argument error: " + e.getMessage());
        } catch (Exception e) {
            result.add("Error creating directory: " + e.getMessage());
            logger.severe("mkdir execution error: " + e.getMessage());
        }

        logger.info("mkdir result: " + result.toString());
        return result;
    }

    /**
     * Create a single directory using existing client method
     */
    private void createSingleDirectory(Client client, String path, ArrayList<String> result) {
        try {
            client.createDirectory(path);
            if (verbose) {
                result.add("Created directory: " + path);
            }
            logger.info("Created directory: " + path);
        } catch (Exception e) {
            // Check error message to provide better feedback
            String errorMsg = e.getMessage().toLowerCase();
            if (errorMsg.contains("already exists") || errorMsg.contains("file exists")) {
                result.add("Error: Directory already exists: " + path);
            } else if (errorMsg.contains("no such file") || errorMsg.contains("parent")) {
                result.add("Error: Parent directory does not exist: " + path);
                result.add("Use -p flag to create parent directories");
            } else {
                result.add("Error creating directory: " + e.getMessage());
            }
            logger.warning("Failed to create directory " + path + ": " + e.getMessage());
        }
    }

    /**
     * Create directory with parent directories as needed
     */
    private void createDirectoryWithParents(Client client, String path, ArrayList<String> result) {
        try {
            // Split path into components
            String[] pathParts = path.split("/");
            String currentPath = "";
            boolean hasCreatedAny = false;

            // Handle absolute vs relative paths
            boolean isAbsolute = path.startsWith("/");
            if (isAbsolute) {
                currentPath = "/";
            }

            // Create each directory level
            for (String part : pathParts) {
                if (part.isEmpty()) continue;

                if (!currentPath.isEmpty() && !currentPath.endsWith("/")) {
                    currentPath += "/";
                }
                currentPath += part;

                try {
                    // Try to create the directory using client method
                    client.createDirectory(currentPath);
                    hasCreatedAny = true;
                    if (verbose) {
                        result.add("Created directory: " + currentPath);
                    }
                    logger.info("Created directory: " + currentPath);
                } catch (Exception e) {
                    String errorMsg = e.getMessage().toLowerCase();
                    if (errorMsg.contains("already exists") || errorMsg.contains("file exists")) {
                        // Directory already exists, that's fine for -p
                        if (verbose) {
                            result.add("Directory already exists: " + currentPath);
                        }
                    } else {
                        // Other error, propagate it
                        throw e;
                    }
                }
            }

            if (!hasCreatedAny) {
                result.add("Directory already exists: " + path);
            }
        } catch (Exception e) {
            result.add("Error creating directory path: " + e.getMessage());
            logger.severe("Failed to create directory path " + path + ": " + e.getMessage());
        }
    }
}