package org.swamps.houseController.dto;

import java.util.ArrayList;
import java.util.List;

public class EmailGroupMessage {

    private String groupName;

    private List<String> users = new ArrayList<>();

    public EmailGroupMessage(){};

    public String getGroupName() {
        return groupName;
    }

    public List<String> getEmailList() {
        return users;
    }

    public EmailGroupMessage(String groupName, List<String> emailList) {
        this.groupName = groupName;
        this.users = emailList;
    }

    public void addEmail(String email) {
        users.add(email) ;
    }
}
