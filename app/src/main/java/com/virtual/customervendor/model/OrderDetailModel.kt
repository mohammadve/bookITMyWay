package com.virtual.customervendor.model

import java.io.Serializable

class OrderDetailModel(
        var id: String = String(),
        var order_id: String? = null,
        var user_id: String? = null,
        var category_id: String? = null,
        var subcategory_id: String? = null,
        var business_id: String? = null,
        var service_id: String? = null,
        var payment_type: String? = null,
        var complete_status: String? = null,
        var datetime: String? = null,
        var date: String? = null,
        var time: String? = null,
        var price: String? = null,
        var status: String? = null,
        var checkin: String? = null,
        var checkindatetime: String? = null,
        var checkout: String? = null,
        var checkoutdatetime: String? = null,
        var category_name: String? = null,
        var subcategory_name: String? = null,
        var customer_name: String? = null,

        var vendor_name: String? = null,

        var vendor_email: String? = null,
        var vendor_phone: String? = null,
        var vendor_countrycode: String? = null,

        var customer_countrycode: String? = null,
        var customer_email: String? = null,
        var customer_phone: String? = null,

        var vendor_alert: String? = String(),

        var business_name: String? = null,

        var serviceinformation : OrderDetailServiceInfo = OrderDetailServiceInfo(),
        var storeItem: ArrayList<StoreCartModel> = ArrayList()
) : Serializable {
    override fun toString(): String {
        return "OrderDetailModel(id='$id', order_id=$order_id, user_id=$user_id, category_id=$category_id, subcategory_id=$subcategory_id, business_id=$business_id, service_id=$service_id, payment_type=$payment_type, complete_status=$complete_status, datetime=$datetime, date=$date, time=$time, price=$price, status=$status, checkin=$checkin, checkindatetime=$checkindatetime, checkout=$checkout, checkoutdatetime=$checkoutdatetime, category_name=$category_name, subcategory_name=$subcategory_name, customer_name=$customer_name, vendor_name=$vendor_name, vendor_email=$vendor_email, vendor_phone=$vendor_phone, vendor_countrycode=$vendor_countrycode, customer_countrycode=$customer_countrycode, customer_email=$customer_email, customer_phone=$customer_phone, vendor_alert=$vendor_alert, business_name=$business_name, serviceinformation=$serviceinformation, storeItem=$storeItem)"
    }
}