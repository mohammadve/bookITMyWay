package com.virtual.customervendor.utills

class AppConstants {
    companion object {

        val API_KEY = "AIzaSyDCf4z9jqOn5HJfDK14Esb2yTbcqT_wGDo"
        val FROM_FORGOT = "from forgot"
        val OFFER_DATA = "offer data"
        val FROM_EDIT = "from edit"
        val FROM_ADD = "from add"
        val FROM_FOOD = "food"
        val FROM_DESSERT = "desert"
        val FROM_DRINK = "drink"
        val FROM_APPTIZERS = "appetizers"
        val FRAGMENT_ONE = 1
        val FRAGMENT_TWO = 2
        val FRAGMENT_THREE = 3
        val FRAGMENT_NUMBER = "fragment_number"
        val default_notification_channel_id = "220443497662"

        val FROM_V_TAXI_CITY = "from_v_taxi_city"
        val FROM_V_TAXI_SERVICE_AREA = "from_v_taxi_service_area"

        val FROM_REVIEW = "from review"
        val FROM_ADDDATA = "from adddata"
        val BUSINESS_DATA = "business data"
        val OREDER_DATA = "order data"
        val STORE_CAT_ID = "store_category_id"

        val STATUS_CHECK_IN = "checkin"
        val STATUS_CHECK_OUT = "checkout"

        val CAL_OFFER = "offer"
        val CAL_NEXT = "next"


        val PRIVACY_POLICY_URL = "https://bookitmyway.com/panel/privacy-policy"
        val TERMS_CONDITIONS_URL = "https://bookitmyway.com/panel/term-and-condition"
        val ABOUT_US_URL = "https://bookitmyway.com/panel/about-us"


        /*urls*/
        val DEV_BASE_URL = "https://bookitmyway.com/paneltest/api/"//tesing base url
//        val DEV_BASE_URL = "https://bookitmyway.com/panel/api/" //live base url

        //http://172.16.200.38/my-app/api
        val BASE_URL = DEV_BASE_URL
        const val CUSTOMER_UPDATE_PROFILE = "update-profile"
        const val AUTH_REGISTRATION_URL = "register"
        const val AUTH_VERIFY_OTP_URL = "verify_otp"
        const val AUTH_VERIFY_OTP_FORGOT = "verifyPasswordOtp"
        const val AUTH_RESETPASSWORD = "resetPasword"
        const val AUTH_LOGIN_URL = "login"
        const val AUTH_FORGOT_URL = "forgotpassword"
        const val GET_CATEGORIES = "get_categories"
        const val GET_ALL_CATEGORIES = "get-all-categories-list"
        const val GET_PRODUCT_CATEGORIES = "get_product_categories"
        const val GET_BUSINESS_LIST = "get-business-list"
        const val GET_CITIES = "getCities"
        const val GET_COLOR_LIST = "colours-list"
        const val GET_SIZE_LIST = "get-clothes-number"

        const val GET_COUNTRIES = "get-countries-list"
        const val SEARCH_BUSINESS = "search-business"
        const val CUSTOMER_INFORM_VENDOR = "customer-inform-vendor"
        const val GET_COUNTRY_CODE = "get-user-country-code"
        const val GET_REGION = "getRegions"
        const val GET_VENDOR_SERVICE = "get-vendor-service"
        const val GET_BUSINESS_SERVICE = "get-business-services"
        const val VENDOR_TRANS_TAXI_SERVICE_REQ = "vendor-transport-taxi-service-request"
        const val VENDOR_TRANS_SIGHTSEEING_SERVICE_REQ = "vendor-transport-sight-seen-request"
        const val VENDOR_PARKING_VALET_REQUEST = "vendor-parking-request"
        const val VENDOR_HEALTH_REQUEST = "health-service"
        const val VENDOR_RESTAURANT_REQUEST = "vendor-restaurant-service"
        const val VENDOR_EVENT_REQUEST = "vendor-event-service"
        const val VENDOR_EVENT_LISTING = "business-events-list"
        const val VENDOR_UPDATE_BUSINESS_DETAIL = "update-business-detail"
        const val VENDOR_ADD_UPDATE_EVENT = "add-vendor-event"

        const val VENDOR_STORE_CATEGORY = "get_store_categories"
        const val VENDOR_STORE_CLOTH_CAT = "cloth-category-list"
        const val VENDOR_DELETE_EVENT = "delete-event-request"
        const val VENDOR_DELETE_SIGHTSEEN = "delete-sightseen-service"
        const val VENDOR_STORE_ITEM_LISTING = "store-item-list"
        const val VENDOR_STORE_ADD_EDIT_DELETE = "add-store-item"
        const val VENDOR_ADD_RESTAURANT_ITEM = "add-restaurant-item"
        const val VENDOR_RESTAURANT_MENU_LISTING = "get-menu-list"
        const val VENDOR_DELETE_RESTAURANT_ITEM = "delete-restaurant-item"
        const val VENDOR_UPDATE_RESTAURANT_MENU_LIST = "update-restaurant-menu-item-list"
        const val CUSTOMER_GET_DISPLAY_ITEM_LIST = "getrestaurantlist"
        const val CUSTOMER_ORDER_INTIATE = "order-initiate"
        const val VENDOR_SIGHTSEEN_LISTING = "business-sightseen-list"
        //        const val BUSINESS_SIGHTSEEN_LIST = "business-sightseen-list"
        const val VENDOR_ADD_UPDATE_SIGHTSEEN = "add-sightseen-service"

        const val UPDATE_USER_IMAGE_URL = "upload_avatar"
        const val VENDOR_STORE_SERVICE_REQUEST = "vendor-store-service-request"

        const val EDIT_USER_IMAGE_URL = "upload_avatar_profile"
        const val UPDATE_BUSSINESS_IMAGE_URL = "upload_business_image"
        const val UPDATE_EVENT_IMAGE_URL = "upload_event_image"
        const val DELETE_BUSINESS_IMAGE = "delete_business_image"

        const val GET_SPECIALITY = "get-doctor-specility"
        const val FOLLOW_UNFOLLOW = "follow-business-request"

        const val GET_APPOINTMENT_REASON ="get-appointment-reason"

        const val BUSINESS_FOLLOWER_LIST = "get-business-followers-list"
        const val CUSTOMER_FOLLOWING_LIST = "get-following-business-list"
        const val CUSTOMER_ORDER_LIST = "my-order-list"
        const val BUSINESS_ORDER_LIST = "business-order-list"
        const val NOTIFICATION_LIST = "get-notification-list"
        const val BUSINESS_ORDER_DETAIL = "order-detail"

        const val CANCEL_ORDER_DETAIL = "cancel-request"

        const val GET_CUSTOMER_BANNER = "get-banner-list"
        const val GET_LANGUAGES = "get-language-list"
        const val SET_LANGUAGES = "update-user-language"
        const val GET_VENDER_OFFER = "get-vendor-offer"
        const val CREATE_OFFER = "create-offer"
        const val UPLOAD_OFFER_IMAGES = "upload-offer-image"
        const val SEND_OFFER = "send-offer"
        const val GET_SERVICE_TIME_SLOT = "get-service-timeslot"

        const val GET_MY_OFFER = "get-my-offer"
        const val GET_OFFER_PRICE = "apply-coupon"
        const val DELETE_EVENT_IMAGE = "delete_event_image"
        const val UPDATE_CHECK_IN_OUT_STATUS = "updatecheckincheckoutstatus"
        const val SUBMIT_HELP_QUERY = "submit-help-query"
        const val GET_BARCODE_DETAIL = "get-barcode-detail"


        const val ORDERSTATUS_PENDING = "0"
        const val ORDERSTATUS_COMPLETED = "1"
        const val ORDERSTATUS_CANCELLED = "3"

        const val STATUS_CANCELLED = "CANCELLED"
        const val STATUS_PENDING = "PENDING"
        const val STATUS_COMPLETED = "COMPLETED"

        const val PAY_ONLINE = "ONLINE"
        const val PAY_LATER = "PAYLATER"

        val KEY_SUCCESS = 200
        val KEY_FAILURE = 0
        const val USER_TYPE_CUSTOMER = "2"
        const val USER_TYPE_VENDOR = "3"
        const val USER_VENDOR = "vendor"
        const val USER_CUSTOMER = "customer"
        const val DEVICE_TYPE_ANDROID = "android"
        const val ACTION_EDIT = "edit"
        const val ACTION_ADD = "add"
        const val ACTION_DELETE = "delete"
        const val CURRENT_COUNTRY = "current"
        const val REGISTER_COUNTRY = "register"
        const val VENDOR_BANNER_IMG_COUNT = 50
        const val VENDOR_BANNER_IMG_COMPRESSION_SIZE = 50000
        const val BANNER_SCROLL_DELAY = 1000 as Long

        const val CAT_TRANSPORTATION = 1
        const val SUBCAT_TRANS_TAXI = 1
        const val SUBCAT_TRANS_LIMO = 2
        const val SUBCAT_TRANS_TOURBUS = 3
        const val SUBCAT_TRANS_SIGHTSEEING = 4
        const val SUBCAT_TRANS_OTHER = 5

        const val CAT_RESTAURANT_DINNIG = 2
        const val SUBCAT_RESTAURANT_DINNIG = 0

        const val CAT_HEALTH_BODYCARE = 3
        const val SUBCAT_HEALTH_DOCTOR = 6
        const val SUBCAT_HEALTH_DENTIST = 7
        const val SUBCAT_HEALTH_HAIR = 8
        const val SUBCAT_HEALTH_NAIL = 9
        const val SUBCAT_HEALTH_PHYSIO = 10
        const val SUBCAT_HEALTH_OTHER = 11

        const val CAT_PARKING_VALET = 4
        const val SUBCAT_PARKING_VALET = 0

        const val CAT_EVENT_TICKET = 5
        const val SUBCAT_EVENT_TICKET = 0

        const val CAT_STORE_SELLER = 6
        const val SUBCAT_STORE_SELLER = 0

              const val STRIPE_KEY = "pk_test_IKI2kAgTcd5D9LL4Nv7uc7ZT"; //test

        //   const val STRIPE_KEY =   "pk_live_3oBerVycojWx2X3SgDkE3Jop" //live


        const val NOTIFICATION_DATA = "notification_data"

        const val STORE_CAT_SEAT_SERVICE = "1"
        const val STORE_CAT_CLOTHING = "2"
        const val STORE_CAT_CUSTOM = "3"

        const val STATUS_I_AM_ARRIVED = "1"
        const val STATUS_I_AM_HERE = "2"
        const val STATUS_I_AM_WAITING = "3"


        const val PAYMENT_PENDING = "0"
        const val PAYMENT_PAID = "1"
        const val PAYMENT_CANCEL = "3"

    }
}
