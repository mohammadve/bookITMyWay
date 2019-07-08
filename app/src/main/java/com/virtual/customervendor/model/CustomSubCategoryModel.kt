package com.virtual.customervendor.model

import java.io.Serializable

class CustomSubCategoryModel(

        var name: String? = null

) : Serializable {
    override fun toString(): String {
        return "CustomSubCategoryModel(name=$name)"
    }
}