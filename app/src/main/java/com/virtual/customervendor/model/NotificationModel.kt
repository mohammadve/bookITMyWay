package com.virtual.customervendor.model

import java.io.Serializable

class NotificationModel(
        var type: String? = null,
        var title: String? = null,
        var text: String? = null,
        var objectid: String? = null,
        var read_status: String? = null,
        var created_date: String? = null,
        var created_time: String? = null,
        var category_id: String? = null,
        var subcategory_id: String? = null
) : Serializable {
    override fun toString(): String {
        return "NotificationModel(type=$type, title=$title, text=$text, objectid=$objectid, read_status=$read_status, created_date=$created_date, created_time=$created_time, category_id=$category_id, subcategory_id=$subcategory_id)"
    }
}