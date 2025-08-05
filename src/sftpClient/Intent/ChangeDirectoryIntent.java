package sftpClient.Intent;

import java.util.ArrayList;
import java.util.logging.Logger;
import sftpClient.Client.Client;

public class ChangeDirectoryIntent extends Intent {
    private static final Logger logger = Logger.getLogger(ChangeDirectoryIntent.class.getName());
    @Override
    public void parse(ArrayList<String> args) {
        this.args = args;
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        //System.out.println(" Raw args before parse: " + args);
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("Usage: cd <directory>");
            logger.info(output.toString());
            return output;
        }

        String newDir = args.get(1);
        boolean success = client.changeDirectory(newDir);
        if (success) {
            output.add("Changed directory to: " + newDir);
        } else {
            output.add("Failed to change directory to: " + newDir);
        }
        logger.info(output.toString());
        return output;
    }
}
