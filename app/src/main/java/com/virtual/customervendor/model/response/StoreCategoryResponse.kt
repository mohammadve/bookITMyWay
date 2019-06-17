package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.StoreCategoryModel


data class StoreCategoryResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<StoreCategoryModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

