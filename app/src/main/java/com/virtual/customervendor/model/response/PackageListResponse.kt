package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.PackageModel


data class PackageListResponse(
        val message: String,
        val status: Int,
        val eventlisting: ArrayList<PackageModel> = ArrayList()) {
    override fun toString(): String {
        return "PackageListResponse(message='$message', status=$status, eventlisting=$eventlisting)"
    }
}

