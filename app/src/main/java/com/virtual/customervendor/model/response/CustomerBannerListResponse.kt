package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.BannerModel


class CustomerBannerListResponse(
        val message: String,
        val user_name: String,
        val profile_pic: String,
        val status: Int,
        val data: ArrayList<BannerModel> = ArrayList<BannerModel>()
) {
    override fun toString(): String {
        return "CustomerBannerListResponse(message='$message', user_name='$user_name', profile_pic='$profile_pic', status=$status, data=$data)"
    }
}