package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SellertypeBuyerTypeModel implements Serializable {
    @SerializedName("sellerBuyer_type")
    @Expose
    private List<SellerBuyerType> sellerBuyer_type = new ArrayList<>();

    public List<SellerBuyerType> getStateModelList() {
        return sellerBuyer_type;
    }

    public void setStateModelList(List<SellerBuyerType> stateModelList) {
        this.sellerBuyer_type = stateModelList;
    }

    public static class SellerBuyerType implements Serializable {
        @SerializedName("id")
        @Expose
        public int id = 0;
        @SerializedName("name")
        @Expose
        public String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
