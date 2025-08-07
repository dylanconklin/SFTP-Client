package sftpClient.Intent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import sftpClient.Client.Client;

public class DeleteIntent extends Intent {
    ArrayList<String> files;
    private static final Logger logger = Logger.getLogger(CreateDirectoryIntent.class.getName());

    @Override
    void parse(ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("rm Error: Missing parameters for file names");
        }
        files = new ArrayList<>(args.subList(1, args.size()).stream().filter(e -> !e.equals(".") && !e.equals("..")).toList());
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        parse(args);
        ArrayList<String> output = new ArrayList<>();

        try {
            client.sftp
                    .ls(client.sftp.pwd())
                    .stream()
                    .filter(e -> files.contains(e.getFilename()))
                    .filter(e -> !e.getAttrs().isDir())
                    .forEach(e -> {
                        try {
                            client.sftp.rm(e.getFilename());
                        } catch (SftpException ex) {
                            output.add("Failed to delete " + e.getFilename() + ".");
                        }
                    });

            client.sftp
                    .ls(client.sftp.pwd())
                    .stream()
                    .filter(e -> files.contains(e.getFilename()))
                    .filter(e -> e.getAttrs().isDir())
                    .forEach(e -> {
                        ArrayList<String> deleteDirArgs = new ArrayList<>();
                        deleteDirArgs.add(e.getFilename());
                        output.addAll(new DeleteDirectoryIntent().execute(client, deleteDirArgs));
                    });
        } catch (Exception e) {
            output.add("Error deleting files.");
        }
        logger.info(output.toString());
        return output;
    }
}