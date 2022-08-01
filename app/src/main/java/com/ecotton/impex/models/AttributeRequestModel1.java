package com.ecotton.impex.models;

import java.io.Serializable;

public class AttributeRequestModel1 implements Serializable {
    String attribute;
    String attribute_value;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute_value() {
        return attribute_value;
    }

    public void setAttribute_value(String attribute_value) {
        this.attribute_value = attribute_value;
    }
}