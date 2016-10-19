package org.swamps.houseController.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.swamps.houseController.controller.ResourceDoesNotExistException;
import org.swamps.houseController.counters.Counted;
import org.swamps.houseController.domain.*;
import org.swamps.houseController.domain.repositories.EmailGroupRepository;
import org.swamps.houseController.domain.repositories.EmailSettingsRepository;
import org.swamps.houseController.dto.*;
import org.swamps.houseController.emailClient.NOPMailSender;

import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;


/**
 *
 * mail.protocol=smtp
 * mail.host=localhost
 * mail.port=25
 * mail.smtp.auth=false
 * mail.smtp.starttls.enable=false
 * mail.from=me@localhost
 * mail.username=
 * mail.password=
 * @return
 */
@Service
public class EmailClientService {

    @Autowired
    EmailSettingsRepository emailSettingsRepository;

    @Autowired
    private EmailGroupRepository emailGroupRepository;


    private JavaMailSender mailSender;

    private List<EmailGroupMessage> groups;

    @PostConstruct
    public void setupEmailSender() {

    }

    public EmailClientDetailsMessage getEmailSettings() throws ResourceDoesNotExistException {
        final EmailServer emailServerSettings = getEmailServerSettings();
            return EmailClientDetailsMessageBuilder.emptyEmailServer()
                    .withUserName(emailServerSettings.getUserName())
                    .withImapPort(emailServerSettings.getImapPort())
                    .withImapServerAddress(emailServerSettings.getImapServerAddress())
                    .withPassword(emailServerSettings.getPassword())
                    .withSmtpServerAddress(emailServerSettings.getSmtpServerAddress())
                    .withSmtpSslPort(emailServerSettings.getSmtpSslPort())
                    .withSmtpTlsPort(emailServerSettings.getSmtpTlsPort())
                    .withServerName(emailServerSettings.getServerName())
                    .withFromAddress(emailServerSettings.getFromAddress())
                    .build();
    }

    public EmailClientDetailsMessage addEmailSettings(EmailClientDetailsMessage emailClientDetailsMessage) {
          EmailServer server = EmailServerBuilder.emptyEmailServer()
                  .withImapServerAddress(emailClientDetailsMessage.getImapServerAddress())
                  .withServerName(emailClientDetailsMessage.getServerName())
                  .withSmtpServerAddress(emailClientDetailsMessage.getSmtpServerAddress())
                  .withImapPort(emailClientDetailsMessage.getImapPort())
                  .withSmtpSslPort(emailClientDetailsMessage.getSmtpSslPort())
                  .withSmtpTlsPort(emailClientDetailsMessage.getSmtpTlsPort())
                  .withPassword(emailClientDetailsMessage.getPassword())
                  .withUserName(emailClientDetailsMessage.getUserName())
                  .withFromAddress(emailClientDetailsMessage.getFromAddress())
                  .build();

        emailSettingsRepository.save(server);
        this.establishSnmpMailSender();
        return emailClientDetailsMessage;
    }

    public void deleteService() {
        final EmailServer emailServerSettings;
        try {
            emailServerSettings = getEmailServerSettings();
            emailSettingsRepository.delete(emailServerSettings.getId());
        } catch (ResourceDoesNotExistException e) {
            //NOP
        }
    }

    public EmailClientDetailsMessage updateServiceDetails(EmailClientDetailsMessage emailDetails) throws ResourceDoesNotExistException {
        final EmailServer emailServerSettings = getEmailServerSettings();
        EmailServer newRecord = EmailServerBuilder.emptyEmailServer()
                .withImapPort(emailDetails.getImapPort())
                .withImapServerAddress(emailDetails.getImapServerAddress())
                .withPassword(emailDetails.getPassword())
                .withServerName(emailDetails.getServerName())
                .withSmtpServerAddress(emailDetails.getSmtpServerAddress())
                .withSmtpSslPort(emailDetails.getSmtpSslPort())
                .withSmtpTlsPort(emailDetails.getSmtpTlsPort())
                .withUserName(emailDetails.getUserName())
                .withServerName(emailDetails.getServerName())
                .withId(emailServerSettings.getId())
                .withFromAddress(emailDetails.getFromAddress())
                .build();

        emailSettingsRepository.save(newRecord);
        this.establishSnmpMailSender();
        return emailDetails;
    }

    private EmailServer getEmailServerSettings() throws ResourceDoesNotExistException {
        if (emailSettingsRepository.count() < 1) {
            throw new ResourceDoesNotExistException();
        }
        final Iterable<EmailServer> allServers = emailSettingsRepository.findAll();
        return allServers.iterator().next();
    }

    public EmailGroupMessage addGroup(EmailGroupMessage emailGroup) {
        EmailGroupBuilder emailGroupBuilder = EmailGroupBuilder.emptyEmailGroup()
                .withName(emailGroup.getGroupName());

        for (String emailAddress : emailGroup.getEmailList()) {
            emailGroupBuilder.withAddress(emailAddress);
        }
            emailGroupRepository.save(emailGroupBuilder.create());

        return emailGroup;
    }

    @Counted(value= "service.email.sent")
    public void sendMessage(String emailGroup, EmailMessage message) throws ResourceDoesNotExistException {
        final EmailClientDetailsMessage emailSettings = getEmailSettings();
        EmailGroup toEmails = emailGroupRepository.getByName(emailGroup);
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            Address address[] = {new InternetAddress(emailSettings.getFromAddress())};
            mimeMessage.addFrom(address);
            for (String emailAddress : toEmails.getEmailAddress()) {
                mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(emailAddress));
            }
            mimeMessage.setSubject(message.getSubject());
            mimeMessage.setText(message.getBody());
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    public void establishSnmpMailSender() {
        try {
            final EmailServer emailServerSettings = getEmailServerSettings();
            JavaMailSenderImpl createdMailSender = new JavaMailSenderImpl();
            Properties mailProperties = new Properties();
            mailProperties.put("mail.smtp.auth", false);
            mailProperties.put("mail.smtp.starttls.enable", false);
            createdMailSender.setJavaMailProperties(mailProperties);
            createdMailSender.setHost(emailServerSettings.getImapServerAddress());
            createdMailSender.setPort(Integer.valueOf(emailServerSettings.getSmtpTlsPort()));
            createdMailSender.setProtocol("smtp");
            createdMailSender.setUsername(emailServerSettings.getUserName());
            createdMailSender.setPassword(emailServerSettings.getPassword());
            mailSender = createdMailSender;
        } catch (ResourceDoesNotExistException e) {
            mailSender = new NOPMailSender();
        }
    }

    public EmailGroupMessage getGroups() {
        return new EmailGroupMessageBuilder().withGroupName("adminGroup").withEmailAddress("email@fred.com").withEmailAddress("email2@harry.com").build();
    }

    public EmailGroupMessage getEmailGroup(String groupName) {
        return null;
    }
}
