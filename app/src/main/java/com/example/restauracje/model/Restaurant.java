package com.example.restauracje.model;

import java.io.Serializable;

public class Restaurant implements Serializable {

    private int id;
    private String name;
    private String description;
    private String street;
    private String city;
    private String postal_code;


    public Restaurant() {
    }


    public Restaurant(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getAddress() {
        String address = "";
        if (!street.isEmpty() && street != null)
            address += street.concat(", ");
        if (postal_code != null && !postal_code.isEmpty())
            address += postal_code.concat(" ");
        if (city != null && !city.isEmpty())
            address += city;

        return address;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postal_code='" + postal_code + '\'' +
                '}';
    }
}
