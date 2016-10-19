package org.swamps.houseController.web.dto;

import java.util.List;

public class EmailGroupMessage {

    private String groupName ;
    private List<String> emaillist ;


    public EmailGroupMessage() {}

    public EmailGroupMessage(String groupName, List<String> emailist) {
        this.groupName = groupName;
        this.emaillist = emailist;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<String> getEmaillist() {
        return emaillist;
    }
}
