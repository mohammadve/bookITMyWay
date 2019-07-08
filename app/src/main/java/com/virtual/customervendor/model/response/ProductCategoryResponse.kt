package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.ProductCategoryModel


data class ProductCategoryResponse(
        val message: String,
        val status: Int,
        val store_category_id: Int,
        val data: ArrayList<ProductCategoryModel>) {
    override fun toString(): String {
        return "ProductCategoryResponse(message='$message', status=$status, store_category_id=$store_category_id, data=$data)"
    }
}

