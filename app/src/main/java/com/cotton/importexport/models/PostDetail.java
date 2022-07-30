package com.cotton.importexport.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PostDetail implements Serializable {
    public int id = 0,company_id=0;
    public String seller_buyer_id = "", user_type = "", product_id = "", product_name = "", no_of_bales = "",
            price = "", address = "", date = "",company_name="",seller_buyer_name="";
    public ArrayList<PostDetailAttribute> attribute_array = new ArrayList<>();

    public String getSeller_buyer_name() {
        return seller_buyer_name;
    }

    public void setSeller_buyer_name(String seller_buyer_name) {
        this.seller_buyer_name = seller_buyer_name;
    }

    public int getId() {
        return id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public ArrayList<PostDetailAttribute> getAttribute_array() {
        return attribute_array;
    }

    public void setAttribute_array(ArrayList<PostDetailAttribute> attribute_array) {
        this.attribute_array = attribute_array;
    }

    public class PostDetailAttribute implements Serializable {
        public int id = 0;
        public String post_id = "", attribute = "", attribute_value = "";

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
    }
}
