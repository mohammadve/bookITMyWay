package com.virtual.customervendor.model

import java.io.Serializable

class RestaurantPriceModel(
        var id: String = "",
        var item_type: String = "",
        var item_name: String = "",
        var item_price: String = "",
        var item_image: ArrayList<String> = ArrayList()
) : Serializable {
    override fun toString(): String {
        return "RestaurantPriceModel(id='$id', item_type='$item_type', item_name='$item_name', item_price='$item_price', item_image=$item_image)"
    }
}