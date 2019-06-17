package com.virtual.customervendor.model

import java.io.Serializable

class SpecialityModel(
        var id: String? = null,
        var name: String? = null,
        var isSelected: Boolean = false) : Serializable {
    override fun toString(): String {
        return "RegionModel(id='$id', id='$id', isSelected=$isSelected)"
    }
}