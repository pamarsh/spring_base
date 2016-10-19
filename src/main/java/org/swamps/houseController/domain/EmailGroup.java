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

    @ElementCollection
    private List<String> addresses;


    public EmailGroup() {}

    public EmailGroup(String name, List<String> addresses) {
        this.name = name;
        this.addresses = addresses;
    }

    public String getName() {
        return name;
    }


    public List<String> getEmailAddress() {
        return addresses;
    }

}
