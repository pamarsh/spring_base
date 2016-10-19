package org.swamps.houseController.dto;


public class EmailMessage {

    private String subject;
    private String body;

    public EmailMessage() {}

    public EmailMessage(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
