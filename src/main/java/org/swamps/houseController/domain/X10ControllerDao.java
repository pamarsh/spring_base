package org.swamps.houseController.domain;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class X10ControllerDao {

    @Id
    private String name;

    private String communicationsAdapter ;

    public X10ControllerDao() {}

    public X10ControllerDao(String name, String communicationsAdapter) {
        this.name = name;
        this.communicationsAdapter = communicationsAdapter;
    }

    public String getName() {
        return name;
    }

    public String getCommunicationsAdapter() {
        return communicationsAdapter;
    }
}
