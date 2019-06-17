package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.ServiceData


data class BusinessDetailUpdateResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<ServiceData>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

