package com.virtual.customervendor.model

data class UserDetail(
        var id: Int,
        var name: String,
        var email: String,
        var mobile_no: String,
        var avatar: String,
        var user_type: String,
        val country_code: String,
        val cn_code: String,
        var laguage: String,
        var stripeaccountid: String,
        var is_deleted: String
) {
    override fun toString(): String {
        return "UserDetail(id=$id, name='$name', email='$email', mobile_no='$mobile_no', avatar='$avatar', user_type='$user_type', country_code='$country_code', cn_code='$cn_code', laguage='$laguage', stripeaccountid='$stripeaccountid', is_deleted='$is_deleted')"
    }
}

