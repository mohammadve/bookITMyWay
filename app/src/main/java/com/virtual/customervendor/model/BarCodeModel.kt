package com.virtual.customervendor.model

import java.io.Serializable

class BarCodeModel(
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
        var status: String? = null,
        var customer_name: String? = null,
        var total_price: String? = null,
        var serviceinformation: OrderDetailServiceInfo = OrderDetailServiceInfo()

) : Serializable {
    override fun toString(): String {
        return "BarCodeModel(id='$id', order_id=$order_id, user_id=$user_id, category_id=$category_id, subcategory_id=$subcategory_id, business_id=$business_id, service_id=$service_id, payment_type=$payment_type, complete_status=$complete_status, datetime=$datetime, status=$status, customer_name=$customer_name, total_price=$total_price, serviceinformation=$serviceinformation)"
    }
}