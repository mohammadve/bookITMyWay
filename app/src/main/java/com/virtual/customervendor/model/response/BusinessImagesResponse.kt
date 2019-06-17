package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.BusinessImage

data class BusinessImagesResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<BusinessImage>) {
    override fun toString(): String {
        return "BusinessImagesResponse(message='$message', status=$status, data=$data)"
    }
}

