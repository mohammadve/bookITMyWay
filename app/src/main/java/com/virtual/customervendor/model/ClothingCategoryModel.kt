package com.virtual.customervendor.model

import java.io.Serializable

class ClothingCategoryModel(
        var id: String? = null,
        var name: String? = null,
        var imageurl: String? = null,
        var isSelected: Boolean? = false
) : Serializable {
    override fun toString(): String {
        return "StoreCategoryModel(id=$id, name=$name, isSelected=$isSelected, imageurl=$imageurl)"
    }
}