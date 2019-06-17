package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.AppointmentReasonModel


data class AppointmentReasonResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<AppointmentReasonModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}