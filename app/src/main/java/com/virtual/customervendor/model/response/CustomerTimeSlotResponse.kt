package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CustomerTimeModel
import java.io.Serializable

class CustomerTimeSlotResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<CustomerTimeModel>) : Serializable {
    override fun toString(): String {
        return "VendorServiceDetailResponse(message='$message', status=$status, data=$data)"
    }
}