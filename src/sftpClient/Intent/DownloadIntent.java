package sftpClient.Intent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import sftpClient.Client.Client;

public class DownloadIntent extends Intent {
    ArrayList<String> files;
    private static final Logger logger = Logger.getLogger(DownloadIntent.class.getName());

    @Override
    void parse(ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("Error: Missing Parameters Like File Names");
            logger.warning("Error: Missing Parameters Like File Names");
        }
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
                    try {
                        OutputStream dest = new FileOutputStream(file);
                        client.sftp.get(file.getName(), dest);
                        dest.close();
                    } catch (Exception e) {
                        file.delete();
                        successful = false;
                    }
                    String result = successful ? "" : "Failed to download " + file.getName() + ".";
                    logger.info(result);
                    return result;
                })
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}