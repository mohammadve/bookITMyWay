package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.BusinessFollowerModel
import com.virtual.customervendor.model.BusinessFollowingModel
import java.io.Serializable

class BusinessFollowingResponse(
        val message: String,
        val status: Int,
        var data: ArrayList<BusinessFollowingModel>) {


    override fun toString(): String {
        return "BusinessFollowingResponse(message='$message', status=$status, data=$data)"
    }
}
