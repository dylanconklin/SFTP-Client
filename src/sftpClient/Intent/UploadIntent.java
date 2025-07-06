package sftpClient.Intent;

import java.util.ArrayList;
import sftpClient.Client.Client;

public class UploadIntent extends Intent {
    @Override
    void parse(ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("Put Error: Missing Parameters Like File Names");
        }
    }

    @Override
    public ArrayList<String> execute(Client client) {
        ArrayList<String> output = new ArrayList<>();
        String filename = args.get(1);
        output.add("Uploading ....  " + filename + " .... Please Wait");
        // Upload the actual file here
        // Show Success of Fail
        return null;
    }
}
