package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.CountryCodeModel
import java.io.Serializable


data class CountrycodeResponse(
        var message: String? = null,
        var status: Int? = 0,
        var data: CountryCodeModel = CountryCodeModel()) : Serializable {
    override fun toString(): String {
        return "CountrycodeResponse(message=$message, status=$status, data=$data)"
    }
}

