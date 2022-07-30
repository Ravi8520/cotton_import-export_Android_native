package com.ecotton.impex.models;

public class AttributeModel {
    String attribute,attribute_value;

    public AttributeModel(String attribute, String attribute_value) {
        this.attribute = attribute;
        this.attribute_value = attribute_value;
    }

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
