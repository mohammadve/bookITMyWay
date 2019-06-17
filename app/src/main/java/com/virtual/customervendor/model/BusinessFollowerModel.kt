package com.virtual.customervendor.model

import java.io.Serializable

class BusinessFollowerModel(
        var user_id: String? = null,
        var name: String? = null,
        var email: String? = null,
        var avatar: String? = null,
        var mobile_no: String? = null,
        var checked: Boolean? = false
) : Serializable {
    override fun toString(): String {
        return "BusinessFollowerModel(name=$name, email=$email, avatar=$avatar, mobile_no=$mobile_no)"
    }
}