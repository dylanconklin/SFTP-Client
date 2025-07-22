package sftpClient.CredentialManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Credentials {
    public String host;
    public int port;
    public String username;
    public String password;

    public Credentials(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }
    @Override
    public String toString() {
        return  "----Curr Cred---- \n" +
                "Host: " + host + "\n" +
                "Port: " + port + "\n" +
                "Username: " + username + "\n" +
                "Password: " + password + "\n" +
                "-----------------";
    }
    public String toJSON() {
        JSONObject jo = new JSONObject();
        jo.put("host", host);
        jo.put("port", port);
        jo.put("username", username);
        jo.put("password", password);
        return jo.toJSONString();
    }

    static Credentials fromJSON(String json) throws Exception {
        System.out.println("Loading credentials JSON:\n" + json);
        JSONObject jo = (JSONObject) new JSONParser().parse(json);
        return new Credentials(
                (String) jo.get("host"),
                (int) ((long) jo.get("port")),
                (String) jo.get("username"),
                (String) jo.get("password")
        );
    }
}
