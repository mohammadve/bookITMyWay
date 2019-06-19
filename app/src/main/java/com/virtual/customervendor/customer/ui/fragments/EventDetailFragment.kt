package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.activity.EventsActivity
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.utills.AppUtils
import kotlinx.android.synthetic.main.fragment_customer_event_detail.*

class EventDetailFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var TAG: String = EventDetailFragment::class.java.name;
    var eventDetail = EventDetail()
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_book_event -> {
                (activity as EventsActivity).setDisplayFragment(4, resources.getString(R.string.booking), false)
            }
        }
    }

    companion object {
        fun newInstance(): EventDetailFragment {
            val fragment = EventDetailFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_customer_event_detail, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_book_event.setOnClickListener(this)
        eventDetail = (activity as EventsActivity).eventDetail()
        tv_event_name.text = eventDetail.name
        tv_fee.text = AppUtils.getRateWithSymbol(eventDetail.ticket_price)
        tv_no_of_ticket.text = "" + eventDetail.total_tickets
        tv_remaining_no_of_ticket.text = "" + (eventDetail.total_tickets - eventDetail.ticket_booked)

        tv_startdate.text = eventDetail.start_date
        tv_enddate.text = eventDetail.end_date

        tv_endtime.text = eventDetail.close_time
        tv_starttime.text = eventDetail.start_time

        tv_des.text = eventDetail.description

        if (eventDetail.event_images != null && eventDetail.event_images.size > 0) {
            initViewPager(eventDetail.event_images, false)
        }
    }

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        var homeSliderAdapter = HomeSliderAdapter(activity!!, mResults, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
    }
}