package com.virtual.customervendor.managers;

import android.content.Context;


import com.virtual.customervendor.model.CountryCodeModel;
import com.virtual.customervendor.model.UserData;
import com.virtual.customervendor.model.UserDataLogin;
import com.virtual.customervendor.model.request.VendorEventServiceRequest;
import com.virtual.customervendor.model.request.VendorRestaurantServiceModel;
import com.virtual.customervendor.model.request.VendorHealthServiceRequest;
import com.virtual.customervendor.model.request.Ven_Sight_Seeing_Service_Request;
import com.virtual.customervendor.model.request.Ven_Taxi_Service_Request;
import com.virtual.customervendor.model.request.VendorParkingRequest;
import com.virtual.customervendor.model.request.VendorStoreServiceRequest;

public class CachingManager {
    public static void cacheApplicationContext(Context applicationContext) {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        applicationCache.setApplicationContext(applicationContext);
    }

    /**
     * This method is used to access application context anywhere in the App
     *
     * @return
     */
    public static Context getApplicationContext() {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        Context applicationContext = applicationCache.getApplicationContext();
        return applicationContext;
    }

    public static UserData getUserSignupDetail() {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        return applicationCache.getUserSignUpDetail();
    }

    public static void setUserSignupDetail(UserData userDataLogin) {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        applicationCache.setUserSignUpDetail(userDataLogin);
    }

//    public static UserDataLogin getUserDetail() {
//        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
//        return applicationCache.getUserDetail();
//    }
//
//    public static void setUserDetail(UserDataLogin userDataLogin) {
//        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
//        applicationCache.SetUserDetail(userDataLogin);
//    }

    public static VendorRestaurantServiceModel getVendorRestaurantInfo() {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        return applicationCache.getVendorRestaurantInfo();
    }

    public static void setVendorRestaurantInfo(VendorRestaurantServiceModel restaurantInfo) {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        applicationCache.SetVendorRestaurantInfo(restaurantInfo);
    }

    public static VendorStoreServiceRequest getVendorStoreInfo() {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        return applicationCache.getVendorStoreInfo();
    }

    public static void setVendorStoreInfo(VendorStoreServiceRequest storeInfo) {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        applicationCache.setVendorStoreInfo(storeInfo);
    }

    public static VendorHealthServiceRequest getVendorDoctorInfo() {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        return applicationCache.getVendorDoctorInfo();
    }

    public static void setVendorDoctorInfo(VendorHealthServiceRequest vendorDoctorInfo) {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        applicationCache.setVendorDoctorInfo(vendorDoctorInfo);
    }

    public static Ven_Taxi_Service_Request getVendorTaxiInfo() {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        return applicationCache.getVendorTaxiInfo();
    }

    public static void setVendorTaxiInfo(Ven_Taxi_Service_Request taxiInfo) {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        SharedPreferenceManager.setBussinessName(taxiInfo.getBusiness_name());
        applicationCache.SetVendorTaxiInfo(taxiInfo);
    }

    public static VendorEventServiceRequest getVendorEventInfo() {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        return applicationCache.getVendorEventInfo();
    }

    public static void setVendorEventInfo(VendorEventServiceRequest eventInfo) {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        SharedPreferenceManager.setBussinessName(eventInfo.getBusiness_name());
        applicationCache.setVendorEventInfo(eventInfo);
    }

    public static VendorParkingRequest getVendorParkingInfo() {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        return applicationCache.getParkingInfo();
    }

    public static void setVendorParkingInfo(VendorParkingRequest parkingRequest) {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        SharedPreferenceManager.setBussinessName(parkingRequest.getBusiness_name());
        applicationCache.setParkingInfo(parkingRequest);
    }

    public static Ven_Sight_Seeing_Service_Request getVendorSightSeenInfo() {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        return applicationCache.getVendorSightSeenInfo();
    }

    public static void setVendorSightSeenInfo(Ven_Sight_Seeing_Service_Request sightSeenInfo) {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        SharedPreferenceManager.setBussinessName(sightSeenInfo.getBusiness_name());
        applicationCache.setVendorSightSeenInfo(sightSeenInfo);
    }


    public static CountryCodeModel getCurrentCountry() {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        return applicationCache.getCurrentCountry();
    }

    public static void setCurrentCountry(CountryCodeModel sightSeenInfo) {
        ApplicationCache applicationCache = ApplicationCache.getApplicationCache();
        applicationCache.setCurrentCountry(sightSeenInfo);
    }
}
