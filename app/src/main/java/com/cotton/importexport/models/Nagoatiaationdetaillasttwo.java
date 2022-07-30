package com.cotton.importexport.models;

import java.io.Serializable;

public class Nagoatiaationdetaillasttwo implements Serializable {
    public int negotiation_id;
    public int seller_id;
    public String seller_name;
    public int buyer_id;
    public String buyer_name;
    public int seller_company_id;
    public String seller_company_name;
    public int buyer_company_id;
    public String buyer_company_name;
    public String negotiation_by;
    public int post_notification_id;
    public String negotiation_type;
    public int current_price;
    public int prev_price;
    public int current_no_of_bales;
    public int prev_no_of_bales;
    public String transmit_condition;
    public String payment_condition;
    public String lab;
    public String header;
    public String notes;
    public String broker_name;

    public int getNegotiation_id() {
        return negotiation_id;
    }

    public void setNegotiation_id(int negotiation_id) {
        this.negotiation_id = negotiation_id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public int getSeller_company_id() {
        return seller_company_id;
    }

    public void setSeller_company_id(int seller_company_id) {
        this.seller_company_id = seller_company_id;
    }

    public String getSeller_company_name() {
        return seller_company_name;
    }

    public void setSeller_company_name(String seller_company_name) {
        this.seller_company_name = seller_company_name;
    }

    public int getBuyer_company_id() {
        return buyer_company_id;
    }

    public void setBuyer_company_id(int buyer_company_id) {
        this.buyer_company_id = buyer_company_id;
    }

    public String getBuyer_company_name() {
        return buyer_company_name;
    }

    public void setBuyer_company_name(String buyer_company_name) {
        this.buyer_company_name = buyer_company_name;
    }

    public String getNegotiation_by() {
        return negotiation_by;
    }

    public void setNegotiation_by(String negotiation_by) {
        this.negotiation_by = negotiation_by;
    }

    public int getPost_notification_id() {
        return post_notification_id;
    }

    public void setPost_notification_id(int post_notification_id) {
        this.post_notification_id = post_notification_id;
    }

    public String getNegotiation_type() {
        return negotiation_type;
    }

    public void setNegotiation_type(String negotiation_type) {
        this.negotiation_type = negotiation_type;
    }

    public int getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(int current_price) {
        this.current_price = current_price;
    }

    public int getPrev_price() {
        return prev_price;
    }

    public void setPrev_price(int prev_price) {
        this.prev_price = prev_price;
    }

    public int getCurrent_no_of_bales() {
        return current_no_of_bales;
    }

    public void setCurrent_no_of_bales(int current_no_of_bales) {
        this.current_no_of_bales = current_no_of_bales;
    }

    public int getPrev_no_of_bales() {
        return prev_no_of_bales;
    }

    public void setPrev_no_of_bales(int prev_no_of_bales) {
        this.prev_no_of_bales = prev_no_of_bales;
    }

    public String getTransmit_condition() {
        return transmit_condition;
    }

    public void setTransmit_condition(String transmit_condition) {
        this.transmit_condition = transmit_condition;
    }

    public String getPayment_condition() {
        return payment_condition;
    }

    public void setPayment_condition(String payment_condition) {
        this.payment_condition = payment_condition;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBroker_name() {
        return broker_name;
    }

    public void setBroker_name(String broker_name) {
        this.broker_name = broker_name;
    }
}
