package com.virtual.customervendor.model.response

import java.io.Serializable

class StoreClothColorModel(
        var id: Int = -1,
        var name: String = "",
        var quantity: String = "",
        var price: String = "",
        var bookingPrice: String = "",


        var isSelected: Boolean = false) : Serializable {
    override fun toString(): String {
        return "StoreItemLocationModel(name=$id, name='$name')"
    }
}