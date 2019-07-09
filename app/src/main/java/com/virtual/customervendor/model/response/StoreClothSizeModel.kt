package com.virtual.customervendor.model.response

import java.io.Serializable

class StoreClothSizeModel(
        var size_id: Int = -1,
        var value: String = "",
        var variants: ArrayList<StoreClothColorModel> =ArrayList(),
        var isSelected: Boolean = false) : Serializable {
    override fun toString(): String {
        return "StoreItemLocationModel(name=$size_id, name='$value')"
    }
}