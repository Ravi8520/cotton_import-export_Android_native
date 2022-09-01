package com.ecotton.impex.models;

import java.io.Serializable;

public class Plan implements Serializable {
    public int id, validity, price;
    public String name = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
