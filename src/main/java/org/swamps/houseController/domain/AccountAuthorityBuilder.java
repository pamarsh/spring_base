package org.swamps.houseController.domain;

public class AccountAuthorityBuilder {
    private String userId;
    private String authority;

    public static AccountAuthorityBuilder newAuthority() {
        return new AccountAuthorityBuilder();
    }

    public AccountAuthorityBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public AccountAuthorityBuilder withAuthority(String authority) {
        this.authority = authority;
        return this;
    }

    public AccountAuthority create() {
        return new AccountAuthority(userId, authority);
    }
}