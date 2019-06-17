package com.virtual.customervendor.model

import java.io.Serializable

class VenderOffersModel (
        var offer_id: String = "",
        var title: String = "",
        var price: String = "",
        var startdate: String = "",
        var enddate: String = "",
        var description: String = "",
        var business_id: String="",
        var coupon: String="",
        var offer_images: ArrayList<VenderOfferImagesModel> = ArrayList()) : Serializable {
    override fun toString(): String {
        return "VenderOffersModel(offer_id='$offer_id', title='$title', price='$price', startdate='$startdate', enddate='$enddate', description='$description', business_id='$business_id', coupon='$coupon', offer_images=$offer_images)"
    }
}