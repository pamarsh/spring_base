package org.swamps.houseController.dto;

import java.util.ArrayList;
import java.util.List;

public class UserAccountDtoBuilder {
    private String userId;
    private String emailAddress;
    private String password;
    private List<String> roles;
    private String firstName;
    private String lastName;
    private int id;

    public static UserAccountDtoBuilder emptyUser() {
        return new UserAccountDtoBuilder();
    }

    private UserAccountDtoBuilder() {
        roles = new ArrayList<>();
    }

    public UserAccountDtoBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserAccountDtoBuilder withEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public UserAccountDtoBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserAccountDtoBuilder withRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public UserAccountDtoBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserAccountDtoBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserAccountDto createUserAccountDto() {
        return new UserAccountDto(id,userId, emailAddress, password, roles, firstName, lastName);
    }

    public UserAccountDtoBuilder withRole(String roleAuthority) {
        roles.add(roleAuthority);
        return this;
    }


    public UserAccountDtoBuilder withId(int id) {
        this.id = id;
        return this;
    }
}