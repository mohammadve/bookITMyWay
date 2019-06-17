package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.ProductCategoryModel


data class ProductCategoryResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<ProductCategoryModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

