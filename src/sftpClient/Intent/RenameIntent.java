package sftpClient.Intent;

import sftpClient.Client.Client;
import com.jcraft.jsch.SftpException;

import java.util.ArrayList;

public class RenameIntent extends Intent {

    @Override
    public void parse(ArrayList<String> args) {
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("rename")) {
            args.remove(0); // remove the "search" command
        }
        // Optional validation
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Search requires a pattern argument.");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        //System.out.println(" Raw args before parse: " + args);
        parse(args);
        //System.out.println(" Raw args before parse: " + args);
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("Usage: rename <old_filename> <new_filename>");
            return output;
        }

        String oldName = args.get(0);
        String newName = args.get(1);

        try {
            client.sftp.rename(oldName, newName);
            output.add("Renamed " + oldName + " to " + newName);
        } catch (SftpException e) {
            output.add("Failed to rename file: " + e.getMessage());
        }

        return output;
    }
}
