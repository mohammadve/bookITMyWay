package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.SpecialityModel

data class SpecialityResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<SpecialityModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}