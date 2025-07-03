package sftpClient;

import sftpClient.CredentialManager.CredentialManager;
import sftpClient.CredentialManager.Credentials;
import sftpClient.IO.IO;
import java.util.ArrayList;
import java.util.Objects;

public class REPL {
    Client client;

    REPL() {
        this.client = new Client(this.credentials);
    Credentials credentials;

    REPL() {
        this.credentials = CredentialManager.getLoginCredentials(CredentialManager.credentialFile());
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
        ArrayList<String> output = new ArrayList<>();
        output.add(input);
        return output;
    }

    public void print(ArrayList<String> output) {
        output.forEach(System.out::println);
    }
}
