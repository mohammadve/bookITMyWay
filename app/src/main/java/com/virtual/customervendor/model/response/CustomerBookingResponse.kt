package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.BookingResponseModel


data class CustomerBookingResponse(
        val message: String,
        val status: Int,
        val data: BookingResponseModel) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

