package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.ItemPriceModel
import java.io.Serializable

class UpdateMenuRequest(
        var service_id: String = "",
        var food_menu: ArrayList<ItemPriceModel> = ArrayList<ItemPriceModel>(),
        var drink_menu: ArrayList<ItemPriceModel> = ArrayList<ItemPriceModel>(),
        var dessert_menu: ArrayList<ItemPriceModel> = ArrayList<ItemPriceModel>()) : Serializable {
    override fun toString(): String {
        return "UpdateMenuRequest(service_id='$service_id', food_menu=$food_menu, drink_menu=$drink_menu, dessert_menu=$dessert_menu)"
    }
}