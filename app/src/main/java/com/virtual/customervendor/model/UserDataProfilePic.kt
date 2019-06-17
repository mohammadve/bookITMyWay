package com.virtual.customervendor.model

data class UserDataProfilePic(
        val avatar: String) {
    override fun toString(): String {
        return "UserDataProfilePic(avatar='$avatar')"
    }
}
