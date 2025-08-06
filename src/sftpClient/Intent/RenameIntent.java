package sftpClient.Intent;

import java.util.ArrayList;
import java.util.logging.Logger;
import sftpClient.Client.Client;
import com.jcraft.jsch.SftpException;

public class RenameIntent extends Intent {
    private static final Logger logger = Logger.getLogger(RenameIntent.class.getName());

    @Override
    public void parse(ArrayList<String> args) {
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("rename")) {
            args.remove(0); // remove the "rename" command
        }
        // Optional validation
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Rename requires a pattern argument.");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        parse(args);
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("Usage: rename <old_filename> <new_filename>");
            logger.info(output.toString());
            return output;
        }

        String oldName = args.get(0);
        String newName = args.get(1);

        try {
            client.sftp.rename(oldName, newName);
            logger.info("Successfully renamed " + oldName + " to " + newName);
        } catch (SftpException e) {
            output.add("Failed to rename file: " + e.getMessage());
            logger.info("Failed to rename " + oldName + ": " + e.getMessage());
        }
        logger.info(output.toString());
        return output;
    }
}
