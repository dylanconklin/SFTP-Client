package sftpClient;

import sftpClient.IO.IO;

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
        String[] parts = input.trim().split("\\s+");
        String command = parts[0];
        switch (command) {
            case "get":
                if (parts.length < 2) {
                    output.add("get Error: Missing Parameters Like File Names");
                } else {
                    String filename = parts[1];
                    output.add("Downloading ....  " + filename + " .... Please Wait");
                    // Get the Actual file here to download
                    // Show Success of Fail
                }
                break;

            case "put":
                if (parts.length < 2) {
                    output.add("Put Error: Missing Parameters Like File Names");
                } else {
                    String filename = parts[1];
                    output.add("Uploading ....  " + filename + " .... Please Wait");
                    // Upload the actual file here
                    // Show Success of Fail
                }
                break;

            case "ls":
                output.add("Listing All the Files In the Current Directory....  ");
                // Actually list all the files in the current directory
                // Show Success of Fail
                break;

            case "rm":
                if (parts.length < 2) {
                    output.add("Rm Error: Missing Parameters Like File Names");
                } else {
                    String filename = parts[1];
                    output.add("Deleting ....  " + filename + " .... Please Wait");
                    // Acutally delete the files here
                    // Show Success of Fail
                }
                break;

            case "exit":
                output.add("Exiting FTP client.");
                break;

            default:
                output.add("Unknown Command: " + command);
        }


        return output;
    }

    public void print(ArrayList<String> output) {
        output.forEach(System.out::println);
    }
}
