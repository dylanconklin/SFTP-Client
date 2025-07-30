package sftpClient.Client;

import com.jcraft.jsch.*;
import java.util.logging.Logger;
import sftpClient.CredentialManager.Credentials;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Client {
    Credentials credentials;
    public Session session;
    public Channel channel;
    public ChannelSftp sftp;
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    public Client(Credentials credentials) {
        this.credentials = credentials;
        System.out.println(".... Attempting to connect with: ");
        System.out.println(credentials);
        connect();
    }

    public void connect() {
        logger.info("Connecting to " + credentials.host + ":" + credentials.port);
        JSch jsch = new JSch();
        try {
            this.session = jsch.getSession(this.credentials.username, this.credentials.host, this.credentials.port);
            this.session.setPassword(credentials.password);
            this.session.setConfig("StrictHostKeyChecking", "no");

            this.session.connect();
            this.channel = this.session.openChannel("sftp");
            this.channel.connect();
            this.sftp = (ChannelSftp) this.channel;
            System.out.println("Connected successfully to " + this.credentials.host);
            logger.info("Connected successfully to " + this.credentials.host);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (this.sftp != null && this.sftp.isConnected()) {
                this.sftp.disconnect();
            }
            if (this.channel != null && this.channel.isConnected()) {
                this.channel.disconnect();
            }
            if (this.session != null && this.session.isConnected()) {
                this.session.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public List<String> listFiles(String path) {
        List<String> files = new ArrayList<>();
        try {
            Vector<ChannelSftp.LsEntry> entries = sftp.ls(path);
            for (ChannelSftp.LsEntry entry : entries) {
                if (!entry.getAttrs().isDir()) {
                    files.add(entry.getFilename());
                }
            }
        } catch (SftpException e) {
            files.add("Failed to list files: " + e.getMessage());
        }
        return files;
    }
    public List<String> listFilesRecursive(String path, int depth) {

        List<String> results = new ArrayList<>();
        if (depth < 0) return results;
        try {
            Vector<ChannelSftp.LsEntry> entries = sftp.ls(path);
            for (ChannelSftp.LsEntry entry : entries) {
                String name = entry.getFilename();
                if (name.equals(".") || name.equals("..")) continue;

                String fullPath = path.endsWith("/") ? path + name : path + "/" + name;

                if (entry.getAttrs().isDir()) {
                    // Recurse into subdirectory
                    results.addAll(listFilesRecursive(fullPath,depth -1));
                } else {
                    results.add(fullPath); // full path of file
                }
            }
        } catch (SftpException e) {
            System.out.println("Failed to list files at " + path + ": " + e.getMessage());
        }

        return results;
    }

    public void copyDirectory(String srcDir, String destDir) throws SftpException {
        Vector<ChannelSftp.LsEntry> files = sftp.ls(srcDir);

        // Safely create destination directory if it doesn't exist
        try {
            sftp.mkdir(destDir);
        } catch (SftpException e) {
            if (e.id != ChannelSftp.SSH_FX_FAILURE) {
                System.out.println("Failed to create directory " + destDir + ": " + e.getMessage());
                throw e;
            }
            // else: directory already exists, safe to ignore
        }

        for (ChannelSftp.LsEntry entry : files) {
            String name = entry.getFilename();
            if (name.equals(".") || name.equals("..")) continue;

            String srcPath = srcDir.endsWith("/") ? srcDir + name : srcDir + "/" + name;
            String destPath = destDir.endsWith("/") ? destDir + name : destDir + "/" + name;

            if (entry.getAttrs().isDir()) {
                // Recursively copy subdirectory
                copyDirectory(srcPath, destPath);
            } else {
                // Copy file with safe resource handling
                try (InputStream input = sftp.get(srcPath)) {
                    sftp.put(input, destPath);
                    System.out.println("Copied file: " + srcPath + " → " + destPath);
                } catch (Exception ex) {
                    System.out.println("Failed to copy file " + srcPath + ": " + ex.getMessage());
                }
            }
        }
    }
    public void deleteDirectory(String path) throws SftpException {
        Vector<ChannelSftp.LsEntry> files = sftp.ls(path);

        for (ChannelSftp.LsEntry entry : files) {
            String name = entry.getFilename();
            if (name.equals(".") || name.equals("..")) continue;

            String fullPath = path + "/" + name;
            if (entry.getAttrs().isDir()) {
                // recursively delete
                deleteDirectory(fullPath);
            } else {
                sftp.rm(fullPath);
                System.out.println("Deleted file: " + fullPath);
            }
        }

        // delete the empty directory after contents are deleted
        sftp.rmdir(path);
    }
    public boolean changeDirectory(String path) {
        //System.out.println(" Raw args before parse: " + path);
        try {
            sftp.cd(path);
            return true;
        } catch (SftpException e) {
            System.out.println("Failed to change directory: " + e.getMessage());
            return false;
        }
    }


}
