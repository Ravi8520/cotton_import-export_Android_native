package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductAttributeModel implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("product_id")
    @Expose
    private int product_id;

    @SerializedName("is_double")
    @Expose
    private int is_double;


    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("value")
    @Expose
    private List<Value> stateModelList = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getLabel() {
        return label;
    }

    public int getIs_double() {
        return is_double;
    }

    public void setIs_double(int is_double) {
        this.is_double = is_double;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Value> getStateModelList() {
        return stateModelList;
    }

    public void setStateModelList(List<Value> stateModelList) {
        this.stateModelList = stateModelList;
    }

    public class Value implements Serializable {
        @SerializedName("value")
        @Expose
        private String value;

        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("is_required")
        @Expose
        private int is_required;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getIs_required() {
            return is_required;
        }

        public void setIs_required(int is_required) {
            this.is_required = is_required;
        }
    }
}
