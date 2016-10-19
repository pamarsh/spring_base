package org.swamps.houseController.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class House {

    @Id
    private Long id ;

    @OneToOne
    private Address address;

    @OneToMany
    private List<UserAccount> userAccount ;

}
