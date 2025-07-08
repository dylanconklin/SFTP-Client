package sftpClient.Client;

import com.jcraft.jsch.*;
import sftpClient.CredentialManager.Credentials;

public class Client {
    Credentials credentials;
    public Session session;
    public Channel channel;
    public ChannelSftp sftp;

    public Client(Credentials credentials) {
        this.credentials = credentials;
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
}
