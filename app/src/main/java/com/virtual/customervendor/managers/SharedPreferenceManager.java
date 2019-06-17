package com.virtual.customervendor.managers;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.virtual.customervendor.model.response.CountrycodeResponse;
import com.virtual.customervendor.utills.AppKeys;
import com.virtual.customervendor.utills.AppLog;

public class SharedPreferenceManager {
    public static final String TAG = SharedPreferenceManager.class.getSimpleName();

    public static String getAuthToken() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.USER_AUTH_TOKEN, "");
        return token;
    }

    public static void setAuthToken(String token) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.USER_AUTH_TOKEN, token);
        editor.commit();
    }

    public static String getFCMToken() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.USER_FCM_TOKEN, "");
        return token;
    }


    public static void setFCMToken(String token) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.USER_FCM_TOKEN, token);
        editor.commit();
    }

    /*customer*/
    public static String getCustomerName() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.CUSTOMER_NAME, "");
        return token;
    }

    public static void setCustomerName(String bus_name) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.CUSTOMER_NAME, bus_name);
        editor.commit();
    }

    public static String getCustomerEmail() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.CUSTOMER_EMAIL, "");
        return token;
    }

    public static void setCustomerEmail(String bus_name) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.CUSTOMER_EMAIL, bus_name);
        editor.commit();
    }

    public static String getCustomerMobileNo() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.CUSTOMER_MOBILE, "");
        return token;
    }

    public static void setCustomerMobilNo(String bus_name) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.CUSTOMER_MOBILE, bus_name);
        editor.commit();
    }

    public static String getCustomerUserId() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.CUSTOMER_USERID, "");
        return token;
    }

    public static void setCustomerUserId(String bus_name) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.CUSTOMER_USERID, bus_name);
        editor.commit();
    }

    public static String getCustomerImage() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.CUSTOMER_IMAGE, "");
        return token;
    }

    public static void setCustomerImage(String custImg) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.CUSTOMER_IMAGE, custImg);
        editor.commit();
    }
    /*customer*/

    /*vendor*/
    public static String getBussinessName() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.VENDOR_BUSSINESSNAME, "");
        return token;
    }

    public static void setBussinessName(String bus_name) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.VENDOR_BUSSINESSNAME, bus_name);
        editor.commit();
    }

//    public static String getVendorImage() {
//        SharedPreferences preference = getDefaultPreference();
//        String token = preference.getString(AppKeys.VENDOR_BUSSINESSIMAGE, "");
//        return token;
//    }

//    public static void setVendorImage(String custImg) {
//        SharedPreferences preference = getDefaultPreference();
//        Editor editor = preference.edit();
//        editor.putString(AppKeys.VENDOR_BUSSINESSIMAGE, custImg);
//        editor.commit();
//    }
    /*vendor end*/

    public static String getSysOTP() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.USER_SYS_OTP, "");
        return token;
    }

    public static void setSysOTP(String sysOTP) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.USER_SYS_OTP, sysOTP);
        editor.commit();
    }

    public static String getUserEmail() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.USER_EMAIL, "");
        return token;
    }

    public static void setUserEmail(String userEmail) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.USER_EMAIL, userEmail);
        editor.commit();
    }

    public static String getUserId() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.USER_ID, "");
        return token;
    }

    public static void setUserId(String user_id) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.USER_ID, user_id);
        editor.commit();
    }

    public static int getSubcategoryId() {
        SharedPreferences preference = getDefaultPreference();
        int token = preference.getInt(AppKeys.KEY_SUBCATEGORY, -1);
        return token;
    }

    public static void setSubcategoryId(int subcat_id) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putInt(AppKeys.KEY_SUBCATEGORY, subcat_id);
        editor.commit();
    }

    public static int getCategoryId() {
        SharedPreferences preference = getDefaultPreference();
        int token = preference.getInt(AppKeys.KEY_CATEGORY, 0);
        return token;
    }

    public static void setCategoryId(int category_id) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putInt(AppKeys.KEY_CATEGORY, category_id);
        editor.commit();
    }

    public static int getServiceId() {
        SharedPreferences preference = getDefaultPreference();
        int token = preference.getInt(AppKeys.KEY_SERVICEID, 0);
        return token;
    }

    public static void setServiceId(int service_id) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putInt(AppKeys.KEY_SERVICEID, service_id);
        editor.commit();
    }

    public static String getBussinessID() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.VENDOR_BUSSINESSID, "");
        return token;
    }

    public static void setBussinessID(String bus_name) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.VENDOR_BUSSINESSID, bus_name);
        editor.commit();
    }

//    public static String getCurrencySymbol() {
//        SharedPreferences preference = getDefaultPreference();
//        String token = preference.getString(AppKeys.CURRENCY_SYMBOL, "");
//        return token;
//    }
//
//    public static void setCurrencySymbol(String bus_name) {
//        SharedPreferences preference = getDefaultPreference();
//        Editor editor = preference.edit();
//        editor.putString(AppKeys.CURRENCY_SYMBOL, bus_name);
//        editor.commit();
//    }

    public static String getUserLanguage() {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(AppKeys.USER_LANGUAGE, "en");
        return token;
    }

    public static void setUserLanguage(String val) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.USER_LANGUAGE, val);
        editor.commit();
    }

    public static CountrycodeResponse getRegisterCountryDetails() {
        SharedPreferences preference = getDefaultPreference();
        String countrycodeResponseString = preference.getString(AppKeys.REGISTER_COUNTRY_DETAILS, null);
        CountrycodeResponse countrycodeResponse = null;
        if (countrycodeResponseString != null) {
            countrycodeResponse = new Gson().fromJson(countrycodeResponseString, CountrycodeResponse.class);
        }
        return countrycodeResponse;
    }

    public static void setRegisterCountryDetails(String val) {
        AppLog.e("setRegisterCountryDetails", val);
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(AppKeys.REGISTER_COUNTRY_DETAILS, val);
        editor.commit();
    }


    public static SharedPreferences getDefaultPreference() {
        return PreferenceManager.getDefaultSharedPreferences(CachingManager.getApplicationContext());
    }

    public static Boolean getBoolean(String key) {
        SharedPreferences preference = getDefaultPreference();
        Boolean token = preference.getBoolean(key, true);
        return token;
    }


    public static void setBoolean(String key, boolean val) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public static String getString(String key) {
        SharedPreferences preference = getDefaultPreference();
        String token = preference.getString(key, null);
        return token;
    }


    public static void setString(String key, String val) {
        SharedPreferences preference = getDefaultPreference();
        Editor editor = preference.edit();
        editor.putString(key, val);
        editor.commit();
    }
}
