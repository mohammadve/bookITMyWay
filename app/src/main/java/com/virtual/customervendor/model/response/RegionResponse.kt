package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.RegionModel


data class RegionResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<RegionModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

