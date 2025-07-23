package sftpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import sftpClient.Client.Client;
import sftpClient.CredentialManager.CredentialManager;
import sftpClient.CredentialManager.Credentials;
import sftpClient.IO.IO;
import sftpClient.Intent.Intent;

public class REPL {
    Credentials credentials;
    Client client;
    private Timer inactivityTimer;
    private static final long TIMEOUT_MS = 300_000; // 5 minute timeout
    private static final Logger logger = Logger.getLogger(REPL.class.getName());

    REPL() {
        this.credentials = CredentialManager.getLoginCredentials(CredentialManager.credentialFile());
        this.client = new Client(this.credentials);
    }

    public void repl() {
        String input = "";
        ArrayList<String> output = new ArrayList<>();
        resetInactivityTimer();

        while (!Objects.equals(input, "quit") && !Objects.equals(input, "exit")) {
            try {
                input = read();
                resetInactivityTimer();
                output = eval(input);
                print(output);
            } catch (Exception e) {
                System.out.println("Error reading input: " + e.getMessage());
                logger.info("Error reading input: " + e.getMessage());
            }
        }

        shutdown();
    }

    public String read() {
        return IO.getInputFromUser(">>> ");
    }

    public ArrayList<String> eval(String input) {
        ArrayList<String> args = new ArrayList<>(Arrays.asList(input.trim().split("\\s+")));
        return Intent
                .getIntent(args.get(0))
                .execute(client, args);
    }

    public void print(ArrayList<String> output) {
        output.forEach(System.out::println);
    }

    private void resetInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }

        inactivityTimer = new Timer(true);
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("\nInactivity timeout. Disconnecting...");
                logger.info("\nInactivity timeout. Disconnecting...");
                shutdown();
                System.exit(0);
            }
        }, TIMEOUT_MS);
    }

    private void shutdown() {
        if (client != null) {
            client.disconnect();
        }
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }
    }
}
