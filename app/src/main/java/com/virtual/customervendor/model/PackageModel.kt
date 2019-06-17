package com.virtual.customervendor.model

import java.io.Serializable

class PackageModel(
        var business_category_id: String? = null,
        var service_id: String? = null,
        var business_subcategory_id: String? = null,
        var action: String? = null,
        var business_id: String? = null,
        var package_name: String? = null,
        var total_tour_vehicle: String? = null,
        var avg_seat_per_vehicle: String? = null,
        var cost: String? = null,
        var service_region_ids: ArrayList<RegionModel> = ArrayList(),
        var all_day: String? = "0",
        var sunday: String? = "0",
        var monday: String? = "0",
        var tuesday: String? = "0",
        var wednesday: String? = "0",
        var thursday: String? = "0",
        var friday: String? = "0",
        var saturday: String? = "0",
        var start_location: String? = null,
        var start_time: String? = null,
        var end_location: String? = null,
        var end_time: String? = null,
        var startinfo: LocationTimeModel = LocationTimeModel(),
        var endinfo: LocationTimeModel = LocationTimeModel(),
        var sight_seens: ArrayList<LocationTimeModel> = ArrayList<LocationTimeModel>(),
        var description: String? = null
) : Serializable {
    override fun toString(): String {
        return "PackageModel(business_category_id=$business_category_id, service_id=$service_id, business_subcategory_id=$business_subcategory_id, action=$action, business_id=$business_id, package_name=$package_name, total_tour_vehicle=$total_tour_vehicle, avg_seat_per_vehicle=$avg_seat_per_vehicle, cost=$cost, service_region_ids=$service_region_ids, all_day=$all_day, sunday=$sunday, monday=$monday, tuesday=$tuesday, wednesday=$wednesday, thursday=$thursday, friday=$friday, saturday=$saturday, start_location=$start_location, start_time=$start_time, end_location=$end_location, end_time=$end_time, startinfo=$startinfo, endinfo=$endinfo, sight_seens=$sight_seens, description=$description)"
    }
}