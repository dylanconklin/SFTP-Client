package sftpClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import sftpClient.Intent.LocalSearchIntent;

/**
 * Test class for the Local Search functionality
 */
public class LocalSearchTest {

    public static void main(String[] args) {
        System.out.println("Testing Local Search Functionality");
        System.out.println("=====================================");

        // Create test directory structure
        setupTestFiles();

        // Run various search tests
        runSearchTests();

        // Clean up test files
        cleanupTestFiles();

        System.out.println("\nAll tests completed!");
    }

    private static void setupTestFiles() {
        System.out.println("Setting up test files...");

        try {
            // Create test directories
            new File("test_search/docs").mkdirs();
            new File("test_search/src/main").mkdirs();
            new File("test_search/config").mkdirs();

            // Create test files with different extensions
            createTestFile("test_search/README.txt", "This is a README file with TODO: update documentation");
            createTestFile("test_search/docs/manual.txt", "User manual content with important notes");
            createTestFile("test_search/src/Main.java",
                    "public class Main {\n    // TODO: implement main method\n    public static void main(String[] args) {}\n}");
            createTestFile("test_search/src/Helper.java",
                    "public class Helper {\n    // Helper class for utilities\n}");
            createTestFile("test_search/src/main/App.java",
                    "public class App {\n    private String name = \"MyApp\";\n}");
            createTestFile("test_search/config/settings.json",
                    "{\n  \"name\": \"test-app\",\n  \"version\": \"1.0.0\"\n}");
            createTestFile("test_search/config/database.xml", "<config>\n  <database>test_db</database>\n</config>");
            createTestFile("test_search/large_file.log", generateLargeContent(1000)); // Create a larger file
                                                                                      // System.out.println("Test files
                                                                                      // created successfully");

        } catch (IOException e) {
            System.err.println("Error creating test files: " + e.getMessage());
        }
    }

    private static void createTestFile(String path, String content) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    private static String generateLargeContent(int lines) {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < lines; i++) {
            content.append("Large log file content line ").append(i).append("\n");
        }
        return content.toString();
    }

    private static void runSearchTests() {
        System.out.println("\nRunning search tests...");

        // Test 1: Search by filename pattern
        System.out.println("\n--- Test 1: Search by filename pattern ---");
        testSearch("lsearch -name *.java test_search");

        // Test 2: Search by content
        System.out.println("\n--- Test 2: Search by content ---");
        testSearch("lsearch -content TODO test_search");

        // Test 3: Search by extension
        System.out.println("\n--- Test 3: Search by extension ---");
        testSearch("lsearch -ext txt test_search");

        // Test 4: Search by size
        System.out.println("\n--- Test 4: Search by size ---");
        testSearch("lsearch -size +1K test_search");

        // Test 5: Case sensitive search
        System.out.println("\n--- Test 5: Case sensitive search ---");
        testSearch("lsearch -name *JAVA -case test_search");

        // Test 6: Non-recursive search
        System.out.println("\n--- Test 6: Non-recursive search ---");
        testSearch("lsearch -name *.txt -nr test_search");

        // Test 7: Error handling - invalid path
        System.out.println("\n--- Test 7: Error handling ---");
        testSearch("lsearch -name *.txt nonexistent_path");
    }

    private static void testSearch(String command) {
        System.out.println("Command: " + command);

        // Parse command into arguments
        ArrayList<String> args = new ArrayList<>();
        String[] parts = command.split("\\s+");
        for (String part : parts) {
            args.add(part);
        }

        // Create and execute search intent
        LocalSearchIntent searchIntent = new LocalSearchIntent();
        ArrayList<String> results = searchIntent.execute(null, args);

        // Display results
        for (String result : results) {
            System.out.println(result);
        }
    }

    private static void cleanupTestFiles() {
        System.out.println("\n🧹 Cleaning up test files...");

        try {
            deleteDirectory(new File("test_search"));
            System.out.println("Test files cleaned up successfully");
        } catch (Exception e) {
            System.err.println("Error cleaning up test files: " + e.getMessage());
        }
    }

    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}
