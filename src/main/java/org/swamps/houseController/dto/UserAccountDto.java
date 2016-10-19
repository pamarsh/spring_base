package org.swamps.houseController.dto;

import java.util.ArrayList;
import java.util.List;

public class UserAccountDto {

    private int id;
    private String userId;

    private String emailAddress ;
    private String password;
    List<String> roles = new ArrayList<>();
    private String firstName;
    private String lastName;

    public UserAccountDto() {}

    public UserAccountDto(int id, String userId, String emailAddress, String password, List<String> roles, String firstName, String lastName) {
        this.id = id;
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.password = password;
        this.roles.clear();
        if ( roles != null ) {
            this.roles.addAll(roles);
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    @Override
    public String toString() {
        return "UserAccountDto{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
