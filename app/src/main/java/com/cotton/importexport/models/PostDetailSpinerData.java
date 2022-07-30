package com.cotton.importexport.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PostDetailSpinerData {
    public ArrayList<SpinerModel> getTransmit_condition() {
        return transmit_condition;
    }

    public void setTransmit_condition(ArrayList<SpinerModel> transmit_condition) {
        this.transmit_condition = transmit_condition;
    }

    public ArrayList<SpinerModel> getLab_list() {
        return lab_list;
    }

    public void setLab_list(ArrayList<SpinerModel> lab_list) {
        this.lab_list = lab_list;
    }

    public ArrayList<SpinerModel> getHeader() {
        return header;
    }

    public void setHeader(ArrayList<SpinerModel> header) {
        this.header = header;
    }

    public ArrayList<SpinerModel> getPayment_condition() {
        return payment_condition;
    }

    public void setPayment_condition(ArrayList<SpinerModel> payment_condition) {
        this.payment_condition = payment_condition;
    }

    public ArrayList<SpinerModel> transmit_condition = new ArrayList<>();
    public ArrayList<SpinerModel> lab_list = new ArrayList<>();
    public ArrayList<SpinerModel> header = new ArrayList<>();
    public ArrayList<SpinerModel> payment_condition = new ArrayList<>();

    public class SpinerModel implements Serializable {
        public String name = "";
        public int id = 0, is_dispatch = 0;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIs_dispatch() {
            return is_dispatch;
        }

        public void setIs_dispatch(int is_dispatch) {
            this.is_dispatch = is_dispatch;
        }
    }
}
