package test.java.sftpClientTests.ClientTests;

import com.github.stefanbirkner.fakesftpserver.rule.FakeSftpServerRule;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import sftpClient.Client.Client;
import sftpClient.CredentialManager.Credentials;
import sftpClient.Intent.RenameIntent;

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
    public void setUp() {
        Credentials credentials = new Credentials(HOSTNAME, sftpServer.getPort(), USERNAME, PASSWORD);

        client = new Client(credentials) {
            @Override
            public void connect() {
                this.session = null;
                this.sftp = new ChannelSftp() {
                    @Override
                    public void rename(String oldpath, String newpath) throws SftpException {
                        if ("missing.txt".equals(oldpath)) {
                            throw new SftpException(ChannelSftp.SSH_FX_NO_SUCH_FILE, "Missing file");
                        }
                    }
                };
            }
        };

        client.connect();
        intent = new RenameIntent();
    }

    @Test
    public void testRenameFileSuccess() {
        ArrayList<String> args = new ArrayList<>(List.of("hello.txt", "yoo.txt"));
        List<String> result = intent.execute(client, args);

        assertTrue(result.toString().contains("Renamed hello.txt to yoo.txt"));
    }

    @Test
    public void testRenameFileMissingSource() {
        ArrayList<String> args = new ArrayList<>(List.of("missing.txt", "yoo.txt"));
        List<String> result = intent.execute(client, args);

        assertTrue(result.toString().contains("Failed to rename file"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRenameMissingArgs() {
        ArrayList<String> args = new ArrayList<>();
        intent.execute(client, args);
    }
}
