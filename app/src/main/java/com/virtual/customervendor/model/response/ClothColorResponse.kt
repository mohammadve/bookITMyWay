package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CityModel


data class ClothColorResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<StoreClothColorModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

