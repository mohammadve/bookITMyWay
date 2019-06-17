package com.virtual.customervendor.model

import java.io.Serializable

class CountryModel(
        var name: String? = null,
        var code: String? = null) : Serializable {
    override fun toString(): String {
        return "CountryModel(name=$name, code=$code)"
    }
}