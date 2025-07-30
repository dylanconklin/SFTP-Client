package sftpClient.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import sftpClient.Client.Client;

public class SearchIntent extends Intent {
    private static final Logger logger = Logger.getLogger(SearchIntent.class.getName());

    @Override
    void parse(ArrayList<String> args) {
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("search")) {
            args.remove(0); // remove the "search" command
        }

        if (args.isEmpty()) {
            logger.warning("Search command received with no pattern argument.");
            throw new IllegalArgumentException("Search requires a pattern argument.");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        System.out.println(" Raw args before parse: " + args);
        logger.info("Executing search with raw args: " + args);

        parse(args);

        System.out.println(" Raw args AFTER parse: " + args);
        logger.info("Parsed args: " + args);

        ArrayList<String> output = new ArrayList<>();

        if (args.isEmpty()) {
            String msg = " Please provide a search keyword.";
            System.out.println(msg);
            logger.info(msg);
            output.add(msg);
            return output;
        }

        String pattern = args.get(0);
        System.out.println(" Searching Please Wait!! ........");
        logger.info("Starting recursive file listing for search pattern: " + pattern);

        List<String> files = client.listFilesRecursive(".", 4);
        // System.out.println(" Rfile : " + files); // Uncomment if needed for debugging
        logger.info("Total files found: " + files.size());

        List<String> matched;

        if (pattern.contains("*")) {
            String regex = pattern.replace("*", ".*");
            matched = files.stream()
                    .filter(name -> name.toLowerCase().matches(regex))
                    .collect(Collectors.toList());
        } else {
            matched = files.stream()
                    .filter(name -> name.toLowerCase().contains(pattern))
                    .collect(Collectors.toList());
        }

        if (matched.isEmpty()) {
            String msg = "No files found matching: " + pattern;
            System.out.println(msg);
            logger.info(msg);
            output.add(msg);
        } else {
            String msg = "Found " + matched.size() + " file(s):";
            System.out.println(msg);
            logger.info(msg);
            output.add(msg);
            matched.forEach(System.out::println);
            output.addAll(matched);
        }

        return output;
    }
}
