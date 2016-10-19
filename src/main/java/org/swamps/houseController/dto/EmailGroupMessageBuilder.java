package org.swamps.houseController.dto;

import java.util.ArrayList;
import java.util.List;

public class EmailGroupMessageBuilder {
    private String groupName;
    private List<String> emailList = new ArrayList<>();


    public static EmailGroupMessageBuilder emptyGroup() {
        return new EmailGroupMessageBuilder();
    }

    public EmailGroupMessageBuilder withGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public EmailGroupMessageBuilder withEmailAddress(String email) {
        emailList.add(email);
        return this;
    }

    public EmailGroupMessage build() {
        return new EmailGroupMessage(groupName, emailList);
    }
}