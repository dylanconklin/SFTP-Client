package sftpClient.Intent;

import java.util.ArrayList;
import java.util.logging.Logger;
import sftpClient.Client.Client;

public class CopyIntent extends Intent {
    private static final Logger logger = Logger.getLogger(CopyIntent.class.getName());
    
    @Override
    void parse(ArrayList<String> args) {
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        return null;
    }
}
