package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompleteTabModel implements Serializable {
    @SerializedName("post_id")
    @Expose
    private int post_id;

    @SerializedName("company_id")
    @Expose
    private int company_id;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("seller_buyer_id")
    @Expose
    private String seller_buyer_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("done_by")
    @Expose
    private String done_by;

    @SerializedName("user_type")
    @Expose
    private String user_type;

    @SerializedName("product_id")
    @Expose
    private String product_id;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("no_of_bales")
    @Expose
    private String no_of_bales;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("d_e")
    @Expose
    private String d_e;

    @SerializedName("buy_for")
    @Expose
    private String buy_for;

    @SerializedName("spinning_meal_name")
    @Expose
    private String spinning_meal_name;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("negotiation_array")
    @Expose
    private List<Negotiation> negotiation_array = new ArrayList();


    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeller_buyer_id() {
        return seller_buyer_id;
    }

    public void setSeller_buyer_id(String seller_buyer_id) {
        this.seller_buyer_id = seller_buyer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDone_by() {
        return done_by;
    }

    public void setDone_by(String done_by) {
        this.done_by = done_by;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getNo_of_bales() {
        return no_of_bales;
    }

    public void setNo_of_bales(String no_of_bales) {
        this.no_of_bales = no_of_bales;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getD_e() {
        return d_e;
    }

    public void setD_e(String d_e) {
        this.d_e = d_e;
    }

    public String getBuy_for() {
        return buy_for;
    }

    public void setBuy_for(String buy_for) {
        this.buy_for = buy_for;
    }

    public String getSpinning_meal_name() {
        return spinning_meal_name;
    }

    public void setSpinning_meal_name(String spinning_meal_name) {
        this.spinning_meal_name = spinning_meal_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Negotiation> getNegotiation_array() {
        return negotiation_array;
    }

    public void setNegotiation_array(List<Negotiation> negotiation_array) {
        this.negotiation_array = negotiation_array;
    }

    public class Negotiation implements Serializable {

    }

}
