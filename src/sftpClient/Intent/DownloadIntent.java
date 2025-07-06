package sftpClient.Intent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import sftpClient.Client.Client;

public class DownloadIntent extends Intent {
    @Override
    void parse(ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        if (args.size() < 2) {
            output.add("get Error: Missing Parameters Like File Names");
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        ArrayList<String> output = new ArrayList<>();
        new ArrayList<>(args.subList(1, args.size()))
                .forEach( filename -> {
                    File destFile = new File(filename);
                    try {
                        OutputStream dest = new FileOutputStream(destFile);
                        client.sftp.get(filename, dest);
                        output.add("Downloaded " + filename + " successfully.");
                        dest.close();
                    } catch (Exception e) {
                        destFile.delete();
                        output.add("Failed to download " + filename + ".");
                    }
                });
        return output;
    }
}
