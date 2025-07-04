package sftpClient.Intent;

import java.util.ArrayList;

public class DownloadIntent extends Intent {
    @Override
    void parse(ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("get Error: Missing Parameters Like File Names");
        }
    }

    @Override
    public ArrayList<String> execute(Client client) {
        ArrayList<String> output = new ArrayList<>();
        String filename = args.get(1);
        output.add("Downloading ....  " + filename + " .... Please Wait");
        // Get the Actual file here to download
        // Show Success of Fail
        return null;
    }
}
