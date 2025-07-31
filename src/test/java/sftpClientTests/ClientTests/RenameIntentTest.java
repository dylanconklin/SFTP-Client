package test.java.sftpClientTests.ClientTests;

import com.github.stefanbirkner.fakesftpserver.rule.FakeSftpServerRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import sftpClient.Client.Client;
import sftpClient.CredentialManager.Credentials;
import sftpClient.Intent.RenameIntent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class RenameIntentTest {
    @Rule
    public FakeSftpServerRule sftpServer = new FakeSftpServerRule();

    private Client client;
    private RenameIntent intent;
    private static final String HOSTNAME = "localhost";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pass";

    @Before
    public void setUp() throws IOException {
        // Create a user on the fake SFTP server
        sftpServer.addUser(USERNAME, PASSWORD);

        // Create a temporary file with content
        Path originalFile = Files.createTempFile("hello", ".txt");
        Files.writeString(originalFile, "Test content");

        // Upload the file to the fake SFTP server
        sftpServer.putFile("/hello.txt", Files.readString(originalFile).getBytes());

        // Setup client
        Credentials credentials = new Credentials(HOSTNAME, sftpServer.getPort(), USERNAME, PASSWORD);
        client = new Client(credentials);
        client.connect(); // 💥 Required to initialize the sftp field!

        intent = new RenameIntent();
    }

    @Test
    public void testRenameFileSuccess() {
        ArrayList<String> args = new ArrayList<>(List.of("hello.txt", "yoo.txt"));
        List<String> result = intent.execute(client, args);

        assertTrue(result.toString().contains("Renamed file"));
    }

    @Test
    public void testRenameFileMissingSource() {
        ArrayList<String> args = new ArrayList<>(List.of("missing.txt", "yoo.txt"));
        List<String> result = intent.execute(client, args);

        assertTrue(result.toString().contains("Failed to rename file"));
    }

    @Test
    public void testRenameMissingArgs() {
        ArrayList<String> args = new ArrayList<>();
        List<String> result = intent.execute(client, args);

        assertTrue(result.toString().contains("Usage: rename <old_filename> <new_filename>"));
    }
}
