package sftpClient.Intent;

import java.util.ArrayList;

public class DeleteIntent extends Intent {
    @Override
    void parse(ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("Rm Error: Missing Parameters Like File Names");
        }
    }

    @Override
    public ArrayList<String> execute() {
        ArrayList<String> output = new ArrayList<>();
        String filename = args.get(1);
        output.add("Deleting ....  " + filename + " .... Please Wait");
        // Acutally delete the files here
        // Show Success of Fail
        return null;
    }
}
