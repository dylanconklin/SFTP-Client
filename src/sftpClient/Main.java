package sftpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        REPL repl = new REPL();
        repl.repl();
    }
}