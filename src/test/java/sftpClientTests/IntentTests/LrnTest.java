package test.java.sftpClientTests.IntentTests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import sftpClient.Intent.Intent;
import sftpClient.Intent.LocalRenameIntent;

/**
 * Unit test for the lrn (Local Rename) command
 * Tests the functionality of LocalRenameIntent
 */
public class LrnTest {
    
    public static void main(String[] args) {
        System.out.println("=== LRN Command Unit Test ===\n");
        
        boolean allTestsPassed = true;
        
        // Test 1: Intent Registration
        allTestsPassed &= testIntentRegistration();
        
        // Test 2: Basic File Rename
        allTestsPassed &= testBasicFileRename();
        
        // Test 3: Basic Directory Rename
        allTestsPassed &= testBasicDirectoryRename();
        
        // Test 4: Missing Arguments
        allTestsPassed &= testMissingArguments();
        
        // Test 5: Non-existent Source
        allTestsPassed &= testNonExistentSource();
        
        // Test 6: Existing Destination
        allTestsPassed &= testExistingDestination();
        
        // Test 7: Empty Arguments
        allTestsPassed &= testEmptyArguments();
        
        // Test 8: Cross-directory Rename
        allTestsPassed &= testCrossDirectoryRename();
        
        // Summary
        System.out.println("\n========================================");
        if (allTestsPassed) {
            System.out.println("ALL TESTS PASSED! lrn command is working correctly.");
        } else {
            System.out.println("SOME TESTS FAILED! Please check the implementation.");
        }
        System.out.println("========================================");
    }
    
    /**
     * Test 1: Verify lrn command is properly registered in Intent system
     */
    private static boolean testIntentRegistration() {
        System.out.println("[TEST] Test 1: Intent Registration");
        
        try {
            Intent intent = Intent.getIntent("lrn");
            
            if (intent == null) {
                System.out.println("FAIL: Intent.getIntent(\"lrn\") returned null");
                System.out.println("Check if lrn case is added to Intent.java switch statement");
                return false;
            }
            
            if (!(intent instanceof LocalRenameIntent)) {
                System.out.println("FAIL: Wrong intent type returned");
                System.out.println("Expected: LocalRenameIntent");
                System.out.println("Actual: " + intent.getClass().getSimpleName());
                return false;
            }
            
            System.out.println("[PASS] lrn command properly registered");
            System.out.println("   Returns correct LocalRenameIntent instance");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during intent registration test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 2: Test basic file rename functionality
     */
    private static boolean testBasicFileRename() {
        System.out.println("\n[TEST] Test 2: Basic File Rename");
        
        File testFile = null;
        File renamedFile = null;
        
        try {
            Intent intent = Intent.getIntent("lrn");
            
            // Create a temporary test file
            testFile = File.createTempFile("lrn_test_", ".txt");
            String originalName = testFile.getName();
            String newName = "lrn_renamed_" + System.currentTimeMillis() + ".txt";
            
            renamedFile = new File(testFile.getParent(), newName);
            
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lrn", testFile.getAbsolutePath(), renamedFile.getAbsolutePath()));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lrn returned no result");
                return false;
            }
            
            String firstLine = result.get(0);
            if (!firstLine.contains("successfully")) {
                System.out.println("[FAIL] Expected success message");
                System.out.println("Actual: " + firstLine);
                return false;
            }
            
            // Verify the file was actually renamed
            if (!renamedFile.exists()) {
                System.out.println("[FAIL] Renamed file does not exist");
                return false;
            }
            
            if (testFile.exists()) {
                System.out.println("[FAIL] Original file still exists");
                return false;
            }
            
            System.out.println("[PASS] File rename works correctly");
            System.out.println("   Renamed: " + originalName + " -> " + newName);
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during basic file rename test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        } finally {
            // Clean up
            if (testFile != null && testFile.exists()) {
                testFile.delete();
            }
            if (renamedFile != null && renamedFile.exists()) {
                renamedFile.delete();
            }
        }
    }
    
    /**
     * Test 3: Test basic directory rename functionality
     */
    private static boolean testBasicDirectoryRename() {
        System.out.println("\n[TEST] Test 3: Basic Directory Rename");
        
        File testDir = null;
        File renamedDir = null;
        
        try {
            Intent intent = Intent.getIntent("lrn");
            
            // Create a temporary test directory
            String tempDirPath = System.getProperty("java.io.tmpdir");
            testDir = new File(tempDirPath, "lrn_test_dir_" + System.currentTimeMillis());
            if (!testDir.mkdir()) {
                System.out.println("[SKIP] Could not create test directory");
                return true;
            }
            
            String originalName = testDir.getName();
            String newName = "lrn_renamed_dir_" + System.currentTimeMillis();
            renamedDir = new File(testDir.getParent(), newName);
            
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lrn", testDir.getAbsolutePath(), renamedDir.getAbsolutePath()));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lrn returned no result for directory");
                return false;
            }
            
            String firstLine = result.get(0);
            if (!firstLine.contains("successfully")) {
                System.out.println("[FAIL] Expected success message for directory");
                System.out.println("Actual: " + firstLine);
                return false;
            }
            
            // Verify the directory was actually renamed
            if (!renamedDir.exists() || !renamedDir.isDirectory()) {
                System.out.println("[FAIL] Renamed directory does not exist");
                return false;
            }
            
            if (testDir.exists()) {
                System.out.println("[FAIL] Original directory still exists");
                return false;
            }
            
