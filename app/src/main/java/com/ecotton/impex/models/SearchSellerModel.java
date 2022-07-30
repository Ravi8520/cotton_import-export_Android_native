package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchSellerModel implements Serializable {
    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("state")
    @Expose
    private List<State> stateModelList = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<State> getStateModelList() {
        return stateModelList;
    }

    public void setStateModelList(List<State> stateModelList) {
        this.stateModelList = stateModelList;
    }

    public class State implements Serializable {
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("count")
        @Expose
        private int count;

        @SerializedName("district")
        @Expose
        private List<District> district = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<District> getDistrict() {
            return district;
        }

        public void setDistrict(List<District> district) {
            this.district = district;
        }
    }

    public class District implements Serializable {
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("buyer_type")
        @Expose
        private String buyer_type;

        @SerializedName("seller_type")
        @Expose
        private String seller_type;

        @SerializedName("count")
        @Expose
        private int count;

        @SerializedName("data")
        @Expose
        private List<Data> data = new ArrayList<>();

        @SerializedName("city")
        @Expose
        private List<City> city = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBuyer_type() {
            return buyer_type;
        }

        public void setBuyer_type(String buyer_type) {
            this.buyer_type = buyer_type;
        }

        public String getSeller_type() {
            return seller_type;
        }

        public void setSeller_type(String seller_type) {
            this.seller_type = seller_type;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

        public List<City> getCity() {
            return city;
        }

        public void setCity(List<City> city) {
            this.city = city;
        }
    }

    public class City implements Serializable
    {
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("count")
        @Expose
        private int count;

        @SerializedName("data")
        @Expose
        private List<Data> data = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }
    }

    public class Data implements Serializable
    {
        @SerializedName("post_id")
        @Expose
        private int post_id;

        @SerializedName("company_id")
        @Expose
        private int company_id;

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("company_name")
        @Expose
        private String company_name;

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("seller_buyer_id")
        @Expose
        private String seller_buyer_id;

        @SerializedName("user_type")
        @Expose
        private String user_type;

        @SerializedName("product_id")
        @Expose
        private String product_id;

        @SerializedName("no_of_bales")
        @Expose
        private String no_of_bales;

        @SerializedName("remaining_bales")
        @Expose
        private String remaining_bales;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
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

        public String getNo_of_bales() {
            return no_of_bales;
        }

        public void setNo_of_bales(String no_of_bales) {
            this.no_of_bales = no_of_bales;
        }

        public String getRemaining_bales() {
            return remaining_bales;
        }

        public void setRemaining_bales(String remaining_bales) {
            this.remaining_bales = remaining_bales;
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
    }
}
