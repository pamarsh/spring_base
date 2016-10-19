package org.swamps.houseController.domain;

import javax.persistence.*;

@Entity
public class AccountAuthority {

    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String authority;

//    @ManyToOne
//    @JoinColumn(name="account_id")
//    private UserAccount userAccount;

    public AccountAuthority() {}

    public AccountAuthority(String userId, String authority) {
        this.userId = userId;
        this.authority = authority;
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthority() {
        return authority;
    }




}
