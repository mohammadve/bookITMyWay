package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.*
import java.io.Serializable

class VendorRestaurantServiceModel(
        var action: String? = null,
        var image_url: String? = null,
        var business_images: ArrayList<BusinessImage> = ArrayList(),
        var business_id: Int? = null,
        var service_id: String? = null,
        var business_name: String? = null,
        var business_contactno: String? = null,
        var country_code: String? = null,
        var dial_code: String? = null,
        var business_email: String? = null,
        var business_city_id: CityModel = CityModel(),
        var business_region_id: RegionModel = RegionModel(),
        var business_address: String? = null,
        var business_category_id: String? = null,
        var business_subcategory_id: String? = null,
        var business_tax: String? = null,
        var total_table: String? = null,
        var seat_per_table: String? = null,
        var time_slot: String? = null,
        var all_day: String? = null,
        var sun: String? = null,
        var mon: String? = null,
        var tue: String? = null,
        var wed: String? = null,
        var thu: String? = null,
        var fri: String? = null,
        var sat: String? = null,
//        var is_24_hours_open: String? = null,
//        var start_time: String? = null,
//        var close_time: String? = null,
        var cost_per_guest: String? = null,
        var food_menu: ArrayList<RestaurantPriceModel> = ArrayList<RestaurantPriceModel>(),
        var drink_menu: ArrayList<RestaurantPriceModel> = ArrayList<RestaurantPriceModel>(),
        var dessert_menu: ArrayList<RestaurantPriceModel> = ArrayList<RestaurantPriceModel>(),
        var description: String? = null,


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
        return "VendorRestaurantServiceModel(action=$action, image_url=$image_url, business_images=$business_images, business_id=$business_id, service_id=$service_id, business_name=$business_name, business_contactno=$business_contactno, country_code=$country_code, dial_code=$dial_code, business_email=$business_email, business_city_id=$business_city_id, business_region_id=$business_region_id, business_address=$business_address, business_category_id=$business_category_id, business_subcategory_id=$business_subcategory_id, business_tax=$business_tax, total_table=$total_table, seat_per_table=$seat_per_table, time_slot=$time_slot, all_day=$all_day, sun=$sun, mon=$mon, tue=$tue, wed=$wed, thu=$thu, fri=$fri, sat=$sat,  cost_per_guest=$cost_per_guest, food_menu=$food_menu, drink_menu=$drink_menu, dessert_menu=$dessert_menu, description=$description)"
    }
}