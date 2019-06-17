package com.virtual.customervendor.model.request

import java.io.File
import java.io.Serializable

class StoreItemAlterRequest(
        var action: String = "",
        var item_id: Int = -1,
        var item_name: String = "",
        var service_id: String = "",
        var product_description: String = "",
        var addonOne: String = "",
        var addonTwo: String = "",
        var product_category: String = "",
        var item_price: String = "") : Serializable {
    override fun toString(): String {
        return "StoreItemAlterRequest(action='$action', item_id=$item_id, item_name='$item_name', service_id='$service_id', product_description='$product_description', addonOne='$addonOne', addonTwo='$addonTwo', product_category='$product_category', item_price='$item_price')"
    }
}