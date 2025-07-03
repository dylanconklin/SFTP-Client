package test.java.sftpClientTests.CredentialManagerTests;

import sftpClient.CredentialManager.CredentialManager;
import sftpClient.CredentialManager.Credentials;
import java.io.*;

public class CredentialManagerTests {
    static String dir = "src/test/java/sftpClientTests/CredentialManagerTests/";
    static String testFilesPath = dir + "TestFiles/";

    private final InputStream SystemIn = System.in;
    private final PrintStream SystemOut = System.out;
    private ByteArrayInputStream userInput;

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

        // when
        Credentials credentials = CredentialManager.getLoginCredentials(file);

        // then
        assert(credentials.host != null && !credentials.host.isEmpty());
        assert(credentials.port != 0);
        assert(credentials.username != null && !credentials.username.isEmpty());
        assert(credentials.password != null && !credentials.password.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void cannotReadCredentialsFromInvalidFile() {
        // given
        File file = new File(testFilesPath + "invalidLogin.json");
        // TODO: Simulate user input for System.in

        // when
        Credentials credentials = CredentialManager.getLoginCredentials(file);

        // then
        fail();
    }
}