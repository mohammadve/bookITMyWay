package com.virtual.customervendor.customer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.*
import com.virtual.customervendor.utills.AppKeys
import com.virtual.customervendor.utills.SlideAnimationUtill
import kotlinx.android.synthetic.main.fragment_booking_confirm.*

class BookingFragmentConfirm : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        var intent: Intent = Intent(context, DashBoardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        (activity as AppCompatActivity).finish()
        SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
    }

    var TAG: String = BookingFragmentConfirm::class.java.name;


    companion object {
        fun newInstance(): BookingFragmentConfirm {
            val fragment = BookingFragmentConfirm()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_confirm, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)
        initView()
    }

    fun initView() {
        if (activity is ParkingValetActivity) {
            var parkingInfo = (activity as ParkingValetActivity).parkingInfo
            var mfieldMap = (activity as ParkingValetActivity).getFieldMap()

            txt_name.text = (parkingInfo.business_name + ", " + parkingInfo.cityname)
            txt_location.text = (activity as ParkingValetActivity).orderNo
            txt_fromdate.text = mfieldMap.get(AppKeys.KEY_FROMDATE) + " | " + mfieldMap.get(AppKeys.KEY_FROMTIME)
            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE) + " | " + mfieldMap.get(AppKeys.KEY_TOTIME)
        } else if (activity is BookRideTaxiActivity) {
            var parkingInfo = (activity as BookRideTaxiActivity).customerBookingListModel
            var mfieldMap = (activity as BookRideTaxiActivity).getFieldMap()

            txt_name.text = (parkingInfo.business_name + ", " + parkingInfo.cityname)
            txt_location.text = (activity as BookRideTaxiActivity).orderNo
            txt_fromdate.text = mfieldMap.get(AppKeys.KEY_FROMDATE) + " | " + mfieldMap.get(AppKeys.KEY_FROMTIME)
//            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE) + " | " + mfieldMap.get(AppKeys.KEY_TOTIME)
        } else if (activity is TableBookingActivity) {
            var restaurantInfo = (activity as TableBookingActivity).customerBookingListModel
            var mfieldMap = (activity as TableBookingActivity).getFieldMap()

            txt_name.text = (restaurantInfo.business_name + ", " + restaurantInfo.cityname)
            txt_location.text = (activity as TableBookingActivity).orderNo
            txt_fromdate.text = mfieldMap.get(AppKeys.KEY_BOOKDATE) + " | " + mfieldMap.get(AppKeys.KEY_BOOKTIME)
//            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE) + " | " + mfieldMap.get(AppKeys.KEY_TOTIME)
        } else if (activity is BookRideLimoActivity) {
            var restaurantInfo = (activity as BookRideLimoActivity).customerBookingListModel
            var mfieldMap = (activity as BookRideLimoActivity).getFieldMap()

            txt_name.text = (restaurantInfo.business_name + ", " + restaurantInfo.cityname)
            txt_location.text = (activity as BookRideLimoActivity).orderNo
            txt_fromdate.text = mfieldMap.get(AppKeys.KEY_FROMDATE) + " | " + mfieldMap.get(AppKeys.KEY_FROMTIME)
//            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE) + " | " + mfieldMap.get(AppKeys.KEY_TOTIME)
        } else if (activity is BookRideTourBusActivity) {
            var restaurantInfo = (activity as BookRideTourBusActivity).customerBookingListModel
            var mfieldMap = (activity as BookRideTourBusActivity).getFieldMap()

            txt_name.text = (restaurantInfo.business_name + ", " + restaurantInfo.cityname)
            txt_location.text = (activity as BookRideTourBusActivity).orderNo
            txt_fromdate.text = mfieldMap.get(AppKeys.KEY_FROMDATE) + " | " + mfieldMap.get(AppKeys.KEY_FROMTIME)
//            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE) + " | " + mfieldMap.get(AppKeys.KEY_TOTIME)
        } else if (activity is BookAppointmentDoctorActivity) {
            var restaurantInfo = (activity as BookAppointmentDoctorActivity).customerBookingListModel
            var mfieldMap = (activity as BookAppointmentDoctorActivity).getFieldMap()

            txt_name.text = (restaurantInfo.business_name + ", " + restaurantInfo.cityname)
            txt_location.text = (activity as BookAppointmentDoctorActivity).orderNo
//            txt_fromdate.text = mfieldMap.get(AppKeys.KEY_FROMDATE) + " | " + mfieldMap.get(AppKeys.KEY_FROMTIME)
            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE) + " | " + mfieldMap.get(AppKeys.KEY_TOTIME)
        } else if (activity is BookAppointmentHairActivity) {
            var restaurantInfo = (activity as BookAppointmentHairActivity).customerBookingListModel
            var mfieldMap = (activity as BookAppointmentHairActivity).getFieldMap()

            txt_name.text = (restaurantInfo.business_name + ", " + restaurantInfo.cityname)
            txt_location.text = mfieldMap.get(AppKeys.KEY_TRANSACTION_ID)
//            txt_fromdate.text = mfieldMap.get(AppKeys.KEY_FROMDATE) + " | " + mfieldMap.get(AppKeys.KEY_FROMTIME)
            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE) + " | " + mfieldMap.get(AppKeys.KEY_TOTIME)
        } else if (activity is BookAppointmentMassageActivity) {
            var restaurantInfo = (activity as BookAppointmentMassageActivity).customerBookingListModel
            var mfieldMap = (activity as BookAppointmentMassageActivity).getFieldMap()
            txt_name.text = (restaurantInfo.business_name + ", " + restaurantInfo.cityname)
            txt_location.text = mfieldMap.get(AppKeys.KEY_TRANSACTION_ID)
//            txt_fromdate.text = mfieldMap.get(AppKeys.KEY_FROMDATE) + " | " + mfieldMap.get(AppKeys.KEY_FROMTIME)
            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE) + " | " + mfieldMap.get(AppKeys.KEY_TOTIME)
        } else if (activity is BookAppointmentNailActivity) {
            var restaurantInfo = (activity as BookAppointmentNailActivity).customerBookingListModel
            var mfieldMap = (activity as BookAppointmentNailActivity).getFieldMap()
            txt_name.text = (restaurantInfo.business_name + ", " + restaurantInfo.cityname)
            txt_location.text = mfieldMap.get(AppKeys.KEY_TRANSACTION_ID)
//            txt_fromdate.text = mfieldMap.get(AppKeys.KEY_FROMDATE) + " | " + mfieldMap.get(AppKeys.KEY_FROMTIME)
            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE) + " | " + mfieldMap.get(AppKeys.KEY_TOTIME)
        } else if (activity is EventsActivity) {
            var restaurantInfo = (activity as EventsActivity).customerBookingListModel
            var mfieldMap = (activity as EventsActivity).getFieldMap()

            txt_name.text = (restaurantInfo.business_name + ", " + restaurantInfo.cityname)
            txt_location.text = mfieldMap.get(AppKeys.KEY_TRANSACTION_ID)
            txt_fromdate.text = mfieldMap.get(AppKeys.KEY_FROMDATE) + " | " + mfieldMap.get(AppKeys.KEY_FROMTIME)
            txt_fromdate.visibility = View.GONE
            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE) + " | " + mfieldMap.get(AppKeys.KEY_TOTIME)
        } else if (activity is PurchaseItemsActivity) {
            var restaurantInfo = (activity as PurchaseItemsActivity).customerBookingListModel
            var mfieldMap = (activity as PurchaseItemsActivity).getFieldMap()

            txt_name.text = (restaurantInfo.business_name + ", " + restaurantInfo.cityname)
            txt_location.text = mfieldMap.get(AppKeys.KEY_TRANSACTION_ID)
            txt_fromdate.visibility = View.GONE
            txt_todate.visibility = View.GONE
        }else if (activity is BookRideSightSeenActivity) {
            var restaurantInfo = (activity as BookRideSightSeenActivity).customerBookingListModel
            var mfieldMap = (activity as BookRideSightSeenActivity).getFieldMap()
            var packageInfo = (activity as BookRideSightSeenActivity).getSightSeenDetail()

            txt_name.text = (restaurantInfo.business_name + ", " + restaurantInfo.cityname)
            txt_location.text = mfieldMap.get(AppKeys.KEY_TRANSACTION_ID)
            txt_fromdate.text = packageInfo.package_name
            txt_todate.text = mfieldMap.get(AppKeys.KEY_TODATE)
        }

    }
}