package org.swamps.houseController.domain;

import java.util.List;
import java.util.Set;

public class EmailGroupBuilder {
    private String name;
    private List<UserAccount> users;

    public EmailGroupBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EmailGroupBuilder withUsers(List<UserAccount> users) {
        this.users = users;
        return this;
    }

    public EmailGroupBuilder withUser(UserAccount userAccount) {
        users.add(userAccount);
        return this;
    }

    public EmailGroup create() {
        return new EmailGroup(name, users);
    }

    public static EmailGroupBuilder emptyEmailGroup() {
        return new EmailGroupBuilder();
    }
}