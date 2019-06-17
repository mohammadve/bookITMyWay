package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.UserDataProfilePic

data class UserProfilePicResponse(
        val message: String,
        val status: Int,
        val data: UserDataProfilePic) {
    override fun toString(): String {
        return "UserProfilePicResponse(message='$message', status=$status, data=$data)"
    }
}

