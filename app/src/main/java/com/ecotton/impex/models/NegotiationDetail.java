package com.ecotton.impex.models;

import java.io.Serializable;
import java.util.ArrayList;

public class NegotiationDetail implements Serializable {
    public String negotiation_id="";
    public String seller_id="";
    public String seller_name="";
    public String buyer_id="";
    public String buyer_name="";
    public String broker_id="";
    public String broker_name="";
    public String negotiation_by="";
    public String post_notification_id;
    public String negotiation_type="";
    public String current_price="";
    public String prev_price="";
    public String current_no_of_bales="";
    public String prev_no_of_bales="";
    public String transmit_condition="";
    public String payment_condition="";
    public String lab="";
    public String notes="";
    public String header="";
    public String header_name="";
    public String product_id="";
    public String product_name="";
    public String post_price="";
    public String post_bales="";
    public String sold_bales="";
    public String remain_bales="";
    ArrayList<DetailAttribute> attribute_array = new ArrayList<DetailAttribute>();
    public String is_highlight_current_price="";
    public String is_highlight_current_bales="";
    public String is_highlight_payment_condition="";
    public String is_highlight_transmit_condition="";
    public String is_highlight_header_name="";
    public String is_highlight_lab="";
    public String is_highlight_broker_name="";
    public String is_highlight_notes="";

    public String is_highlight_delivery_condition="";
    public String is_highlight_country_dispatch="";
    public String is_highlight_port_dispatch="";
    public String is_highlight_country_destination="";
    public String is_highlight_port_destination="";
    public String is_highlight_delivery_period="";


    public String country_origin_name;
    public String delivery_period;
    public String delivery_condition_id;
    public String delivery_condition_name;
    public String country_dispatch_id;
    public String country_dispatch_name;


    public String port_dispatch_id;
    public String port_dispatch_name;
    public String country_destination_id;
    public String country_destination_name;

    public String seller_company_name="";
    public String buyer_company_name="";

    public String getCountry_origin_name() {
        return country_origin_name;
    }

    public void setCountry_origin_name(String country_origin_name) {
        this.country_origin_name = country_origin_name;
    }

    public String getDelivery_period() {
        return delivery_period;
    }

    public void setDelivery_period(String delivery_period) {
        this.delivery_period = delivery_period;
    }

    public String getDelivery_condition_id() {
        return delivery_condition_id;
    }

    public void setDelivery_condition_id(String delivery_condition_id) {
        this.delivery_condition_id = delivery_condition_id;
    }

    public String getDelivery_condition_name() {
        return delivery_condition_name;
    }

    public void setDelivery_condition_name(String delivery_condition_name) {
        this.delivery_condition_name = delivery_condition_name;
    }

    public String getCountry_dispatch_id() {
        return country_dispatch_id;
    }

    public void setCountry_dispatch_id(String country_dispatch_id) {
        this.country_dispatch_id = country_dispatch_id;
    }

    public String getCountry_dispatch_name() {
        return country_dispatch_name;
    }

    public void setCountry_dispatch_name(String country_dispatch_name) {
        this.country_dispatch_name = country_dispatch_name;
    }

    public String getPort_dispatch_id() {
        return port_dispatch_id;
    }

    public void setPort_dispatch_id(String port_dispatch_id) {
        this.port_dispatch_id = port_dispatch_id;
    }

    public String getPort_dispatch_name() {
        return port_dispatch_name;
    }

    public void setPort_dispatch_name(String port_dispatch_name) {
        this.port_dispatch_name = port_dispatch_name;
    }

    public String getSeller_company_name() {
        return seller_company_name;
    }

    public void setSeller_company_name(String seller_company_name) {
        this.seller_company_name = seller_company_name;
    }

    public String getBuyer_company_name() {
        return buyer_company_name;
    }

    public void setBuyer_company_name(String buyer_company_name) {
        this.buyer_company_name = buyer_company_name;
    }

    public String getCountry_destination_id() {
        return country_destination_id;
    }

