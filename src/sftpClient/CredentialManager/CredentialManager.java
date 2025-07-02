package sftpClient.CredentialManager;

import sftpClient.IO.IO;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CredentialManager {
    static public Credentials getLoginCredentials() {
        Credentials credentials;
        try {
            credentials = Credentials.fromJSON(String.join("\n", IO.read(new File("login.cred"))));
        } catch(Exception e) {
            credentials = createNewLogin();
            saveLoginCredentials(credentials);
        }
        return credentials;
    }

    static public void saveLoginCredentials(Credentials credentials) {
        IO.write(credentials.toJSON(), new File("login.cred"));
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