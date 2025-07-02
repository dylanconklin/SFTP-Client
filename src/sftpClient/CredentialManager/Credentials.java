package sftpClient.CredentialManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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

    Credentials() {
        this.host = "";
        this.port = 0;
        this.username = "";
        this.password = "";
    }

    public String toJSON() {
        JSONObject jo = new JSONObject();
        jo.put("host", host);
        jo.put("port", port);
        jo.put("username", username);
        jo.put("password", password);
        return jo.toJSONString();
    }

    static Credentials fromJSON(String json) {
        Credentials credentials = new Credentials();
        try {
            JSONObject jo = (JSONObject) new JSONParser().parse(json);
            credentials = new Credentials(
                    (String) jo.get("host"),
                    (Integer) jo.get("port"),
                    (String) jo.get("username"),
                    (String) jo.get("password")
            );
        } catch (Exception e) {
        }
        return credentials;
    }
}
