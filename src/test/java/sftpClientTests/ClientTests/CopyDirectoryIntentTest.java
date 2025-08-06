package test.java.sftpClientTests.ClientTests;

import com.github.stefanbirkner.fakesftpserver.rule.FakeSftpServerRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import sftpClient.Client.Client;
import sftpClient.CredentialManager.Credentials;
import sftpClient.Intent.CopyDirectoryIntent;
import com.jcraft.jsch.ChannelSftp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sftpClient.Intent.CreateDirectoryIntent;

import static org.junit.Assert.assertTrue;

public class CopyDirectoryIntentTest {

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
        // Create a dummy subclass to override connect()
        Credentials credentials = new Credentials(HOST, sftpServer.getPort(), USERNAME, PASSWORD);
        Client fakeClient = new Client(credentials) {
            @Override
            public void connect() {
                // don't call real connect
                this.session = null;
                this.sftp = new ChannelSftp() {
                    @Override
                    public void mkdir(String path) {
                        System.out.println("Mock mkdir: " + path);
                    }
                };
            }
        };

        CreateDirectoryIntent intent = new CreateDirectoryIntent();

        ArrayList<String> args = new ArrayList<>();
        args.add("newDir"); // only source provided

        List<String> output = intent.execute(fakeClient, args);

        assertTrue(output.get(0).contains("Success"));
    }

    @Test
    public void testCopyDirectoryToCustomTarget() {
        CopyDirectoryIntent intent = new CopyDirectoryIntent();
        ArrayList<String> args = new ArrayList<>();
        args.add("sourceDir");
        args.add("copiedDir");

        List<String> output = intent.execute(client, args);
        System.out.println(output.get(0));
        assertTrue(output.get(0).contains("Success: Copied directory from sourceDir to copiedDir"));
    }

    @Test
    public void testCopyDirectoryFailsWithMissingSource() {
        CopyDirectoryIntent intent = new CopyDirectoryIntent();

        ArrayList<String> args = new ArrayList<>();
        args.add("nonExistentDir");
        args.add("random");

        List<String> output = intent.execute(client, args);
        System.out.println(" Raw args before parse: " + output.get(0));
        assertTrue(output.get(0).contains("Failed:"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopyDirectoryNoArgumentsThrows() {
        CopyDirectoryIntent intent = new CopyDirectoryIntent();

        ArrayList<String> args = new ArrayList<>();
        args.add("");

        intent.execute(client, args);
    }
}
