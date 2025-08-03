package sftpClient.Intent;

import java.util.ArrayList;
import java.util.logging.Logger;
import sftpClient.Client.Client;

public class CreateDirectoryIntent extends Intent {
    private static final Logger logger = Logger.getLogger(CreateDirectoryIntent.class.getName());
    @Override
    public void parse(ArrayList<String> args) {
            if (!args.isEmpty() && args.get(0).equalsIgnoreCase("mkdir")) {
                args.remove(0);
            }

            if (args.size() != 1) {
                throw new IllegalArgumentException("Usage: mkdir <remoteDirectory>");
            }
        }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        parse(args); 

        String newDir = args.get(0);

        ArrayList<String> result = new ArrayList<>();
        try {
            client.createDirectory(newDir);
            result.add("Success: Created directory " + newDir);
        } catch (Exception e) {
            result.add("Failed: " + e.getMessage());
            logger.warning(result.toString());
        }
        return result;
    }
}

