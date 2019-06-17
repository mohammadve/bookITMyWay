package com.virtual.customervendor.model

import java.io.Serializable

class AllCategoryModel(
        var category_id: String = "",
        var subcategory_id: String = "",
        var name: String = "") : Serializable {
    override fun toString(): String {
        return "AllCategoryModel(category_id='$category_id', subcategory_id='$subcategory_id', name='$name')"
    }
}