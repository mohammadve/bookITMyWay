package com.virtual.customervendor.vendor.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.request.VendorEventServiceRequest
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorEventsActivity
import kotlinx.android.synthetic.main.fragment_event_review_vendor.*
import java.io.File

class VendorEventReviewFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var TAG: String = VendorEventReviewFragment::class.java.name
    var vendorEventServiceRequest = VendorEventServiceRequest()
    var bus_imageFile: ArrayList<File>? = null
    var ser_imageFile: ArrayList<File>? = ArrayList()
    var strImages: ArrayList<String>? = ArrayList()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null
    var homeSliderAdapterEvent: HomeSliderAdapter? = null
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                if (chk_term.isChecked) {
                    if (activity is VendorEventsActivity)
                        (activity as VendorEventsActivity).hitApi()
                } else {
                    UiValidator.displayMsgSnack(nest, activity!!, activity!!.resources.getString(R.string.check_tern))
                }
            }
            R.id.btn_edit -> {
                if (activity is VendorEventsActivity)
                    (activity as VendorEventsActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
            }
            R.id.txt_chk -> {
                AppLog.e(TAG, "clicked")
            }
        }
    }

    companion object {
        fun newInstance(from: String): VendorEventReviewFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorEventReviewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun putAllDataToFieldMap() {
        setData(vendorEventServiceRequest)
        AppLog.e(TAG, vendorEventServiceRequest.toString())
    }

    fun setData(vendorEventServiceRequest: VendorEventServiceRequest) {
        initViewPager((activity as VendorEventsActivity).mResults, false)
        ed_bname.setText(vendorEventServiceRequest.business_name)
        ed_bcontact.setText(vendorEventServiceRequest.dial_code + " " + vendorEventServiceRequest.business_contactno)
        ed_email.setText(vendorEventServiceRequest.business_email)
        ed_city.setText(vendorEventServiceRequest.business_city_id.cityname)
        ed_area.setText(vendorEventServiceRequest.business_region_id.regionname)
        ed_address.setText(vendorEventServiceRequest.business_address)
        ed_eventname.setText(vendorEventServiceRequest.event_name)
        ed_price.setText(vendorEventServiceRequest.ticket_price)
        ed_ticketcount.setText(vendorEventServiceRequest.total_tickets)
        ed_startdate.setText(vendorEventServiceRequest.start_date)
        ed_enddate.setText(vendorEventServiceRequest.end_date)
        ed_starttime.setText(vendorEventServiceRequest.start_time)
        ed_closingtime.setText(vendorEventServiceRequest.close_time)
        ed_desc.setText(vendorEventServiceRequest.description)
        ed_venue.setText(vendorEventServiceRequest.venue)

        if (vendorEventServiceRequest.business_tax != null && !vendorEventServiceRequest.business_tax.equals("")) {
            hint_tax.visibility = View.VISIBLE
            ed_tax.visibility = View.VISIBLE
            ed_tax.setText(vendorEventServiceRequest.business_tax)
        } else {
            hint_tax.visibility = View.GONE
            ed_tax.visibility = View.GONE
        }

        if ((activity as VendorEventsActivity).strImages.size > 0) {
            var imgList = java.util.ArrayList<BusinessImage>()
            for (url in (activity as VendorEventsActivity).strImages) {
                imgList.add(BusinessImage(url, ""))
            }

            SCREEN_COUNT = imgList.size
            homeSliderAdapterEvent = HomeSliderAdapter(activity, imgList, this, false)
            viewPager2!!.setAdapter(homeSliderAdapterEvent)
            AppUtill.handlePager(activity!!, imgList.size, layoutDots2, viewPager2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_event_review_vendor, container, false)
        ser_imageFile = (context as VendorEventsActivity).ser_imageFile
        strImages = (context as VendorEventsActivity).strImages
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vendorEventServiceRequest = (context as VendorEventsActivity).getEventFieldPojo()
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

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(activity, mResults, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
    }

}