            System.out.println("[PASS] Directory rename works correctly");
            System.out.println("   Renamed: " + originalName + " -> " + newName);
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during basic directory rename test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        } finally {
            // Clean up
            if (testDir != null && testDir.exists()) {
                testDir.delete();
            }
            if (renamedDir != null && renamedDir.exists()) {
                renamedDir.delete();
            }
        }
    }
    
    /**
     * Test 4: Test lrn with missing arguments
     */
    private static boolean testMissingArguments() {
        System.out.println("\n[TEST] Test 4: Missing Arguments");
        
        try {
            Intent intent = Intent.getIntent("lrn");
            
            // Test with no arguments
            ArrayList<String> args1 = new ArrayList<>(Arrays.asList("lrn"));
            ArrayList<String> result1 = intent.execute(null, args1);
            
            if (result1 == null || result1.isEmpty() || !result1.get(0).toLowerCase().contains("error")) {
                System.out.println("[FAIL] Should return error for missing arguments");
                return false;
            }
            
            // Test with only one argument
            ArrayList<String> args2 = new ArrayList<>(Arrays.asList("lrn", "source"));
            ArrayList<String> result2 = intent.execute(null, args2);
            
            if (result2 == null || result2.isEmpty() || !result2.get(0).toLowerCase().contains("error")) {
                System.out.println("[FAIL] Should return error for missing destination");
                return false;
            }
            
            System.out.println("[PASS] Missing arguments handled correctly");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during missing arguments test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 5: Test lrn with non-existent source
     */
    private static boolean testNonExistentSource() {
        System.out.println("\n[TEST] Test 5: Non-existent Source");
        
        try {
            Intent intent = Intent.getIntent("lrn");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lrn", "/non/existent/file", "newname"));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] Should return error for non-existent source");
                return false;
            }
            
            String errorMessage = result.get(0);
            if (!errorMessage.toLowerCase().contains("error") || !errorMessage.contains("does not exist")) {
                System.out.println("[FAIL] Expected error message for non-existent source");
                System.out.println("Actual: " + errorMessage);
                return false;
            }
            
            System.out.println("[PASS] Non-existent source handled correctly");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during non-existent source test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 6: Test lrn with existing destination
     */
    private static boolean testExistingDestination() {
        System.out.println("\n[TEST] Test 6: Existing Destination");
        
        File testFile1 = null;
        File testFile2 = null;
        
        try {
            Intent intent = Intent.getIntent("lrn");
            
            // Create two temporary files
            testFile1 = File.createTempFile("lrn_source_", ".txt");
            testFile2 = File.createTempFile("lrn_dest_", ".txt");
            
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lrn", testFile1.getAbsolutePath(), testFile2.getAbsolutePath()));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] Should return error for existing destination");
                return false;
            }
            
            String errorMessage = result.get(0);
            if (!errorMessage.toLowerCase().contains("error") || !errorMessage.contains("already exists")) {
                System.out.println("[FAIL] Expected error message for existing destination");
                System.out.println("Actual: " + errorMessage);
                return false;
            }
            
            System.out.println("[PASS] Existing destination handled correctly");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during existing destination test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        } finally {
            // Clean up
            if (testFile1 != null && testFile1.exists()) {
                testFile1.delete();
            }
            if (testFile2 != null && testFile2.exists()) {
                testFile2.delete();
            }
        }
    }
    
    /**
     * Test 7: Test lrn with empty arguments
     */
    private static boolean testEmptyArguments() {
        System.out.println("\n[TEST] Test 7: Empty Arguments");
        
        try {
            Intent intent = Intent.getIntent("lrn");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lrn", "", "newname"));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] Should return error for empty source name");
                return false;
            }
            
            String errorMessage = result.get(0);
            if (!errorMessage.toLowerCase().contains("error") || !errorMessage.contains("cannot be empty")) {
                System.out.println("[FAIL] Expected error message for empty source");
                System.out.println("Actual: " + errorMessage);
                return false;
            }
            
            System.out.println("[PASS] Empty arguments handled correctly");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during empty arguments test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 8: Test cross-directory rename
     */
    private static boolean testCrossDirectoryRename() {
        System.out.println("\n[TEST] Test 8: Cross-directory Rename");
        
        File testFile = null;
        File targetDir = null;
        File renamedFile = null;
        
        try {
            Intent intent = Intent.getIntent("lrn");
            
            // Create a test file and target directory
            testFile = File.createTempFile("lrn_cross_", ".txt");
            String tempDirPath = System.getProperty("java.io.tmpdir");
            targetDir = new File(tempDirPath, "lrn_target_dir_" + System.currentTimeMillis());
            
            if (!targetDir.mkdir()) {
                System.out.println("[SKIP] Could not create target directory");
                return true;
            }
            
            renamedFile = new File(targetDir, "moved_file.txt");
            
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lrn", testFile.getAbsolutePath(), renamedFile.getAbsolutePath()));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] Cross-directory rename returned no result");
                return false;
            }
            
            String firstLine = result.get(0);
            if (!firstLine.contains("successfully")) {
                System.out.println("[FAIL] Expected success for cross-directory rename");
                System.out.println("Actual: " + firstLine);
                return false;
            }
            
            // Verify the file was moved
            if (!renamedFile.exists()) {
                System.out.println("[FAIL] File was not moved to target directory");
                return false;
            }
            
            System.out.println("[PASS] Cross-directory rename works correctly");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during cross-directory rename test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        } finally {
            // Clean up
            if (testFile != null && testFile.exists()) {
                testFile.delete();
            }
            if (renamedFile != null && renamedFile.exists()) {
                renamedFile.delete();
            }
            if (targetDir != null && targetDir.exists()) {
                targetDir.delete();
            }
        }
    }
}
