package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataModel implements Serializable {
    @SerializedName("deal_id")
    @Expose
    private int deal_id;

    @SerializedName("is_dispatch")
    @Expose
    private int is_dispatch;

    @SerializedName("is_sample")
    @Expose
    private int is_sample;

    @SerializedName("is_seller_otp_verify")
    @Expose
    private int is_seller_otp_verify;

    @SerializedName("is_buyer_otp_verify")
    @Expose
    private int is_buyer_otp_verify;

    @SerializedName("is_broker_otp_verify")
    @Expose
    private int is_broker_otp_verify;

    @SerializedName("broker_id")
    @Expose
    private int broker_id;

    @SerializedName("seller_company_id")
    @Expose
    private int seller_company_id;

    @SerializedName("buyer_company_id")
    @Expose
    private int buyer_company_id;

    @SerializedName("post_notification_id")
    @Expose
    private String post_notification_id;

    @SerializedName("post_date")
    @Expose
    private String post_date;

    @SerializedName("buyer_id")
    @Expose
    private String buyer_id;

    @SerializedName("buyer_name")
    @Expose
    private String buyer_name;

    @SerializedName("seller_id")
    @Expose
    private String seller_id;

    @SerializedName("seller_name")
    @Expose
    private String seller_name;

    @SerializedName("broker_name")
    @Expose
    private String broker_name;

    @SerializedName("negotiation_by")
    @Expose
    private String negotiation_by;

    @SerializedName("negotiation_type")
    @Expose
    private String negotiation_type;

    @SerializedName("seller_company_name")
    @Expose
    private String seller_company_name;

    @SerializedName("buyer_company_name")
    @Expose
    private String buyer_company_name;

    @SerializedName("post_price")
    @Expose
    private String post_price;

    @SerializedName("post_bales")
    @Expose
    private String post_bales;

    @SerializedName("sell_bales")
    @Expose
    private String sell_bales;

    @SerializedName("sell_price")
    @Expose
    private String sell_price;

    @SerializedName("payment_condition")
    @Expose
    private String payment_condition;

    @SerializedName("transmit_condition")
    @Expose
    private String transmit_condition;

    @SerializedName("lab")
    @Expose
    private String lab;

    @SerializedName("lab_report")
    @Expose
    private String lab_report;

    @SerializedName("transmit_deal")
    @Expose
    private String transmit_deal;

    @SerializedName("without_gst")
    @Expose
    private String without_gst;

    @SerializedName("gst_reciept")
    @Expose
    private String gst_reciept;

    @SerializedName("lab_report_mime")
    @Expose
    private String lab_report_mime;

    @SerializedName("transmit_deal_mime")
    @Expose
    private String transmit_deal_mime;

    @SerializedName("without_gst_mime")
    @Expose
    private String without_gst_mime;

    @SerializedName("gst_reciept_mime")
    @Expose
    private String gst_reciept_mime;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("lab_report_status")
    @Expose
    private String lab_report_status;

    @SerializedName("email_otp")
    @Expose
    private String email_otp;

    @SerializedName("mobile_otp")
    @Expose
    private String mobile_otp;

    @SerializedName("attribute_array")
    @Expose
    private List<AttributeArray> attribute_array;

    @SerializedName("debit_note_array")
    @Expose
    private List<DebitArray> debit_note_array = new ArrayList<>();

    public int getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(int deal_id) {
        this.deal_id = deal_id;
    }

    public int getIs_dispatch() {
        return is_dispatch;
    }

    public void setIs_dispatch(int is_dispatch) {
        this.is_dispatch = is_dispatch;
    }

    public int getIs_sample() {
        return is_sample;
    }

    public void setIs_sample(int is_sample) {
        this.is_sample = is_sample;
    }

    public int getIs_seller_otp_verify() {
        return is_seller_otp_verify;
    }

    public void setIs_seller_otp_verify(int is_seller_otp_verify) {
        this.is_seller_otp_verify = is_seller_otp_verify;
    }

    public int getIs_buyer_otp_verify() {
        return is_buyer_otp_verify;
    }

    public void setIs_buyer_otp_verify(int is_buyer_otp_verify) {
        this.is_buyer_otp_verify = is_buyer_otp_verify;
    }

    public int getIs_broker_otp_verify() {
        return is_broker_otp_verify;
    }

    public void setIs_broker_otp_verify(int is_broker_otp_verify) {
        this.is_broker_otp_verify = is_broker_otp_verify;
    }

    public int getBroker_id() {
        return broker_id;
    }

    public void setBroker_id(int broker_id) {
        this.broker_id = broker_id;
    }

    public int getSeller_company_id() {
        return seller_company_id;
    }

    public void setSeller_company_id(int seller_company_id) {
        this.seller_company_id = seller_company_id;
    }

    public int getBuyer_company_id() {
        return buyer_company_id;
    }

    public void setBuyer_company_id(int buyer_company_id) {
        this.buyer_company_id = buyer_company_id;
    }

    public String getPost_notification_id() {
        return post_notification_id;
    }

    public void setPost_notification_id(String post_notification_id) {
        this.post_notification_id = post_notification_id;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
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

    public String getNegotiation_type() {
        return negotiation_type;
    }

    public void setNegotiation_type(String negotiation_type) {
        this.negotiation_type = negotiation_type;
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

    public String getSell_bales() {
        return sell_bales;
    }

    public void setSell_bales(String sell_bales) {
        this.sell_bales = sell_bales;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getPayment_condition() {
        return payment_condition;
    }

    public void setPayment_condition(String payment_condition) {
        this.payment_condition = payment_condition;
    }

    public String getTransmit_condition() {
        return transmit_condition;
    }

    public void setTransmit_condition(String transmit_condition) {
        this.transmit_condition = transmit_condition;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getLab_report() {
        return lab_report;
    }

    public void setLab_report(String lab_report) {
        this.lab_report = lab_report;
    }

    public String getTransmit_deal() {
        return transmit_deal;
    }

    public void setTransmit_deal(String transmit_deal) {
        this.transmit_deal = transmit_deal;
    }

    public String getWithout_gst() {
        return without_gst;
    }

    public void setWithout_gst(String without_gst) {
        this.without_gst = without_gst;
    }

    public String getGst_reciept() {
        return gst_reciept;
    }

    public void setGst_reciept(String gst_reciept) {
        this.gst_reciept = gst_reciept;
    }

    public String getLab_report_mime() {
        return lab_report_mime;
    }

    public void setLab_report_mime(String lab_report_mime) {
        this.lab_report_mime = lab_report_mime;
    }

    public String getTransmit_deal_mime() {
        return transmit_deal_mime;
    }

    public void setTransmit_deal_mime(String transmit_deal_mime) {
        this.transmit_deal_mime = transmit_deal_mime;
    }

    public String getWithout_gst_mime() {
        return without_gst_mime;
    }

    public void setWithout_gst_mime(String without_gst_mime) {
        this.without_gst_mime = without_gst_mime;
    }

    public String getGst_reciept_mime() {
        return gst_reciept_mime;
    }

    public void setGst_reciept_mime(String gst_reciept_mime) {
        this.gst_reciept_mime = gst_reciept_mime;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLab_report_status() {
        return lab_report_status;
    }

    public void setLab_report_status(String lab_report_status) {
        this.lab_report_status = lab_report_status;
    }

    public String getEmail_otp() {
        return email_otp;
    }

    public void setEmail_otp(String email_otp) {
        this.email_otp = email_otp;
    }

    public String getMobile_otp() {
        return mobile_otp;
    }

    public void setMobile_otp(String mobile_otp) {
        this.mobile_otp = mobile_otp;
    }

    public List<AttributeArray> getAttribute_array() {
        return attribute_array;
    }

    public void setAttribute_array(List<AttributeArray> attribute_array) {
        this.attribute_array = attribute_array;
    }

    public List<DebitArray> getDebit_note_array() {
        return debit_note_array;
    }

    public void setDebit_note_array(List<DebitArray> debit_note_array) {
        this.debit_note_array = debit_note_array;
    }
    public class AttributeArray implements Serializable {
        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("attribute")
        @Expose
        private String attribute;

        @SerializedName("attribute_value")
        @Expose
        private String attribute_value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

    public class DebitArray implements Serializable {

    }
}
