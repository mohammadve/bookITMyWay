package com.virtual.customervendor.model.request

import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.RegionModel
import java.io.Serializable

class VendorBusinessDetailRequest(
        var business_id: String? = null,
        var business_name: String? = null,
        var business_contactno: String? = null,
        var business_email: String? = null,
        var business_city_id: CityModel = CityModel(),
        var business_region_id: RegionModel = RegionModel(),
        var country_code: String? = null,
        var dial_code: String? = null,
        var business_tax: String? = null,
        var business_address: String? = null

) : Serializable {
    override fun toString(): String {
        return "VendorBusinessDetailRequest(business_id=$business_id, business_name=$business_name, business_contactno=$business_contactno, business_email=$business_email, business_city_id=$business_city_id, business_region_id=$business_region_id, country_code=$country_code, dial_code=$dial_code, business_tax=$business_tax, business_address=$business_address)"
    }
}