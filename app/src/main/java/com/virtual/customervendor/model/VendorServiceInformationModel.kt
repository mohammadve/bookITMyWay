package com.virtual.customervendor.model

import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.CityModel
import java.io.Serializable

class VendorServiceInformationModel(
        var service_id: String ? = null,
        var total_taxi: String ? = null,
        var avg_seat_per_taxi: String ? = null,
        var rate_per_km: String ? = null,
        var region_ids: ArrayList<RegionModel> = ArrayList<RegionModel>(),
        var all_day: String ? = null,
        var monday: String ? = null,
        var tuesday: String ? = null,
        var wednesday: String ? = null,
        var thursday: String ? = null,
        var friday: String ? = null,
        var saturday: String? = null,
        var sunday: String ? = null,
        var is_24_hours_open: String? = null,
        var start_time: String? = null,
        var close_time: String? = null,
        var description: String ? = null
) : Serializable {
    override fun toString(): String {
        return "VendorServiceInformationModel(service_id=$service_id, total_taxi=$total_taxi, avg_seat_per_taxi=$avg_seat_per_taxi, rate_per_km=$rate_per_km, region_ids=$region_ids, all_day=$all_day, monday=$monday, tuesday=$tuesday, wednesday=$wednesday, thursday=$thursday, friday=$friday, saturday=$saturday, sunday=$sunday, is_24_hours_open=$is_24_hours_open, start_time=$start_time, close_time=$close_time, description=$description)"
    }
}