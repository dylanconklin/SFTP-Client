package sftpClient.Intent;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import sftpClient.REPL;
import sftpClient.Client.Client;

public class UploadIntent extends Intent {
    ArrayList<String> files;
    private static final Logger logger = Logger.getLogger(UploadIntent.class.getName());

    @Override
    void parse(ArrayList<String> args) {
        files = new ArrayList<>(args.subList(1, args.size()));
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        parse(args);
        return files
                .stream()
                .map(File::new)
                .map(file -> {
                    boolean successful = true;
                    try (FileInputStream dest = new FileInputStream(file)) {
                        client.sftp.put(dest, file.getName());
                    } catch (Exception e) {
                        successful = false;
                    }
                    String result = successful ? "" : "Failed to upload " + file.getName() + ".";
                    logger.info(result);
                    return result;
                })
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
