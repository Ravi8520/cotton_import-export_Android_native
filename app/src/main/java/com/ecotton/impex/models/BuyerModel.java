package com.ecotton.impex.models;

public class BuyerModel {
    String companyName,ownername, address, contact, port, country;

    public BuyerModel(String companyName, String ownername, String address, String contact, String port, String country) {
        this.companyName = companyName;
        this.address = address;
        this.contact = contact;
        this.port = port;
        this.country = country;
        this.ownername = ownername;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
