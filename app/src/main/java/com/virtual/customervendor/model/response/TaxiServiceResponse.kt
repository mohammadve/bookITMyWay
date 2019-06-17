package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.ServiceData


data class TaxiServiceResponse(
        val message: String,
        val status: Int,
        val data: ServiceData) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

