package com.cotton.importexport.models;

import java.io.Serializable;

public class AttributeRequestModel implements Serializable {
    String attribute;
    String attribute_value;

    String from;
    String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
