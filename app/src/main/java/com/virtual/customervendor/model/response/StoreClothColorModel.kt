package com.virtual.customervendor.model.response

import java.io.Serializable

class StoreClothColorModel(
        var color_id: Int = -1,
        var name: String = "",
        var quantity: String = "",
        var price: String = "",
        var pre_order_price: String = "",


        var isSelected: Boolean = false) : Serializable {
    override fun toString(): String {
        return "StoreItemLocationModel(name=$color_id, name='$name')"
    }
}