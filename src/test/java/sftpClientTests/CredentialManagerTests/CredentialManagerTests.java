package test.java.sftpClientTests.CredentialManagerTests;

import sftpClient.CredentialManager.CredentialManager;
import sftpClient.CredentialManager.Credentials;
import java.io.*;

public class CredentialManagerTests {
    static String dir = "src/test/java/sftpClientTests/CredentialManagerTests/";
    static String testFilesPath = dir + "TestFiles/";

    private final InputStream SystemIn = System.in;
    private final PrintStream SystemOut = System.out;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setIn(SystemIn);
        System.setOut(SystemOut);
    }

    @org.junit.jupiter.api.Test
    void canReadCredentialsFromValidFile() {
        // given
        File file = new File(testFilesPath + "validLogin.json");
        Credentials expectedCredentials = new Credentials("pdx.edu", 22, "user", "abc123");

        // when
        Credentials credentials = CredentialManager.getLoginCredentials(file);

        // then
        assert (credentials.toJSON().equals(expectedCredentials.toJSON()));
    }

    @org.junit.jupiter.api.Test
    void cannotReadCredentialsFromMissingFile() {
        // given
        File file = new File(testFilesPath + "missing.json");
        String input = "pdx.edu\n22\nusername\npassword\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Credentials expectedCredentials = new Credentials("pdx.edu", 22, "username", "password");

        // when
        Credentials credentials = CredentialManager.getLoginCredentials(file);
        file.delete();

        // then
        assert (credentials.toJSON().equals(expectedCredentials.toJSON()));
    }
}