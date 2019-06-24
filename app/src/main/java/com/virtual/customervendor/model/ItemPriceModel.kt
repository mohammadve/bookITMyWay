package com.virtual.customervendor.model

import java.io.Serializable

class ItemPriceModel(
        var itemName: String = "",
        var itemPrice: String = "",
        var serviceTime: String? = "",
        var isSelected: Boolean = false): Serializable {
    override fun toString(): String {
        return "ItemPriceModel(itemName='$itemName', itemPrice='$itemPrice')"
    }
}