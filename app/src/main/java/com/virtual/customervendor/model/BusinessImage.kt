package com.virtual.customervendor.model
import java.io.Serializable

data class BusinessImage(
        val url: String,
        val image: String) : Serializable {
    override fun toString(): String {
        return "BusinessImage(url='$url', image='$image')"
    }
}
