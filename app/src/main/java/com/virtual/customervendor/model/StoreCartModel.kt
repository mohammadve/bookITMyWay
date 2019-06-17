package com.virtual.customervendor.model

import java.io.Serializable

class StoreCartModel(
        var item_id: String? = null,
        var item_name: String? = null,
        var quantity: String? = null,
        var price: String? = null) : Serializable {
    override fun toString(): String {
        return "RegionModel(item_name='$item_name', quantity='$quantity', price=$price)"
    }


}