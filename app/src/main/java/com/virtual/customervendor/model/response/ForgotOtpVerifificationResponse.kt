package com.virtual.customervendor.model.response


data class ForgotOtpVerifificationResponse(
        val message: String,
        val status: Int,
        val user_id: String) {
    override fun toString(): String {
        return "ForgotOtpVerifificationResponse(message='$message', status=$status, user_id='$user_id')"
    }
}

