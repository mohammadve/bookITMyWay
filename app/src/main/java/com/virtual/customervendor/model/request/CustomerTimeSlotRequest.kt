package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.CityModel
import java.io.Serializable

class CustomerTimeSlotRequest(
        var service_id: String? = null,
        var business_id: String? = null,
        var date: String? = null,
        var category_id: String? = null,
        var subcategory_id: String? = null

) : Serializable {
    override fun toString(): String {
        return "CustomerTimeSlotRequest(service_id=$service_id, business_id=$business_id, date=$date, category_id=$category_id, subcategory_id=$subcategory_id)"
    }
}

