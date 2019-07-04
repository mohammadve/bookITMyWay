package com.virtual.customervendor.model.response

import java.io.Serializable

class StoreClothSizeModel(
        var id: Int = -1,
        var value: String = "",
        var data: ArrayList<StoreClothColorModel> =ArrayList(),
        var isSelected: Boolean = false) : Serializable {
    override fun toString(): String {
        return "StoreItemLocationModel(name=$id, name='$value')"
    }
}