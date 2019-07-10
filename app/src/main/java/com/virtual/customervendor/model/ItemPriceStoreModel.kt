package com.virtual.customervendor.model

import java.io.Serializable

class ItemPriceStoreModel(
        var item_id: Int = -1,
        var item_name: String = "",
        var item_description: String = "",
        var product_category: String = "",
        var item_image: ArrayList<String> = ArrayList(),
        var add_on_one : String = "",
        var add__one : ArrayList<String> = ArrayList(),
        var add_on_two: String = "",
        var quantity: Int = 1,
        var item_price: String = "",
        var isReleasingSoon: String = "",
        var releasingDate: String = "",
        var pre_order_price: String = "") : Serializable {
    override fun toString(): String {
        return "ItemPriceStoreModel(item_id=$item_id, item_name='$item_name', item_description='$item_description', product_category='$product_category', item_image=$item_image, add_on_one='$add_on_one', add__one=$add__one, add_on_two='$add_on_two', quantity=$quantity, item_price='$item_price', isReleasingSoon='$isReleasingSoon', releasingDate='$releasingDate', pre_order_price='$pre_order_price')"
    }
}