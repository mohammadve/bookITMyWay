package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.RestaurantMenuModel


data class RestaurantMenuListingResponse(
        var message: String,
        var status: Int,
        var data: RestaurantMenuModel = RestaurantMenuModel()) {
    override fun toString(): String {
        return "RestaurantMenuListingResponse(message='$message', status=$status, data=$data)"
    }
}

