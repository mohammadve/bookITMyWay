package com.virtual.customervendor.model

import java.io.Serializable

class StoreItemLocationModel(
        var item_id: Int = -1,
        var location_name: String = ""
) : Serializable {
    override fun toString(): String {
        return "StoreItemLocationModel(item_id=$item_id, location_name='$location_name')"
    }
}