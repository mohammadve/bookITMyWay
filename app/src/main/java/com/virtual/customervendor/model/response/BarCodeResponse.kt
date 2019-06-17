package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.BarCodeModel

class BarCodeResponse(
        val message: String,
        val status: Int,
        var data: BarCodeModel) {

    override fun toString(): String {
        return "CustomerOrderResponse(message='$message', status=$status, data=$data)"
    }
}
