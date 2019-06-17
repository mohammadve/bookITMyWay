package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.BusinessFollowerModel
import java.io.Serializable

class BusinessFollowerResponse(
        val message: String,
        val status: Int,
        var data: ArrayList<BusinessFollowerModel>) {


    override fun toString(): String {
        return "BusinessFollowerResponse(message='$message', status=$status, data=$data)"
    }
}
