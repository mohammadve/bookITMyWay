package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.NotificationModel

class NotificationResponse(
        val message: String,
        val status: Int,
        var data: ArrayList<NotificationModel>) {

    override fun toString(): String {
        return "CustomerOrderResponse(message='$message', status=$status, data=$data)"
    }
}
