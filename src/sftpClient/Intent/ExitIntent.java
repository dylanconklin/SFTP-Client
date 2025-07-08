package sftpClient.Intent;

import sftpClient.Client.Client;

import java.util.ArrayList;

public class ExitIntent extends Intent {
    @Override
    void parse(ArrayList<String> args) {
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        return new ArrayList<>();
    }
}
