package sftpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        System.out.print(">>> ");
        try {
            input = bufferedReader.readLine();
        } catch(Exception e) {
            input = "";
        }
        return input;
    }

    public ArrayList<String> eval(String input) {
        ArrayList<String> output = new ArrayList<>();
        output.add(input);
        return output;
    }

    public void print(ArrayList<String> output) {
        output.forEach(System.out::println);
    }
}
