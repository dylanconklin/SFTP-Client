package sftpClient.CredentialManager;

import sftpClient.IO.IO;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CredentialManager {
    static public Credentials getLoginCredentials() {
        Credentials credentials;
        try {
            ArrayList<String> input = IO.read(new File("login.cred"));
            credentials = new Credentials(input.get(0), Integer.parseInt(input.get(1)), input.get(2), input.get(3));
        } catch(Exception e) {
            credentials = createNewLogin();
            saveLoginCredentials(credentials);
        }
        return credentials;
    }

    static public void saveLoginCredentials(Credentials credentials) {
        IO.write(new ArrayList<>(Arrays.asList(
                credentials.host,
                Integer.toString(credentials.port),
                credentials.username,
                credentials.password
        )), new File("login.cred"));
    }

    static public void saveLoginCredentials(String host, int port, String username, String password) {
        saveLoginCredentials(new Credentials(host, port, username, password));
    }

    static Credentials createNewLogin() {
        Credentials credentials;
        String host = IO.getInputFromUser("hostname : ");
        int port = Integer.parseInt(IO.getInputFromUser("port     : "));
        String username = IO.getInputFromUser("username : ");
        String password = IO.getInputFromUser("password : ");
        credentials = new Credentials(host, port, username, password);
        return credentials;
    }
}