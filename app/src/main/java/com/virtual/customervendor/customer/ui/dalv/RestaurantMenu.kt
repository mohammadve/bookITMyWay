package com.virtual.customervendor.model

/**
 * Created by dalvendrakumar on 7/12/18.
 */
class RestaurantMenu(var itemName:String,var itemPrice: String,var menu_type : String){
    override fun toString(): String {
        return "RestaurantMenu(itemName='$itemName', itemPrice='$itemPrice', menu_type='$menu_type')"
    }
}