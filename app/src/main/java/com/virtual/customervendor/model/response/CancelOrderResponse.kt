package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CustomerOrderModel
import com.virtual.customervendor.model.OrderDetailModel

class CancelOrderResponse(
        val message: String,
        val status: Int,
        val order_id: String) {

    override fun toString(): String {
        return "CancelOrderResponse(message='$message', status=$status, order_id='$order_id')"
    }
}
