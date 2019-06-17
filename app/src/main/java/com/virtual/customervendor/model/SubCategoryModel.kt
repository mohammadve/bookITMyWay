package com.virtual.customervendor.model

import java.io.Serializable

class SubCategoryModel(
        var id: String = "",
        var business_subcategory_name: String = "") : Serializable {
    override fun toString(): String {
        return "CategoryModel(id='$id', business_subcategory_name='$business_subcategory_name')"
    }
}