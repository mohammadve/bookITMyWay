package com.virtual.customervendor.model

import java.io.Serializable

class LocationTimeModel(
        var location: String = String(),
        var time: String = String()
) : Serializable {
    override fun toString(): String {
        return "LocationTimeModel(location=$location, time=$time)"
    }
}