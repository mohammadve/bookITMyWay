package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CityModel


data class ClothSizeResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<StoreClothSizeModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

