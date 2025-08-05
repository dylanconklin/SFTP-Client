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

        parse(args);

        System.out.println("Searching for " + args);

        ArrayList<String> output = new ArrayList<>();

        if (args.isEmpty()) {
            String msg = " Please provide a search keyword.";
            System.out.println(msg);
            output.add(msg);
            logger.info(output.toString());
            return output;
        }

        String pattern = args.get(0);
        logger.info("Searching Please Wait!! ........");
        System.out.println("Searching Please Wait!! ........");

        List<String> files = client.listFilesRecursive(".", 4);
        logger.info("Total files found: " + files.size());
        System.out.println("\n\nTotal files found: " + files.size());

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
            output.add(msg);
        } else {
            String msg = "Found " + matched.size() + " file(s):";
            output.add(msg);
            matched.forEach(System.out::println);
            output.addAll(matched);
        }
        logger.info(output.toString());
        return output;
    }
}
