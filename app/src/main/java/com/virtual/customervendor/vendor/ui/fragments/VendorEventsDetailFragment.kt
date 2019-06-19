package com.virtual.customervendor.vendor.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.vendor.ui.activity.VendorEventsDetailActivity
import kotlinx.android.synthetic.main.fragment_events_detail_vendor.*
import java.io.File

class VendorEventsDetailFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {

    var imageFile: File? = null
    var TAG: String = VendorEventsDetailFragment::class.java.name
    var eventRequest = EventDetail()
    var homeSliderAdapter: HomeSliderAdapter? = null
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> activity?.onBackPressed()
            R.id.btn_next -> {
            }
        }
    }

    companion object {
        fun newInstance(): VendorEventsDetailFragment {
            val fragment = VendorEventsDetailFragment()
            return fragment
        }
    }

    private fun setData() {
        try {
            ed_eventname.setText(eventRequest.name)
            ed_price.setText(AppUtils.getRegisterRateWithSymbol(eventRequest.ticket_price))
            txt_ticket.setText("" + eventRequest.total_tickets)
            tv_remaining_no_of_ticket.text = "" + (eventRequest.total_tickets - eventRequest.ticket_booked)

            ed_sdate.setText(eventRequest.start_date)
            ed_edate.setText(eventRequest.end_date)
            ed_stime.setText(eventRequest.start_time)
            ed_etime.setText(eventRequest.close_time)
            txt_desc.setText(eventRequest.description)
            txt_venue.setText(eventRequest.venue)
            if (eventRequest.event_images!!.size > 0) {
                homeSliderAdapter = HomeSliderAdapter(activity, eventRequest.event_images, this, false)
                viewPager!!.setAdapter(homeSliderAdapter)
                AppUtill.handlePager(activity!!, eventRequest!!.event_images!!.size, layoutDots, viewPager)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPagerItemClicked(position: Int) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_events_detail_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventRequest = (context as VendorEventsDetailActivity).getEventFieldPojo()
        setData()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
    }
}