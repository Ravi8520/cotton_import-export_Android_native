package com.ecotton.impex.models.companylist;

import java.io.Serializable;

public class CompanyDirectory implements Serializable {

    public int company_id = 0, is_my_favourite = 0, favourite_id = 0;
    public String company_name = "", address = "", mobile_number = "", country = "", state = "", district = "", email = "";

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getIs_my_favourite() {
        return is_my_favourite;
    }

    public void setIs_my_favourite(int is_my_favourite) {
        this.is_my_favourite = is_my_favourite;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getFavourite_id() {
        return favourite_id;
    }

    public void setFavourite_id(int favourite_id) {
        this.favourite_id = favourite_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
