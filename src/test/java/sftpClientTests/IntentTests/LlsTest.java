package test.java.sftpClientTests.IntentTests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import sftpClient.Intent.Intent;
import sftpClient.Intent.LocalListIntent;

/**
 * Unit test for the lls (Local List) command
 * Tests the functionality of LocalListIntent
 */
public class LlsTest {
    
    public static void main(String[] args) {
        System.out.println("=== LLS Command Unit Test ===\n");
        
        boolean allTestsPassed = true;
        
        // Test 1: Intent Registration
        allTestsPassed &= testIntentRegistration();
        
        // Test 2: Basic Functionality (current directory)
        allTestsPassed &= testBasicFunctionality();
        
        // Test 3: Directory Path Argument
        allTestsPassed &= testDirectoryPathArgument();
        
        // Test 4: Non-existent Directory Handling
        allTestsPassed &= testNonExistentDirectory();
        
        // Test 5: File vs Directory Handling
        allTestsPassed &= testFileVsDirectoryHandling();
        
        // Test 6: Empty Directory Handling
        allTestsPassed &= testEmptyDirectoryHandling();
        
        // Summary
        System.out.println("\n========================================");
        if (allTestsPassed) {
            System.out.println("ALL TESTS PASSED! lls command is working correctly.");
        } else {
            System.out.println("SOME TESTS FAILED! Please check the implementation.");
        }
        System.out.println("========================================");
    }
    
    /**
     * Test 1: Verify lls command is properly registered in Intent system
     */
    private static boolean testIntentRegistration() {
        System.out.println("[TEST] Test 1: Intent Registration");
        
        try {
            Intent intent = Intent.getIntent("lls");
            
            if (intent == null) {
                System.out.println("FAIL: Intent.getIntent(\"lls\") returned null");
                System.out.println("Check if lls case is added to Intent.java switch statement");
                return false;
            }
            
            if (!(intent instanceof LocalListIntent)) {
                System.out.println("FAIL: Wrong intent type returned");
                System.out.println("Expected: LocalListIntent");
                System.out.println("Actual: " + intent.getClass().getSimpleName());
                return false;
            }
            
            System.out.println("[PASS] lls command properly registered");
            System.out.println("   Returns correct LocalListIntent instance");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during intent registration test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 2: Test basic lls functionality (current directory)
     */
    private static boolean testBasicFunctionality() {
        System.out.println("\n[TEST] Test 2: Basic lls Functionality (Current Directory)");
        
        try {
            Intent intent = Intent.getIntent("lls");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lls"));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null) {
                System.out.println("[FAIL] execute() returned null");
                return false;
            }
            
            if (result.isEmpty()) {
                System.out.println("[FAIL] execute() returned empty result");
                return false;
            }
            
            // Check if output contains expected elements
            String firstLine = result.get(0);
            if (!firstLine.startsWith("Listing directory:")) {
                System.out.println("[FAIL] First line should start with 'Listing directory:'");
                System.out.println("Actual: " + firstLine);
                return false;
            }
            
            // Check for header line
            boolean hasHeader = false;
            for (String line : result) {
                if (line.contains("Type") && line.contains("Size") && line.contains("Name")) {
                    hasHeader = true;
                    break;
                }
            }
            
            if (!hasHeader) {
                System.out.println("[FAIL] Missing column headers");
                return false;
            }
            
            System.out.println("[PASS] lls returns proper directory listing format");
            System.out.println("   Found " + result.size() + " lines of output");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during basic functionality test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 3: Test lls with directory path argument
     */
    private static boolean testDirectoryPathArgument() {
        System.out.println("\n[TEST] Test 3: Directory Path Argument");
        
        try {
            Intent intent = Intent.getIntent("lls");
            
            // Test with current directory explicitly
            String currentDir = System.getProperty("user.dir");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lls", currentDir));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lls failed with directory path argument");
                return false;
            }
            
            String firstLine = result.get(0);
            if (!firstLine.contains(currentDir)) {
                System.out.println("[FAIL] Output doesn't contain specified directory path");
                System.out.println("Expected path: " + currentDir);
                System.out.println("Actual: " + firstLine);
                return false;
            }
            
            System.out.println("[PASS] lls works correctly with directory path argument");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception with directory path argument");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 4: Test lls with non-existent directory
     */
    private static boolean testNonExistentDirectory() {
        System.out.println("\n[TEST] Test 4: Non-existent Directory Handling");
        
        try {
            Intent intent = Intent.getIntent("lls");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lls", "/non/existent/directory"));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lls should return error message for non-existent directory");
                return false;
            }
            
            String errorMessage = result.get(0);
            if (!errorMessage.toLowerCase().contains("error") || !errorMessage.contains("does not exist")) {
                System.out.println("[FAIL] Expected error message for non-existent directory");
                System.out.println("Actual: " + errorMessage);
                return false;
            }
            
            System.out.println("[PASS] lls handles non-existent directory correctly");
            System.out.println("   Error message: " + errorMessage);
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception with non-existent directory");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 5: Test lls with a file path (should show error)
     */
    private static boolean testFileVsDirectoryHandling() {
        System.out.println("\n[TEST] Test 5: File vs Directory Handling");
        
        try {
            Intent intent = Intent.getIntent("lls");
            
            // Try to find a file in the current directory to test with
            File currentDir = new File(System.getProperty("user.dir"));
            File[] files = currentDir.listFiles();
            String testFilePath = null;
            
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        testFilePath = file.getAbsolutePath();
                        break;
                    }
                }
            }
            
            if (testFilePath == null) {
                System.out.println("[SKIP] No files found in current directory to test with");
                return true;
            }
            
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lls", testFilePath));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lls should return error message for file path");
                return false;
            }
            
            String errorMessage = result.get(0);
            if (!errorMessage.toLowerCase().contains("error") || !errorMessage.contains("not a directory")) {
                System.out.println("[FAIL] Expected error message for file path");
                System.out.println("Actual: " + errorMessage);
                return false;
            }
            
            System.out.println("[PASS] lls handles file path correctly");
            System.out.println("   Error message: " + errorMessage);
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception with file path test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 6: Test lls with empty directory (create temporary empty directory)
     */
    private static boolean testEmptyDirectoryHandling() {
        System.out.println("\n[TEST] Test 6: Empty Directory Handling");
        
        File tempDir = null;
        try {
            Intent intent = Intent.getIntent("lls");
            
            // Create a temporary empty directory
            tempDir = new File(System.getProperty("java.io.tmpdir"), "lls_test_empty_" + System.currentTimeMillis());
            if (!tempDir.mkdir()) {
                System.out.println("[SKIP] Could not create temporary directory for test");
                return true;
            }
            
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lls", tempDir.getAbsolutePath()));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lls should handle empty directory");
                return false;
            }
            
            // Check if it mentions the directory is empty
            boolean hasEmptyMessage = false;
            for (String line : result) {
                if (line.toLowerCase().contains("empty")) {
                    hasEmptyMessage = true;
                    break;
                }
            }
            
            if (!hasEmptyMessage) {
                System.out.println("[FAIL] Expected message about empty directory");
                System.out.println("Output: " + result);
                return false;
            }
            
            System.out.println("[PASS] lls handles empty directory correctly");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception with empty directory test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        } finally {
            // Clean up temporary directory
            if (tempDir != null && tempDir.exists()) {
                tempDir.delete();
            }
        }
    }
}
