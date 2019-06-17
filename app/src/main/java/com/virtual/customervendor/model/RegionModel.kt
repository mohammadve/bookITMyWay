package com.virtual.customervendor.model

import java.io.Serializable

class RegionModel(
        var regionid: String? = null,
        var regionname: String? = null,
        var isSelected: Boolean = false) : Serializable {
    override fun toString(): String {
        return "RegionModel(regionid='$regionid', regionname='$regionname', isSelected=$isSelected)"
    }
}