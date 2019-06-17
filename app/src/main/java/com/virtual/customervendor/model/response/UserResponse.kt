package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.UserData


data class UserResponse(
        val message: String,
        val status: Int,
        val data: UserData) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

