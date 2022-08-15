package com.ecotton.impex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BrokerReportModel implements Serializable {
    @SerializedName("company_list")
    @Expose
    private List<CompanyList> company_list;

    @SerializedName("member_list")
    @Expose
    private List<MemberList> member_list;

    @SerializedName("report_url")
    @Expose
    private String report_url;

    public String getReport_url() {
        return report_url;
    }

    public void setReport_url(String report_url) {
        this.report_url = report_url;
    }

    public List<CompanyList> getCompany_list() {
        return company_list;
    }

    public void setCompany_list(List<CompanyList> company_list) {
        this.company_list = company_list;
    }

    public List<MemberList> getMember_list() {
        return member_list;
    }

    public void setMember_list(List<MemberList> member_list) {
        this.member_list = member_list;
    }

    public static class CompanyList implements Serializable {
        @SerializedName("company_id")
        @Expose
        private int company_id;

        @SerializedName("company_name")
        @Expose
        private String company_name;

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }
    }

    public static class MemberList implements Serializable {
        @SerializedName("company_id")
        @Expose
        private int company_id;

        @SerializedName("seller_buyer_id")
        @Expose
        private int seller_buyer_id;

        @SerializedName("seller_buyer_name")
        @Expose
        private String seller_buyer_name;

        @SerializedName("type")
        @Expose
        private String type;

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }

        public int getSeller_buyer_id() {
            return seller_buyer_id;
        }

        public void setSeller_buyer_id(int seller_buyer_id) {
            this.seller_buyer_id = seller_buyer_id;
        }

        public String getSeller_buyer_name() {
            return seller_buyer_name;
        }

        public void setSeller_buyer_name(String seller_buyer_name) {
            this.seller_buyer_name = seller_buyer_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
