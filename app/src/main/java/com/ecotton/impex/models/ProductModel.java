package com.ecotton.impex.models;

import java.io.Serializable;

public class ProductModel implements Serializable {

    public int id = 0;
    public String name = "";

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
