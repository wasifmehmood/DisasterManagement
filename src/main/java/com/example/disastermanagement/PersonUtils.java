package com.example.disastermanagement;


public class PersonUtils {

    private String disasterId;
    private String date;
    private String address;
    private String city;
    private String engagement;
    private String disEngagement;

    public PersonUtils(String disasterId, String date, String address, String city, String engagement, String disEngagement) {
        this.disasterId = disasterId;
        this.date = date;
        this.address = address;
        this.city = city;
        this.engagement = engagement;
        this.disEngagement = disEngagement;
    }

    public String getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(String disasterId) {
        this.disasterId = disasterId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getEngagement() {
        return engagement;
    }

    public void setEngagement(String engagement) {
        this.engagement = engagement;
    }

    public String getDisEngagement() {
        return disEngagement;
    }

    public void setDisEngagement(String disEngagement) {
        this.disEngagement = disEngagement;
    }
}