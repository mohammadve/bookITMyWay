package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.*
import java.io.Serializable

class VendorStoreServiceRequest(
        var business_images: ArrayList<BusinessImage> = ArrayList(),
        var action: String? = null,
        var image_url: String? = null,
        var business_id: Int? = null,
        var service_id: String? = null,
        var business_name: String? = null,
        var business_contactno: String? = null,
        var country_code: String? = null,
        var dial_code: String? = null,
        var business_email: String? = null,
        var business_city_id: CityModel = CityModel(),
        var business_region_id: RegionModel = RegionModel(),
        var service_region_ids: ArrayList<CityModel> = ArrayList(),
        var business_address: String? = null,
        var business_tax: String? = null,
        var business_category_id: String? = null,
        var store_category_id: String? = null,
        var store_subcategory_ids: ArrayList<Int>? = ArrayList(),
        var store_subcategory_list: ArrayList<ClothingCategoryModel> = ArrayList(),
        var store_subcategory: ArrayList<ClothingCategoryModel>? = null,
        var store_customSubCategory: ArrayList<String>? = ArrayList(),
        var storecategory: String? = null,
        var business_subcategory_id: String? = null,
        var all_day: String? = null,
        var sun: String? = null,
        var mon: String? = null,
        var tue: String? = null,
        var wed: String? = null,
        var thu: String? = null,
        var fri: String? = null,
        var sat: String? = null,
        var start_time: String? = null,
        var close_time: String? = null,
        var stadium_address: ArrayList<StoreItemLocationModel> = ArrayList(),
        var arena_address: ArrayList<StoreItemLocationModel> = ArrayList(),
        var other_address: ArrayList<StoreItemLocationModel> = ArrayList(),
//        var store_items: ArrayList<ItemPriceStoreModel> = ArrayList(),
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
        return "VendorStoreServiceRequest(business_images=$business_images, action=$action, image_url=$image_url, business_id=$business_id, service_id=$service_id, business_name=$business_name, business_contactno=$business_contactno, country_code=$country_code, dial_code=$dial_code, business_email=$business_email, business_city_id=$business_city_id, business_region_id=$business_region_id, service_region_ids=$service_region_ids, business_address=$business_address, business_tax=$business_tax, business_category_id=$business_category_id, store_category_id=$store_category_id, store_subcategory_ids=$store_subcategory_ids, store_subcategory_list=$store_subcategory_list, store_subcategory=$store_subcategory, store_customSubCategory=$store_customSubCategory, storecategory=$storecategory, business_subcategory_id=$business_subcategory_id, all_day=$all_day, sun=$sun, mon=$mon, tue=$tue, wed=$wed, thu=$thu, fri=$fri, sat=$sat, start_time=$start_time, close_time=$close_time, stadium_address=$stadium_address, arena_address=$arena_address, other_address=$other_address, description=$description, monday_time=$monday_time, tuesday_time=$tuesday_time, wednesday_time=$wednesday_time, thursday_time=$thursday_time, friday_time=$friday_time, saturday_time=$saturday_time, sunday_time=$sunday_time, dateTime=$dateTime)"
    }
}