package com.ecotton.impex.models;

import java.io.Serializable;

public class MCXModel implements Serializable {

    public String name = "";
    public int current_price, open_price, close_price, high_price, low_price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(int current_price) {
        this.current_price = current_price;
    }

    public int getOpen_price() {
        return open_price;
    }

    public void setOpen_price(int open_price) {
        this.open_price = open_price;
    }

    public int getClose_price() {
        return close_price;
    }

    public void setClose_price(int close_price) {
        this.close_price = close_price;
    }

    public int getHigh_price() {
        return high_price;
    }

    public void setHigh_price(int high_price) {
        this.high_price = high_price;
    }

    public int getLow_price() {
        return low_price;
    }

    public void setLow_price(int low_price) {
        this.low_price = low_price;
    }
}
