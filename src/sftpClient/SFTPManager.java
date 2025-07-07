package sftpClient;

import com.jcraft.jsch.*;
import sftpClient.CredentialManager.Credentials;

import java.io.File;

public class SFTPManager {
    private Session session;
    private ChannelSftp sftpChannel;
    private Credentials credentials;

    public SFTPManager(Credentials credentials) {
        this.credentials = credentials;
    }

    public boolean connect() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(credentials.username, credentials.host, credentials.port);
            session.setPassword(credentials.password);

            // Skip host key checking (for development - not recommended for production)
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;

            return true;
        } catch (JSchException e) {
            System.err.println("Failed to connect: " + e.getMessage());
            return false;
        }
    }

    public boolean downloadFile(String remoteFilePath, String localFilePath) {
        if (sftpChannel == null || !sftpChannel.isConnected()) {
            System.err.println("SFTP channel is not connected");
            return false;
        }

        try {
            // Create local directory if it doesn't exist
            File localFile = new File(localFilePath);
            File parentDir = localFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            sftpChannel.get(remoteFilePath, localFilePath);
            return true;
        } catch (SftpException e) {
            System.err.println("Failed to download file: " + e.getMessage());
            return false;
        }
    }

    public boolean uploadFile(String localFilePath, String remoteFilePath) {
        if (sftpChannel == null || !sftpChannel.isConnected()) {
            System.err.println("SFTP channel is not connected");
            return false;
        }

        try {
            sftpChannel.put(localFilePath, remoteFilePath);
            return true;
        } catch (SftpException e) {
            System.err.println("Failed to upload file: " + e.getMessage());
            return false;
        }
    }

    public String[] listFiles(String remotePath) {
        if (sftpChannel == null || !sftpChannel.isConnected()) {
            System.err.println("SFTP channel is not connected");
            return new String[0];
        }

        try {
            java.util.Vector<ChannelSftp.LsEntry> fileList = sftpChannel.ls(remotePath);
            return fileList.stream()
                    .map(entry -> entry.getFilename())
                    .filter(name -> !name.equals(".") && !name.equals(".."))
                    .toArray(String[]::new);
        } catch (SftpException e) {
            System.err.println("Failed to list files: " + e.getMessage());
            return new String[0];
        }
    }

    public boolean deleteFile(String remoteFilePath) {
        if (sftpChannel == null || !sftpChannel.isConnected()) {
            System.err.println("SFTP channel is not connected");
            return false;
        }

        try {
            sftpChannel.rm(remoteFilePath);
            return true;
        } catch (SftpException e) {
            System.err.println("Failed to delete file: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        if (sftpChannel != null && sftpChannel.isConnected()) {
            sftpChannel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    public boolean isConnected() {
        return sftpChannel != null && sftpChannel.isConnected() &&
                session != null && session.isConnected();
    }
}
