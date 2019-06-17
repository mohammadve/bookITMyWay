package com.virtual.customervendor.model

import java.io.Serializable

class ProductCategoryModel(
        var id: String = "",
        var name: String = "",
        var image: String = "",
        var newimage: String = "") : Serializable {
    override fun toString(): String {
        return "ProductCategoryModel(id='$id', name='$name', image='$image', newimage='$newimage')"
    }
}

