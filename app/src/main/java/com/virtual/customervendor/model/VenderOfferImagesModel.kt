package com.virtual.customervendor.model

import java.io.Serializable

class VenderOfferImagesModel( var media: String="") : Serializable {
    override fun toString(): String {
        return "VenderOfferImagesModel(media='$media')"
    }
}