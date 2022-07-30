package com.cotton.importexport.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NotificatioListModel implements Serializable {
    @SerializedName("notification_id")
    @Expose
    private int notification_id;

    @SerializedName("posted_company_id")
    @Expose
    private int posted_company_id;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("seller_buyer_id")
    @Expose
    private String seller_buyer_id;

    @SerializedName("send_by")
    @Expose
    private String send_by;

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

    @SerializedName("d_e")
    @Expose
    private String d_e;

    @SerializedName("buy_for")
    @Expose
    private String buy_for;

    @SerializedName("spinning_meal_name")
    @Expose
    private String spinning_meal_name;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("district")
    @Expose
    private String district;

    @SerializedName("attribute_array")
    @Expose
    private List<AttributeList> attribute_array;

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getPosted_company_id() {
        return posted_company_id;
    }

    public void setPosted_company_id(int posted_company_id) {
        this.posted_company_id = posted_company_id;
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

    public String getSend_by() {
        return send_by;
    }

    public void setSend_by(String send_by) {
        this.send_by = send_by;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<AttributeList> getAttribute_array() {
        return attribute_array;
    }

    public void setAttribute_array(List<AttributeList> attribute_array) {
        this.attribute_array = attribute_array;
    }

    public class AttributeList implements Serializable
    {
        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("notification_id")
        @Expose
        private String notification_id;

        @SerializedName("attribute")
        @Expose
        private String attribute;

        @SerializedName("attribute_value")
        @Expose
        private String attribute_value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNotification_id() {
            return notification_id;
        }

        public void setNotification_id(String notification_id) {
            this.notification_id = notification_id;
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
    }
}
