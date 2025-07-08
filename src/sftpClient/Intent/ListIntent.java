package sftpClient.Intent;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.jcraft.jsch.ChannelSftp;
import sftpClient.Client.Client;

public class ListIntent extends Intent {
    @Override
    void parse(ArrayList<String> args) {
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<ChannelSftp.LsEntry> output = new ArrayList<>();
        try {
            String path = ".";

            ArrayList<ChannelSftp.LsEntry> dirItems =
                    new ArrayList<>(client.sftp.ls(path));

            ArrayList<ChannelSftp.LsEntry> hiddenDirectories =
                    dirItems
                            .stream()
                            .filter((e) -> e.getFilename().startsWith("."))
                            .filter((e) -> e.getAttrs().isDir())
                            .sorted()
                            .collect(Collectors.toCollection(ArrayList::new));

            ArrayList<ChannelSftp.LsEntry> hiddenFiles =
                    dirItems
                            .stream()
                            .filter((e) -> e.getFilename().startsWith("."))
                            .filter((e) -> !e.getAttrs().isDir())
                            .sorted()
                            .collect(Collectors.toCollection(ArrayList::new));

            ArrayList<ChannelSftp.LsEntry> directories =
                    dirItems
                            .stream()
                            .filter((e) -> !e.getFilename().startsWith("."))
                            .filter((e) -> e.getAttrs().isDir())
                            .sorted()
                            .collect(Collectors.toCollection(ArrayList::new));

            ArrayList<ChannelSftp.LsEntry> files =
                    dirItems
                            .stream()
                            .filter((e) -> !e.getFilename().startsWith("."))
                            .filter((e) -> !e.getAttrs().isDir())
                            .sorted()
                            .collect(Collectors.toCollection(ArrayList::new));

            output.addAll(hiddenDirectories);
            output.addAll(hiddenFiles);
            output.addAll(directories);
            output.addAll(files);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return output
                .stream()
                .map((e) -> e.getFilename() + (e.getAttrs().isDir() ? "/" : ""))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}