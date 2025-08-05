package sftpClient.Intent;

import sftpClient.Client.Client;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DeleteDirectoryIntent extends Intent {
    private static final Logger logger = Logger.getLogger(DeleteDirectoryIntent.class.getName());

    @Override
    public void parse(ArrayList<String> args) {
        if (args.get(0).equalsIgnoreCase("rmdir")) {
            args.remove(0);
        }
        if (args.size() != 1) {
            throw new IllegalArgumentException("Usage: rmdir <remoteDirectory>");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        parse(args);
        String target = args.get(0);
        ArrayList<String> result = new ArrayList<>();
        try {
            client.deleteDirectory(target);
            result.add("Successfully deleted directory: " + target);
        } catch (Exception e) {
            result.add("Failed to delete directory: " + e.getMessage());
        }
        return result;
    }
}
