package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.RestaurantDetail

/**
 * Created by dalvendrakumar on 10/12/18.
 */
class RestaurantDetailResponse (var status:String,var message:String,var data:RestaurantDetail){
    override fun toString(): String {
        return "RestaurantDetailResponse(status='$status', message='$message', data=$data)"
    }
}
