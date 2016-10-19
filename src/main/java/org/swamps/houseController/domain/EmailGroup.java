package org.swamps.houseController.domain;



import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "EmailGroup", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "NAME") })
public class EmailGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "NAME", unique = true, nullable = false, length = 30)
    private String name;


    @OneToMany
    //@JoinColumn(name="id")
    private List<UserAccount> users;


    public EmailGroup() {}

    public EmailGroup(String name, List<UserAccount> users) {
        this.name = name;
        this.users = users;
    }

    public String getName() {
        return name;
    }


    public List<UserAccount> getEmailAddress() {
        return users;
    }

}
