package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestModel implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("company_id")
    @Expose
    private int company_id;

    @SerializedName("requested_id")
    @Expose
    private int requested_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("mobile_number")
    @Expose
    private String mobile_number;

    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("company_name")
    @Expose
    private String company_name;

    @SerializedName("requested_name")
    @Expose
    private String requested_name;

    @SerializedName("requested_company_broker")
    @Expose
    private String requested_company_broker;

    @SerializedName("request_at")
    @Expose
    private String request_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getRequested_id() {
        return requested_id;
    }

    public void setRequested_id(int requested_id) {
        this.requested_id = requested_id;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getRequested_name() {
        return requested_name;
    }

    public void setRequested_name(String requested_name) {
        this.requested_name = requested_name;
    }

    public String getRequested_company_broker() {
        return requested_company_broker;
    }

    public void setRequested_company_broker(String requested_company_broker) {
        this.requested_company_broker = requested_company_broker;
    }

    public String getRequest_at() {
        return request_at;
    }

    public void setRequest_at(String request_at) {
        this.request_at = request_at;
    }
}
