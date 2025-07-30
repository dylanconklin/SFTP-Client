package sftpClient;


public class Main {
    public static void main(String[] args) {
        LoggerConfig.setup();
        REPL repl = new REPL();
        repl.repl();
    }
}
