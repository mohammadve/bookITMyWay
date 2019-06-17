package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.LanguageModel

class LanguageResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<LanguageModel> = ArrayList<LanguageModel>()
) {
    override fun toString(): String {
        return "LanguageResponse(message='$message', status=$status, data=$data)"
    }
}