package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.UserDataLogin


data class OtpVerificationResponse(
        val message: String,
        val status: Int,
        val data: UserDataLogin) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

