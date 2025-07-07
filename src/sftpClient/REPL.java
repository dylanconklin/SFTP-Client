package sftpClient;

import sftpClient.Client.Client;
import sftpClient.CredentialManager.CredentialManager;
import sftpClient.CredentialManager.Credentials;
import sftpClient.IO.IO;
import sftpClient.Intent.Intent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class REPL {
    Credentials credentials;
    Client client;

    REPL() {
        this.credentials = CredentialManager.getLoginCredentials(CredentialManager.credentialFile());
        this.client = new Client(this.credentials);
    }

    public void repl() {
        String input = "";
        ArrayList<String> output = new ArrayList<>();
        while (!Objects.equals(input, "quit") && !Objects.equals(input, "exit")) {
            input = read();
            output = eval(input);
            print(output);
        }
        this.client.disconnect();
    }

    public String read() {
        return IO.getInputFromUser(">>> ");
    }

    public ArrayList<String> eval(String input) {
        ArrayList<String> args = new ArrayList<>(Arrays.asList(input.trim().split("\\s+")));
        return Intent
                .getIntent(args.get(0))
                .execute(client, args);
    }

    public void print(ArrayList<String> output) {
        output.forEach(System.out::println);
    }
}
