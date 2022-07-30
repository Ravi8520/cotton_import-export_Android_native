package com.cotton.importexport.models.companylist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CompanyListModel implements Serializable {

    @SerializedName("company_id")
    @Expose
    private int company_id;

    @SerializedName("company_name")
    @Expose
    private String company_name;

    @SerializedName("company_type")
    @Expose
    private String company_type;

    @SerializedName("is_setupped")
    @Expose
    private int is_setupped;

    public int getCompany_id() {
        return company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getCompany_type() {
        return company_type;
    }

    public int getIs_setupped() {
        return is_setupped;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public void setIs_setupped(int is_setupped) {
        this.is_setupped = is_setupped;
    }
}
