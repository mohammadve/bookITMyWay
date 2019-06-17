package com.virtual.customervendor.model

import java.io.Serializable

class CountryCodeModel(
        var code: String? = null,
        var countryCode: String? = null,
        var name: String? = null) : Serializable {
    override fun toString(): String {
        return "CountryCodeModel(code=$code, countryCode=$countryCode, name=$name)"
    }
}