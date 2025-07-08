package sftpClient.Intent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import sftpClient.Client.Client;

public class UploadIntent extends Intent {
    ArrayList<String> files;

    @Override
    void parse(ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("Error: Missing Parameters Like File Names");
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
                    String result = "Failed to upload " + file.getName() + ".";
                    try (FileInputStream dest = new FileInputStream(file)) {
                        client.sftp.put(dest, file.getName());
                        result = "Uploaded " + file.getName() + " successfully.";
                        dest.close();
                    } catch (Exception e) {
                        result = "Failed to upload " + file.getName() + ": " + e.getMessage();
                    }
                    return result;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
