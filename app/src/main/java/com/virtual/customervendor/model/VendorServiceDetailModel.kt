package com.virtual.customervendor.model

import java.io.Serializable

class VendorServiceDetailModel(

        var service_id: String? = null,
        var total_taxi: String? = null,
        var avg_seat_per_taxi: String? = null,
        var rate_per_km: String? = null,
        var region_ids: ArrayList<RegionModel> = ArrayList<RegionModel>(),
        var all_day: String? = null,
        var monday: String? = null,
        var tuesday: String? = null,
        var wednesday: String? = null,
        var thursday: String? = null,
        var friday: String? = null,
        var saturday: String? = null,
        var sunday: String? = null,
        var is_24_hours_open: String? = null,
        var start_time: String? = null,
        var close_time: String? = null,
        var description: String? = null,

        var businessData: BusinessDetail = BusinessDetail(),

        /* restaurant*/

        var time_slot: String? = null,
        var cost_per_guest: String? = null,
        var total_table: String? = null,
        var seat_per_table: String? = null,
        var food_menu: ArrayList<RestaurantPriceModel> = ArrayList(),
        var drink_menu: ArrayList<RestaurantPriceModel> = ArrayList(),
        var dessert_menu: ArrayList<RestaurantPriceModel> = ArrayList(),
        var appetizers_menu: ArrayList<RestaurantPriceModel> = ArrayList(),

        /*Health Bodycare*/
        var fees_per_visit: String? = null,
        var visiting_hours_slot: ArrayList<TimeSlotModel> = ArrayList<TimeSlotModel>(),
        var service_menu: ArrayList<ItemPriceModel> = ArrayList<ItemPriceModel>(),
        var required_person_per_hr: String? = null,
        var business_specialit: SpecialityModel = SpecialityModel(),
        var speciality: String? = null,
        /*Parking/Valet*/
        var parking_charges: String? = null,
        var parking_capacity: String? = null,

        /*Sight Seeing*/
        var package_name: String? = null,
        var total_tour_vehicle: String? = null,
        var avg_seat_per_vehicle: String? = null,
        var cost: String? = null,
        var start_location: String? = null,
        var end_location: String? = null,
        var end_time: String? = null,
        var sight_seens: ArrayList<LocationTimeModel> = ArrayList<LocationTimeModel>(),

        /*Events/Tickets*/

        /*Store/Purchse*/
        var store_category_id: String? = null,
        var stadium_address: ArrayList<StoreItemLocationModel> = ArrayList(),
        var arena_address: ArrayList<StoreItemLocationModel> = ArrayList(),
        var other_address: ArrayList<StoreItemLocationModel> = ArrayList(),
        var storecategory: String? = null

) : Serializable {
    override fun toString(): String {
        return "VendorServiceDetailModel(service_id=$service_id, total_taxi=$total_taxi, avg_seat_per_taxi=$avg_seat_per_taxi, rate_per_km=$rate_per_km, region_ids=$region_ids, all_day=$all_day, monday=$monday, tuesday=$tuesday, wednesday=$wednesday, thursday=$thursday, friday=$friday, saturday=$saturday, sunday=$sunday, is_24_hours_open=$is_24_hours_open, start_time=$start_time, close_time=$close_time, description=$description, businessData=$businessData, time_slot=$time_slot, cost_per_guest=$cost_per_guest, total_table=$total_table, seat_per_table=$seat_per_table, food_menu=$food_menu, drink_menu=$drink_menu, dessert_menu=$dessert_menu, fees_per_visit=$fees_per_visit, visiting_hours_slot=$visiting_hours_slot, service_menu=$service_menu, required_person_per_hr=$required_person_per_hr, business_specialit=$business_specialit, speciality=$speciality, parking_charges=$parking_charges, parking_capacity=$parking_capacity, package_name=$package_name, total_tour_vehicle=$total_tour_vehicle, avg_seat_per_vehicle=$avg_seat_per_vehicle, cost=$cost, start_location=$start_location, end_location=$end_location, end_time=$end_time, sight_seens=$sight_seens, store_category_id=$store_category_id, stadium_address=$stadium_address, arena_address=$arena_address, other_address=$other_address, storecategory=$storecategory)"
    }
}