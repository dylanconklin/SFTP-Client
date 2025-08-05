package test.java.sftpClientTests.ClientTests;

import com.github.stefanbirkner.fakesftpserver.rule.FakeSftpServerRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import sftpClient.Client.Client;
import sftpClient.CredentialManager.Credentials;
import sftpClient.Intent.CreateDirectoryIntent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateDirectoryIntentTest {

    @Rule
    public FakeSftpServerRule sftpServer = new FakeSftpServerRule();

    private Client client;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pass";
    private static final String HOST = "localhost";

    @Before
    public void setup() throws IOException {
        sftpServer.addUser(USERNAME, PASSWORD);
        Credentials credentials = new Credentials(HOST, sftpServer.getPort(), USERNAME, PASSWORD);
        client = new Client(credentials);

        // Setup: Create sample directory with a file inside
        sftpServer.putFile("/sourceDir/sample.txt", "Hello world!".getBytes());
    }

    @Test
    public void testCreateDirectorySuccess() {
        CreateDirectoryIntent intent = new CreateDirectoryIntent();

        ArrayList<String> args = new ArrayList<>();
        args.add("newDir"); // only source provided, dest auto-generated

        List<String> output = intent.execute(client, args);

        // Should contain success message with both src and dest
        assertTrue(output.get(0).contains("Success: Created directory"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDirectoryFailsWithMissingDirecroryName() {
        CreateDirectoryIntent intent = new CreateDirectoryIntent();

        ArrayList<String> args = new ArrayList<>();
        args.add(""); // empty string is invalid

        intent.execute(client, args);  // should throw IllegalArgumentException from parse()
    }

}
