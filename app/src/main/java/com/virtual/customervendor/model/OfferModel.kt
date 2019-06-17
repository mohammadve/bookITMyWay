package com.virtual.customervendor.model

import java.io.Serializable

class OfferModel(
        var name: String = "",
        var rollno: String = "") : Serializable {
    override fun toString(): String {
        return "OfferModel(name='$name', rollno='$rollno')"
    }
}