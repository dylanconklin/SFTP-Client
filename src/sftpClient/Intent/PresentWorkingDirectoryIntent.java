package sftpClient.Intent;

import sftpClient.Client.Client;
import java.util.ArrayList;

public class PresentWorkingDirectoryIntent extends Intent {
    @Override
    void parse(ArrayList<String> args) {}

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        try {
            output.add(client.sftp.pwd());
        } catch(Exception ex) {}
        return output;
    }
}
