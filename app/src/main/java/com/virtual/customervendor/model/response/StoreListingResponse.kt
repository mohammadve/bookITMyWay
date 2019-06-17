package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.ItemPriceStoreModel


data class StoreListingResponse(
        val message: String,
        val status: Int,
        val itemlisting: ArrayList<ItemPriceStoreModel>) {
    override fun toString(): String {
        return "StoreListingResponse(message='$message', status=$status, itemlisting=$itemlisting)"
    }
}

