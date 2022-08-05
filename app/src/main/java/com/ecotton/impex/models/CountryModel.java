package com.ecotton.impex.models;

import java.io.Serializable;

public class CountryModel implements Serializable {
    private String name = "";
    private int id = 0;

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
