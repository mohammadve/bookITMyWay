package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.LanguageModel
import com.virtual.customervendor.model.OfferModel
import com.virtual.customervendor.model.VenderOffersModel

class VenderOffersResponse (
        val message: String,
        val status: Int,
        val data: ArrayList<VenderOffersModel> = ArrayList<VenderOffersModel>()
) {
    override fun toString(): String {
        return "VenderOffersResponse(message='$message', status=$status, data=$data)"
    }
}