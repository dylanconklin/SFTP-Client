package sftpClient.Intent;

import java.util.ArrayList;
import sftpClient.Client.Client;

public abstract class Intent {
    ArrayList<String> args;

    abstract void parse(ArrayList<String> args);

    abstract public ArrayList<String> execute(Client client, ArrayList<String> args);

    public static Intent getIntent(String command) {
        ArrayList<String> output = new ArrayList<>();
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

            case "exit":
            case "quit":
                intent = new ExitIntent();
                break;

            case "search":
                intent = new SearchIntent();
                break;
            case "cpdir":
                intent = new CopyDirectoryIntent();
                break;
            case "rmdir":
                intent = new DeleteDirectoryIntent();
                break;
            default:
                output.add("Unknown Command: " + command);
        }
        return intent;
    }
}