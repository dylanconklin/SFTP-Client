package test.java.sftpClientTests.CredentialManagerTests;

import sftpClient.CredentialManager.CredentialManager;
import sftpClient.CredentialManager.Credentials;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class CredentialManagerTests {
    static String dir = "src/test/java/sftpClientTests/CredentialManagerTests/";
    static String testFilesPath = dir + "TestFiles/";

    private final InputStream SystemIn = System.in;
    private ByteArrayInputStream userInput;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.AfterAll
    static void deleteFiles() {
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