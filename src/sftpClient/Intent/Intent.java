package sftpClient.Intent;

import java.util.ArrayList;

public abstract class Intent {
    ArrayList<String> args;
    abstract void parse(ArrayList<String> args);
    abstract public ArrayList<String> execute();

    public static Intent getIntent(String input) {
        ArrayList<String> output = new ArrayList<>();
        String[] parts = input.trim().split("\\s+");
        String command = parts[0];
        Intent intent = null;
        switch (command) {
            case "get":
                intent = new DownloadIntent();
                break;

            case "put":
                intent = new UploadIntent();
                break;

            case "ls":
                intent = new ListIntent();
                break;

            case "rm":
                intent = new DeleteIntent();
                break;

            default:
                output.add("Unknown Command: " + command);
        }
        return intent;
    }
}
