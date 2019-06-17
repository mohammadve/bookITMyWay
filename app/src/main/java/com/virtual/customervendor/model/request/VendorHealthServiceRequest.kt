package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.*
import java.io.Serializable

class VendorHealthServiceRequest(
        var business_id: Int? = null,
        var business_images: ArrayList<BusinessImage> = ArrayList(),
        var action: String? = null,
        var service_id: String? = null,
        var image_url: String? = null,
        var business_name: String? = null,
        var business_contactno: String? = null,
        var country_code: String? = null,
        var dial_code: String? = null,
        var business_email: String? = null,
        var business_city_id: CityModel = CityModel(),
        var business_region_id: RegionModel = RegionModel(),
        var business_specialit: SpecialityModel = SpecialityModel(),
        var speciality: String? = null,
        var business_address: String? = null,
        var business_tax: String? = null,
        var business_category_id: String? = null,
        var business_subcategory_id: String? = null,
        var fees_per_visit: String? = "",
        var time_slot: String? = null,
        var required_person_per_hr: String? = "",
        var description: String? = "",
        var all_day: String? = "0",
        var sun: String? = "0",
        var mon: String? = "0",
        var tue: String? = "0",
        var wed: String? = "0",
        var thu: String? = "0",
        var fri: String? = "0",
        var sat: String? = "0",
        var visiting_hours_slot: ArrayList<TimeSlotModel> = ArrayList<TimeSlotModel>(),
        var service_menu: ArrayList<ItemPriceModel> = ArrayList<ItemPriceModel>()
) : Serializable {
    override fun toString(): String {
        return "VendorHealthServiceRequest(business_id=$business_id, business_images=$business_images, action=$action, service_id=$service_id, image_url=$image_url, business_name=$business_name, business_contactno=$business_contactno, country_code=$country_code, dial_code=$dial_code, business_email=$business_email, business_city_id=$business_city_id, business_region_id=$business_region_id, business_specialit=$business_specialit, speciality=$speciality, business_address=$business_address, business_tax=$business_tax, business_category_id=$business_category_id, business_subcategory_id=$business_subcategory_id, fees_per_visit=$fees_per_visit, time_slot=$time_slot, required_person_per_hr=$required_person_per_hr, description=$description, all_day=$all_day, sun=$sun, mon=$mon, tue=$tue, wed=$wed, thu=$thu, fri=$fri, sat=$sat, visiting_hours_slot=$visiting_hours_slot, service_menu=$service_menu)"
    }
}