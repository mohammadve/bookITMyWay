package com.virtual.customervendor.model

import java.io.Serializable

class CustomerOrderModel(
        var id: String? = null,
        var order_id: String? = null,
        var user_id: String? = null,
        var category_id: String? = null,
        var subcategory_id: String? = null,
        var business_id: String? = null,
        var service_id: String? = null,
        var payment_type: String? = null,
        var complete_status: String? = null,

        var customer_name: String? = null,

        var vendor_name: String? = null,

        var business_name: String? = null,

        var date: String? = null,
        var time: String? = null,

        var serviceinformation: OrderDetailServiceInfo = OrderDetailServiceInfo()

) : Serializable {
    override fun toString(): String {
        return "CustomerOrderModel(id=$id, order_id=$order_id, user_id=$user_id, category_id=$category_id, subcategory_id=$subcategory_id, business_id=$business_id, service_id=$service_id, payment_type=$payment_type, complete_status=$complete_status, customer_name=$customer_name, vendor_name=$vendor_name, business_name=$business_name, date=$date, time=$time, serviceinformation=$serviceinformation)"
    }
}