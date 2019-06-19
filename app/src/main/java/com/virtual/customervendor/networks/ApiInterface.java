package com.virtual.customervendor.networks;

import com.virtual.customervendor.model.EventDetail;
import com.virtual.customervendor.model.PackageModel;
import com.virtual.customervendor.model.StorePlaceOrderModel;
import com.virtual.customervendor.model.request.CustomerTimeSlotRequest;
import com.virtual.customervendor.model.request.UpdateMenuRequest;
import com.virtual.customervendor.model.request.VendorBusinessDetailRequest;
import com.virtual.customervendor.model.request.VendorRestaurantServiceModel;
import com.virtual.customervendor.model.request.VendorStoreServiceRequest;
import com.virtual.customervendor.model.response.ApplyOfferResponse;
import com.virtual.customervendor.model.response.AppointmentReasonResponse;
import com.virtual.customervendor.model.response.BarCodeResponse;
import com.virtual.customervendor.model.response.BusinessDetailUpdateResponse;
import com.virtual.customervendor.model.response.BusinessFollowerResponse;
import com.virtual.customervendor.model.response.BusinessFollowingResponse;
import com.virtual.customervendor.model.response.BusinessImagesResponse;
import com.virtual.customervendor.model.response.BusinessListResponse;
import com.virtual.customervendor.model.response.BusinessOrderDetailResponse;
import com.virtual.customervendor.model.response.CancelOrderResponse;
import com.virtual.customervendor.model.response.CategoryAllResponse;
import com.virtual.customervendor.model.response.CategoryResponse;
import com.virtual.customervendor.model.response.CityResponse;
import com.virtual.customervendor.model.response.CommonResponse;
import com.virtual.customervendor.model.response.CountryResponse;
import com.virtual.customervendor.model.response.CountrycodeResponse;
import com.virtual.customervendor.model.response.CustomerBannerListResponse;
import com.virtual.customervendor.model.response.CustomerBookingResponse;
import com.virtual.customervendor.model.response.CustomerDisplayListResponse;
import com.virtual.customervendor.model.response.CustomerOrderResponse;
import com.virtual.customervendor.model.response.CustomerTimeSlotResponse;
import com.virtual.customervendor.model.response.EventListingResponse;
import com.virtual.customervendor.model.response.FollowUnfollowResponse;
import com.virtual.customervendor.model.response.ForgotOtpVerifificationResponse;
import com.virtual.customervendor.model.response.ForgotResponse;
import com.virtual.customervendor.model.response.LanguageResponse;
import com.virtual.customervendor.model.response.LoginResponse;
import com.virtual.customervendor.model.response.NotificationResponse;
import com.virtual.customervendor.model.response.OtpVerificationResponse;
import com.virtual.customervendor.model.response.PackageListResponse;
import com.virtual.customervendor.model.response.ProductCategoryResponse;
import com.virtual.customervendor.model.response.RegionResponse;
import com.virtual.customervendor.model.response.ResetPassResponse;
import com.virtual.customervendor.model.response.RestaurantMenuListingResponse;
import com.virtual.customervendor.model.response.SearchResponse;
import com.virtual.customervendor.model.response.SpecialityResponse;
import com.virtual.customervendor.model.response.StoreCategoryResponse;
import com.virtual.customervendor.model.response.StoreListingResponse;
import com.virtual.customervendor.model.response.TaxiServiceResponse;
import com.virtual.customervendor.model.response.UserProfilePicResponse;
import com.virtual.customervendor.model.response.UserResponse;
import com.virtual.customervendor.model.response.VenderOffersResponse;
import com.virtual.customervendor.model.response.VendorServiceDetailResponse;
import com.virtual.customervendor.utills.AppConstants;
import com.virtual.customervendor.utills.AppKeys;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface ApiInterface {

    @POST(AppConstants.AUTH_REGISTRATION_URL)
    @Headers({"Content-Type: application/json"})
    Observable<UserResponse> userRegistration(@QueryMap Map<String, String> mutableMap);

    @POST(AppConstants.AUTH_LOGIN_URL)
    @Headers({"Content-Type: application/json"})
    Observable<LoginResponse> userLogin(@retrofit2.http.Body com.virtual.customervendor.model.request.LoginRequest loginRequest);

    @POST(AppConstants.CUSTOMER_UPDATE_PROFILE)
    @Headers({"Content-Type: application/json"})
    Observable<LoginResponse> customerUpdateProfile(@Header("Authorization") String auth,@Query(AppKeys.USER_NAME) String mobile_no);

    @POST(AppConstants.AUTH_FORGOT_URL)
    @Headers({"Content-Type: application/json"})
    Observable<ForgotResponse> userForgot(@Query(AppKeys.MOBILE_NO) String mobile_no);

    @POST(AppConstants.AUTH_VERIFY_OTP_URL)
    @Headers({"Content-Type: application/json"})
    Observable<OtpVerificationResponse> verifyOtp(@QueryMap Map<String, String> mutableMap);

    @POST(AppConstants.AUTH_VERIFY_OTP_FORGOT)
    @Headers({"Content-Type: application/json"})
    Observable<ForgotOtpVerifificationResponse> verifyOtpForgot(@QueryMap Map<String, String> mutableMap);

    @POST(AppConstants.AUTH_RESETPASSWORD)
    @Headers({"Content-Type: application/json"})
    Observable<ResetPassResponse> resetPasword(@QueryMap Map<String, String> mutableMap);


    @POST(AppConstants.VENDOR_TRANS_TAXI_SERVICE_REQ)
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> taxiServiceSetUp(@Header("Authorization") String auth, @retrofit2.http.Body com.virtual.customervendor.model.request.Ven_Taxi_Service_Request restServiceModel);

    @POST(AppConstants.VENDOR_TRANS_SIGHTSEEING_SERVICE_REQ)
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> sightSeenServiceSetUp(@Header("Authorization") String auth, @retrofit2.http.Body com.virtual.customervendor.model.request.Ven_Sight_Seeing_Service_Request venSightSeeingServiceRequest);


    @POST(AppConstants.VENDOR_RESTAURANT_REQUEST)
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> restaurantServiceSetUp(@Header("Authorization") String auth, @retrofit2.http.Body VendorRestaurantServiceModel restServiceModel);


    @POST(AppConstants.VENDOR_STORE_SERVICE_REQUEST)
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> storeServiceSetUp(@Header("Authorization") String auth, @retrofit2.http.Body VendorStoreServiceRequest restServiceModel);


    @POST(AppConstants.VENDOR_HEALTH_REQUEST)//interenet
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> healthServiceSetUp(@Header("Authorization") String auth, @retrofit2.http.Body com.virtual.customervendor.model.request.VendorHealthServiceRequest restServiceModel);

    @POST(AppConstants.VENDOR_PARKING_VALET_REQUEST)
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> parkingServiceSetUp(@Header("Authorization") String auth, @retrofit2.http.Body com.virtual.customervendor.model.request.VendorParkingRequest restServiceModel);

    @POST(AppConstants.VENDOR_EVENT_REQUEST)
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> eventServiceSetUp(@Header("Authorization") String auth, @retrofit2.http.Body com.virtual.customervendor.model.request.VendorEventServiceRequest restServiceModel);


    @Multipart
    @POST(AppConstants.UPDATE_USER_IMAGE_URL)
    Single<UserProfilePicResponse> updateUserProfileImage(@Part MultipartBody.Part image);


    @Multipart
    @POST(AppConstants.EDIT_USER_IMAGE_URL)
    Single<UserProfilePicResponse> uploadAvatarAfterLogin(@Header("Authorization") String auth,@Part MultipartBody.Part image);


    @Multipart
    @POST(AppConstants.UPDATE_BUSSINESS_IMAGE_URL)
    Single<BusinessImagesResponse> updateBussinessImages(@Header("Authorization") String auth, @Query(AppKeys.BUSINESS_ID) String bussinessId, @Part ArrayList<MultipartBody.Part> image);

    @POST(AppConstants.DELETE_BUSINESS_IMAGE)
    @FormUrlEncoded
    Observable<BusinessImagesResponse> deleteBusinessImage(@Header("Authorization") String auth, @Field(AppKeys.VENDOR_DELETE_BUSINESS_IMAGES) ArrayList<String> deleteImages, @Field(AppKeys.BUSINESS_ID) String business_id);

//    @Multipart
//    @POST(AppConstants.UPDATE_EVENT_IMAGE_URL)
//    Single<UserProfilePicResponse> updateEventImage(@Header("Authorization") String auth, @Query(AppKeys.VENDOR_EVENT_ID) String bussinessId, @Part MultipartBody.Part image);

    @Multipart
    @POST(AppConstants.UPDATE_EVENT_IMAGE_URL)
    Single<BusinessImagesResponse> updateEventImage(@Header("Authorization") String auth, @Query(AppKeys.VENDOR_EVENT_ID) String bussinessId, @Part ArrayList<MultipartBody.Part> image);

    @GET(AppConstants.GET_BUSINESS_LIST)
    @Headers({"Content-Type: application/json"})
    Observable<BusinessListResponse> getBusinessList(@Header("Authorization") String auth, @Query(AppKeys.DEVICE_TOKEN) String deviceToken, @Query(AppKeys.DEVICE_TYPE) String deviceType);

    @GET(AppConstants.GET_CATEGORIES)
    @Headers({"Content-Type: application/json"})
    Observable<CategoryResponse> getCategory();

    @GET(AppConstants.GET_PRODUCT_CATEGORIES)
    @Headers({"Content-Type: application/json"})
    Observable<ProductCategoryResponse> getProductCategory();

    @POST(AppConstants.GET_CITIES)
    @Headers({"Content-Type: application/json"})
    Observable<CityResponse> getCities(@Query(AppKeys.RESION_CODE) String resion_code);

    @GET(AppConstants.GET_ALL_CATEGORIES)
    @Headers({"Content-Type: application/json"})
    Observable<CategoryAllResponse> getAllCategory();


    @GET(AppConstants.GET_COUNTRIES)
    @Headers({"Content-Type: application/json"})
    Observable<CountryResponse> getCountries();

    @POST(AppConstants.SEARCH_BUSINESS)
    @Headers({"Content-Type: application/json"})
    Observable<SearchResponse> searchBusiness(@Header("Authorization") String auth, @QueryMap Map<String, String> mutableMap);

    @POST(AppConstants.CUSTOMER_INFORM_VENDOR)
    @Headers({"Content-Type: application/json"})
    Observable<BusinessOrderDetailResponse> customerInformVendor(@Header("Authorization") String auth, @Query(AppKeys.KEY_ORDERID) String orderid,@Query(AppKeys.KEY_ALERT_STATUS) String alert_status);

    @GET(AppConstants.GET_COUNTRY_CODE)
    @Headers({"Content-Type: application/json"})
    Observable<CountrycodeResponse> getCountryCode();

    @POST(AppConstants.GET_REGION)
    @Headers({"Content-Type: application/json"})
    Observable<RegionResponse> getRegion(@Query(AppKeys.CN_CODE) String cn_code);

    @GET(AppConstants.VENDOR_STORE_CATEGORY)
    @Headers({"Content-Type: application/json"})
    Observable<StoreCategoryResponse> getStoreCategory();

    @GET(AppConstants.GET_VENDOR_SERVICE)
    @Headers({"Content-Type: application/json"})
    Observable<VendorServiceDetailResponse> getVendorServiceDetail(@Header("Authorization") String auth);

    @POST(AppConstants.GET_BUSINESS_SERVICE)
    @Headers({"Content-Type: application/json"})
    Observable<VendorServiceDetailResponse> getBusinessServiceDetail(@Header("Authorization") String auth, @Query(AppKeys.CATEGORY_ID) int category_id, @Query(AppKeys.SUBCATEGORY_ID) int subcategory_id, @Query(AppKeys.SERVICE_ID) int service_id, @Query(AppKeys.BUSINESS_ID) int business_id);


    @POST(AppConstants.VENDOR_EVENT_LISTING)//INTERNET
    @Headers({"Content-Type: application/json"})
    Observable<EventListingResponse> getEventListing(@Header("Authorization") String auth, @Query(AppKeys.BUSINESS_ID) int business_id, @Query(AppKeys.USER_TYPE) String usertype);

    @POST(AppConstants.VENDOR_UPDATE_BUSINESS_DETAIL)
    @Headers({"Content-Type: application/json"})
    Observable<BusinessDetailUpdateResponse> updateBusinessDetail(@Header("Authorization") String auth, @retrofit2.http.Body VendorBusinessDetailRequest detailRequest);


    @POST(AppConstants.VENDOR_ADD_UPDATE_EVENT)
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> addUpdateEvent(@Header("Authorization") String auth, @retrofit2.http.Body EventDetail eventDetailRequest);

    @POST(AppConstants.VENDOR_DELETE_EVENT)
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> deleteEvent(@Header("Authorization") String auth, @Query(AppKeys.SERVICE_ID) String service_id);


    @POST(AppConstants.VENDOR_DELETE_SIGHTSEEN)
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> deleteSightSeen(@Header("Authorization") String auth, @Query(AppKeys.SERVICE_ID) String service_id);

    @POST(AppConstants.VENDOR_STORE_ITEM_LISTING)
    @Headers({"Content-Type: application/json"})
    Observable<StoreListingResponse> getStoreItemListing(@Header("Authorization") String auth, @Query(AppKeys.SERVICE_ID) int service_id, @Query(AppKeys.PRODUCT_CATEGORY) int product_category);

    @Multipart
    @POST(AppConstants.VENDOR_STORE_ADD_EDIT_DELETE)
    Observable<StoreListingResponse> storeItemAddDeleteEdit(@Header("Authorization") String auth, @PartMap Map<String, RequestBody> map, @Part ArrayList<MultipartBody.Part> image);

    @Multipart
    @POST(AppConstants.VENDOR_ADD_RESTAURANT_ITEM)
    Observable<StoreListingResponse> restaurantItemAdd(@Header("Authorization") String auth, @PartMap Map<String, RequestBody> map, @Part MultipartBody.Part image);


    @POST(AppConstants.VENDOR_DELETE_RESTAURANT_ITEM)
    @Headers({"Content-Type: application/json"})
    Observable<RestaurantMenuListingResponse> deleteRestaurantItem(@Header("Authorization") String auth, @Query(AppKeys.ITEM_ID) String item_id);

    @POST(AppConstants.VENDOR_RESTAURANT_MENU_LISTING)
    @Headers({"Content-Type: application/json"})
    Observable<RestaurantMenuListingResponse> getRestaurantMenuList(@Header("Authorization") String auth, @Query(AppKeys.SERVICE_ID) int service_id);


    @POST(AppConstants.VENDOR_UPDATE_RESTAURANT_MENU_LIST)
    @Headers({"Content-Type: application/json"})
    Observable<RestaurantMenuListingResponse> updateRestaurantMenuList(@Header("Authorization") String auth, @retrofit2.http.Body UpdateMenuRequest itemAlterRequest);

    @POST(AppConstants.CUSTOMER_GET_DISPLAY_ITEM_LIST)
    @Headers({"Content-Type: application/json"})
    Observable<CustomerDisplayListResponse> getRestaurentsList(@Header("Authorization") String auth, @QueryMap Map<String, String> mutableMap);


    @POST(AppConstants.CUSTOMER_ORDER_INTIATE)
    @Headers({"Content-Type: application/json"})
    Observable<CustomerBookingResponse> initiateOrder(@Header("Authorization") String auth, @QueryMap Map<String, String> mutableMap);

    @POST(AppConstants.CUSTOMER_ORDER_INTIATE)
    @Headers({"Content-Type: application/json"})
    Observable<CustomerBookingResponse> initiateOrder(@Header("Authorization") String auth, @retrofit2.http.Body StorePlaceOrderModel storePlaceOrderModel);

    @POST(AppConstants.VENDOR_ADD_UPDATE_SIGHTSEEN)
    @Headers({"Content-Type: application/json"})
    Observable<TaxiServiceResponse> addUpdateSightSeen(@Header("Authorization") String auth, @retrofit2.http.Body PackageModel eventDetailRequest);

    @GET(AppConstants.GET_SPECIALITY)
    @Headers({"Content-Type: application/json"})
    Observable<SpecialityResponse> getSpeciality();

    @GET(AppConstants.GET_APPOINTMENT_REASON)
    @Headers({"Content-Type: application/json"})
    Observable<AppointmentReasonResponse> getAppointmentReason(@Header("Authorization") String auth);


    @POST(AppConstants.FOLLOW_UNFOLLOW)
    @Headers({"Content-Type: application/json"})
    Observable<FollowUnfollowResponse> folloUnfollow(@Header("Authorization") String auth, @Query(AppKeys.BUSINESS_ID) String business_id, @Query(AppKeys.KEY_STATUS) String status);

    @POST(AppConstants.BUSINESS_FOLLOWER_LIST)
    @Headers({"Content-Type: application/json"})
    Observable<BusinessFollowerResponse> getBusinessFollowerList(@Header("Authorization") String auth, @Query(AppKeys.BUSINESS_ID) String business_id, @Query(AppKeys.OFFSET) int offset);

    @GET(AppConstants.CUSTOMER_FOLLOWING_LIST)
    @Headers({"Content-Type: application/json"})
    Observable<BusinessFollowingResponse> getBusinessFollowingList(@Header("Authorization") String auth, @Query(AppKeys.OFFSET) int offset);

    @GET(AppConstants.CUSTOMER_ORDER_LIST)
    @Headers({"Content-Type: application/json"})
    Observable<CustomerOrderResponse> getCustomerOrderList(@Header("Authorization") String auth, @Query(AppKeys.OFFSET) int offset);

    @POST(AppConstants.BUSINESS_ORDER_LIST)
    @Headers({"Content-Type: application/json"})
    Observable<CustomerOrderResponse> getBusinessOrderList(@Header("Authorization") String auth, @Query(AppKeys.BUSINESS_ID) String business_id,
                                                           @Query(AppKeys.OFFSET) int offset, @Query(AppKeys.CATEGORY_ID) int ctegoryId,
                                                           @Query(AppKeys.SUBCATEGORY_ID) int subCtegoryId, @Query(AppKeys.ORDER_TYPE) String order_type,
                                                           @Query(AppKeys.ORDER_KEYWORD) String key,@Query(AppKeys.ORDER_FILTERTYPE) String filtertype,
                                                           @Query(AppKeys.ORDER_FROM_DATE) String fromdate,@Query(AppKeys.ORDER_TO_DATE) String todate

    );


    @POST(AppConstants.BUSINESS_ORDER_DETAIL)
    @Headers({"Content-Type: application/json"})
    Observable<BusinessOrderDetailResponse> getBusinessOrderDetail(@Header("Authorization") String auth, @Query(AppKeys.KEY_ORDERID) String orderid, @Query(AppKeys.KEY_TYPE) String type);

    @POST(AppConstants.CANCEL_ORDER_DETAIL)
    @Headers({"Content-Type: application/json"})
    Observable<CancelOrderResponse> cancelOrder(@Header("Authorization") String auth, @Query(AppKeys.KEY_ORDERID) String orderid);


    @POST(AppConstants.NOTIFICATION_LIST)
    @Headers({"Content-Type: application/json"})
    Observable<NotificationResponse> getNotificationList(@Header("Authorization") String auth, @QueryMap Map<String, String> mutableMap);

    @GET(AppConstants.GET_LANGUAGES)
    @Headers({"Content-Type: application/json"})
    Observable<LanguageResponse> getLanguage();

    @POST(AppConstants.SET_LANGUAGES)
    @Headers({"Content-Type: application/json"})
    Observable<CommonResponse> setLanguage(@Header("Authorization") String auth, @Query(AppKeys.CODE) String code);

    @POST(AppConstants.GET_VENDER_OFFER) //internet connections
    @Headers({"Content-Type: application/json"})
    Observable<VenderOffersResponse> getVenderOffers(@Header("Authorization") String auth, @Query(AppKeys.BUSINESS_ID) String businessId);

    @POST(AppConstants.CREATE_OFFER)
    @Headers({"Content-Type: application/json"})
    Observable<CommonResponse> createOffers(@Header("Authorization") String auth, @QueryMap Map<String, String> mutableMap);

    @Multipart
    @POST(AppConstants.UPLOAD_OFFER_IMAGES)
    Single<BusinessImagesResponse> uploadOfferImages(@Header("Authorization") String auth, @Query(AppKeys.OFFER_ID) String offerId, @Part ArrayList<MultipartBody.Part> image);


//    @Multipart
//    @POST(AppConstants.UPDATE_EVENT_IMAGE_URL)
//    Single<BusinessImagesResponse> updateEventImage(@Header("Authorization") String auth, @Query(AppKeys.VENDOR_EVENT_ID) String bussinessId, @Part ArrayList<MultipartBody.Part> image);

    @POST(AppConstants.SEND_OFFER)
    @Headers({"Content-Type: application/json"})
    Observable<CommonResponse> sendOffer(@Header("Authorization") String auth, @Query(AppKeys.OFFER_ID) String offerId, @Query(AppKeys.FOLLOWER_LIST) String followers_list);


    @POST(AppConstants.GET_SERVICE_TIME_SLOT)
    @Headers({"Content-Type: application/json"})
    Observable<CustomerTimeSlotResponse> getServiceTimeSLOT(@Header("Authorization") String auth, @retrofit2.http.Body CustomerTimeSlotRequest storePlaceOrderModel);

    @POST(AppConstants.VENDOR_SIGHTSEEN_LISTING)
    @Headers({"Content-Type: application/json"})
    Observable<PackageListResponse> getPackageList(@Header("Authorization") String auth, @Query(AppKeys.BUSINESS_ID) int business_id);

//    @POST(AppConstants.BUSINESS_SIGHTSEEN_LIST)
//    @Headers({"Content-Type: application/json"})
//    Observable<SightSeenListResponse> getSightSeenList(@Header("Authorization") String auth, @Query(AppKeys.BUSINESS_ID) String businessId);

    @POST(AppConstants.GET_MY_OFFER)
    @Headers({"Content-Type: application/json"})
    Observable<VenderOffersResponse> getMyOffers(@Header("Authorization") String auth, @Query(AppKeys.BUSINESS_ID) String businessId);

    @POST(AppConstants.GET_OFFER_PRICE)
    @Headers({"Content-Type: application/json"})
    Observable<ApplyOfferResponse> getOfferPrice(@Header("Authorization") String auth, @Query(AppKeys.OFFER_BUSINESS_ID) String businessId, @Query(AppKeys.KEY_COUPON) String coupon, @Query(AppKeys.KEY_OREDER_PRICE) String order_price);

    @POST(AppConstants.DELETE_EVENT_IMAGE)
    @FormUrlEncoded
    Observable<BusinessImagesResponse> deleteEventImage(@Header("Authorization") String auth, @Field(AppKeys.VENDOR_DELETE_EVENT_IMAGES) ArrayList<String> deleteImages, @Field(AppKeys.KEY_SERVICEID) String service_id);

    @GET(AppConstants.GET_CUSTOMER_BANNER)
    @Headers({"Content-Type: application/json"})
    Observable<CustomerBannerListResponse> getCustomerBannerList(@Header("Authorization") String auth);

    @POST(AppConstants.UPDATE_CHECK_IN_OUT_STATUS)
    @Headers({"Content-Type: application/json"})
    Observable<BusinessOrderDetailResponse> updateCheckInOutStatus(@Header("Authorization") String auth, @Query(AppKeys.KEY_ORDERID) String orderid, @Query(AppKeys.KEY_STATUS) String status);

    @POST(AppConstants.SUBMIT_HELP_QUERY)
    @Headers({"Content-Type: application/json"})
    Observable<BusinessOrderDetailResponse> submitHelpQuery(@Header("Authorization") String auth, @Query(AppKeys.KEY_TITLE) String title, @Query(AppKeys.KEY_MESSAGE) String message);

    @POST(AppConstants.GET_BARCODE_DETAIL)
    @Headers({"Content-Type: application/json"})
    Observable<BarCodeResponse> getBarCodeDetail(@Header("Authorization") String auth, @Query(AppKeys.KEY_BARCODE) String barcode);
}

