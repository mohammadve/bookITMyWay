package com.virtual.customervendor.model

import java.io.Serializable

class SearchModel(
        var busines_id: String? = null,
        var category_id: String? = null,
        var subcategory_id: String? = null,
        var business_name: String? = null,
        var service_id: String? = null) : Serializable {
    override fun toString(): String {
        return "SearchModel(busines_id=$busines_id, category_id=$category_id, subcategory_id=$subcategory_id, business_name=$business_name, service_id=$service_id)"
    }
}