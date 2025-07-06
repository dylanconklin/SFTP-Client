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
            System.out.println(credentials.toString());
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
        while (true) {
            String host = IO.getInputFromUser("hostname : ").trim();
            String portInput = IO.getInputFromUser("port     : ").trim();
            String username = IO.getInputFromUser("username : ").trim();
            String password = IO.getInputFromUser("password : ").trim();

            // Simple validation checks
            if (host.isEmpty() || username.isEmpty() || password.isEmpty()) {
                System.out.println("Host, username, and password cannot be empty. Please try again.\n");
                continue;
            }

            int port;
            try {
                port = Integer.parseInt(portInput);
                if (port < 1 || port > 999999) {
                    System.out.println("Port must be between 1 and 999999. Please try again.\n");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number. Please enter a numeric value.\n");
                continue;
            }

            // All validations passed
            return new Credentials(host, port, username, password);
        }
    }
}