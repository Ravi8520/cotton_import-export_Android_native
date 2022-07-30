package com.cotton.importexport.models;

import java.io.Serializable;

public class MakeDeal implements Serializable {
    public int deal_id=0;
    public String email_otp="";
    public String mobile_otp="";

    public int getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(int deal_id) {
        this.deal_id = deal_id;
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
}
