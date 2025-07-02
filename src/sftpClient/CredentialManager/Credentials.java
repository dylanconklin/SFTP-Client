package sftpClient.CredentialManager;

public class Credentials {
    String host;
    int port;
    String username;
    String password;

    Credentials(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }
}
