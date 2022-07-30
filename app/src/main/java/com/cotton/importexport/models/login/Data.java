package com.cotton.importexport.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("is_invited")
    @Expose
    private int is_invited;

    @SerializedName("is_user_plan")
    @Expose
    private int is_user_plan;

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("api_token")
    @Expose
    private String apiToken;

    @SerializedName("company_name")
    @Expose
    private String company_name;

    @SerializedName("company_id")
    @Expose
    private String company_id;

    @SerializedName("user_type")
    @Expose
    private String user_type;

    @SerializedName("image")
    @Expose
    private String image;

    public int getIs_invited() {
        return is_invited;
    }

    public void setIs_invited(int is_invited) {
        this.is_invited = is_invited;
    }

    public String getImage() {
        return image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getCompany_id() {
        return company_id;
    }

    public int getIs_user_plan() {
        return is_user_plan;
    }

    public void setIs_user_plan(int is_user_plan) {
        this.is_user_plan = is_user_plan;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
