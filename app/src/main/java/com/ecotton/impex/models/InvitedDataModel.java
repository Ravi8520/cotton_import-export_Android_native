package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvitedDataModel implements Serializable {
    @SerializedName("company_id")
    @Expose
    private int company_id;

    @SerializedName("company_broker_id")
    @Expose
    private int company_broker_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("mobile_number")
    @Expose
    private String mobile_number;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("company_name")
    @Expose
    private String company_name;

    @SerializedName("company_iec")
    @Expose
    private String company_iec;

    @SerializedName("company_gst_no")
    @Expose
    private String company_gst_no;

    @SerializedName("company_type")
    @Expose
    private String company_type;

    @SerializedName("company_broker_name")
    @Expose
    private String company_broker_name;

    @SerializedName("company_broker_code")
    @Expose
    private String company_broker_code;

    public int getCompany_id() {
        return company_id;
    }

    public String getCompany_iec() {
        return company_iec;
    }

    public void setCompany_iec(String company_iec) {
        this.company_iec = company_iec;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getCompany_broker_id() {
        return company_broker_id;
    }

    public void setCompany_broker_id(int company_broker_id) {
        this.company_broker_id = company_broker_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_gst_no() {
        return company_gst_no;
    }

    public void setCompany_gst_no(String company_gst_no) {
        this.company_gst_no = company_gst_no;
    }

    public String getCompany_type() {
        return company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getCompany_broker_name() {
        return company_broker_name;
    }

    public void setCompany_broker_name(String company_broker_name) {
        this.company_broker_name = company_broker_name;
    }

    public String getCompany_broker_code() {
        return company_broker_code;
    }

    public void setCompany_broker_code(String company_broker_code) {
        this.company_broker_code = company_broker_code;
    }
}
