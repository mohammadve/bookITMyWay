package com.virtual.customervendor.model

import java.io.Serializable

class ServiceRegionModel(
        var name: String = "",
        var rollno: String = "",
        var isSelected: Boolean = false) : Serializable {
    override fun toString(): String {
        return "ServiceRegionModel(name='$name', rollno='$rollno', isSelected=$isSelected)"
    }
}