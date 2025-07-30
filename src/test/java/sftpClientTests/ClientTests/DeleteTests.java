package test.java.sftpClientTests.ClientTests;

import com.github.stefanbirkner.fakesftpserver.rule.FakeSftpServerRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import sftpClient.Client.Client;
import sftpClient.CredentialManager.Credentials;
import sftpClient.Intent.DownloadIntent;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.fail;

public class DeleteTests {
    @Rule
    public FakeSftpServerRule sftpServer = new FakeSftpServerRule();

    DownloadIntent intent = new DownloadIntent();
    Client client;
    Credentials credentials;
    static String hostname = "localhost";
    static String username = "user";
    static String password = "pass";

    @Before
    public void setUp() {
        sftpServer.addUser(username, password);

        try {
            sftpServer.putFile("file1.txt", "", StandardCharsets.UTF_8);
            sftpServer.createDirectory("dir1");
            sftpServer.putFile("dir1/file2.txt", "", StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            fail();
        }

        credentials = new Credentials(hostname, sftpServer.getPort(), username, password);
        client = new Client(credentials);
    }

    @Test
    public void deleteSingleFileTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("rm", "file1.txt"));

        // when
        intent.execute(client, args);

        // then
        assert(!sftpServer.existsFile("file1.txt"));
        assert(sftpServer.existsFile("dir1/file2.txt"));
    }

    @Test
    public void deleteDirectoryTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("rm", "dir1"));

        // when
        intent.execute(client, args);

        // then
        assert(sftpServer.existsFile("file1.txt"));
        assert(!sftpServer.existsFile("dir1/file2.txt"));
    }

    @Test
    public void deleteDirectoryAndFileTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("rm", "dir1", "file1.txt"));

        // when
        intent.execute(client, args);

        // then
        assert(!sftpServer.existsFile("file1.txt"));
        assert(!sftpServer.existsFile("dir1/file2.txt"));
    }
}