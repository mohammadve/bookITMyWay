package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CustomerBookingListModel


data class CustomerDisplayListResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<CustomerBookingListModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

