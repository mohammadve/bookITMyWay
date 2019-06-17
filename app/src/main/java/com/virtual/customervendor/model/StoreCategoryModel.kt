package com.virtual.customervendor.model

import java.io.Serializable

class StoreCategoryModel(
        var id: String? = null,
        var category_name: String? = null,
        var isSelected: Boolean? = false
) : Serializable {
    override fun toString(): String {
        return "StoreCategoryModel(id=$id, category_name=$category_name, isSelected=$isSelected)"
    }
}