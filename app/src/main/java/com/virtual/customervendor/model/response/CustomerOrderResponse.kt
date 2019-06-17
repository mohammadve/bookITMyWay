package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CustomerOrderModel

class CustomerOrderResponse(
        val message: String,
        val status: Int,
        var data: ArrayList<CustomerOrderModel>) {

    override fun toString(): String {
        return "CustomerOrderResponse(message='$message', status=$status, data=$data)"
    }
}
