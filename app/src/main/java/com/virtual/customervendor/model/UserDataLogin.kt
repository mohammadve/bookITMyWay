package com.virtual.customervendor.model

data class UserDataLogin(
        val token: String? = null,
        val user: UserDetail) {
    override fun toString(): String {
        return "UserDataLogin(token=$token, user=$user)"
    }
}
