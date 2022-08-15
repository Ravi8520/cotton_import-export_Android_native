package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContractProductModel implements Serializable {

    @SerializedName("product_id")
    @Expose
    private int product_id = 0;

    @SerializedName("broker_id")
    @Expose
    private int broker_id = 0;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("broker_name")
    @Expose
    private String broker_name;

    @SerializedName("report_url")
    @Expose
    private String report_url;

    public String getReport_url() {
        return report_url;
    }

    public void setReport_url(String report_url) {
        this.report_url = report_url;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getBroker_id() {
        return broker_id;
    }

    public void setBroker_id(int broker_id) {
        this.broker_id = broker_id;
    }

    public String getBroker_name() {
        return broker_name;
    }

    public void setBroker_name(String broker_name) {
        this.broker_name = broker_name;
    }
}
