package sftpClient.CredentialManager;

import sftpClient.IO.IO;
import java.io.*;

public class CredentialManager {
    public static File credentialFile() {
        return new File("login.cred");
    }

    static public Credentials getLoginCredentials(File file) {
        Credentials credentials;
        try {
            credentials = Credentials.fromJSON(String.join("\n", IO.read(file)));
        } catch(Exception e) {
            credentials = createNewLogin();
            saveLoginCredentials(credentials, file);
        }
        return credentials;
    }

    static public void saveLoginCredentials(Credentials credentials, File file) {
        IO.write(credentials.toJSON(), file);
    }

    static Credentials createNewLogin() {
        String host = IO.getInputFromUser("hostname : ");
        int port = Integer.parseInt(IO.getInputFromUser("port     : "));
        String username = IO.getInputFromUser("username : ");
        String password = IO.getInputFromUser("password : ");
        return new Credentials(host, port, username, password);
    }
}