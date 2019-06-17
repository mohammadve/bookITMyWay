package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.*
import java.io.Serializable

class VendorParkingRequest(
        var business_images: ArrayList<BusinessImage> = ArrayList(),
        var business_id: Int? = null,
        var action: String? = null,
        var image_url: String? = null,
        var service_id: String? = null,
        var business_name: String? = null,
        var business_contactno: String? = null,
        var country_code: String? = null,
        var dial_code: String? = null,
        var business_email: String? = null,
        var business_city_id: CityModel = CityModel(),
        var business_region_id: RegionModel = RegionModel(),
        var business_address: String? = null,
        var business_tax: String? = null,
        var business_category_id: String? = null,
        var business_subcategory_id: String? = null,
        var parking_charges: String? = "",
        var parking_capacity: String? = "",
        var description: String? = "",
        var all_day: String? = "0",
        var sun: String? = "0",
        var mon: String? = "0",
        var tue: String? = "0",
        var wed: String? = "0",
        var thu: String? = "0",
        var fri: String? = "0",
        var sat: String? = "0",
        var is_24_hours_open: String? = null,
        var start_time: String? = null,
        var close_time: String? = null

) : Serializable {
    override fun toString(): String {
        return "VendorParkingRequest(business_images=$business_images, business_id=$business_id, action=$action, image_url=$image_url, service_id=$service_id, business_name=$business_name, business_contactno=$business_contactno, country_code=$country_code, dial_code=$dial_code, business_email=$business_email, business_city_id=$business_city_id, business_region_id=$business_region_id, business_address=$business_address, business_tax=$business_tax, business_category_id=$business_category_id, business_subcategory_id=$business_subcategory_id, parking_charges=$parking_charges, parking_capacity=$parking_capacity, description=$description, all_day=$all_day, sun=$sun, mon=$mon, tue=$tue, wed=$wed, thu=$thu, fri=$fri, sat=$sat, is_24_hours_open=$is_24_hours_open, start_time=$start_time, close_time=$close_time)"
    }
}