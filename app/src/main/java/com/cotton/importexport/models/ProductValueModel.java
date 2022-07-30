package com.cotton.importexport.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductValueModel implements Serializable {

    @SerializedName("is_required")
    @Expose
    private float is_required = 0;

    @SerializedName("duration")
    @Expose
    private float duration = 0;
    @SerializedName("min")
    @Expose
    private String min;

    @SerializedName("max")
    @Expose
    private String max;

    public void setIs_required(float is_required) {
        this.is_required = is_required;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getMinSelected() {
        return minSelected;
    }

    public void setMinSelected(String minSelected) {
        this.minSelected = minSelected;
    }

    public String getMaxSelected() {
        return maxSelected;
    }

    public void setMaxSelected(String maxSelected) {
        this.maxSelected = maxSelected;
    }

    private String minSelected;

    private String maxSelected;
    @SerializedName("lable")
    @Expose
    private String lable;


    public float getIs_required() {
        return is_required;
    }

    public void setIs_required(int is_required) {
        this.is_required = is_required;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }
}
