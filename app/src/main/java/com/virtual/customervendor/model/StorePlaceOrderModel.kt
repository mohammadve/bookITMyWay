package com.virtual.customervendor.model

import java.io.Serializable

class StorePlaceOrderModel(
        var service_id: String = "",
        var business_id: String = "",
        var category_id: String = "",
        var subcategory_id: String = "",
        var price: String = "",
        var payment_type: String = "",
        var offer_price: String = "",
        var order_price: String = "",
        var card_token: String = "",
        var offer_id: String = "",
        var order_items: ArrayList<ItemPriceStoreModel> = ArrayList(),
        var tax: String = "",
        var total_price: String = "",
        var delivery_address1: String = "",
        var delivery_address2: String = "",
        var delivery_city: String = "",
        var delivery_region: String = "",
        var delivery_zipcode: String = "",
        var delivery_mobile_no: String = "",
        var store_category_id: String = "",
        var sevice_area: String = "",
        var section: String = "",
        var row: String = "",
        var seat_number: String = "") : Serializable {
    override fun toString(): String {
        return "StorePlaceOrderModel(service_id='$service_id', business_id='$business_id', category_id='$category_id', subcategory_id='$subcategory_id', price='$price', payment_type='$payment_type', offer_price='$offer_price', order_price='$order_price', card_token='$card_token', offer_id='$offer_id', order_items=$order_items, tax='$tax', total_price='$total_price', delivery_address1='$delivery_address1', delivery_address2='$delivery_address2', delivery_city='$delivery_city', delivery_region='$delivery_region', delivery_zipcode='$delivery_zipcode', delivery_mobile_no='$delivery_mobile_no', store_category_id='$store_category_id', sevice_area='$sevice_area', section='$section', row='$row', seat_number='$seat_number')"
    }
}