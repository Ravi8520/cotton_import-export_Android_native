package com.ecotton.impex.models;

public class AttributeModelFromTo {
    String to, from, attribute;

    public AttributeModelFromTo(String to, String from, String attribute) {
        this.to = to;
        this.from = from;
        this.attribute = attribute;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
