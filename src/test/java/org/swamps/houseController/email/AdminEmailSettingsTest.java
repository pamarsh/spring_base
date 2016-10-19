package org.swamps.houseController.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.swamps.houseController.HouseControllerApplication;
import org.swamps.houseController.dto.*;
import org.swamps.houseController.restClient.RestClientBuilder;
import utils.hamcrest.ReflectionMatcher;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


/**
 * smtp    3025
 * smtps    3465
 * pop3    3110
 * pop3s    3995
 * imap    3143
 * imaps    3993
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HouseControllerApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=8081")
public class AdminEmailSettingsTest {

    private static final String USER_PASSWORD = "abcdef123";
    private static final String USER_NAME = "hascode";
    private static final String EMAIL_USER_ADDRESS = "hascode@localhost";
    private static final String EMAIL_TO = "someone@localhost.com";
    private static final String EMAIL_SUBJECT = "Test E-Mail";
    private static final String EMAIL_TEXT = "This is a test e-mail.";
    private static final String LOCALHOST = "127.0.0.1";
    private static final Integer SMTP_PORT =  3025;
    private static final String SEND_EMAIL_URI = "http://localhost:8081/settings/email/test";

    private static final String EMAIL_GROUP = "DummyGroup";
    private static final String EMAIL_GROUP_SETTING_URI = "http://localhost:8081/settings/email/group";
    private static final String EMAIL_SERVICE_URI = "http://localhost:8081/settings/email/service";
    private GreenMail mailServer;

    @Value("${server.port}")
    private int port;

    private RestTemplate restTemplate = new RestClientBuilder().withUsername("admin").withPassword("admin").build();

    @Before
    public void setUpEmailServer() {
        mailServer = new GreenMail(ServerSetupTest.SMTP);
        mailServer.start();
    }

    @After
    public void tearDownEmailServer() {
        mailServer.stop();
    }

    @Before
    public void cleanupEmailSettings() { restTemplate.delete(EMAIL_SERVICE_URI ); }

    @Test
    public void ensureSuccessfulInjectionOfEmailClientService() {
        final EmailClientDetailsMessage serverDetailsMessage = createFirstEmailService();
        EmailClientDetailsMessage retrievedDto = restTemplate.getForObject("http://localhost:8081/settings/email/service", EmailClientDetailsMessage.class);
        assertThat(retrievedDto, is(new ReflectionMatcher(serverDetailsMessage)));
    }

    @Test
    public void ensureSuccessfulDeletionOfEmailServerCredentials() {

        final EmailClientDetailsMessage serverDetailsMessage = createFirstEmailService();
        restTemplate.delete(EMAIL_SERVICE_URI);
        try {
            EmailClientDetailsMessage retrievedDto = restTemplate.getForObject("http://localhost:8081/settings/email/service", EmailClientDetailsMessage.class);
            fail(" should have been a 404 Not Found Exception");
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getMessage(), is(containsString("404 Not Found")));
        }
    }

    @Test
    public void ensureSuccessfulUpdateOfEmailClientDetails() {
        final EmailClientDetailsMessage serverDetailsMessage = createFirstEmailService();
        final EmailClientDetailsMessage updatedEmailService = EmailClientDetailsMessageBuilder.emptyEmailServer()
                .withImapPort("994")
                .withImapServerAddress("imap.ggml.com")
                .withPassword("testPassword1")
                .withSmtpServerAddress("smtp.ggm.com")
                .withSmtpSslPort("425")
                .withSmtpTlsPort(547)
                .withUserName("paulwmarsh")
                .withServerName("testservice1")
                .withFromAddress("fromAddress")
                .build();
        restTemplate.put(EMAIL_SERVICE_URI, updatedEmailService);
        EmailClientDetailsMessage retrievedDto = restTemplate.getForObject("http://localhost:8081/settings/email/service", EmailClientDetailsMessage.class);
        assertThat(retrievedDto, is(notNullValue()));
        assertThat(retrievedDto, is(new ReflectionMatcher(updatedEmailService)));
    }

    @Test
    public void adminAbleToTestSendingEmailToGroupOfRecipients() throws IOException, MessagingException {
        final EmailClientDetailsMessage serverDetailsMessage = createFirstEmailService();

        mailServer.setUser(EMAIL_USER_ADDRESS, USER_NAME, USER_PASSWORD);

        String groupName = setupEmailGroup();

        EmailMessage message = EmailMessageBuilder.emptyMessage()
                .withSubject(EMAIL_SUBJECT)
                .withBody(EMAIL_TEXT+ "\r\n")
                .build();

        restTemplate.postForObject(SEND_EMAIL_URI + "/" + EMAIL_GROUP, message, EmailMessage.class);

        EmailMessage recievedMessage = getEmailMessage();
        assertThat(recievedMessage, is(new ReflectionMatcher(message)));
    }

    private void createEmailServer(EmailClientDetailsMessage serverDetailsDto) {
        ResponseEntity<EmailClientDetailsMessage> response = restTemplate.postForEntity("http://localhost:8081/settings/email/service", serverDetailsDto, EmailClientDetailsMessage.class, Collections.EMPTY_MAP);
    }

    private EmailClientDetailsMessage createFirstEmailService() {
        final EmailClientDetailsMessage serverDetailsMessage = EmailClientDetailsMessageBuilder.emptyEmailServer()
                .withImapPort("993")
                .withImapServerAddress(LOCALHOST)
                .withPassword(USER_PASSWORD)
                .withSmtpServerAddress(LOCALHOST)
                .withSmtpSslPort("465")
                .withSmtpTlsPort(SMTP_PORT)
                .withUserName(USER_NAME)
                .withServerName("testServer")
                .withFromAddress(EMAIL_USER_ADDRESS)
                .build();
        createEmailServer(serverDetailsMessage);
        return serverDetailsMessage;
    }

    private String setupEmailGroup() {
        EmailGroupMessage emailGroup = EmailGroupMessageBuilder.emptyGroup()
                .withGroupName(EMAIL_GROUP)
                .withEmailAddress(EMAIL_TO)
                .build();

        return restTemplate.postForObject(EMAIL_GROUP_SETTING_URI, emailGroup, EmailGroupMessage.class).getGroupName();
    }

    public EmailMessage getEmailMessage() throws MessagingException, IOException {
        // fetch messages from server
        MimeMessage[] messages = mailServer.getReceivedMessages();
        assertNotNull(messages);
        assertThat(messages.length, is(1));
        MimeMessage m = messages[0];
        assertThat(m.getSubject(), is(EMAIL_SUBJECT));
        assertThat(String.valueOf(m.getContent()), containsString(EMAIL_TEXT));
        assertThat(m.getFrom()[0].toString(), is(EMAIL_USER_ADDRESS));

        return EmailMessageBuilder.emptyMessage().withSubject(m.getSubject())
                .withBody(m.getContent().toString())
                .build();

    }
}