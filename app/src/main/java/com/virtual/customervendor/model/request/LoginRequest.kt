package com.virtual.customervendor.model.request

import java.io.Serializable

class LoginRequest(
        var mobile_no: String = "",
        var device_token: String = "",
        var device_type: String = "",
        var country_code: String = "",
        var password: String = "") : Serializable {
    override fun toString(): String {
        return "LoginRequest(mobile_no='$mobile_no', password='$password')"
    }
}


