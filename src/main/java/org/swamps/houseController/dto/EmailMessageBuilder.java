package org.swamps.houseController.dto;

public class EmailMessageBuilder {
    private String subject;
    private String body;

    public static EmailMessageBuilder emptyMessage() {
        return new EmailMessageBuilder();
    }

    public EmailMessageBuilder withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailMessageBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public EmailMessage build() {
        return new EmailMessage(subject, body);
    }
}