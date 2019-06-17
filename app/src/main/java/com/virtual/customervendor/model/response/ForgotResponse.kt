package com.virtual.customervendor.model.response


data class ForgotResponse(
        val message: String,
        val status: Int,
        val Otp: String) {
    override fun toString(): String {
        return "ForgotResponse(message='$message', status=$status, Otp='$Otp')"
    }
}

