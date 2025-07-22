package sftpClient.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import sftpClient.Client.Client;

public class SearchIntent extends Intent {

    @Override
    void parse(ArrayList<String> args) {
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("search")) {
            args.remove(0); // remove the "search" command
        }
        // Optional validation
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Search requires a pattern argument.");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
     //   System.out.println(" Raw args before parse: " + args);
        parse(args);
//        System.out.println(" Raw args AFTER parse: " + args);
        ArrayList<String> output = new ArrayList<>();

        if (args.isEmpty()) {
            output.add(" Please provide a search keyword.");
            return output;
        }

        String pattern = args.get(0);
        System.out.println(" Searching Please Wait!! ........");
        //List<String> files = client.listFiles(".");
        List<String> files = client.listFilesRecursive(".", 4);
        //System.out.println(" Rfile : " + files);

        List<String> matched;

        if (pattern.contains("*")) {
            String regex = pattern.replace("*", ".*"); // simple wildcard
            matched = files.stream()
                    .filter(name -> name.toLowerCase().matches(regex))
                    .collect(Collectors.toList());
        } else {
            matched = files.stream()
                    .filter(name -> name.toLowerCase().contains(pattern))
                    .collect(Collectors.toList());
        }
        if (matched.isEmpty()) {
            output.add("No files found matching: " + pattern);
        } else {
            output.add("Found " + matched.size() + " file(s):");
            output.addAll(matched);
        }

        return output;
    }
}
