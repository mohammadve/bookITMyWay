package com.virtual.customervendor.model

data class ServiceData(
        var business_id: String,
        val service_id: String
         ){
    override fun toString(): String {
        return "ServiceData(business_id='$business_id', service_id='$service_id')"
    }
}
