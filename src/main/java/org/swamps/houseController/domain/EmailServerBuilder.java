package org.swamps.houseController.domain;

public class EmailServerBuilder {
    private String serverName = "";
    private String smtpServerAddress = "";
    private Integer smtpTlsPort = 0;
    private String smtpSslPort = "";
    private String userName = "";
    private String password = "";
    private String imapServerAddress = "";
    private String imapPort = "";
    private int id = 0;
    private String fromAddress;


    public static EmailServerBuilder emptyEmailServer() {
        return new EmailServerBuilder();
    }

    public EmailServerBuilder withServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public EmailServerBuilder withSmtpServerAddress(String smtpServerAddress) {
        this.smtpServerAddress = smtpServerAddress;
        return this;
    }

    public EmailServerBuilder withSmtpTlsPort(Integer smtpTlsPort) {
        this.smtpTlsPort = smtpTlsPort;
        return this;
    }

    public EmailServerBuilder withSmtpSslPort(String smtpSslPort) {
        this.smtpSslPort = smtpSslPort;
        return this;
    }

    public EmailServerBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public EmailServerBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public EmailServerBuilder withImapServerAddress(String imapServerAddress) {
        this.imapServerAddress = imapServerAddress;
        return this;
    }

    public EmailServerBuilder withImapPort(String imapPort) {
        this.imapPort = imapPort;
        return this;
    }

    public EmailServerBuilder withFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
        return this;
    }

    public EmailServer build() {
        return new EmailServer(id, serverName, smtpServerAddress, smtpTlsPort, smtpSslPort, userName, password, imapServerAddress, imapPort, fromAddress);
    }


    public EmailServerBuilder withId(int id) {
        this.id = id;
        return  this;
    }
}