package sftpClient;

import java.util.ArrayList;
import java.util.Arrays;
import sftpClient.Intent.Intent;
import sftpClient.Intent.LocalPrintWorkingDirectoryIntent;

/**
 * Unit test for the lpwd (Local Print Working Directory) command
 * Tests the functionality of LocalPrintWorkingDirectoryIntent
 */
public class LpwdTest {
    
    public static void main(String[] args) {
        System.out.println("=== LPWD Command Unit Test ===\n");
        
        boolean allTestsPassed = true;
        
        // Test 1: Intent Registration
        allTestsPassed &= testIntentRegistration();
        
        // Test 2: Basic Functionality
        allTestsPassed &= testBasicFunctionality();
        
        // Test 3: Argument Handling
        allTestsPassed &= testArgumentHandling();
        
        // Test 4: Null Client Handling
        allTestsPassed &= testNullClientHandling();
        
        // Summary
        System.out.println("\n========================================");
        if (allTestsPassed) {
            System.out.println("ALL TESTS PASSED! lpwd command is working correctly.");
        } else {
            System.out.println("SOME TESTS FAILED! Please check the implementation.");
        }
        System.out.println("========================================");
    }
    
    /**
     * Test 1: Verify lpwd command is properly registered in Intent system
     */
    private static boolean testIntentRegistration() {
        System.out.println("[TEST] Test 1: Intent Registration");
        
        try {
            Intent intent = Intent.getIntent("lpwd");
            
            if (intent == null) {
                System.out.println("FAIL: Intent.getIntent(\"lpwd\") returned null");
                System.out.println("Check if lpwd case is added to Intent.java switch statement");
                return false;
            }
            
            if (!(intent instanceof LocalPrintWorkingDirectoryIntent)) {
                System.out.println(" FAIL: Wrong intent type returned");
                System.out.println(" Expected: LocalPrintWorkingDirectoryIntent");
                System.out.println("   Actual: " + intent.getClass().getSimpleName());
                return false;
            }
            
            System.out.println("[PASS] lpwd command properly registered");
            System.out.println("   Returns correct LocalPrintWorkingDirectoryIntent instance");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during intent registration test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 2: Test basic lpwd functionality
     */
    private static boolean testBasicFunctionality() {
        System.out.println("\n[TEST] Test 2: Basic lpwd Functionality");
        
        try {
            Intent intent = Intent.getIntent("lpwd");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lpwd"));
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null) {
                System.out.println("[FAIL] execute() returned null");
                return false;
            }
            
            if (result.isEmpty()) {
                System.out.println("[FAIL] execute() returned empty result");
                return false;
            }
            
            String output = result.get(0);
            if (!output.startsWith("Current local directory:")) {
                System.out.println("[FAIL] Incorrect output format");
                System.out.println("   Expected format: 'Current local directory: ...'");
                System.out.println("   Actual output: " + output);
                return false;
            }
            
            System.out.println("[PASS] lpwd returns correct format");
            System.out.println("   Output: " + output);
            
            // Verify the directory path exists
            String dirPath = output.substring("Current local directory: ".length());
            if (dirPath.isEmpty()) {
                System.out.println("[FAIL] Directory path is empty");
                return false;
            }
            
            System.out.println("[PASS] Directory path is not empty");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception during basic functionality test");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 3: Test argument handling (with extra arguments)
     */
    private static boolean testArgumentHandling() {
        System.out.println("\n[TEST] Test 3: Argument Handling");
        
        try {
            Intent intent = Intent.getIntent("lpwd");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lpwd", "extra", "arguments", "here"));
            
            // Test execute method with extra arguments (skip parse test since it's not public)
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lpwd failed with extra arguments");
                return false;
            }
            
            String output = result.get(0);
            if (!output.startsWith("Current local directory:")) {
                System.out.println("[FAIL] lpwd output format incorrect with extra args");
                return false;
            }
            
            System.out.println("[PASS] lpwd works correctly with extra arguments");
            System.out.println("   Output: " + output);
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception with extra arguments");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 4: Test null client handling (lpwd doesn't need SFTP connection)
     */
    private static boolean testNullClientHandling() {
        System.out.println("\n[TEST] Test 4: Null Client Handling");
        
        try {
            Intent intent = Intent.getIntent("lpwd");
            ArrayList<String> args = new ArrayList<>(Arrays.asList("lpwd"));
            
            // lpwd should work with null client since it's a local command
            ArrayList<String> result = intent.execute(null, args);
            
            if (result == null || result.isEmpty()) {
                System.out.println("[FAIL] lpwd failed with null client");
                return false;
            }
            
            String output = result.get(0);
            if (output.startsWith("Error")) {
                System.out.println("[FAIL] lpwd should not error with null client");
                System.out.println("   Output: " + output);
                return false;
            }
            
            System.out.println("[PASS] lpwd works correctly with null client");
            System.out.println("   This is expected since lpwd is a local command");
            return true;
            
        } catch (Exception e) {
            System.out.println("[FAIL] Exception with null client");
            System.out.println("   Exception: " + e.getMessage());
            return false;
        }
    }
}
