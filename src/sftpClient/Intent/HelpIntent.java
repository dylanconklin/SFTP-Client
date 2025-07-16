package sftpClient.Intent;

import java.util.ArrayList;
import sftpClient.Client.Client;

/**
 * Intent for displaying help information about available commands
 */
public class HelpIntent extends Intent {

    @Override
    void parse(ArrayList<String> args) {
        // No parsing needed for help command
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        output.add("SFTP Client - Available Commands:");
        output.add("");

        output.add("Remote Server Commands:");
        output.add("  get <remote_file> [local_file]   - Download file from server");
        output.add("  put <local_file> [remote_file]   - Upload file to server");
        output.add("  ls [remote_path]                 - List files in remote directory");
        output.add("  rm <remote_file>                 - Delete file on server");
        output.add("");

        output.add("Local File Search:");
        output.add("  lsearch -name <pattern> [path]   - Search by filename pattern (* and ? wildcards)");
        output.add("  lsearch -content <text> [path]   - Search for text content in files");
        output.add("  lsearch -ext <extension> [path]  - Search by file extension");
        output.add("  lsearch -size <size> [path]      - Search by file size (+1M, -500K, 1024)");
        output.add("");

        output.add("Search Options:");
        output.add("  -case                            - Case sensitive search");
        output.add("  -nr, -norecursive               - Search only in specified directory");
        output.add("");

        output.add("General Commands:");
        output.add("  help                             - Show this help message");
        output.add("  exit, quit                       - Exit the application");
        output.add("");

        output.add("Search Examples:");
        output.add("  lsearch -name \"*.java\"           - Find all Java files");
        output.add("  lsearch -content \"TODO\" src/     - Find TODO comments in src directory");
        output.add("  lsearch -ext txt                 - Find all .txt files");
        output.add("  lsearch -size +1M                - Find files larger than 1MB");
        output.add("  lsearch -name \"test*\" -case      - Case-sensitive search for files starting with 'test'");

        return output;
    }
}
