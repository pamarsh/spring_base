package org.swamps.houseController.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EmailServer {

    @Id
    @GeneratedValue
    private int id;

    private String serverName;
    private String smtpServerAddress;
    private Integer smtpTlsPort;
    private String smtpSslPort;
    private String userName ;
    private String password;
    private String imapServerAddress;
    private String imapPort;
    private String fromAddress;

    public EmailServer() {}

    public EmailServer(int id, String serverName, String smtpServerAddress, Integer smtpTlsPort, String smtpSslPort, String userName, String password, String imapServerAddress, String imapPort, String fromAddress) {
        this.serverName = serverName;
        this.smtpServerAddress = smtpServerAddress;
        this.smtpTlsPort = smtpTlsPort;
        this.smtpSslPort = smtpSslPort;
        this.userName = userName;
        this.password = password;
        this.imapServerAddress = imapServerAddress;
        this.imapPort = imapPort;
        this.fromAddress = fromAddress;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getServerName() {
        return serverName;
    }

    public String getSmtpServerAddress() {
        return smtpServerAddress;
    }

    public Integer getSmtpTlsPort() {
        return smtpTlsPort;
    }

    public String getSmtpSslPort() {
        return smtpSslPort;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getImapServerAddress() {
        return imapServerAddress;
    }

    public String getImapPort() {
        return imapPort;
    }

    public String getFromAddress() {
        return fromAddress;
    }
}
