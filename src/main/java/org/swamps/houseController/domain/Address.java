package org.swamps.houseController.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Address  {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = " street")
    private String street ;

    @Column(name = "city")
    private String city ;

    @Column( name = "country")
    private String country ;

    @Column(name = "postcode")
    private String postcode ;

    @Column(name = "state")
    private String state ;


    public Address() {}

    public Address(String street, String city, String country, String postcode, String state) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.postcode = postcode;
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
