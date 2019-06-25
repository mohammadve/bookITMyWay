package com.virtual.customervendor.model

import java.io.Serializable

class CustomerTimeModel(
        var slot: String? = null,
        var status: String? = null,
        var remain: Int = 0,
        var totalseat: String? = null


) : Serializable {
    override fun toString(): String {
        return "CustomerTimeModel(slot=$slot, status=$status, remain=$remain, totalseat=$totalseat)"
    }
}