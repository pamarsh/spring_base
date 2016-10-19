package org.swamps.houseController.domain;

import java.util.List;

public class UserAccountBuilder {
    private String userId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private List<AccountAuthority> roles;




    public static UserAccountBuilder emptyAccount() {
        return new UserAccountBuilder();
    }


    public UserAccountBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserAccountBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserAccountBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserAccountBuilder withEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public UserAccountBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserAccountBuilder withRoles(List<AccountAuthority> roles) {
        this.roles = roles;
        return this;
    }


    public UserAccount create() {
        return new UserAccount(userId, firstName, lastName, emailAddress, password, roles);
    }
}