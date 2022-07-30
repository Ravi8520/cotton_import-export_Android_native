package com.ecotton.impex.models;

import java.io.Serializable;

public class CountryModel implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
