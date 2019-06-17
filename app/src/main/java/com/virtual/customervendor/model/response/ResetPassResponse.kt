package com.virtual.customervendor.model.response


data class ResetPassResponse(
        val message: String,
        val token: String,
        val status: Int) {
    override fun toString(): String {
        return "ResetPassResponse(message='$message', token='$token', status=$status)"
    }
}

