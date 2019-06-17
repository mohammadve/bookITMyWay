package com.virtual.customervendor.model.response

import com.virtual.customervendor.model.EventDetail


data class EventListingResponse(
        val message: String,
        val status: Int,
        val eventlisting: ArrayList<EventDetail>) {
    override fun toString(): String {
        return "EventListingResponse(message='$message', status=$status, eventlisting=$eventlisting)"
    }
}

