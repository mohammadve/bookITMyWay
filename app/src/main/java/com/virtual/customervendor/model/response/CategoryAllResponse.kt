package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.AllCategoryModel


data class CategoryAllResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<AllCategoryModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

