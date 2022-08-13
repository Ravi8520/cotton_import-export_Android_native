package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostDetailsModel implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("company_id")
    @Expose
    private int company_id;

    @SerializedName("seller_buyer_id")
    @Expose
    private String seller_buyer_id;

    @SerializedName("seller_buyer_name")
    @Expose
    private String seller_buyer_name;

    @SerializedName("user_type")
    @Expose
    private String user_type;

    @SerializedName("product_id")
    @Expose
    private String product_id;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("company_name")
    @Expose
    private String company_name;

    @SerializedName("no_of_bales")
    @Expose
    private String no_of_bales;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("attribute_array")
    @Expose
    private List<Attribute> attribute_array = new ArrayList();

    @SerializedName("sent_to")
    @Expose
    private List<SendTo> sent_to = new ArrayList();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getSeller_buyer_id() {
        return seller_buyer_id;
    }

    public void setSeller_buyer_id(String seller_buyer_id) {
        this.seller_buyer_id = seller_buyer_id;
    }
    public String getUpdated_at() {
        return updated_at;
    }

    public List<SendTo> getSent_to() {
        return sent_to;
    }

    public void setSent_to(List<SendTo> sent_to) {
        this.sent_to = sent_to;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
    public String getSeller_buyer_name() {
        return seller_buyer_name;
    }

    public void setSeller_buyer_name(String seller_buyer_name) {
        this.seller_buyer_name = seller_buyer_name;
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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public List<Attribute> getAttribute_array() {
        return attribute_array;
    }

    public void setAttribute_array(List<Attribute> attribute_array) {
        this.attribute_array = attribute_array;
    }

    public class Attribute implements Serializable {

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


        public String getAttribute() {
            return attribute;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
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

    public class SendTo implements Serializable {
        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("city")
        @Expose
        private String city;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
