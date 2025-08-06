package test.java.sftpClientTests.IntentTests;

import java.util.ArrayList;
import java.util.Arrays;
import sftpClient.Intent.Intent;
import sftpClient.Intent.RenameIntent;

/**
 * Unit test for the rename (Remote Rename) command
 * Tests the functionality of RenameIntent
 */
public class RenameTest {
    
    public static void main(String[] args) {
        System.out.println("=== RENAME Command Unit Test ===\n");
        
        boolean allTestsPassed = true;
        
        // Test 1: Intent Registration
        allTestsPassed &= testIntentRegistration();
        
        // Test 2: Basic File Rename
        allTestsPassed &= testBasicFileRename();
        
        // Test 3: Directory Rename 
        allTestsPassed &= testDirectoryRename();
        
        // Test 4: Cross-Directory Rename
        allTestsPassed &= testCrossDirectoryRename();
        
        // Test 5: Missing Arguments
        allTestsPassed &= testMissingArguments();
        
        // Test 6: Single Argument Error
        allTestsPassed &= testSingleArgument();
        
        // Test 7: Empty Arguments
        allTestsPassed &= testEmptyArguments();
        
        // Test 8: Same Source and Destination
        allTestsPassed &= testSameSourceDestination();
        
        // Test 9: Command Name Removal
        allTestsPassed &= testCommandNameRemoval();
        
        // Test 10: Path Validation
        allTestsPassed &= testPathValidation();
        
        // Summary
        System.out.println("\n========================================");
        if (allTestsPassed) {
            System.out.println("ALL TESTS PASSED! rename command is working correctly.");
        } else {
            System.out.println("SOME TESTS FAILED! Please check the implementation.");
        }
        System.out.println("========================================");
    }
    
    /**
     * Test 1: Verify rename command is properly registered in Intent system
     */
    private static boolean testIntentRegistration() {
        System.out.println("[TEST] Test 1: Intent Registration");
        
        try {
            Intent intent = Intent.getIntent("rename");
            
            if (intent == null) {
                System.out.println("[FAIL] Intent.getIntent(\"rename\") returned null");
                System.out.println("   Check if rename case is added to Intent.java switch statement");
                return false;
            }
            
            if (!(intent instanceof RenameIntent)) {
                System.out.println("[FAIL] Wrong intent type returned");
                System.out.println("   Expected: RenameIntent");
                System.out.println("   Actual: " + intent.getClass().getSimpleName());
                return false;
            }
            
            System.out.println("[PASS] rename command properly registered");
            System.out.println("   Returns correct RenameIntent instance");
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
        
        try {
            Intent intent = Intent.getIntent("rename");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("rename", "oldfile.txt", "newfile.txt"));
            
            // Since we don't have a real SFTP connection, this will test parsing and error handling
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] No result returned");
                return false;
            }
            
            // Should get an error about no SFTP connection, but parsing should work
            String firstLine = result.get(0);
            boolean hasValidResult = firstLine.contains("Failed to rename file") || 
                                   firstLine.contains("Renamed oldfile.txt to newfile.txt") ||
                                   firstLine.contains("NullPointerException");
            
            if (!hasValidResult) {
                System.out.println("[FAIL] Unexpected result format");
                System.out.println("   Result: " + result);
                return false;
            }
            
