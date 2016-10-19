package org.swamps.houseController.dto;

public class EmailClientDetailsMessageBuilder {
    private String serverName;
    private String smtpServerAddress;
    private Integer smtpTlsPort;
    private String smtpSslPort;
    private String userName;
    private String password;
    private String imapServerAddress;
    private String imapPort;
    private String fromAddress;

    public static EmailClientDetailsMessageBuilder emptyEmailServer() {
        return new EmailClientDetailsMessageBuilder();
    }

    public EmailClientDetailsMessageBuilder withServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public EmailClientDetailsMessageBuilder withSmtpServerAddress(String smtpServerAddress) {
        this.smtpServerAddress = smtpServerAddress;
        return this;
    }

    public EmailClientDetailsMessageBuilder withSmtpTlsPort(Integer smtpTlsPort) {
        this.smtpTlsPort = smtpTlsPort;
        return this;
    }

    public EmailClientDetailsMessageBuilder withSmtpSslPort(String smtpSslPort) {
        this.smtpSslPort = smtpSslPort;
        return this;
    }

    public EmailClientDetailsMessageBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public EmailClientDetailsMessageBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public EmailClientDetailsMessageBuilder withImapServerAddress(String imapServerAddress) {
        this.imapServerAddress = imapServerAddress;
        return this;
    }

    public EmailClientDetailsMessageBuilder withImapPort(String imapPort) {
        this.imapPort = imapPort;
        return this;
    }

    public EmailClientDetailsMessageBuilder withFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
        return this;
    }

    public EmailClientDetailsMessage build() {
        return new EmailClientDetailsMessage(0,serverName, smtpServerAddress, smtpTlsPort, smtpSslPort, userName, password, imapServerAddress, imapPort, fromAddress);
    }
}