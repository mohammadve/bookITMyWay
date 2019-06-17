package com.virtual.customervendor.model

import java.io.File
import java.io.Serializable

class StoreItemModel(
        var item_id: Int = -1,
        var item_name: String = "",
        var item_price: String = "",
        var itemdesc: String = "",
        var img_url: String = "",
        var img_file: File ,
        var addonone: String = "",
        var addontwo: String = "") : Serializable {
    override fun toString(): String {
        return "StoreItemModel(item_id=$item_id, item_name='$item_name', item_price='$item_price', itemdesc='$itemdesc', img_url='$img_url', img_file=$img_file, addonone='$addonone', addontwo='$addontwo')"
    }
}