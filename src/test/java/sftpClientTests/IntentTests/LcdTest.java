package test.java.sftpClientTests.IntentTests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import sftpClient.Intent.Intent;
import sftpClient.Intent.LocalChangeDirectoryIntent;

/**
 * Unit test for the lcd (Local Change Directory) command
 * Tests the functionality of LocalChangeDirectoryIntent
 */
public class LcdTest {
    
    public static void main(String[] args) {
        System.out.println("=== LCD Command Unit Test ===\n");
        
        boolean allTestsPassed = true;
        
        // Store original directory to restore later
        String originalDirectory = LocalChangeDirectoryIntent.getCurrentLocalDirectory();
        
        try {
            // Test 1: Intent Registration
            allTestsPassed &= testIntentRegistration();
            
            // Test 2: Change to Parent Directory
            allTestsPassed &= testChangeToParentDirectory();
            
            // Test 3: Change to Absolute Path
            allTestsPassed &= testChangeToAbsolutePath();
            
            // Test 4: Change to Relative Path
            allTestsPassed &= testChangeToRelativePath();
            
            // Test 5: Non-existent Directory Handling
            allTestsPassed &= testNonExistentDirectory();
            
            // Test 6: File vs Directory Handling
            allTestsPassed &= testFileVsDirectoryHandling();
            
            // Test 7: Missing Arguments
            allTestsPassed &= testMissingArguments();
            
            // Test 8: Current Directory (.)
            allTestsPassed &= testCurrentDirectory();
            
        } finally {
            // Restore original directory
            LocalChangeDirectoryIntent.setCurrentLocalDirectory(originalDirectory);
        }
        
        // Summary
        System.out.println("\n========================================");
        if (allTestsPassed) {
            System.out.println("ALL TESTS PASSED! lcd command is working correctly.");
        } else {
            System.out.println("SOME TESTS FAILED! Please check the implementation.");
        }
        System.out.println("========================================");
    }
    
