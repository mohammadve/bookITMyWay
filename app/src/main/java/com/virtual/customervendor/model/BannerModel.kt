package com.virtual.customervendor.model

import java.io.Serializable

class BannerModel(
        var url: String? = null,
        var link: String? = null
      ): Serializable {
    override fun toString(): String {
        return "BannerModel(url=$url, link=$link)"
    }
}