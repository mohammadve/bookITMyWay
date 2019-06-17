package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.BusinessDetail

data class BusinessListResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<BusinessDetail>
) {
    override fun toString(): String {
        return "BusinessListResponse(message='$message', status=$status, data=$data)"
    }
}

