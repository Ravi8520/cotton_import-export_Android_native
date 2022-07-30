package com.cotton.importexport.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyWalletModel implements Serializable {
    @SerializedName("wallet_amount")
    @Expose
    private String wallet_amount;

    @SerializedName("transaction_history")
    @Expose
    private List<Transaction> transaction_history = new ArrayList();

    public String getWallet_amount() {
        return wallet_amount;
    }

    public void setWallet_amount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public List<Transaction> getTransaction_history() {
        return transaction_history;
    }

    public void setTransaction_history(List<Transaction> transaction_history) {
        this.transaction_history = transaction_history;
    }

    public class Transaction implements Serializable
    {
        @SerializedName("amount")
        @Expose
        private int amount;

        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("user_type")
        @Expose
        private String user_type;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("date")
        @Expose
        private String date;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

}
