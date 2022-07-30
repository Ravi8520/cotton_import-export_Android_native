package com.cotton.importexport.api;

import com.google.gson.annotations.SerializedName;

public class ResponseModel<T> {

    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public T data;

    @SerializedName("page")
    public int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
