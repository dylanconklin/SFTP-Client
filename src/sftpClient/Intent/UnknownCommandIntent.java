package sftpClient.Intent;

import org.apache.sshd.server.Command;
import sftpClient.Client.Client;

import java.util.ArrayList;

public class UnknownCommandIntent extends Intent {
    ArrayList<String> args = new ArrayList<>();

    @Override
    void parse(ArrayList<String> args) {
        this.args = args;
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        parse(args);
        if(args.isEmpty()) {
            output.add("Missing command.");
        } else {
            output.add("Invalid command: " + this.args.get(0));
        }
        return output;
    }
}
