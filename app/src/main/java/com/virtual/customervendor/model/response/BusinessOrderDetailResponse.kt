package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CustomerOrderModel
import com.virtual.customervendor.model.OrderDetailModel

class BusinessOrderDetailResponse(
        val message: String,
        val status: Int,
        var data: OrderDetailModel) {

    override fun toString(): String {
        return "CustomerOrderResponse(message='$message', status=$status, data=$data)"
    }
}
