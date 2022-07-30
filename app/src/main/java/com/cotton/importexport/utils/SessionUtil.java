package com.cotton.importexport.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;


public class SessionUtil {

    private SharedPreferences preferences;
    public static final String ISLOGIN = "isLogin";
    public static final String FCMTOKEN = "fcmtoken";
    public static final String USERID = "userId";
    public static final String NAME = "name";
    public static final String PHARMACYLOGO = "pharmacyLogo";
    public static final String EMAIL = "email";
    public static final String MOBILE_NO = "mobile_no";
    public static final String PASS = "pass";
    public static final String IS_PHARMACYDETAIL_COMPLETE = "isPharmacyDetailComplete";
    public static final String IS_PHARMACY_APPROVED = "isPharmacyApproved";
    public static final String PHARMACY_STATUS = "pharmacyOpenCloseStatus";
    public static final String PHARMACY_NAME = "pharmacyName";
    public static final String REFERRAL_CODE = "referralCode";
    public static final String ALLOWED_DELIVERY_TYPE = "allowedDeliveryType";
    public static final String API_TOKEN = "apiToken";
    public static final String COMPANY_NAME = "companyname";
    public static final String COMPANY_ID = "companyid";
    public static final String USER_TYPE = "usertype";
    public static final String USER_AVAILIBILTY = "userAvailibiltyStatus";
    public static final String SELLER_PROFILE = "sellerProfile";
    public static final String IS_SETUP = "issettup";


    public SessionUtil(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setFCMToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FCMTOKEN, token);
        editor.apply();
    }

    public String getFcmtoken() {
        return preferences.getString(FCMTOKEN, "");
    }

    public void setData(HashMap<String, String> map) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ISLOGIN, true);
        editor.putString(API_TOKEN, "Bearer " + map.get(API_TOKEN));
        editor.putString(MOBILE_NO, map.get(MOBILE_NO));
        editor.putString(PASS, map.get(PASS));
        editor.putString(COMPANY_NAME, map.get(COMPANY_NAME));
        editor.putString(USER_TYPE, map.get(USER_TYPE));
        editor.putString(USERID, map.get(USERID));
        editor.putString(COMPANY_ID, map.get(COMPANY_ID));
        editor.putString(IS_SETUP, map.get(IS_SETUP));
        editor.putString(EMAIL, map.get(EMAIL));
        editor.putString(NAME, map.get(NAME));
        editor.apply();
    }

    public String getSetup() {
        return preferences.getString(IS_SETUP, "0");
    }

    public boolean isLogin() {
        return preferences.getBoolean(ISLOGIN, false);
    }

    public String getMobileNo() {
        return preferences.getString(MOBILE_NO, "");
    }

    public String getCompanyName() {
        return preferences.getString(COMPANY_NAME, "");
    }


    public void setMobileNo(String mobileNo) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MOBILE_NO, mobileNo);
        editor.apply();
    }

    public void setPass(String pass) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PASS, pass);
        editor.apply();
    }

    public String getPass() {
        return preferences.getString(PASS, "");
    }

    public void setApiToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(API_TOKEN, token);
        editor.apply();
    }


    public String getApiToken() {
        return preferences.getString(API_TOKEN, "");
    }

    public String getUserid() {
        return preferences.getString(USERID, "");
    }

    public String getCompanyId() {
        return preferences.getString(COMPANY_ID, "");
    }

    public void setPharmacyStatus(String pharmacyStatus) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PHARMACY_STATUS, pharmacyStatus);
        editor.apply();
    }

    public String getPharmacyStatus() {
        return preferences.getString(PHARMACY_STATUS, "");
    }

    public String getPharmacyName() {
        return preferences.getString(PHARMACY_NAME, "");
    }

    public String getName() {
        return preferences.getString(NAME, "");
    }

    public String getEmail() {
        return preferences.getString(EMAIL, "");
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getPharmacylogo() {
        return preferences.getString(PHARMACYLOGO, "");
    }

    public void setPharmacylogo(String pharmacylogo) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PHARMACYLOGO, pharmacylogo);
        editor.apply();
    }


    public String getReferralCode() {
        return preferences.getString(REFERRAL_CODE, "");
    }

    public String getUsertype() {
        return preferences.getString(USER_TYPE, "");
    }

    public String getUserAvailibilty() {
        return preferences.getString(USER_AVAILIBILTY, "");
    }

    public void setUserAvailibilty(String status) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_AVAILIBILTY, status);
        editor.apply();
    }

    public String getAllowedDeliveryType() {
        return preferences.getString(ALLOWED_DELIVERY_TYPE, "");
    }

    public String getSellerProfile() {
        return preferences.getString(SELLER_PROFILE, "");
    }

    public void setSellerProfile(String profile) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELLER_PROFILE, profile);
        editor.apply();
    }

    public void logOut() {
        String fcmToken = getFcmtoken();
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        setFCMToken(fcmToken);
    }
}
