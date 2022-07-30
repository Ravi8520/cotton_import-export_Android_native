package com.cotton.importexport.models;

import java.io.Serializable;

public class StationModel implements Serializable {
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
