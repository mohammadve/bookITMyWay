package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.LocationTimeModel
import java.io.Serializable

class Ven_Sight_Seeing_Service_Request(

        var action: String? = null,
        var business_id: String? = null,
        var service_id: String? = null,
        var image_url: String? = null,
        var business_name: String? = null,
        var business_images: ArrayList<BusinessImage> = ArrayList(),
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
        var package_name: String? = null,
        var total_tour_vehicle: String? = null,
        var avg_seat_per_vehicle: String? = null,
        var cost: String? = null,
        var service_region_ids: ArrayList<RegionModel> = ArrayList<RegionModel>(),
        var description: String? = null,
        var all_day: String? = "0",
        var sun: String? = "0",
        var mon: String? = "0",
        var tue: String? = "0",
        var wed: String? = "0",
        var thu: String? = "0",
        var fri: String? = "0",
        var sat: String? = "0",
        var startinfo: LocationTimeModel = LocationTimeModel(),
        var sight_seens: ArrayList<LocationTimeModel> = ArrayList<LocationTimeModel>(),
        var endinfo: LocationTimeModel = LocationTimeModel()
) : Serializable {
    override fun toString(): String {
        return "Ven_Sight_Seeing_Service_Request(action=$action, business_id=$business_id, service_id=$service_id, image_url=$image_url, business_name=$business_name, business_images=$business_images, business_contactno=$business_contactno, country_code=$country_code, dial_code=$dial_code, business_email=$business_email, business_city_id=$business_city_id, business_region_id=$business_region_id, business_address=$business_address, business_tax=$business_tax, business_category_id=$business_category_id, business_subcategory_id=$business_subcategory_id, package_name=$package_name, total_tour_vehicle=$total_tour_vehicle, avg_seat_per_vehicle=$avg_seat_per_vehicle, cost=$cost, service_region_ids=$service_region_ids, description=$description, all_day=$all_day, sun=$sun, mon=$mon, tue=$tue, wed=$wed, thu=$thu, fri=$fri, sat=$sat, startinfo=$startinfo, sight_seens=$sight_seens, endinfo=$endinfo)"
    }
}