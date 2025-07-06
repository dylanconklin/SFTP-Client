package sftpClient.Intent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import sftpClient.Client.Client;

public class DownloadIntent extends Intent {
    ArrayList<String> files;

    @Override
    void parse(ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("get Error: Missing Parameters Like File Names");
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
                    String result = "Failed to download " + file.getName() + ".";
                    try {
                        OutputStream dest = new FileOutputStream(file);
                        client.sftp.get(file.getName(), dest);
                        result = "Downloaded " + file.getName() + " successfully.";
                        dest.close();
                    } catch (Exception e) {
                        file.delete();
                    }
                    return result;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
}