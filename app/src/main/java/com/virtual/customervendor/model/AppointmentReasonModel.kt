package com.virtual.customervendor.model

import java.io.Serializable

class AppointmentReasonModel(
        var reason: String? = null
      ) : Serializable {
    override fun toString(): String {
        return "AppointmentReasonModel(reason=$reason)"
    }
}