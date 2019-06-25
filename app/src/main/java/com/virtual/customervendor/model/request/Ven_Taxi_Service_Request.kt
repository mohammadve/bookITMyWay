package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.*
import java.io.Serializable

class Ven_Taxi_Service_Request(
        var action: String? = null,
        var business_id: String? = null,
        var service_id: String? = null,
//        var image_url: String? = null,
        var business_images: ArrayList<BusinessImage> = ArrayList(),
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
        var total_taxi: String? = null,
        var avg_seat_per_taxi: String? = null,
        var rate_per_km: String? = null,
        var service_region_ids: ArrayList<RegionModel> = ArrayList(),
        var description: String? = null,
        var all_day: String? = null,
        var sun: String? = null,
        var mon: String? = null,
        var tue: String? = null,
        var wed: String? = null,
        var thu: String? = null,
        var fri: String? = null,
        var sat: String? = null,
        var is_24_hours_open: String? = null,
        var start_time: String? = null,
        var close_time: String? = null,

        var monday_time: ArrayList<DayAviliability.TimeSlot> = ArrayList(),
        var tuesday_time: ArrayList<DayAviliability.TimeSlot> = ArrayList(),
        var wednesday_time: ArrayList<DayAviliability.TimeSlot> = ArrayList(),
        var thursday_time: ArrayList<DayAviliability.TimeSlot> = ArrayList(),
        var friday_time: ArrayList<DayAviliability.TimeSlot> = ArrayList(),
        var saturday_time: ArrayList<DayAviliability.TimeSlot> = ArrayList(),
        var sunday_time: ArrayList<DayAviliability.TimeSlot> = ArrayList(),

//        var sunday_time: ArrayList<Slots> = ArrayList(),

        var dateTime: ArrayList<DayAviliability> = ArrayList()) : Serializable {

    override fun toString(): String {
        return "Ven_Taxi_Service_Request(action=$action, business_id=$business_id, service_id=$service_id, business_images=$business_images, business_name=$business_name, business_contactno=$business_contactno, country_code=$country_code, dial_code=$dial_code, business_email=$business_email, business_city_id=$business_city_id, business_region_id=$business_region_id, business_address=$business_address, business_tax=$business_tax, business_category_id=$business_category_id, business_subcategory_id=$business_subcategory_id, total_taxi=$total_taxi, avg_seat_per_taxi=$avg_seat_per_taxi, rate_per_km=$rate_per_km, service_region_ids=$service_region_ids, description=$description, all_day=$all_day, sun=$sun, mon=$mon, tue=$tue, wed=$wed, thu=$thu, fri=$fri, sat=$sat, is_24_hours_open=$is_24_hours_open, start_time=$start_time, close_time=$close_time)"
    }
}