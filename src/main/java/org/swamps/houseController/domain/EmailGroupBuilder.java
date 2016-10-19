package org.swamps.houseController.domain;

import java.util.ArrayList;
import java.util.List;

public class EmailGroupBuilder {
    private String name;
    private List<String> emailAddresses = new ArrayList<>();

    public EmailGroupBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EmailGroupBuilder withAddresses(List<String> emailAddress) {
        this.emailAddresses = emailAddress;
        return this;
    }

    public EmailGroupBuilder withAddress(String emailAddress) {
        emailAddresses.add(emailAddress);
        return this;
    }

    public EmailGroup create() {
        return new EmailGroup(name, emailAddresses);
    }

    public static EmailGroupBuilder emptyEmailGroup() {
        return new EmailGroupBuilder();
    }


}