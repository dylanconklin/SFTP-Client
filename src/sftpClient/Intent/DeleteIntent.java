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
            output.add("rm Error: Missing Parameters Like File Names");
        }
        files = new ArrayList<>(args.subList(1, args.size()));
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        parse(args);
        ArrayList<String> output = new ArrayList<>();

        try {
            ArrayList<ChannelSftp.LsEntry> filesAndDirectories = new ArrayList<>(client.sftp
                    .ls(client.sftp.pwd())
                    .stream()
                    .filter(e -> files.contains(e.getFilename()))
                    .toList());

            // Delete Content in Non-Empty Directories
            filesAndDirectories
                    .stream()
                    .filter((e) -> e.getAttrs().isDir())
                    .map((e) -> {
                        String result = "";
                        try {
                            result = client.sftp.pwd() + "/" + e.getFilename();
                        } catch (SftpException ex) {
                        }
                        return result;
                    })
                    .filter(e -> !e.isEmpty())
                    .filter(e -> {
                        boolean result = false;
                        try {
                            result = !client.sftp.ls(e).isEmpty();
                        } catch (SftpException ex) {
                        }
                        return result;
                    })
                    .forEach(e -> {
                        try {
                            String currentDir = client.sftp.pwd();
                            DeleteIntent di = new DeleteIntent();
                            ArrayList<String> argList = new ArrayList<>(List.of("rm"));

                            client.sftp.cd(e);
                            client.sftp.ls(e)
                                    .stream()
                                    .map(ChannelSftp.LsEntry::getFilename)
                                    .filter(name -> !name.equals(".") && !name.equals(".."))
                                    .forEach(argList::add);
                            di.execute(client, argList);
                            client.sftp.cd(currentDir);
                        } catch (SftpException ex) {
                        } finally {
                            try {
                                client.sftp.cd("..");
                            } catch (SftpException ex) {}
                        }
                    });

            // Delete Directories and Files
            output.addAll(
                    filesAndDirectories
                            .stream()
                            .map((e) -> {
                                String filename = e.getFilename();
                                String result = "Failed to delete " + filename + ".";
                                try {
                                    String filepath = client.sftp.pwd() + "/" + filename;
                                    if (e.getAttrs().isDir()) {
                                        client.sftp.rmdir(filepath);
                                    } else {
                                        client.sftp.rm(filepath);
                                    }
                                    result = "Deleted " + filename + " successfully.";
                                } catch (SftpException ex) {
                                }
                                return result;
                            })
                            .toList()
            );
        } catch (Exception e) {
            output.add("Error deleting files.");
        }
        logger.info(output.toString());
        return output;
    }
}