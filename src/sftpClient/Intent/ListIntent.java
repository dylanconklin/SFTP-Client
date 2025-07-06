package sftpClient.Intent;

import java.util.ArrayList;
import sftpClient.Client.Client;

public class ListIntent extends Intent {
    @Override
    void parse(ArrayList<String> args) {
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        output.add("Listing All the Files In the Current Directory....  ");
        // Actually list all the files in the current directory
        // Show Success of Fail
        return null;
    }
}
