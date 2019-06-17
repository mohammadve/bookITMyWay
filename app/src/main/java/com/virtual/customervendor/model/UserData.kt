package com.virtual.customervendor.model

data class UserData(
        var imageUrl: String? = "",
        val name: String,
        val email: String,
        val mobile_no: String,
        val user_type: String,
        val password: String,
        val device_type: String,
        val country_code: String,
        val cn_code: String,
        val token: String,
        val systemotp: String) {
    override fun toString(): String {
        return "UserData(imageUrl=$imageUrl, name='$name', email='$email', mobile_no='$mobile_no', user_type='$user_type', password='$password', device_type='$device_type', country_code='$country_code', token='$token', systemotp='$systemotp')"
    }
}
