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
import com.virtual.customervendor.model.request.VendorRestaurantServiceModel
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorRestaurantActivity
import kotlinx.android.synthetic.main.fragment_restaurant_review_vendor.*
import java.io.File
import java.util.*

class VendorRestaurantReviewFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var TAG: String = VendorRestaurantReviewFragment::class.java.name
    var apiInterface: ApiInterface? = null
    var vendorRestaurantServiceModel = VendorRestaurantServiceModel()
    var imageFile: File? = null
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                if (chk_term.isChecked) {
                    if (activity is VendorRestaurantActivity)
                        (activity as VendorRestaurantActivity).hitApi()
                } else {
                    UiValidator.displayMsgSnack(nest, activity!!, activity!!.resources.getString(R.string.check_tern))
                }
            }
            R.id.btn_edit -> {
                if (activity is VendorRestaurantActivity)
                    (activity as VendorRestaurantActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
            }
            R.id.txt_chk -> {
                startActivity(Intent(activity, TermsAndConditionActivityVendor::class.java))
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_restaurant_review_vendor, container, false)
        return mView
    }

    companion object {
        fun newInstance(from: String): VendorRestaurantReviewFragment {
            val args = Bundle()
            args.putString("name", from)
            val fragment = VendorRestaurantReviewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun putAllDataToFieldMap() {
        if (activity is VendorRestaurantActivity)
            try {
                setData(vendorRestaurantServiceModel)
                AppLog.e(TAG, vendorRestaurantServiceModel.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

    fun setData(vendorRestaurantServiceModel: VendorRestaurantServiceModel) {
        ed_bname.setText(vendorRestaurantServiceModel.business_name)
        ed_bcontact.setText(vendorRestaurantServiceModel.dial_code + " " + vendorRestaurantServiceModel.business_contactno)
        ed_email.setText(vendorRestaurantServiceModel.business_email)
        ed_city.setText(vendorRestaurantServiceModel.business_city_id.cityname)
        ed_area.setText(vendorRestaurantServiceModel.business_region_id.regionname)
        ed_address.setText(vendorRestaurantServiceModel.business_address)
        ed_taxicount.setText(vendorRestaurantServiceModel.total_table)
        ed_rate.setText(vendorRestaurantServiceModel.seat_per_table)
        ed_cost.setText(vendorRestaurantServiceModel.cost_per_guest)

        ed_avtimeslot.setText(vendorRestaurantServiceModel.time_slot)

        if (vendorRestaurantServiceModel.business_tax != null && !vendorRestaurantServiceModel.business_tax.equals("")) {
            hint_tax.visibility = View.VISIBLE
            ed_tax.visibility = View.VISIBLE
            ed_tax.setText(vendorRestaurantServiceModel.business_tax)
        }

//        if (AppUtils.getStatusBoolean(vendorRestaurantServiceModel.all_day)) {
//            chk_alldays.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.all_day)
//            cons_day.visibility = View.GONE
//        } else {
//            chk_alldays.visibility = View.GONE
//            chk_monday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.mon)
//            chk_tuesday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.tue)
//            chk_wednesday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.wed)
//            chk_thursday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.thu)
//            chk_friday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.fri)
//            chk_saturday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.sat)
//            chk_sunday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.sun)
//        }
//
//        if (AppUtils.getStatusBoolean(vendorRestaurantServiceModel.is_24_hours_open)) {
//            ed_starttime.visibility = View.GONE
//            ed_closingtime.visibility = View.GONE
//            txt_starttime.visibility = View.GONE
//            txt_closingtime.visibility = View.GONE
//            chk_24time.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.is_24_hours_open)
//        } else {
//            chk_24time.visibility = View.GONE
//            ed_starttime.setText(vendorRestaurantServiceModel.start_time)
//            ed_closingtime.setText(vendorRestaurantServiceModel.close_time)
//        }

        ed_desc.setText(vendorRestaurantServiceModel.description)

        txtSlotsMon.setText(getSlots(vendorRestaurantServiceModel.dateTime.get(0).slots))
        txtSlotsTue.setText(getSlots(vendorRestaurantServiceModel.dateTime.get(1).slots))
        txtSlotsWed.setText(getSlots(vendorRestaurantServiceModel.dateTime.get(2).slots))
        txtSlotsThu.setText(getSlots(vendorRestaurantServiceModel.dateTime.get(3).slots))
        txtSlotsFri.setText(getSlots(vendorRestaurantServiceModel.dateTime.get(4).slots))
        txtSlotsSat.setText(getSlots(vendorRestaurantServiceModel.dateTime.get(5).slots))
        txtSlotsSun.setText(getSlots(vendorRestaurantServiceModel.dateTime.get(6).slots))

        initViewPager((activity as VendorRestaurantActivity).mResults, false)

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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        vendorRestaurantServiceModel = (context as VendorRestaurantActivity).getFieldPojo()
        if (activity is VendorRestaurantActivity) {
            imageFile = (context as VendorRestaurantActivity).imageFile
        }
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