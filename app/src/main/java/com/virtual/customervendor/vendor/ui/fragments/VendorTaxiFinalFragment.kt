package com.virtual.customervendor.vendor.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.TermsAndConditionActivityVendor
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.DayAviliability
import com.virtual.customervendor.model.OfferModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.request.Ven_Taxi_Service_Request
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorLimoActivity
import com.virtual.customervendor.vendor.ui.activity.VendorTaxiActivity
import com.virtual.customervendor.vendor.ui.activity.VendorTourBusActivity
import kotlinx.android.synthetic.main.fragment_taxi_final_vendor.*
import java.io.File
import kotlin.collections.ArrayList

class VendorTaxiFinalFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var datetime: String? = null
    var manager: FragmentManager? = null
    val list: ArrayList<OfferModel> = java.util.ArrayList()
    var count: Int = 0
    var TAG: String = VendorTaxiFinalFragment::class.java.name
    var taxi_Service_Request = Ven_Taxi_Service_Request()
    var imageFile: ArrayList<File>? = null
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                if (chk_term.isChecked) {
                    if (activity is VendorTaxiActivity) {
                        (activity as VendorTaxiActivity).hitApi(taxi_Service_Request)
                    } else if (activity is VendorLimoActivity) {
                        (activity as VendorLimoActivity).hitApi(taxi_Service_Request)
                    } else if (activity is VendorTourBusActivity) {
                        (activity as VendorTourBusActivity).hitApi(taxi_Service_Request)
                    }
                } else {
                    UiValidator.displayMsgSnack(nest, activity!!, activity!!.resources.getString(R.string.check_tern))
                }
            }
            R.id.btn_edit -> {
                if (activity is VendorTaxiActivity)
                    (activity as VendorTaxiActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
                else if (activity is VendorLimoActivity)
                    (activity as VendorLimoActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
                else if (activity is VendorTourBusActivity)
                    (activity as VendorTourBusActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
            }
            R.id.txt_chk -> {
                startActivity(Intent(activity, TermsAndConditionActivityVendor::class.java))
            }
        }
    }

    companion object {
        fun newInstance(from: String): VendorTaxiFinalFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorTaxiFinalFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun putAllDataToFieldMap() {
        if (activity is VendorTaxiActivity) {
            taxi_Service_Request = (context as VendorTaxiActivity).getTaxiFieldPojo()
            imageFile = (context as VendorTaxiActivity).imageFiles
        } else if (activity is VendorLimoActivity) {
            taxi_Service_Request = (context as VendorLimoActivity).getLimoFieldPojo()
//            imageFile = (context as VendorLimoActivity).imageFile
        } else if (activity is VendorTourBusActivity) {
            taxi_Service_Request = (context as VendorTourBusActivity).getTourBusFieldPojo()
//            imageFile = (context as VendorTourBusActivity).imageFile
        }
        try {
            setData(taxi_Service_Request)
            AppLog.e(TAG, taxi_Service_Request.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setData(taxi_Service_Request: Ven_Taxi_Service_Request) {
        ed_bname.setText(taxi_Service_Request.business_name)
        ed_bcontact.setText(taxi_Service_Request.dial_code + " " + taxi_Service_Request.business_contactno)
        ed_email.setText(taxi_Service_Request.business_email)
        ed_city.setText(taxi_Service_Request.business_city_id.cityname)
        ed_area.setText(taxi_Service_Request.business_region_id.regionname)
        ed_address.setText(taxi_Service_Request.business_address)
        ed_taxicount.setText(taxi_Service_Request.total_taxi)
        ed_seat.setText(taxi_Service_Request.avg_seat_per_taxi)
        ed_rate.setText(taxi_Service_Request.rate_per_km)

        if (taxi_Service_Request.business_tax != null && !taxi_Service_Request.business_tax.equals("")) {
            hint_tax.visibility = View.VISIBLE
            ed_tax.visibility = View.VISIBLE
            ed_tax.setText(taxi_Service_Request.business_tax)
        } else {
            hint_tax.visibility = View.GONE
            ed_tax.visibility = View.GONE
        }

        var data = StringBuilder()
        for (regionModel: RegionModel in taxi_Service_Request.service_region_ids) {
            if (data.length > 0) {
                data.append(", ")
            }
            data.append(regionModel.regionname)
        }
        ed_service.setText(data.toString())

//        if (AppUtils.getStatusBoolean(taxi_Service_Request.all_day)) {
//            chk_alldays.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.all_day)
//            cons_day.visibility = View.GONE
//        } else {
//            chk_alldays.visibility = View.GONE
//            chk_monday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.mon)
//            chk_tuesday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.tue)
//            chk_wednesday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.wed)
//            chk_thursday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.thu)
//            chk_friday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.fri)
//            chk_saturday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.sat)
//            chk_sunday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.sun)
//        }

//        if (AppUtils.getStatusBoolean(taxi_Service_Request.is_24_hours_open)) {
//            ed_starttime.visibility = View.GONE
//            txt_starttime.visibility = View.GONE
//            ed_closingtime.visibility = View.GONE
//            txt_closingtime.visibility = View.GONE
//            chk_24time.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.is_24_hours_open)
//        } else {
//            chk_24time.visibility = View.GONE
//            ed_starttime.setText(taxi_Service_Request.start_time)
//            ed_closingtime.setText(taxi_Service_Request.close_time)
//        }
        ed_desc.setText(taxi_Service_Request.description)

        txtSlotsMon.setText(getSlots(taxi_Service_Request.dateTime.get(0).slots))
        txtSlotsTue.setText(getSlots(taxi_Service_Request.dateTime.get(1).slots))
        txtSlotsWed.setText(getSlots(taxi_Service_Request.dateTime.get(2).slots))
        txtSlotsThu.setText(getSlots(taxi_Service_Request.dateTime.get(3).slots))
        txtSlotsFri.setText(getSlots(taxi_Service_Request.dateTime.get(4).slots))
        txtSlotsSat.setText(getSlots(taxi_Service_Request.dateTime.get(5).slots))
        txtSlotsSun.setText(getSlots(taxi_Service_Request.dateTime.get(6).slots))

        if (activity is VendorTaxiActivity)
            initViewPager((activity as VendorTaxiActivity).mResults, false)
        else if (activity is VendorLimoActivity)
            initViewPager((activity as VendorLimoActivity).mResults, false)
        else if (activity is VendorTourBusActivity)
            initViewPager((activity as VendorTourBusActivity).mResults, false)
    }

    private fun getSlots(slots: ArrayList<DayAviliability.TimeSlot>): String {
        val builder = StringBuilder()

        for (timeSlots in slots) {
            if(timeSlots.startTime.length>0 && timeSlots.stopTime.length>0  )
                builder.append(timeSlots.startTime+" to "+timeSlots.stopTime+"\n")
        }
        var str:String=builder.toString()

        if(str.equals(""))
            str="none"
        else
            str=str.substring(0,str.length-1)

        return str
    }

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(activity, mResults, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_taxi_final_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        putAllDataToFieldMap()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        btn_edit.setOnClickListener(this)
        txt_chk.setOnClickListener(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }
}