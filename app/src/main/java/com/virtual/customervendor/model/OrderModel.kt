package com.virtual.customervendor.model

import java.io.Serializable

class OrderModel(
        var order_no: String? = null,
        var item_count: String? = null,
        var date: String? = null,
        var time: String? = null
) : Serializable {
    override fun toString(): String {
        return "OrderModel(order_no=$order_no, item_count=$item_count, date=$date, time=$time)"
    }
}