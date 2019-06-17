package com.virtual.customervendor.model

import java.io.Serializable

class BusinessFollowingModel(
        var business_id: String? = null,
        var business_name: String? = null,
        var email_id: String? = null,
        var business_image: String? = null,
        var cityname: String? = null
) : Serializable {
    override fun toString(): String {
        return "BusinessFollowingModel(business_id=$business_id, business_name=$business_name, email_id=$email_id, business_image=$business_image, cityname=$cityname)"
    }
}