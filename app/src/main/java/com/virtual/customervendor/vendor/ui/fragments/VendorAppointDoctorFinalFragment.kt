package com.virtual.customervendor.vendor.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.TermsAndConditionActivity
import com.virtual.customervendor.commonActivity.TermsAndConditionActivityVendor
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.ItemPriceModel
import com.virtual.customervendor.model.OfferModel
import com.virtual.customervendor.model.TimeSlotModel
import com.virtual.customervendor.model.request.VendorHealthServiceRequest
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.*
import com.virtual.customervendor.vendor.ui.adapter.HealthAddServiceAdapter
import kotlinx.android.synthetic.main.fragment_appoint_doctor_final_vendor.*
import kotlin.collections.ArrayList

class VendorAppointDoctorFinalFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var datetime: String? = null
    var manager: FragmentManager? = null
    val list: ArrayList<OfferModel> = java.util.ArrayList()
    var count: Int = 0
    var TAG: String = VendorAppointDoctorFinalFragment::class.java.name
    var request = VendorHealthServiceRequest()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null
    var mResults = ArrayList<BusinessImage>()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                if (chk_term.isChecked) {
                    if (activity is VendorDoctorActivity) {
                        (activity as VendorDoctorActivity).hitApi(request)
                    } else if (activity is VendorHairActivity) {
                        (activity as VendorHairActivity).hitApi(request)
                    } else if (activity is VendorNailsActivity) {
                        (activity as VendorNailsActivity).hitApi(request)
                    } else if (activity is VendorMassagePhysioActivity) {
                        (activity as VendorMassagePhysioActivity).hitApi(request)
                    }
                } else {
                    UiValidator.displayMsgSnack(nest, activity!!, activity!!.resources.getString(R.string.check_tern))
                }
            }
            R.id.btn_edit -> {
                if (activity is VendorDoctorActivity)
                    (activity as VendorDoctorActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
                else if (activity is VendorHairActivity)
                    (activity as VendorHairActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
                else if (activity is VendorNailsActivity)
                    (activity as VendorNailsActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
                else if (activity is VendorMassagePhysioActivity)
                    (activity as VendorMassagePhysioActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
            }
            R.id.txt_chk -> {
                startActivity(Intent(activity, TermsAndConditionActivityVendor::class.java))
            }
        }
    }

    companion object {
        fun newInstance(from: String): VendorAppointDoctorFinalFragment {
            val args = Bundle()
            args.putString("name", from)
            val fragment = VendorAppointDoctorFinalFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun putAllDataToFieldMap() {
        if (activity is VendorDoctorActivity) {
            request = (context as VendorDoctorActivity).getDoctorFieldPojo()
            mResults = (context as VendorDoctorActivity).mResults
        } else if (activity is VendorHairActivity) {
            request = (context as VendorHairActivity).getHairFieldPojo()
            mResults = (context as VendorHairActivity).mResults
        } else if (activity is VendorNailsActivity) {
            request = (context as VendorNailsActivity).getNailFieldPojo()
            mResults = (context as VendorNailsActivity).mResults
        } else if (activity is VendorMassagePhysioActivity) {
            request = (context as VendorMassagePhysioActivity).getMassageFieldPojo()
            mResults = (context as VendorMassagePhysioActivity).mResults
        }
        try {
            setData(request)
            AppLog.e(TAG, request.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setData(request: VendorHealthServiceRequest) {
        ed_bname.setText(request.business_name)
        ed_bcontact.setText(request.dial_code + " " + request.business_contactno)
        ed_email.setText(request.business_email)
        ed_city.setText(request.business_city_id.cityname)
        ed_area.setText(request.business_region_id.regionname)
        ed_address.setText(request.business_address)
        ed_fees.setText(request.fees_per_visit)
        ed_timeslot.setText(request.time_slot)

        if (request.business_tax != null && !request.business_tax.equals("")) {
            hint_tax.visibility = View.VISIBLE
            ed_tax.visibility = View.VISIBLE
            ed_tax.setText(request.business_tax)
        } else {
            hint_tax.visibility = View.GONE
            ed_tax.visibility = View.GONE
        }

        if (AppUtils.getStatusBoolean(request.all_day)) {
            chk_alldays.isChecked = AppUtils.getStatusBoolean(request.all_day)
            cons_day.visibility = View.GONE
        } else {
            chk_alldays.visibility = View.GONE
            chk_monday.isChecked = AppUtils.getStatusBoolean(request.mon)
            chk_tuesday.isChecked = AppUtils.getStatusBoolean(request.tue)
            chk_wednesday.isChecked = AppUtils.getStatusBoolean(request.wed)
            chk_thursday.isChecked = AppUtils.getStatusBoolean(request.thu)
            chk_friday.isChecked = AppUtils.getStatusBoolean(request.fri)
            chk_saturday.isChecked = AppUtils.getStatusBoolean(request.sat)
            chk_sunday.isChecked = AppUtils.getStatusBoolean(request.sun)
        }

        var data = StringBuilder()
        for (regionModel: TimeSlotModel in request.visiting_hours_slot) {
            if (data.length > 0) {
                data.append("\n")
            }
            data.append(regionModel.fromTime + activity!!.resources.getString(R.string.to) + regionModel.toTime)
        }
        ed_bussinesshours.setText(data)
        ed_desc.setText(request.description)
        ed_avgperson.setText(request.required_person_per_hr)


        if (request.service_menu.size > 0) {
            txt_servicemenu.visibility = View.VISIBLE
            rv_timing.visibility = View.VISIBLE
            createAdapterView(request.service_menu)
        }
        initViewPager(mResults, false)
    }

    private fun createAdapterView(timeList: ArrayList<ItemPriceModel>) {
        var serviceAdapter = HealthAddServiceAdapter(activity!!, AppConstants.FROM_REVIEW, timeList)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_timing.layoutManager = manager
        rv_timing.adapter = (serviceAdapter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_appoint_doctor_final_vendor, container, false)
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

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(activity, mResults, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }
}