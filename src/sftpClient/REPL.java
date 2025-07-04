package sftpClient;

import sftpClient.IO.IO;
import sftpClient.Intent.Intent;
import java.util.ArrayList;
import java.util.Objects;

public class REPL {
    public void repl() {
        String input = "";
        ArrayList<String> output = new ArrayList<>();
        while (!Objects.equals(input, "quit") && !Objects.equals(input, "exit")) {
            input = read();
            output = eval(input);
            print(output);
        }
    }

    public String read() {
        return IO.getInputFromUser(">>> ");
    }

    public ArrayList<String> eval(String input) {
        ArrayList<String> output = new ArrayList<>();
        Intent intent = Intent.getIntent(input);
        intent.execute();
        return output;
    }

    public void print(ArrayList<String> output) {
        output.forEach(System.out::println);
    }
}
