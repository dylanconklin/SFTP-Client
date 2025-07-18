package sftpClient.Client;

import com.jcraft.jsch.*;
import sftpClient.CredentialManager.Credentials;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Client {
    Credentials credentials;
    public Session session;
    public Channel channel;
    public ChannelSftp sftp;

    public Client(Credentials credentials) {
        this.credentials = credentials;
        System.out.println(".... Attempting to connect with: ");
        System.out.println(credentials);
        connect();
    }

    public void connect() {
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
            files.add(" Failed to list files: " + e.getMessage());
        }
        return files;
    }

}