    /**
     * Test 1: Verify lcd command is properly registered in Intent system
     */
    private static boolean testIntentRegistration() {
        System.out.println("[TEST] Test 1: Intent Registration");
        
        try {
            Intent intent = Intent.getIntent("lcd");
            
            if (intent == null) {
                System.out.println("FAIL: Intent.getIntent(\"lcd\") returned null");
                System.out.println("Check if lcd case is added to Intent.java switch statement");
                return false;
            }
            
            if (!(intent instanceof LocalChangeDirectoryIntent)) {
                System.out.println("FAIL: Wrong intent type returned");
                System.out.println("Expected: LocalChangeDirectoryIntent");
                System.out.println("Actual: " + intent.getClass().getSimpleName());
                return false;
            }
            
            System.out.println("[PASS] lcd command properly registered");
            System.out.println("   Returns correct LocalChangeDirectoryIntent instance");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during intent registration test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 2: Test changing to parent directory (..)
     */
    private static boolean testChangeToParentDirectory() {
        System.out.println("\n[TEST] Test 2: Change to Parent Directory (..)");
        
        try {
            Intent intent = Intent.getIntent("lcd");
            String currentDir = LocalChangeDirectoryIntent.getCurrentLocalDirectory();
            
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lcd", ".."));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lcd .. returned no result");
                return false;
            }
            
            String firstLine = result.get(0);
            if (!firstLine.contains("successfully")) {
                System.out.println("[FAIL] Expected success message");
                System.out.println("Actual: " + firstLine);
                return false;
            }
            
            String newDir = LocalChangeDirectoryIntent.getCurrentLocalDirectory();
            if (newDir.equals(currentDir)) {
                System.out.println("[FAIL] Directory did not change");
                return false;
            }
            
            System.out.println("[PASS] lcd .. works correctly");
            System.out.println("   Changed from: " + currentDir);
            System.out.println("   Changed to: " + newDir);
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during parent directory test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 3: Test changing to absolute path
     */
    private static boolean testChangeToAbsolutePath() {
        System.out.println("\n[TEST] Test 3: Change to Absolute Path");
        
        try {
            Intent intent = Intent.getIntent("lcd");
            String tempDir = System.getProperty("java.io.tmpdir");
            
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lcd", tempDir));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lcd with absolute path returned no result");
                return false;
            }
            
            String firstLine = result.get(0);
            if (!firstLine.contains("successfully")) {
                System.out.println("[FAIL] Expected success message");
                System.out.println("Actual: " + firstLine);
                return false;
            }
            
            String newDir = LocalChangeDirectoryIntent.getCurrentLocalDirectory();
            if (!newDir.equals(new File(tempDir).getCanonicalPath())) {
                System.out.println("[FAIL] Directory not changed to expected path");
                System.out.println("Expected: " + new File(tempDir).getCanonicalPath());
                System.out.println("Actual: " + newDir);
                return false;
            }
            
            System.out.println("[PASS] lcd with absolute path works correctly");
            System.out.println("   Changed to: " + newDir);
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during absolute path test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 4: Test changing to relative path
     */
    private static boolean testChangeToRelativePath() {
        System.out.println("\n[TEST] Test 4: Change to Relative Path");
        
        try {
            Intent intent = Intent.getIntent("lcd");
            
            // First change to a known directory (temp)
            String tempDir = System.getProperty("java.io.tmpdir");
            LocalChangeDirectoryIntent.setCurrentLocalDirectory(tempDir);
            
            // Find a subdirectory in temp to test with
            File tempFile = new File(tempDir);
            File[] subdirs = tempFile.listFiles(File::isDirectory);
            
            if (subdirs == null || subdirs.length == 0) {
                System.out.println("[SKIP] No subdirectories found in temp directory to test with");
                return true;
            }
            
            String targetSubdir = subdirs[0].getName();
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lcd", targetSubdir));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lcd with relative path returned no result");
                return false;
            }
            
            String firstLine = result.get(0);
            if (!firstLine.contains("successfully")) {
                System.out.println("[FAIL] Expected success message");
                System.out.println("Actual: " + firstLine);
                return false;
            }
            
            System.out.println("[PASS] lcd with relative path works correctly");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during relative path test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 5: Test lcd with non-existent directory
     */
    private static boolean testNonExistentDirectory() {
        System.out.println("\n[TEST] Test 5: Non-existent Directory Handling");
        
        try {
            Intent intent = Intent.getIntent("lcd");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lcd", "/non/existent/directory"));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lcd should return error message for non-existent directory");
                return false;
            }
            
            String errorMessage = result.get(0);
            if (!errorMessage.toLowerCase().contains("error") || !errorMessage.contains("does not exist")) {
                System.out.println("[FAIL] Expected error message for non-existent directory");
                System.out.println("Actual: " + errorMessage);
                return false;
            }
            
            System.out.println("[PASS] lcd handles non-existent directory correctly");
            System.out.println("   Error message: " + errorMessage);
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception with non-existent directory");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 6: Test lcd with a file path (should show error)
     */
    private static boolean testFileVsDirectoryHandling() {
        System.out.println("\n[TEST] Test 6: File vs Directory Handling");
        
        try {
            Intent intent = Intent.getIntent("lcd");
            
            // Try to find a file in the current directory to test with
            File currentDir = new File(LocalChangeDirectoryIntent.getCurrentLocalDirectory());
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
            
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lcd", testFilePath));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lcd should return error message for file path");
                return false;
            }
            
            String errorMessage = result.get(0);
            if (!errorMessage.toLowerCase().contains("error") || !errorMessage.contains("not a directory")) {
                System.out.println("[FAIL] Expected error message for file path");
                System.out.println("Actual: " + errorMessage);
                return false;
            }
            
            System.out.println("[PASS] lcd handles file path correctly");
            System.out.println("   Error message: " + errorMessage);
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception with file path test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 7: Test lcd with missing arguments
     */
    private static boolean testMissingArguments() {
        System.out.println("\n[TEST] Test 7: Missing Arguments");
        
        try {
            Intent intent = Intent.getIntent("lcd");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lcd"));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lcd should return error message for missing arguments");
                return false;
            }
            
            String errorMessage = result.get(0);
            if (!errorMessage.toLowerCase().contains("error") || !errorMessage.contains("requires")) {
                System.out.println("[FAIL] Expected error message for missing arguments");
                System.out.println("Actual: " + errorMessage);
                return false;
            }
            
            System.out.println("[PASS] lcd handles missing arguments correctly");
            System.out.println("   Error message: " + errorMessage);
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception with missing arguments test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 8: Test lcd with current directory (.)
     */
    private static boolean testCurrentDirectory() {
        System.out.println("\n[TEST] Test 8: Current Directory (.)");
        
        try {
            Intent intent = Intent.getIntent("lcd");
            String beforeDir = LocalChangeDirectoryIntent.getCurrentLocalDirectory();
            
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lcd", "."));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lcd . returned no result");
                return false;
            }
            
            String firstLine = result.get(0);
            if (!firstLine.contains("successfully")) {
                System.out.println("[FAIL] Expected success message");
                System.out.println("Actual: " + firstLine);
                return false;
            }
            
            String afterDir = LocalChangeDirectoryIntent.getCurrentLocalDirectory();
            if (!beforeDir.equals(afterDir)) {
                System.out.println("[FAIL] Current directory should not change with '.'");
                System.out.println("Before: " + beforeDir);
                System.out.println("After: " + afterDir);
                return false;
            }
            
            System.out.println("[PASS] lcd . works correctly (stays in same directory)");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during current directory test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
}
