package sftpClient.Intent;

import java.util.ArrayList;
import sftpClient.Client.Client;

public class CopyIntent extends Intent {
    @Override
    void parse(ArrayList<String> args) {
    }

    @Override
    public ArrayList<String> execute(Client client) {
        return null;
    }
}