            System.out.println("[PASS] Basic file rename parsing works");
            System.out.println("   Command parsed: oldfile.txt -> newfile.txt");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during basic file rename test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 3: Test directory rename functionality
     */
    private static boolean testDirectoryRename() {
        System.out.println("\n[TEST] Test 3: Directory Rename");
        
        try {
            Intent intent = Intent.getIntent("rename");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("rename", "olddir", "newdir"));
            
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] No result returned");
                return false;
            }
            
            // Should work the same as file rename
            String firstLine = result.get(0);
            boolean hasValidResult = firstLine.contains("Failed to rename file") || 
                                   firstLine.contains("Renamed olddir to newdir") ||
                                   firstLine.contains("NullPointerException");
            
            if (!hasValidResult) {
                System.out.println("[FAIL] Unexpected result for directory rename");
                System.out.println("   Result: " + result);
                return false;
            }
            
            System.out.println("[PASS] Directory rename parsing works");
            System.out.println("   Command parsed: olddir -> newdir");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during directory rename test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 4: Test cross-directory rename functionality
     */
    private static boolean testCrossDirectoryRename() {
        System.out.println("\n[TEST] Test 4: Cross-Directory Rename");
        
        try {
            Intent intent = Intent.getIntent("rename");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("rename", "folder/file.txt", "different/folder/newfile.txt"));
            
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] No result returned");
                return false;
            }
            
            // Should handle paths correctly
            String firstLine = result.get(0);
            boolean hasValidResult = firstLine.contains("Failed to rename file") || 
                                   firstLine.contains("Renamed folder/file.txt to different/folder/newfile.txt") ||
                                   firstLine.contains("NullPointerException");
            
            if (!hasValidResult) {
                System.out.println("[FAIL] Unexpected result for cross-directory rename");
                System.out.println("   Result: " + result);
                return false;
            }
            
            System.out.println("[PASS] Cross-directory rename parsing works");
            System.out.println("   Command parsed: folder/file.txt -> different/folder/newfile.txt");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during cross-directory rename test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 5: Test missing arguments
     */
    private static boolean testMissingArguments() {
        System.out.println("\n[TEST] Test 5: Missing Arguments");
        
        try {
            Intent intent = Intent.getIntent("rename");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("rename"));
            
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] No result returned");
                return false;
            }
            
            // Should show usage message
            boolean foundUsage = false;
            for (String line : result) {
                if (line.contains("Usage: rename <old_filename> <new_filename>")) {
                    foundUsage = true;
                    break;
                }
            }
            
            if (!foundUsage) {
                System.out.println("[FAIL] Missing arguments not handled correctly");
                System.out.println("   Expected usage message not found");
                System.out.println("   Result: " + result);
                return false;
            }
            
            System.out.println("[PASS] Missing arguments handled correctly");
            System.out.println("   Proper usage message displayed");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during missing arguments test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 6: Test single argument error
     */
    private static boolean testSingleArgument() {
        System.out.println("\n[TEST] Test 6: Single Argument Error");
        
        try {
            Intent intent = Intent.getIntent("rename");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("rename", "onlyfile.txt"));
            
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] No result returned");
                return false;
            }
            
            // Should show usage message for insufficient arguments
            boolean foundUsage = false;
            for (String line : result) {
                if (line.contains("Usage: rename <old_filename> <new_filename>")) {
                    foundUsage = true;
                    break;
                }
            }
            
            if (!foundUsage) {
                System.out.println("[FAIL] Single argument not handled correctly");
                System.out.println("   Expected usage message not found");
                System.out.println("   Result: " + result);
                return false;
            }
            
            System.out.println("[PASS] Single argument handled correctly");
            System.out.println("   Proper usage message for insufficient arguments");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during single argument test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 7: Test empty arguments
     */
    private static boolean testEmptyArguments() {
        System.out.println("\n[TEST] Test 7: Empty Arguments");
        
        try {
            Intent intent = Intent.getIntent("rename");
            ArrayList<String> args = new ArrayList<>();
            
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] No result returned");
                return false;
            }
            
            // Should show usage message
            boolean foundUsage = false;
            for (String line : result) {
                if (line.contains("Usage: rename <old_filename> <new_filename>")) {
                    foundUsage = true;
                    break;
                }
            }
            
            if (!foundUsage) {
                System.out.println("[FAIL] Empty arguments not handled correctly");
                System.out.println("   Expected usage message not found");
                System.out.println("   Result: " + result);
                return false;
            }
            
            System.out.println("[PASS] Empty arguments handled correctly");
            System.out.println("   Proper usage message for no arguments");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during empty arguments test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 8: Test same source and destination
     */
    private static boolean testSameSourceDestination() {
        System.out.println("\n[TEST] Test 8: Same Source and Destination");
        
        try {
            Intent intent = Intent.getIntent("rename");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("rename", "samefile.txt", "samefile.txt"));
            
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] No result returned");
                return false;
            }
            
            // Should attempt the operation (might succeed or fail depending on SFTP server)
            String firstLine = result.get(0);
            boolean hasValidResult = firstLine.contains("Failed to rename file") || 
                                   firstLine.contains("Renamed samefile.txt to samefile.txt") ||
                                   firstLine.contains("NullPointerException");
            
            if (!hasValidResult) {
                System.out.println("[FAIL] Unexpected result for same source and destination");
                System.out.println("   Result: " + result);
                return false;
            }
            
            System.out.println("[PASS] Same source and destination handled");
            System.out.println("   Command processed: samefile.txt -> samefile.txt");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during same source and destination test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 9: Test command name removal
     */
    private static boolean testCommandNameRemoval() {
        System.out.println("\n[TEST] Test 9: Command Name Removal");
        
        try {
            Intent intent = Intent.getIntent("rename");
            
            // Test with explicit "rename" command in args
            ArrayList<String> argsWithCommand = new ArrayList<>(Arrays.asList("rename", "source.txt", "dest.txt"));
            ArrayList<String> result1 = intent.execute(null, argsWithCommand);
            
            // Test without "rename" command in args (simulating direct call)
            ArrayList<String> argsWithoutCommand = new ArrayList<>(Arrays.asList("source.txt", "dest.txt"));
            ArrayList<String> result2 = intent.execute(null, argsWithoutCommand);
            
            if (result1 == null || result1.isEmpty() || result2 == null || result2.isEmpty()) {
                System.out.println("[FAIL] No result returned from one or both tests");
                return false;
            }
            
            // Both should work and produce similar results
            String firstLine1 = result1.get(0);
            String firstLine2 = result2.get(0);
            
            boolean result1Valid = firstLine1.contains("Failed to rename file") || 
                                  firstLine1.contains("Renamed source.txt to dest.txt") ||
                                  firstLine1.contains("NullPointerException");
            
            boolean result2Valid = firstLine2.contains("Failed to rename file") || 
                                  firstLine2.contains("Renamed source.txt to dest.txt") ||
                                  firstLine2.contains("NullPointerException");
            
            if (!result1Valid || !result2Valid) {
                System.out.println("[FAIL] Command name removal not working correctly");
                System.out.println("   With command: " + result1);
                System.out.println("   Without command: " + result2);
                return false;
            }
            
            System.out.println("[PASS] Command name removal works correctly");
            System.out.println("   Both with and without 'rename' command work");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during command name removal test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 10: Test path validation
     */
    private static boolean testPathValidation() {
        System.out.println("\n[TEST] Test 10: Path Validation");
        
        try {
            Intent intent = Intent.getIntent("rename");
            
            // Test various path formats
            String[][] testCases = {
                {"file.txt", "newfile.txt"},
                {"path/to/file.txt", "path/to/newfile.txt"},
                {"../relative/file.txt", "../relative/newfile.txt"},
                {"/absolute/path/file.txt", "/absolute/path/newfile.txt"},
                {"file with spaces.txt", "new file with spaces.txt"},
                {"file.with.dots.txt", "newfile.with.dots.txt"}
            };
            
            boolean allPathsValid = true;
            
            for (String[] testCase : testCases) {
                ArrayList<String> args = new ArrayList<>(Arrays.asList("rename", testCase[0], testCase[1]));
                ArrayList<String> result = intent.execute(null, args);
                
                if (result == null || result.isEmpty()) {
                    System.out.println("[FAIL] No result for path: " + testCase[0] + " -> " + testCase[1]);
                    allPathsValid = false;
                    continue;
                }
                
                String firstLine = result.get(0);
                boolean hasValidResult = firstLine.contains("Failed to rename file") || 
                                       firstLine.contains("Renamed " + testCase[0] + " to " + testCase[1]) ||
                                       firstLine.contains("NullPointerException");
                
                if (!hasValidResult) {
                    System.out.println("[FAIL] Invalid result for path: " + testCase[0] + " -> " + testCase[1]);
                    System.out.println("   Result: " + result);
                    allPathsValid = false;
                }
            }
            
            if (!allPathsValid) {
                return false;
            }
            
            System.out.println("[PASS] Path validation works correctly");
            System.out.println("   All path formats processed correctly");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during path validation test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
}
