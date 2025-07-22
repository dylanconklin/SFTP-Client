package sftpClient.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import sftpClient.Client.Client;

/**
 * Intent for searching files on the local machine
 * Supports multiple search modes:
 * - lsearch -name <pattern> [path] : Search by filename pattern
 * - lsearch -content <text> [path] : Search for text content in files
 * - lsearch -ext <extension> [path] : Search by file extension
 * - lsearch -size <size> [path] : Search by file size (e.g., +1M, -500K)
 */
public class LocalSearchIntent extends Intent {

    private String searchMode = "name"; // Default search mode
    private String searchPattern = "";
    private String searchPath = "."; // Default to current directory
    private boolean caseSensitive = false;
    private boolean recursive = true;

    @Override
    void parse(ArrayList<String> args) {
        // Default values
        searchMode = "name";
        searchPattern = "";
        searchPath = ".";
        caseSensitive = false;
        recursive = true;

        // Parse arguments
        for (int i = 1; i < args.size(); i++) {
            String arg = args.get(i);

            switch (arg) {
                case "-name":
                case "-n":
                    if (i + 1 < args.size()) {
                        searchMode = "name";
                        searchPattern = args.get(++i);
                    }
                    break;

                case "-content":
                case "-c":
                    if (i + 1 < args.size()) {
                        searchMode = "content";
                        searchPattern = args.get(++i);
                    }
                    break;

                case "-ext":
                case "-e":
                    if (i + 1 < args.size()) {
                        searchMode = "extension";
                        searchPattern = args.get(++i);
                        if (!searchPattern.startsWith(".")) {
                            searchPattern = "." + searchPattern;
                        }
                    }
                    break;

                case "-size":
                case "-s":
                    if (i + 1 < args.size()) {
                        searchMode = "size";
                        searchPattern = args.get(++i);
                    }
                    break;

                case "-case":
                    caseSensitive = true;
                    break;

                case "-norecursive":
                case "-nr":
                    recursive = false;
                    break;

                default:
                    // If it's not a flag, treat it as the search path
                    if (!arg.startsWith("-") && searchPath.equals(".")) {
                        searchPath = arg;
                    }
                    break;
            }
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();

        // Parse arguments
        parse(args);

        // Validate search pattern
        if (searchPattern.isEmpty()) {
            output.add("Error: Search pattern is required");
            output.add("Usage: lsearch -name <pattern> [path]");
            output.add("       lsearch -content <text> [path]");
            output.add("       lsearch -ext <extension> [path]");
            output.add("Options: -case (case sensitive), -nr (non-recursive)");
            return output;
        }

        // Validate search path
        File searchDir = new File(searchPath);
        if (!searchDir.exists()) {
            output.add("Error: Search path does not exist: " + searchPath);
            return output;
        }

        output.add("Searching for " + searchMode + ": '" + searchPattern + "' in " + searchPath);
        output.add("   Mode: " + (caseSensitive ? "Case sensitive" : "Case insensitive") +
                ", " + (recursive ? "Recursive" : "Non-recursive"));
        output.add("");

        try {
            ArrayList<File> results = performSearch(searchDir);

            if (results.isEmpty()) {
                output.add("No files found matching the search criteria.");
            } else {
                output.add("Found " + results.size() + " result(s):");
                output.add("");

                for (File file : results) {
                    formatSearchResult(file, output);
                }
            }

        } catch (Exception e) {
            output.add("Error during search: " + e.getMessage());
        }

        return output;
    }

    private ArrayList<File> performSearch(File searchDir) throws IOException {
        ArrayList<File> results = new ArrayList<>();

        if (recursive) {
            searchRecursive(searchDir, results);
        } else {
            searchNonRecursive(searchDir, results);
        }

        return results;
    }

    private void searchRecursive(File dir, ArrayList<File> results) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(dir.getAbsolutePath()))) {
            paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(this::matchesSearchCriteria)
                    .forEach(results::add);
        }
    }

    private void searchNonRecursive(File dir, ArrayList<File> results) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && matchesSearchCriteria(file)) {
                    results.add(file);
                }
            }
        }
    }

    private boolean matchesSearchCriteria(File file) {
        switch (searchMode) {
            case "name":
                return matchesName(file);

            case "content":
                return matchesContent(file);

            case "extension":
                return matchesExtension(file);

            case "size":
                return matchesSize(file);

            default:
                return false;
        }
    }

    private boolean matchesName(File file) {
        String fileName = caseSensitive ? file.getName() : file.getName().toLowerCase();
        String pattern = caseSensitive ? searchPattern : searchPattern.toLowerCase();

        // Convert simple wildcards to regex
        String regexPattern = pattern.replace("*", ".*").replace("?", ".");

        try {
            return Pattern.matches(regexPattern, fileName);
        } catch (Exception e) {
            // If regex fails, fall back to simple contains check
            return fileName.contains(pattern);
        }
    }

    private boolean matchesContent(File file) {
        // Only search in text files (avoid binary files)
        if (!isTextFile(file)) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String searchText = caseSensitive ? searchPattern : searchPattern.toLowerCase();

            while ((line = reader.readLine()) != null) {
                String lineToCheck = caseSensitive ? line : line.toLowerCase();
                if (lineToCheck.contains(searchText)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Skip files that can't be read
        }

        return false;
    }

    private boolean matchesExtension(File file) {
        String fileName = file.getName();
        String extension = caseSensitive ? searchPattern : searchPattern.toLowerCase();
        String fileExt = "";

        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            fileExt = caseSensitive ? fileName.substring(lastDot) : fileName.substring(lastDot).toLowerCase();
        }

        return fileExt.equals(extension);
    }

    private boolean matchesSize(File file) {
        try {
            long fileSize = file.length();
            long targetSize = parseSizePattern(searchPattern);

            if (searchPattern.startsWith("+")) {
                return fileSize > targetSize;
            } else if (searchPattern.startsWith("-")) {
                return fileSize < targetSize;
            } else {
                // Exact size (within 10% tolerance)
                return Math.abs(fileSize - targetSize) <= (targetSize * 0.1);
            }
        } catch (Exception e) {
            return false;
        }
    }

    private long parseSizePattern(String sizePattern) {
        String cleanPattern = sizePattern.replaceAll("[+-]", "");

        if (cleanPattern.toLowerCase().endsWith("k")) {
            return Long.parseLong(cleanPattern.substring(0, cleanPattern.length() - 1)) * 1024;
        } else if (cleanPattern.toLowerCase().endsWith("m")) {
            return Long.parseLong(cleanPattern.substring(0, cleanPattern.length() - 1)) * 1024 * 1024;
        } else if (cleanPattern.toLowerCase().endsWith("g")) {
            return Long.parseLong(cleanPattern.substring(0, cleanPattern.length() - 1)) * 1024 * 1024 * 1024;
        } else {
            return Long.parseLong(cleanPattern);
        }
    }

    private boolean isTextFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".txt") || fileName.endsWith(".java") || fileName.endsWith(".json") ||
                fileName.endsWith(".xml") || fileName.endsWith(".html") || fileName.endsWith(".css") ||
                fileName.endsWith(".js") || fileName.endsWith(".py") || fileName.endsWith(".md") ||
                fileName.endsWith(".yml") || fileName.endsWith(".yaml") || fileName.endsWith(".properties") ||
                fileName.endsWith(".log") || fileName.endsWith(".csv") || fileName.endsWith(".sql");
    }

    private void formatSearchResult(File file, ArrayList<String> output) {
        try {
            String relativePath = getRelativePath(file);
            String size = formatFileSize(file.length());
            String type = file.isDirectory() ? "[DIR]" : "[FILE]";

            output.add(String.format("  %s %s (%s)", type, relativePath, size));

            // If searching by content, show a preview
            if (searchMode.equals("content") && isTextFile(file)) {
                String preview = getContentPreview(file);
                if (!preview.isEmpty()) {
                    output.add("    Preview: " + preview);
                }
            }

        } catch (Exception e) {
            output.add("  [ERROR] " + file.getName() + " (could not read file info)");
        }
    }

    private String getRelativePath(File file) {
        try {
            Path basePath = Paths.get(searchPath).toAbsolutePath();
            Path filePath = file.toPath().toAbsolutePath();
            return basePath.relativize(filePath).toString();
        } catch (Exception e) {
            return file.getAbsolutePath();
        }
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024)
            return bytes + " B";
        if (bytes < 1024 * 1024)
            return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024)
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    private String getContentPreview(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String searchText = caseSensitive ? searchPattern : searchPattern.toLowerCase();

            while ((line = reader.readLine()) != null) {
                String lineToCheck = caseSensitive ? line : line.toLowerCase();
                if (lineToCheck.contains(searchText)) {
                    // Return the first 100 characters of the matching line
                    return line.length() > 100 ? line.substring(0, 100) + "..." : line;
                }
            }
        } catch (IOException e) {
            // Ignore
        }
        return "";
    }
}
