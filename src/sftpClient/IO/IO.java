package sftpClient.IO;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class IO {
    public static void write(ArrayList<String> text, File file) {
        write(String.join("\n", text), file);
    }

   public static void write(String text, File file) {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.flush();
        } catch(Exception e) {
        }
    }

    public static ArrayList<String> read(File file) {
        ArrayList<String> result = new ArrayList<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            result = br.lines().collect(Collectors.toCollection(ArrayList::new));
        } catch(Exception e) {
        }
        return result;
    }

    public static String read(File file, String delimiter) {
        return String.join(delimiter, read(file));
    }

    public static String getInputFromUser(String prompt) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        System.out.print(prompt);
        try {
            input = bufferedReader.readLine();
        } catch(Exception e) {
        }
        return input;
    }
}