package com.cotton.importexport.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyPostActiveModel implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("seller_buyer_id")
    @Expose
    private String  seller_buyer_id;

    @SerializedName("user_type")
    @Expose
    private String  user_type;

    @SerializedName("product_id")
    @Expose
    private String  product_id;

    @SerializedName("product_name")
    @Expose
    private String  product_name;

    @SerializedName("no_of_bales")
    @Expose
    private String  no_of_bales;

    @SerializedName("price")
    @Expose
    private String  price;

    @SerializedName("address")
    @Expose
    private String  address;

    @SerializedName("date")
    @Expose
    private String  date;

    @SerializedName("type")
    @Expose
    private String  type;

    @SerializedName("active")
    @Expose
    private String  active;

    @SerializedName("remaining_bales")
    @Expose
    private String  remaining_bales;

    @SerializedName("attribute_array")
    @Expose
    private List<Attribute> attribute_array=new ArrayList<>();

    @SerializedName("buyer_array")
    @Expose
    private List<buyer_array> buyer_array=new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeller_buyer_id() {
        return seller_buyer_id;
    }

    public void setSeller_buyer_id(String seller_buyer_id) {
        this.seller_buyer_id = seller_buyer_id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRemaining_bales() {
        return remaining_bales;
    }

    public void setRemaining_bales(String remaining_bales) {
        this.remaining_bales = remaining_bales;
    }

    public List<Attribute> getAttribute_array() {
        return attribute_array;
    }

    public void setAttribute_array(List<Attribute> attribute_array) {
        this.attribute_array = attribute_array;
    }

    public List<MyPostActiveModel.buyer_array> getBuyer_array() {
        return buyer_array;
    }

    public void setBuyer_array(List<MyPostActiveModel.buyer_array> buyer_array) {
        this.buyer_array = buyer_array;
    }

    public class Attribute implements Serializable{
        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("post_id")
        @Expose
        private String post_id;

        @SerializedName("attribute")
        @Expose
        private String attribute;

        @SerializedName("notification_id")
        @Expose
        private String notification_id;

        @SerializedName("attribute_value")
        @Expose
        private String attribute_value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getAttribute_value() {
            return attribute_value;
        }

        public void setAttribute_value(String attribute_value) {
            this.attribute_value = attribute_value;
        }

        public String getNotification_id() {
            return notification_id;
        }

        public void setNotification_id(String notification_id) {
            this.notification_id = notification_id;
        }
    }

    private class buyer_array implements Serializable
    {

    }

}
