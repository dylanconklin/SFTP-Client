package sftpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import sftpClient.Intent.Intent;

/**
 * Interactive test launcher for search commands
 * Type commands one by one and see results immediately
 */
public class InteractiveSearchTest {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Interactive Local File Search Test ===");
        System.out.println("Type 'help' to see available commands");
        System.out.println("Type 'examples' to see search examples");
        System.out.println("Type 'quit' or 'exit' to quit");
        System.out.println("============================================");
        System.out.println();
        
        while (true) {
            System.out.print("search> ");
            String input = scanner.nextLine().trim();
            
            // Handle quit commands
            if (input.equals("quit") || input.equals("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            
            // Handle empty input
            if (input.isEmpty()) {
                continue;
            }
            
            // Handle special commands
            if (input.equals("examples")) {
                showExamples();
                continue;
            }
            
            // Execute the command
            executeCommand(input);
            System.out.println();
        }
        
        scanner.close();
    }
    
    private static void executeCommand(String command) {
        try {
            // Parse command into arguments
            ArrayList<String> args = new ArrayList<>(Arrays.asList(command.split("\\s+")));
            
            // Get and execute intent
            Intent intent = Intent.getIntent(args.get(0));
            if (intent != null) {
                ArrayList<String> results = intent.execute(null, args);
                
                // Display results
                for (String result : results) {
                    System.out.println(result);
                }
            } else {
                System.out.println("Unknown command: " + args.get(0));
                System.out.println("Type 'help' to see available commands");
            }
        } catch (Exception e) {
            System.out.println("Error executing command: " + e.getMessage());
        }
    }
    
    private static void showExamples() {
        System.out.println("=== Search Command Examples ===");
        System.out.println();
        System.out.println("Basic filename search:");
        System.out.println("  lsearch -name *.java src");
        System.out.println("  lsearch -name Main* src");
        System.out.println("  lsearch -name *Test* src");
        System.out.println();
        System.out.println("Content search:");
        System.out.println("  lsearch -content TODO src");
        System.out.println("  lsearch -content Intent src");
        System.out.println("  lsearch -content \"public class\" src");
        System.out.println();
        System.out.println("Extension search:");
        System.out.println("  lsearch -ext java src");
        System.out.println("  lsearch -ext class sftpClient");
        System.out.println("  lsearch -ext txt .");
        System.out.println();
        System.out.println("Size search:");
        System.out.println("  lsearch -size +1K src");
        System.out.println("  lsearch -size +5K .");
        System.out.println("  lsearch -size -500 src");
        System.out.println();
        System.out.println("Advanced options:");
        System.out.println("  lsearch -name *JAVA -case src     (case sensitive)");
        System.out.println("  lsearch -name *.txt -nr .         (non-recursive)");
        System.out.println();
        System.out.println("Other commands:");
        System.out.println("  help                               (show help)");
        System.out.println("  examples                           (show this list)");
        System.out.println("  quit                               (exit program)");
        System.out.println();
    }
}
