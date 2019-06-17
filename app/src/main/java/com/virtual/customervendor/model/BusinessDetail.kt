package com.virtual.customervendor.model

import java.io.Serializable

data class BusinessDetail(
        var category_id: Int? = null,
        var subcategory_id: Int? = null,
        var service_id: Int? = null,
        var business_id: Int? = null,
        var business_name: String? = null,
        var contact_number: String? = null,
        var country_code: String? = null,
        var dial_code:String? = null,
        var email_id: String? = null,
        var city_id: Int? = null,
        var region_id: Int? = null,
        var business_tax: String? = null,
        var address: String? = null,
        var cities: CityModel = CityModel(),
        var regions: RegionModel = RegionModel(),
        var business_image: String? = null,
        var followBusiness: String? = null,
        var business_images: ArrayList<BusinessImage> = ArrayList(),
        var is_approved: Int? = null) : Serializable {
    override fun toString(): String {
        return "BusinessDetail(category_id=$category_id, subcategory_id=$subcategory_id, service_id=$service_id, business_id=$business_id, business_name=$business_name, contact_number=$contact_number, country_code=$country_code, dial_code=$dial_code, email_id=$email_id, city_id=$city_id, region_id=$region_id, business_tax=$business_tax, address=$address, cities=$cities, regions=$regions, business_image=$business_image, followBusiness=$followBusiness, business_images=$business_images, is_approved=$is_approved)"
    }
}
