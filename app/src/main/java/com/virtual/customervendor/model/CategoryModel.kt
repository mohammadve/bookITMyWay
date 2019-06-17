package com.virtual.customervendor.model

import java.io.Serializable

class CategoryModel(
        var id: String = "",
        var category_name: String = "") : Serializable {
    override fun toString(): String {
        return "CategoryModel(id='$id', business_category_name='$category_name')"
    }
}