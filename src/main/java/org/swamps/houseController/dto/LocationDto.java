package org.swamps.houseController.dto;

public class LocationDto {

    private int houseNumber ;
    private String street;
    private String city;
    private String state;
    private String country;

    public LocationDto() {}

    public LocationDto(int houseNumber, String street, String city, String state, String country) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

}
