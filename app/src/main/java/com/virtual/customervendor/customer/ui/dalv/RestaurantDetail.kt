package com.virtual.customervendor.model

/**
 * Created by dalvendrakumar on 10/12/18.
 */
class RestaurantDetail (var business_id:String,var service_id:String,var business_name:String,var contact_number:String,var business_email_id:String,var business_image:String,
                        var cityname:String,var total_table:String,var seat_per_table:String,var all_day:String,var monday:String,var tuesday:String,
                        var wednesday:String,var thursday:String,var friday:String,var saturday:String,var sunday:String,var is_24_hours_open:String,
                        var start_time:String,var close_time:String,var description:String,var address:String,var menuitemlist:ArrayList<RestaurantMenu>){
    override fun toString(): String {
        return "RestaurantDetail(business_id='$business_id', service_id='$service_id', business_name='$business_name', contact_number='$contact_number', business_email_id='$business_email_id', business_image='$business_image', cityname='$cityname', total_table='$total_table', seat_per_table='$seat_per_table', all_day='$all_day', monday='$monday', tuesday='$tuesday', wednesday='$wednesday', thursday='$thursday', friday='$friday', saturday='$saturday', sunday='$sunday', is_24_hours_open='$is_24_hours_open', start_time='$start_time', close_time='$close_time', description='$description',Address='$address', menuitemlist=$menuitemlist)"
    }
}

