package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.VendorServiceDetailModel
import java.io.Serializable

class SightSeenListResponse (
        val message: String,
        val status: Int,
        val eventlisting: ArrayList<VendorServiceDetailModel> = ArrayList()) {
    override fun toString(): String {
        return "SightSeenListResponse(message='$message', status=$status, eventlisting=$eventlisting)"
    }
}