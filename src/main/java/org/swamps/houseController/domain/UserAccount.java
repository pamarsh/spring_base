package org.swamps.houseController.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity()
@Table(name = "ACCOUNT")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "USER_ID", unique = true, nullable = false)
    private String userId;

    @Column(name = "FIRST_NAME", unique = false, nullable = false, length = 30)
    private String firstName;

    @Column(name = "LAST_NAME", unique = false, nullable = false, length = 30)
    private String lastName;

    @Column(name = "EMAIL", unique = true, nullable = true)
    private String emailAddress;

    @Column(name = "PASSWORD", unique = false, nullable = false)
    private String password ; // TODO encript this password

    @Column(name = "ROLES")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.DELETE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST})
    private List<AccountAuthority> roles;

    public UserAccount(){}

    public UserAccount(String userId, String firstName, String lastName, String emailAddress, String password, List<AccountAuthority> roles) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<AccountAuthority> getRoles() {
        return roles;
    }

    public void setRoles(List<AccountAuthority> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
