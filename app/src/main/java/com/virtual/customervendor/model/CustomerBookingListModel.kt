package com.virtual.customervendor.model

import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.CityModel
import java.io.Serializable

class CustomerBookingListModel(
        var service_id: String? = null,
        var business_id: String? = null,
        var business_name: String? = null,
        var contact_number: String? = null,
        var business_email_id: String? = null,
        var business_image: String? = null,
        var cityname: String? = null,
        var description: String? = null
) : Serializable {
    override fun toString(): String {
        return "CustomerBookingListModel(service_id=$service_id, business_id=$business_id, business_name=$business_name, contact_number=$contact_number, business_email_id=$business_email_id, business_image=$business_image, cityname=$cityname, description=$description)"
    }
}