package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.RegionModel
import java.io.Serializable

class VendorEventServiceRequest(
        var business_images: ArrayList<BusinessImage> = ArrayList(),
        var action: String? = null,
        var business_id: String? = null,
        var service_id: String? = null,
        var bus_image_url: String? = null,
        var business_name: String = String(),
        var business_contactno: String = String(),
        var country_code: String = String(),
        var dial_code: String? = null,
        var business_email: String = String(),
        var business_city_id: CityModel = CityModel(),
        var business_region_id: RegionModel = RegionModel(),
        var business_address: String = String(),
        var business_tax: String? = null,
        var business_category_id :Int =0,
        var business_subcategory_id :Int =0,
        var event_image_url: String? = null,
        var event_name: String = "",
        var ticket_price: String = "",
        var total_tickets: String = "",
        var start_date: String = "",
        var end_date: String = "",
        var start_time: String = "",
        var close_time: String = "",
        var venue: String = "",
        var description: String = "") : Serializable {
    override fun toString(): String {
        return "VendorEventServiceRequest(business_images=$business_images, action=$action, business_id=$business_id, service_id=$service_id, bus_image_url=$bus_image_url, business_name='$business_name', business_contactno='$business_contactno', country_code='$country_code', dial_code=$dial_code, business_email='$business_email', business_city_id=$business_city_id, business_region_id=$business_region_id, business_address='$business_address', business_tax=$business_tax, business_category_id=$business_category_id, business_subcategory_id=$business_subcategory_id, event_image_url=$event_image_url, event_name='$event_name', ticket_price='$ticket_price', total_tickets='$total_tickets', start_date='$start_date', end_date='$end_date', start_time='$start_time', close_time='$close_time', venue='$venue', description='$description')"
    }
}