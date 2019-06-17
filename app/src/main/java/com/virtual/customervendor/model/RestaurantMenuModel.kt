package com.virtual.customervendor.model

import java.io.Serializable

class RestaurantMenuModel(
        var food_menu: ArrayList<RestaurantPriceModel> = ArrayList(),
        var drink_menu: ArrayList<RestaurantPriceModel> = ArrayList(),
        var dessert_menu: ArrayList<RestaurantPriceModel> = ArrayList(),
        var appetizers_menu: ArrayList<RestaurantPriceModel> = ArrayList()
) : Serializable {
    override fun toString(): String {
        return "RestaurantMenuModel(food_menu=$food_menu, drink_menu=$drink_menu, dessert_menu=$dessert_menu, appetizers_menu=$appetizers_menu)"
    }
}