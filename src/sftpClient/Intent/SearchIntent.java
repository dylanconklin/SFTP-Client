package sftpClient.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import sftpClient.Client.Client;

public class SearchIntent extends Intent {

    @Override
    void parse(ArrayList<String> args) {
        // Optional validation
        if (args == null || args.isEmpty()) {
            throw new IllegalArgumentException("Search requires a pattern argument.");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();

        if (args == null || args.isEmpty()) {
            output.add(" Please provide a search keyword.");
            return output;
        }

        String pattern = args.get(0); // Example: "log", ".txt"
        String path = "."; // Default remote directory

        List<String> files = client.listFiles(path);

        // Simple case-insensitive substring match
        List<String> matched = files.stream()
                .filter(name -> name.toLowerCase().contains(pattern.toLowerCase()))
                .collect(Collectors.toList());

        if (matched.isEmpty()) {
            output.add("🔍 No files found matching: " + pattern);
        } else {
            output.add("🔍 Found " + matched.size() + " file(s):");
            output.addAll(matched);
        }

        return output;
    }
}
