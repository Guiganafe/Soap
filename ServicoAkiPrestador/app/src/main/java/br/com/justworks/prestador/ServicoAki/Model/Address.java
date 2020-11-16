package br.com.justworks.prestador.ServicoAki.Model;

import java.util.HashMap;
import java.util.Map;

public class Address {
    private Boolean active, defaul_address;
    private String addressName, addressType, city, country, neighborhood, number, state, street, userId, zipCode;
    private double latitude, longitude;

    public Address() {
    }

    public Address(Boolean active, String addressName, String addressType, String city, String country, String neighborhood, String number, String state, String street, String userId, String zipCode, double latitude, double longitude) {
        this.active = active;
        this.addressName = addressName;
        this.addressType = addressType;
        this.city = city;
        this.country = country;
        this.neighborhood = neighborhood;
        this.number = number;
        this.state = state;
        this.street = street;
        this.userId = userId;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("active", active);
        data.put("addressName", addressName);
        data.put("addressType", addressType);
        data.put("city", city);
        data.put("country", country);
        data.put("neighborhood", neighborhood);
        data.put("number", number);
        data.put("state", state);
        data.put("street", street);
        data.put("userId", userId);
        data.put("zipCode", zipCode);
        data.put("longitude", longitude);
        data.put("latitude", latitude);

        return data;
    }
}
