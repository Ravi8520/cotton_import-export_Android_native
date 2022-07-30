package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dashboard implements Serializable {
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("bales")
    @Expose
    private int bales;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("color_code")
    @Expose
    private int color_code;
    @SerializedName("data")
    @Expose
    private List<StateModel> stateModelList = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBales() {
        return bales;
    }

    public void setBales(int bales) {
        this.bales = bales;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor_code() {
        return color_code;
    }

    public void setColor_code(int color_code) {
        this.color_code = color_code;
    }

    public List<StateModel> getStateModelList() {
        return stateModelList;
    }

    public void setStateModelList(List<StateModel> stateModelList) {
        this.stateModelList = stateModelList;
    }

    public class StateModel implements Serializable {
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("bales")
        @Expose
        private int bales;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("color_code")
        @Expose
        private String color_code;
        @SerializedName("data")
        @Expose
        private List<CityModel> cityModelList = new ArrayList<>();

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getBales() {
            return bales;
        }

        public void setBales(int bales) {
            this.bales = bales;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor_code() {
            return color_code;
        }

        public void setColor_code(String color_code) {
            this.color_code = color_code;
        }

        public List<CityModel> getCityModelList() {
            return cityModelList;
        }

        public void setCityModelList(List<CityModel> cityModelList) {
            this.cityModelList = cityModelList;
        }
    }

    public class CityModel implements Serializable {
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("bales")
        @Expose
        private int bales;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("color_code")
        @Expose
        private String color_code;
        @SerializedName("data")
        @Expose
        private List<CompanyModel> companyModelList = new ArrayList<>();

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getBales() {
            return bales;
        }

        public void setBales(int bales) {
            this.bales = bales;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor_code() {
            return color_code;
        }

        public void setColor_code(String color_code) {
            this.color_code = color_code;
        }

        public List<CompanyModel> getCompanyModelList() {
            return companyModelList;
        }

        public void setCompanyModelList(List<CompanyModel> companyModelList) {
            this.companyModelList = companyModelList;
        }
    }

    public class CompanyModel implements Serializable {
        @SerializedName("post_id")
        @Expose
        private int post_id;
        @SerializedName("company_id")
        @Expose
        private int company_id;
        @SerializedName("company_name")
        @Expose
        private String company_name;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("no_of_bales")
        @Expose
        private String no_of_bales;
        @SerializedName("remaining_bales")
        @Expose
        private String remaining_bales;
        @SerializedName("price")
        @Expose
        private String price;

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

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    }
}


