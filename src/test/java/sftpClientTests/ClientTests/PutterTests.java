package test.java.sftpClientTests.ClientTests;

import com.github.stefanbirkner.fakesftpserver.rule.FakeSftpServerRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import sftpClient.Client.Client;
import sftpClient.CredentialManager.Credentials;
import sftpClient.Intent.UploadIntent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PutterTests {
    @Rule
    public FakeSftpServerRule sftpServer = new FakeSftpServerRule();

    UploadIntent intent = new UploadIntent();
    Client client;
    Credentials credentials;
    static String hostname = "localhost";
    static String username = "user";
    static String password = "pass";

    @Before
    public void setUp() {
        sftpServer.addUser(username, password);
        credentials = new Credentials(hostname, sftpServer.getPort(), username, password);
    }

    @Test
    public void putSingleExistingFileTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("put", "file1.txt"));
        ArrayList<String> expectedOutput = new ArrayList<>(List.of("Uploaded file1.txt successfully."));

        // when
        ArrayList<String> output = intent.execute(client, args);

        // then
        assert(output.equals(expectedOutput));
    }

    @Test
    public void putMultipleExistingFilesTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("put", "file1.txt", "file3.txt", "file5.txt"));
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList(
                "Uploaded file1.txt successfully.",
                "Uploaded file3.txt successfully.",
                "Uploaded file5.txt successfully."
        ));

        // when
        ArrayList<String> output = intent.execute(client, args);

        // then
        assert(output.equals(expectedOutput));
    }

    @Test
    public void putSingleMissingFileTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("put", "file2.txt"));
        ArrayList<String> expectedOutput = new ArrayList<>(List.of("Failed to upload file2.txt."));

        // when
        ArrayList<String> output = intent.execute(client, args);

        // then
        assert(output.equals(expectedOutput));
    }

    @Test
    public void putMultipleMissingFilesTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("put", "file2.txt", "file4.txt"));
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList(
                "Failed to upload file2.txt.",
                "Failed to upload file4.txt."
        ));

        // when
        ArrayList<String> output = intent.execute(client, args);

        // then
        assert(output.equals(expectedOutput));
    }

    @Test
    public void putMultipleExistingAndMissingFilesTest() {
        // given
        ArrayList<String> args = new ArrayList<>(Arrays.asList("put", "file1.txt", "file2.txt", "file3.txt", "file4.txt", "file5.txt"));
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList(
                "Uploaded file1.txt successfully.",
                "Failed to upload file2.txt.",
                "Uploaded file3.txt successfully.",
                "Failed to upload file4.txt.",
                "Uploaded file5.txt successfully."
        ));

        // when
        ArrayList<String> output = intent.execute(client, args);

        // then
        assert(output.equals(expectedOutput));
    }
}