package com.virtual.customervendor.model

import java.io.Serializable

class LanguageModel(
        var name: String? = null,
        var code: String? = null,
        var checked: Boolean? = false): Serializable {
    override fun toString(): String {
        return "LanguageModel(name=$name, code=$code)"
    }
}