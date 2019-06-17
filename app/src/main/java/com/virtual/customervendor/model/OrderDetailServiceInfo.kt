package com.virtual.customervendor.model

import java.io.Serializable

class OrderDetailServiceInfo(
        var from_date_new: String? = null,
        var expect_guest: String? = null,
        var book_date: String? = null,
        var book_time: String? = null,

        /*parking*/
        var licence_plate: String? = null,
        var from_date: String? = null,
        var from_time: String? = null,
        var to_date: String? = null,
        var to_time: String? = null,

        /*taxi*/
        var from_location: String? = null,
        var to_location: String? = null,

        /*event*/
        var number_of_person: String? = null,

        /*Sightseen*/
        var number_of_tourist: String? = null,
        var special_instruction: String? = null,
        var booking_date: String? = null,

        var barcode: String? = null


) : Serializable {
    override fun toString(): String {
        return "OrderDetailServiceInfo(from_date_new=$from_date_new, expect_guest=$expect_guest, book_date=$book_date, book_time=$book_time, licence_plate=$licence_plate, from_date=$from_date, from_time=$from_time, to_date=$to_date, to_time=$to_time, from_location=$from_location, to_location=$to_location, number_of_person=$number_of_person, number_of_tourist=$number_of_tourist, special_instruction=$special_instruction, booking_date=$booking_date, barcode=$barcode)"
    }
}