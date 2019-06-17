package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.UserDataLogin

data class LoginResponse(
        val message: String,
        val status: Int,
        val success: UserDataLogin
       ) {
    override fun toString(): String {
        return "LoginResponse(message='$message', status=$status, success=$success)"
    }
}

