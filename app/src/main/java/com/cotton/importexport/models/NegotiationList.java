package com.cotton.importexport.models;

import java.io.Serializable;
import java.util.ArrayList;

public class NegotiationList implements Serializable {
    public int post_id = 0,notification_id=0,count = 0;
    public String status = "", seller_buyer_id = "", name = "", broker_name = "", user_type = "", product_id = "", product_name = "",
            no_of_bales = "", price = "", address = "", d_e = "", buy_for = "", negotiation_type = "", spinning_meal_name = "",
            best_price = "", best_bales = "", best_name = "";
    public ArrayList<PostDetail> post_detail = new ArrayList<>();
    public ArrayList<NotificatioDetail> notification_detail = new ArrayList<>();

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeller_buyer_id() {
        return seller_buyer_id;
    }

    public void setSeller_buyer_id(String seller_buyer_id) {
        this.seller_buyer_id = seller_buyer_id;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public ArrayList<NotificatioDetail> getNotification_detail() {
        return notification_detail;
    }

    public void setNotification_detail(ArrayList<NotificatioDetail> notification_detail) {
        this.notification_detail = notification_detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBroker_name() {
        return broker_name;
    }

    public void setBroker_name(String broker_name) {
        this.broker_name = broker_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
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

    public String getNo_of_bales() {
        return no_of_bales;
    }

    public void setNo_of_bales(String no_of_bales) {
        this.no_of_bales = no_of_bales;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getD_e() {
        return d_e;
    }

    public void setD_e(String d_e) {
        this.d_e = d_e;
    }

    public String getBuy_for() {
        return buy_for;
    }

    public void setBuy_for(String buy_for) {
        this.buy_for = buy_for;
    }

    public String getNegotiation_type() {
        return negotiation_type;
    }

    public void setNegotiation_type(String negotiation_type) {
        this.negotiation_type = negotiation_type;
    }

    public String getSpinning_meal_name() {
        return spinning_meal_name;
    }

    public void setSpinning_meal_name(String spinning_meal_name) {
        this.spinning_meal_name = spinning_meal_name;
    }

    public String getBest_price() {
        return best_price;
    }

    public void setBest_price(String best_price) {
        this.best_price = best_price;
    }

    public String getBest_bales() {
        return best_bales;
    }

    public void setBest_bales(String best_bales) {
        this.best_bales = best_bales;
    }

    public String getBest_name() {
        return best_name;
    }

    public void setBest_name(String best_name) {
        this.best_name = best_name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<PostDetail> getPost_detail() {
        return post_detail;
    }

    public void setPost_detail(ArrayList<PostDetail> post_detail) {
        this.post_detail = post_detail;
    }

    public static class PostDetail implements Serializable {

        public int negotiation_id = 0, post_notification_id = 0, posted_company_id = 0, negotiation_by_company_id = 0,
                buyer_id = 0, seller_id = 0, current_price = 0, prev_price = 0, current_no_of_bales = 0, prev_no_of_bales = 0;
        public String posted_company_name = "", negotiation_by_company_name = "", buyer_name = "", seller_name = "",
                negotiation_by = "", negotiation_type = "", transmit_condition = "", payment_condition = "", lab = "",
                broker_name = "", updated_at = "";

        public int getNegotiation_id() {
            return negotiation_id;
        }

        public void setNegotiation_id(int negotiation_id) {
            this.negotiation_id = negotiation_id;
        }

        public int getPost_notification_id() {
            return post_notification_id;
        }

        public void setPost_notification_id(int post_notification_id) {
            this.post_notification_id = post_notification_id;
        }

        public int getPosted_company_id() {
            return posted_company_id;
        }

        public void setPosted_company_id(int posted_company_id) {
            this.posted_company_id = posted_company_id;
        }

        public int getNegotiation_by_company_id() {
            return negotiation_by_company_id;
        }

        public void setNegotiation_by_company_id(int negotiation_by_company_id) {
            this.negotiation_by_company_id = negotiation_by_company_id;
        }

        public int getBuyer_id() {
            return buyer_id;
        }

        public void setBuyer_id(int buyer_id) {
            this.buyer_id = buyer_id;
        }

        public int getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(int seller_id) {
            this.seller_id = seller_id;
        }

        public int getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(int current_price) {
            this.current_price = current_price;
        }

        public int getPrev_price() {
            return prev_price;
        }

        public void setPrev_price(int prev_price) {
            this.prev_price = prev_price;
        }

        public int getCurrent_no_of_bales() {
            return current_no_of_bales;
        }

        public void setCurrent_no_of_bales(int current_no_of_bales) {
            this.current_no_of_bales = current_no_of_bales;
        }

        public int getPrev_no_of_bales() {
            return prev_no_of_bales;
        }

        public void setPrev_no_of_bales(int prev_no_of_bales) {
            this.prev_no_of_bales = prev_no_of_bales;
        }

        public String getPosted_company_name() {
            return posted_company_name;
        }

        public void setPosted_company_name(String posted_company_name) {
            this.posted_company_name = posted_company_name;
        }

        public String getNegotiation_by_company_name() {
            return negotiation_by_company_name;
        }

        public void setNegotiation_by_company_name(String negotiation_by_company_name) {
            this.negotiation_by_company_name = negotiation_by_company_name;
        }

        public String getBuyer_name() {
            return buyer_name;
        }

        public void setBuyer_name(String buyer_name) {
            this.buyer_name = buyer_name;
        }

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
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

        public String getBroker_name() {
            return broker_name;
        }

        public void setBroker_name(String broker_name) {
            this.broker_name = broker_name;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }

    public static class NotificatioDetail implements Serializable {

        public int negotiation_id = 0, post_notification_id = 0, posted_company_id = 0, negotiation_by_company_id = 0,
                buyer_id = 0, seller_id = 0, current_price = 0, prev_price = 0, current_no_of_bales = 0, prev_no_of_bales = 0;
        public String posted_company_name = "", negotiation_by_company_name = "", buyer_name = "", seller_name = "",
                negotiation_by = "", negotiation_type = "", transmit_condition = "", payment_condition = "", lab = "",
                broker_name = "", updated_at = "";

        public int getNegotiation_id() {
            return negotiation_id;
        }

        public void setNegotiation_id(int negotiation_id) {
            this.negotiation_id = negotiation_id;
        }

        public int getPost_notification_id() {
            return post_notification_id;
        }

        public void setPost_notification_id(int post_notification_id) {
            this.post_notification_id = post_notification_id;
        }

        public int getPosted_company_id() {
            return posted_company_id;
        }

        public void setPosted_company_id(int posted_company_id) {
            this.posted_company_id = posted_company_id;
        }

        public int getNegotiation_by_company_id() {
            return negotiation_by_company_id;
        }

        public void setNegotiation_by_company_id(int negotiation_by_company_id) {
            this.negotiation_by_company_id = negotiation_by_company_id;
        }

        public int getBuyer_id() {
            return buyer_id;
        }

        public void setBuyer_id(int buyer_id) {
            this.buyer_id = buyer_id;
        }

        public int getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(int seller_id) {
            this.seller_id = seller_id;
        }

        public int getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(int current_price) {
            this.current_price = current_price;
        }

        public int getPrev_price() {
            return prev_price;
        }

        public void setPrev_price(int prev_price) {
            this.prev_price = prev_price;
        }

        public int getCurrent_no_of_bales() {
            return current_no_of_bales;
        }

        public void setCurrent_no_of_bales(int current_no_of_bales) {
            this.current_no_of_bales = current_no_of_bales;
        }

        public int getPrev_no_of_bales() {
            return prev_no_of_bales;
        }

        public void setPrev_no_of_bales(int prev_no_of_bales) {
            this.prev_no_of_bales = prev_no_of_bales;
        }

        public String getPosted_company_name() {
            return posted_company_name;
        }

        public void setPosted_company_name(String posted_company_name) {
            this.posted_company_name = posted_company_name;
        }

        public String getNegotiation_by_company_name() {
            return negotiation_by_company_name;
        }

        public void setNegotiation_by_company_name(String negotiation_by_company_name) {
            this.negotiation_by_company_name = negotiation_by_company_name;
        }

        public String getBuyer_name() {
            return buyer_name;
        }

        public void setBuyer_name(String buyer_name) {
            this.buyer_name = buyer_name;
        }

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
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

        public String getBroker_name() {
            return broker_name;
        }

        public void setBroker_name(String broker_name) {
            this.broker_name = broker_name;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }

}
