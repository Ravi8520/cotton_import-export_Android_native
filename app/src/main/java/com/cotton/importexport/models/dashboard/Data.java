package com.cotton.importexport.models.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Data implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;
}
