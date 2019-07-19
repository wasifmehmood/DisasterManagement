package com.example.disastermanagement;

public class VolunteerUtils {

    private String name;
    private String address;
    private String city;
    private String contact;
    private String email;
    private String disasterId;
    private String date;

    public VolunteerUtils(String name, String disasterId, String city, String contact, String email, String address, String date) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.contact = contact;
        this.email = email;
        this.disasterId = disasterId;
        this.date = date;
    }

    public VolunteerUtils(String name, String disasterId) {
        this.name = name;
        this.disasterId = disasterId;
    }

    public String getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(String disasterId) {
        this.disasterId = disasterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }
}
