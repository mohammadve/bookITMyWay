package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.ApplyOfferModel

class ApplyOfferResponse(
        val message: String,
        val status: Int,
        val data: ApplyOfferModel = ApplyOfferModel()
) {
    override fun toString(): String {
        return "VenderOffersResponse(message='$message', status=$status, data=$data)"
    }
}