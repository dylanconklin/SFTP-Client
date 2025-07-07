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

public class GetterTests {
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
            sftpServer.putFile("file3.txt", "", StandardCharsets.UTF_8);
            sftpServer.putFile("file5.txt", "", StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            fail();
        }

        credentials = new Credentials(hostname, sftpServer.getPort(), username, password);
    }

    @Test
    public void getSingleExistingFileTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("get", "file1.txt"));
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("Downloaded file1.txt successfully."));

        // when
        ArrayList<String> output = intent.execute(client, args);

        // then
        assert(output.equals(expectedOutput));
    }

    @Test
    public void getMultipleExistingFilesTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("get", "file1.txt", "file3.txt", "file5.txt"));
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList(
                "Downloaded file1.txt successfully.",
                "Downloaded file3.txt successfully.",
                "Downloaded file5.txt successfully."
        ));

        // when
        ArrayList<String> output = intent.execute(client, args);

        // then
        assert(output.equals(expectedOutput));
    }

    @Test
    public void getSingleMissingFileTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("get", "file2.txt"));
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("Failed to download file2.txt."));

        // when
        ArrayList<String> output = intent.execute(client, args);

        // then
        assert(output.equals(expectedOutput));
    }

    @Test
    public void getMultipleMissingFilesTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("get", "file2.txt", "file4.txt"));
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList(
                "Failed to download file2.txt.",
                "Failed to download file4.txt."
        ));

        // when
        ArrayList<String> output = intent.execute(client, args);

        // then
        assert(output.equals(expectedOutput));
    }

    @Test
    public void getMultipleExistingAndMissingFilesTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("get", "file1.txt", "file2.txt", "file3.txt", "file4.txt", "file5.txt"));
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList(
                "Downloaded file1.txt successfully.",
                "Failed to download file2.txt.",
                "Downloaded file3.txt successfully.",
                "Failed to download file4.txt.",
                "Downloaded file5.txt successfully."
        ));

        // when
        ArrayList<String> output = intent.execute(client, args);

        // then
        assert(output.equals(expectedOutput));
    }
}