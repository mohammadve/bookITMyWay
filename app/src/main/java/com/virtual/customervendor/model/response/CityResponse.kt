package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CityModel


data class CityResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<CityModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

