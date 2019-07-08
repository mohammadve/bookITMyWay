package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.ClothingCategoryModel


data class ClothingCategoryResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<ClothingCategoryModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

