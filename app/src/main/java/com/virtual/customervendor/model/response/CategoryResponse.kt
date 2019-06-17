package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CategoryModel


data class CategoryResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<CategoryModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

