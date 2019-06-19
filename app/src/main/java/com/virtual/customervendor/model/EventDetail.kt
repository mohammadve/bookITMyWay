package com.virtual.customervendor.model

import java.io.Serializable

data class EventDetail(

        var action: String? = null,
        var business_category_id: String? = null,
        var business_subcategory_id: String? = null,
        var service_id: String? = null,
        var business_id: String? = null,
        var name: String? = null,
        var ticket_price: String? = null,
        var total_tickets: Int = 0,
        var ticket_booked: Int = 0,
        var start_date: String? = null,
        var end_date: String? = null,
        var start_time: String? = null,
        var close_time: String? = null,
        var description: String? = null,
        var venue: String = "",
        var event_images: ArrayList<BusinessImage> = ArrayList()) : Serializable {
    override fun toString(): String {
        return "EventDetail(action=$action, business_category_id=$business_category_id, business_subcategory_id=$business_subcategory_id, service_id=$service_id, business_id=$business_id, name=$name, ticket_price=$ticket_price, total_tickets=$total_tickets, ticket_booked=$ticket_booked, start_date=$start_date, end_date=$end_date, start_time=$start_time, close_time=$close_time, description=$description, venue='$venue', event_images=$event_images)"
    }
}
