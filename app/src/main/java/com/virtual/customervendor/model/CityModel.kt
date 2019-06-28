package com.virtual.customervendor.model

import java.io.Serializable

class CityModel(
        var cityid: String? = null,
        var cityname: String? = null,
var isSelected: Boolean = false) : Serializable {
    override fun toString(): String {
        return "CityModel(cityid='$cityid', cityname='$cityname')"
    }
}