package sftpClient.Intent;

import java.util.ArrayList;

public abstract class Intent {
    ArrayList<String> args;
    abstract void parse();
    abstract void execute();
}
