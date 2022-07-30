package com.cotton.importexport.models;

import java.io.Serializable;
import java.util.ArrayList;

public class BuyerFilterRequest implements Serializable {

    public String company_id="";
    public String product_id="";

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public ArrayList<AttributeModel> getAttribute_array() {
        return attribute_array;
    }

    public void setAttribute_array(ArrayList<AttributeModel> attribute_array) {
        this.attribute_array = attribute_array;
    }

    public String state_id="";
    public ArrayList<AttributeModel> attribute_array=new ArrayList<>();

    public static class  AttributeModel implements Serializable{
        public String attribute="";
        public String from="";
        public String to="";

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
}
