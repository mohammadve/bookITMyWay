package com.virtual.customervendor.vendor.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.virtual.customervendor.model.request.VendorParkingRequest
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorParkingValetActivity
import kotlinx.android.synthetic.main.fragment_parking_review_vendor.*
import java.io.File

class VendorParkingReviewFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var TAG: String = VendorParkingReviewFragment::class.java.name
    var vendorRequest = VendorParkingRequest()
    var imageFile: File? = null


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                if (chk_term.isChecked) {
                    if (activity is VendorParkingValetActivity)
                        (activity as VendorParkingValetActivity).hitApi(vendorRequest)
                } else {
                    UiValidator.displayMsgSnack(nest, activity!!, activity!!.resources.getString(R.string.check_tern))
                }
            }
            R.id.btn_edit -> {
                if (activity is VendorParkingValetActivity)
                    (activity as VendorParkingValetActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
            }
            R.id.txt_chk -> {
                startActivity(Intent(activity, TermsAndConditionActivityVendor::class.java))
            }
        }
    }

    companion object {
        fun newInstance(from: String): VendorParkingReviewFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorParkingReviewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun putAllDataToFieldMap() {
        if (activity is VendorParkingValetActivity)
            vendorRequest = (context as VendorParkingValetActivity).getValetRequestPojo()
        try {
            setData(vendorRequest)
            AppLog.e(TAG, vendorRequest.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setData(mfieldMap: VendorParkingRequest) {
        ed_bname.setText(mfieldMap.business_name)
        ed_bcontact.setText(mfieldMap.dial_code + " " + mfieldMap.business_contactno)
        ed_email.setText(mfieldMap.business_email)
        ed_city.setText(mfieldMap.business_city_id.cityname)
        ed_area.setText(mfieldMap.business_region_id.regionname)
        ed_address.setText(mfieldMap.business_address)
        ed_taxicount.setText(mfieldMap.parking_capacity)
        ed_rate.setText(mfieldMap.parking_charges)

        if (mfieldMap.business_tax != null && !mfieldMap.business_tax.equals("")) {
            hint_tax.visibility = View.VISIBLE
            ed_tax.visibility = View.VISIBLE
            ed_tax.setText(mfieldMap.business_tax)
        } else {
            hint_tax.visibility = View.GONE
            ed_tax.visibility = View.GONE
        }

//        if (AppUtils.getStatusBoolean(mfieldMap.all_day)) {
//            chk_alldays.isChecked = true
//            cons_day.visibility = View.GONE
//        } else {
//            chk_alldays.visibility = View.GONE
//            chk_monday.isChecked = AppUtils.getStatusBoolean(mfieldMap.mon)
//            chk_tuesday.isChecked = AppUtils.getStatusBoolean(mfieldMap.tue)
//            chk_wednesday.isChecked = AppUtils.getStatusBoolean(mfieldMap.wed)
//            chk_thursday.isChecked = AppUtils.getStatusBoolean(mfieldMap.thu)
//            chk_friday.isChecked = AppUtils.getStatusBoolean(mfieldMap.fri)
//            chk_saturday.isChecked = AppUtils.getStatusBoolean(mfieldMap.sat)
//            chk_sunday.isChecked = AppUtils.getStatusBoolean(mfieldMap.sun)
//        }

//        if (AppUtils.getStatusBoolean(mfieldMap.is_24_hours_open)) {
//            ed_starttime.visibility = View.GONE
//            ed_closingtime.visibility = View.GONE
//            txt_starttime.visibility = View.GONE
//            txt_closingtime.visibility = View.GONE
//            chk_24time.isChecked = true
//        } else {
//            chk_24time.visibility = View.GONE
//            ed_starttime.setText(mfieldMap.start_time)
//            ed_closingtime.setText(mfieldMap.close_time)
//        }

        ed_desc.setText(mfieldMap.description)

        txtSlotsMon.setText(getSlots(mfieldMap.dateTime.get(0).slots))
        txtSlotsTue.setText(getSlots(mfieldMap.dateTime.get(1).slots))
        txtSlotsWed.setText(getSlots(mfieldMap.dateTime.get(2).slots))
        txtSlotsThu.setText(getSlots(mfieldMap.dateTime.get(3).slots))
        txtSlotsFri.setText(getSlots(mfieldMap.dateTime.get(4).slots))
        txtSlotsSat.setText(getSlots(mfieldMap.dateTime.get(5).slots))
        txtSlotsSun.setText(getSlots(mfieldMap.dateTime.get(6).slots))

        initViewPager((activity as VendorParkingValetActivity).mResults, false)
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_parking_review_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageFile = (context as VendorParkingValetActivity).imageFile
        putAllDataToFieldMap()

        initView(view)
    }

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        var homeSliderAdapter = HomeSliderAdapter(activity, mResults, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
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