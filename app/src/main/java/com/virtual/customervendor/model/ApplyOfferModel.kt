package com.virtual.customervendor.model

import java.io.Serializable

class ApplyOfferModel(
        var offer_id: String = "",
        var offer_price: String = "",
        var order_price: String = ""
) : Serializable {
    override fun toString(): String {
        return "ApplyOfferModel(offer_id='$offer_id', offer_price='$offer_price', order_price='$order_price')"
    }
}