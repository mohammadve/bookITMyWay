package com.virtual.customervendor.managers;

import android.content.Context;

import com.virtual.customervendor.model.CountryCodeModel;
import com.virtual.customervendor.model.UserData;
import com.virtual.customervendor.model.request.Ven_Sight_Seeing_Service_Request;
import com.virtual.customervendor.model.request.Ven_Taxi_Service_Request;
import com.virtual.customervendor.model.request.VendorEventServiceRequest;
import com.virtual.customervendor.model.request.VendorHealthServiceRequest;
import com.virtual.customervendor.model.request.VendorParkingRequest;
import com.virtual.customervendor.model.request.VendorRestaurantServiceModel;
import com.virtual.customervendor.model.request.VendorStoreServiceRequest;


public class ApplicationCache {

    private static ApplicationCache applicationCache = null;
    private Context applicationContext = null;
    private UserData userData;
//    private UserDataLogin  userDataLogin;
    private VendorRestaurantServiceModel restaurantServiceModel;
    private VendorStoreServiceRequest storeServiceRequest;
    private Ven_Taxi_Service_Request venTaxiServiceRequest;
    private VendorHealthServiceRequest doctoreServiceRequest;
    private Ven_Sight_Seeing_Service_Request sightSeenInfo;
    private VendorParkingRequest parkingRequest;
    private VendorEventServiceRequest eventInfo;
    private CountryCodeModel currentCountry;


    public static ApplicationCache getApplicationCache() {
        if (applicationCache == null) {
            applicationCache = new ApplicationCache();
        }
        return applicationCache;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }


    public UserData getUserSignUpDetail() {
        return userData;
    }

    public void setUserSignUpDetail(UserData userData) {
        this.userData = userData;
    }

//    public UserDataLogin getUserDetail() {
//        return userDataLogin;
//    }
//
//    public void SetUserDetail(UserDataLogin userDataLogin) {
//        this.userDataLogin = userDataLogin;
//    }

    public VendorRestaurantServiceModel getVendorRestaurantInfo() {
        return restaurantServiceModel;
    }

    public void SetVendorRestaurantInfo(VendorRestaurantServiceModel restaurantServiceModel) {
        this.restaurantServiceModel = restaurantServiceModel;
    }

    public VendorStoreServiceRequest getVendorStoreInfo() {
        return storeServiceRequest;
    }

    public void setVendorStoreInfo(VendorStoreServiceRequest storeServiceRequest) {
        this.storeServiceRequest = storeServiceRequest;
    }
    public VendorHealthServiceRequest getVendorDoctorInfo() {
        return doctoreServiceRequest;
    }

    public void setVendorDoctorInfo(VendorHealthServiceRequest doctoreServiceRequest) {
        this.doctoreServiceRequest = doctoreServiceRequest;
    }

    public VendorParkingRequest getParkingInfo() {
        return parkingRequest;
    }

    public void setParkingInfo(VendorParkingRequest parkingServiceRequest) {
        this.parkingRequest = parkingServiceRequest;
    }

    public Ven_Taxi_Service_Request getVendorTaxiInfo() {
        return venTaxiServiceRequest;
    }

    public void SetVendorTaxiInfo(Ven_Taxi_Service_Request taxiServiceRequest) {
        this.venTaxiServiceRequest = taxiServiceRequest;
    }

    public VendorEventServiceRequest getVendorEventInfo() {
        return eventInfo;
    }

    public void setVendorEventInfo(VendorEventServiceRequest eventInfo) {
        this.eventInfo = eventInfo;
    }

    public Ven_Sight_Seeing_Service_Request getVendorSightSeenInfo() {
        return sightSeenInfo;
    }

    public void setVendorSightSeenInfo(Ven_Sight_Seeing_Service_Request sightSeenInfo) {
        this.sightSeenInfo = sightSeenInfo;
    }

    public CountryCodeModel getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(CountryCodeModel currentCountry) {
        this.currentCountry = currentCountry;
    }

}
