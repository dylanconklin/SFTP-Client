package sftpClient.CredentialManager;

import java.io.*;

public class CredentialManager {
    static public Credentials getLoginCredentials() {
        Credentials credentials = new Credentials("", 0, "", "");
        try {
            File loginFile = new File("login.cred");
            FileReader fr = new FileReader(loginFile);
            BufferedReader br = new BufferedReader(fr);
            String host = br.readLine();
            int port = Integer.parseInt(br.readLine());
            String login = br.readLine();
            String password = br.readLine();
            credentials = new Credentials(host, port, login, password);
        } catch(Exception e) {
            credentials = createNewLogin();
            saveLoginCredentials(credentials);
        }
        return credentials;
    }

    static public void saveLoginCredentials(Credentials credentials) {
        try {
            File loginFile = new File("login.cred");
            FileWriter fw = new FileWriter(loginFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(credentials.host + "\n");
            bw.write(credentials.port + "\n");
            bw.write(credentials.username + "\n");
            bw.write(credentials.password + "\n");
        } catch(Exception e) {
        }
    }

    static public void saveLoginCredentials(String host, int port, String username, String password) {
        saveLoginCredentials(new Credentials(host, port, username, password));
    }

    static Credentials createNewLogin() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String host = "";
        int port = 0;
        String username = "";
        String password = "";
        Credentials credentials = new Credentials(host, port, username, password);
        try {
            System.out.print("hostname : ");
            host = bufferedReader.readLine();
            System.out.print("port     : ");
            port = Integer.parseInt(bufferedReader.readLine());
            System.out.print("username : ");
            username = bufferedReader.readLine();
            System.out.print("password : ");
            password = bufferedReader.readLine();
            credentials = new Credentials(host, port, username, password);
        } catch(Exception e) {
        }
        return credentials;
    }
}