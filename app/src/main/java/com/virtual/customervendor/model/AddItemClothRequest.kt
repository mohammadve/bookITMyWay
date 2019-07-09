package com.virtual.customervendor.model

import com.virtual.customervendor.model.response.StoreClothSizeModel
import java.io.Serializable

class AddItemClothRequest (
        var item_name: String = "",
        var isReleasingSoon: String = "",
        var releasingDate: String = "",
        var service_id: String = "",
        var action: String = "",
        var product_category: String = "",
        var product_description: String = "",
        var sizes: ArrayList<StoreClothSizeModel> =ArrayList()) : Serializable {
    override fun toString(): String {
        return "AddItemClothRequest(item_name='$item_name', isReleasingSoon='$isReleasingSoon', releasingDate='$releasingDate', store_id='$service_id', product_category='$product_category', product_description='$product_description', sizes=$sizes)"
    }
}