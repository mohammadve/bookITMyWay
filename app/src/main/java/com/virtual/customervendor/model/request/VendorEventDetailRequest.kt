package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.RegionModel
import java.io.Serializable

class VendorEventDetailRequest(
        var action: String? = null,
        var business_id: String? = null,
        var business_category_id: String? = null,
        var business_subcategory_id: String? = null,
        var event_name: String? = null,
        var ticket_price: CityModel = CityModel(),
        var total_tickets: RegionModel = RegionModel(),
        var start_date: String? = null,
        var end_date: String? = null,
        var start_time: String? = null,
        var close_time: String? = null,
        var description: String? = null
) : Serializable {
    override fun toString(): String {
        return "VendorEventDetailRequest(business_id=$business_id, business_category_id=$business_category_id, business_subcategory_id=$business_subcategory_id, event_name=$event_name, ticket_price=$ticket_price, total_tickets=$total_tickets, start_date=$start_date, end_date=$end_date, start_time=$start_time, close_time=$close_time, description=$description)"
    }
}