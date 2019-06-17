package com.virtual.customervendor.model

import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.CityModel
import java.io.Serializable

class BookingResponseModel(
        var order_id: String? = null
) : Serializable {
    override fun toString(): String {
        return "BookingResponseModel(order_id=$order_id)"
    }
}