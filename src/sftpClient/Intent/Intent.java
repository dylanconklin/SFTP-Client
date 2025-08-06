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

            case "mkdir":
                intent = new CreateDirectoryIntent();
                break;

            case "lpwd":
                intent = new LocalPrintWorkingDirectoryIntent();
                break;

            case "lls":
                intent = new LocalListIntent();
                break;

            case "lcd":
                intent = new LocalChangeDirectoryIntent();
                break;

            case "lrn":
                intent = new LocalRenameIntent();
                break;

            case "exit":
            case "quit":
                intent = new ExitIntent();
                break;
   
            case "lsearch":
                intent = new LocalSearchIntent();
                break;
            
            case "help":
                intent = new HelpIntent();
                break;
            
            case "search":
                intent = new SearchIntent();
                break;
            
            case "cpdir":
                intent = new CopyDirectoryIntent();
                break;

            case "cd":
                intent = new ChangeDirectoryIntent();
                break;
            
            case "rmdir":
                intent = new DeleteDirectoryIntent();
                break;

            case "rename":
                intent = new RenameIntent();
                break;

            case "pwd":
                intent = new PresentWorkingDirectoryIntent();
                break;

            default:
                intent = new UnknownCommandIntent();
                break;
        }
        return intent;
    }
}