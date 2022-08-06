package com.ecotton.impex.models;

import java.io.Serializable;

public class ProtModel implements Serializable {
    private int id=0;
    private String name="";

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
