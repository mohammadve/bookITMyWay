package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CountryModel


data class  CountryResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<CountryModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