    public void setCountry_destination_id(String country_destination_id) {
        this.country_destination_id = country_destination_id;
    }

    public String getCountry_destination_name() {
        return country_destination_name;
    }

    public void setCountry_destination_name(String country_destination_name) {
        this.country_destination_name = country_destination_name;
    }

    public String getPort_destination_id() {
        return port_destination_id;
    }

    public void setPort_destination_id(String port_destination_id) {
        this.port_destination_id = port_destination_id;
    }

    public String getPort_destination_name() {
        return port_destination_name;
    }

    public void setPort_destination_name(String port_destination_name) {
        this.port_destination_name = port_destination_name;
    }

    public String port_destination_id;
    public String port_destination_name;

    public String getIs_highlight_delivery_condition() {
        return is_highlight_delivery_condition;
    }

    public void setIs_highlight_delivery_condition(String is_highlight_delivery_condition) {
        this.is_highlight_delivery_condition = is_highlight_delivery_condition;
    }

    public String getIs_highlight_country_dispatch() {
        return is_highlight_country_dispatch;
    }

    public void setIs_highlight_country_dispatch(String is_highlight_country_dispatch) {
        this.is_highlight_country_dispatch = is_highlight_country_dispatch;
    }

    public String getIs_highlight_port_dispatch() {
        return is_highlight_port_dispatch;
    }

    public void setIs_highlight_port_dispatch(String is_highlight_port_dispatch) {
        this.is_highlight_port_dispatch = is_highlight_port_dispatch;
    }

    public String getIs_highlight_country_destination() {
        return is_highlight_country_destination;
    }

    public void setIs_highlight_country_destination(String is_highlight_country_destination) {
        this.is_highlight_country_destination = is_highlight_country_destination;
    }

    public String getIs_highlight_port_destination() {
        return is_highlight_port_destination;
    }

    public void setIs_highlight_port_destination(String is_highlight_port_destination) {
        this.is_highlight_port_destination = is_highlight_port_destination;
    }

    public String getIs_highlight_delivery_period() {
        return is_highlight_delivery_period;
    }

    public void setIs_highlight_delivery_period(String is_highlight_delivery_period) {
        this.is_highlight_delivery_period = is_highlight_delivery_period;
    }

    public String transmit_condition_id="";
    public String payment_condition_id="";
    public String lab_id="";
    public String negotiation_by_company_name="";

    public String getTransmit_condition_id() {
        return transmit_condition_id;
    }

    public void setTransmit_condition_id(String transmit_condition_id) {
        this.transmit_condition_id = transmit_condition_id;
    }

    public String getPayment_condition_id() {
        return payment_condition_id;
    }

    public void setPayment_condition_id(String payment_condition_id) {
        this.payment_condition_id = payment_condition_id;
    }

    public String getLab_id() {
        return lab_id;
    }

    public void setLab_id(String lab_id) {
        this.lab_id = lab_id;
    }

    public String getNegotiation_id() {
        return negotiation_id;
    }

