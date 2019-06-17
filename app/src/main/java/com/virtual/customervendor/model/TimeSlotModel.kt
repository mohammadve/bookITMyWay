package com.virtual.customervendor.model

import java.io.Serializable

class TimeSlotModel(
        var fromTime: String? = "",
        var toTime: String? = ""
       ) : Serializable {
    override fun toString(): String {
        return "TimeSlotModel(fromTime=$fromTime, toTime=$toTime)"
    }
}