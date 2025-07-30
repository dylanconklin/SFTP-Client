package sftpClient.Intent;

import java.util.ArrayList;
import java.util.logging.Logger;
import sftpClient.Client.Client;

public class DeleteIntent extends Intent {
    private static final Logger logger = Logger.getLogger(DeleteIntent.class.getName());
    @Override
    void parse(ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("Rm Error: Missing Parameters Like File Names");
            logger.warning("Rm Error: Missing Parameters Like File Names");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        String filename = args.get(1);
        output.add("Deleting ....  " + filename + " .... Please Wait");
        logger.info("Deleting ....  " + filename + " .... Please Wait");
        // Acutally delete the files here
        // Show Success of Fail
        return null;
    }
}