    public void setNegotiation_id(String negotiation_id) {
        this.negotiation_id = negotiation_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBroker_id() {
        return broker_id;
    }

    public void setBroker_id(String broker_id) {
        this.broker_id = broker_id;
    }

    public String getBroker_name() {
        return broker_name;
    }

    public void setBroker_name(String broker_name) {
        this.broker_name = broker_name;
    }

    public String getNegotiation_by() {
        return negotiation_by;
    }

    public void setNegotiation_by(String negotiation_by) {
        this.negotiation_by = negotiation_by;
    }

    public String getPost_notification_id() {
        return post_notification_id;
    }

    public void setPost_notification_id(String post_notification_id) {
        this.post_notification_id = post_notification_id;
    }

    public String getNegotiation_by_company_name() {
        return negotiation_by_company_name;
    }

    public void setNegotiation_by_company_name(String negotiation_by_company_name) {
        this.negotiation_by_company_name = negotiation_by_company_name;
    }

    public String getNegotiation_type() {
        return negotiation_type;
    }

    public void setNegotiation_type(String negotiation_type) {
        this.negotiation_type = negotiation_type;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getPrev_price() {
        return prev_price;
    }

    public void setPrev_price(String prev_price) {
        this.prev_price = prev_price;
    }

    public String getCurrent_no_of_bales() {
        return current_no_of_bales;
    }

    public void setCurrent_no_of_bales(String current_no_of_bales) {
        this.current_no_of_bales = current_no_of_bales;
    }

    public String getPrev_no_of_bales() {
        return prev_no_of_bales;
    }

    public void setPrev_no_of_bales(String prev_no_of_bales) {
        this.prev_no_of_bales = prev_no_of_bales;
    }

    public String getTransmit_condition() {
        return transmit_condition;
    }

    public void setTransmit_condition(String transmit_condition) {
        this.transmit_condition = transmit_condition;
    }

    public String getPayment_condition() {
        return payment_condition;
    }

    public void setPayment_condition(String payment_condition) {
        this.payment_condition = payment_condition;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader_name() {
        return header_name;
    }

    public void setHeader_name(String header_name) {
        this.header_name = header_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPost_price() {
        return post_price;
    }

    public void setPost_price(String post_price) {
        this.post_price = post_price;
    }

    public String getPost_bales() {
        return post_bales;
    }

    public void setPost_bales(String post_bales) {
        this.post_bales = post_bales;
    }

    public String getSold_bales() {
        return sold_bales;
    }

    public void setSold_bales(String sold_bales) {
        this.sold_bales = sold_bales;
    }

    public String getRemain_bales() {
        return remain_bales;
    }

    public void setRemain_bales(String remain_bales) {
        this.remain_bales = remain_bales;
    }

    public ArrayList<DetailAttribute> getAttribute_array() {
        return attribute_array;
    }

    public void setAttribute_array(ArrayList<DetailAttribute> attribute_array) {
        this.attribute_array = attribute_array;
    }

    public String getIs_highlight_current_price() {
        return is_highlight_current_price;
    }

    public void setIs_highlight_current_price(String is_highlight_current_price) {
        this.is_highlight_current_price = is_highlight_current_price;
    }

    public String getIs_highlight_current_bales() {
        return is_highlight_current_bales;
    }

    public void setIs_highlight_current_bales(String is_highlight_current_bales) {
        this.is_highlight_current_bales = is_highlight_current_bales;
    }

    public String getIs_highlight_payment_condition() {
        return is_highlight_payment_condition;
    }

    public void setIs_highlight_payment_condition(String is_highlight_payment_condition) {
        this.is_highlight_payment_condition = is_highlight_payment_condition;
    }

    public String getIs_highlight_transmit_condition() {
        return is_highlight_transmit_condition;
    }

    public void setIs_highlight_transmit_condition(String is_highlight_transmit_condition) {
        this.is_highlight_transmit_condition = is_highlight_transmit_condition;
    }

    public String getIs_highlight_header_name() {
        return is_highlight_header_name;
    }

    public void setIs_highlight_header_name(String is_highlight_header_name) {
        this.is_highlight_header_name = is_highlight_header_name;
    }

    public String getIs_highlight_lab() {
        return is_highlight_lab;
    }

    public void setIs_highlight_lab(String is_highlight_lab) {
        this.is_highlight_lab = is_highlight_lab;
    }

    public String getIs_highlight_broker_name() {
        return is_highlight_broker_name;
    }

    public void setIs_highlight_broker_name(String is_highlight_broker_name) {
        this.is_highlight_broker_name = is_highlight_broker_name;
    }

    public String getIs_highlight_notes() {
        return is_highlight_notes;
    }

    public void setIs_highlight_notes(String is_highlight_notes) {
        this.is_highlight_notes = is_highlight_notes;
    }

    public class DetailAttribute implements Serializable {
        public int id = 0;
        public String post_id = "", attribute = "", attribute_value = "";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
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
}
