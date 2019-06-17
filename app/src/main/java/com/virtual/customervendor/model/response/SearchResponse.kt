package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.SearchModel


data class  SearchResponse(
        val message: String,
        val status: Int,
        val data: ArrayList<SearchModel>) {
    override fun toString(): String = " message=" + message + " status=" + status + " data=" + data
}

