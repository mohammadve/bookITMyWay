package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.VendorServiceDetailModel
import java.io.Serializable

class VendorServiceDetailResponse(
        val message: String,
        val status: Int,
        val data: VendorServiceDetailModel) : Serializable {
    override fun toString(): String {
        return "VendorServiceDetailResponse(message='$message', status=$status, data=$data)"
    }
}