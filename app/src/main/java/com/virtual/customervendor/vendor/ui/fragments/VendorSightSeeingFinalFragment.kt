package com.virtual.customervendor.vendor.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.TermsAndConditionActivityVendor
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.LocationTimeModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.request.Ven_Sight_Seeing_Service_Request
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorSightSeeingActivity
import com.virtual.customervendor.vendor.ui.adapter.SeightSeeing_sight_V
import kotlinx.android.synthetic.main.fragment_seight_final_vendor.*
import java.io.File

class VendorSightSeeingFinalFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var datetime: String? = null
    var manager: FragmentManager? = null
    var count: Int = 0
    var TAG: String = VendorSightSeeingFinalFragment::class.java.name
    var sight_Seeing_Service_Request = Ven_Sight_Seeing_Service_Request()
    var seightSeeing_sight_V: SeightSeeing_sight_V? = null
    var imageFile: File? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                if (chk_term.isChecked) {
                    if (activity is VendorSightSeeingActivity)
                        (activity as VendorSightSeeingActivity).hitApi(sight_Seeing_Service_Request)
                } else {
                    UiValidator.displayMsgSnack(nest, activity!!, activity!!.resources.getString(R.string.check_tern))
                }
            }
            R.id.btn_edit -> {
                if (activity is VendorSightSeeingActivity)
                    (activity as VendorSightSeeingActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
            }
            R.id.txt_chk -> {
                startActivity(Intent(activity, TermsAndConditionActivityVendor::class.java))
            }

        }
    }

    companion object {
        fun newInstance(from: String): VendorSightSeeingFinalFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorSightSeeingFinalFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun putAllDataToFieldMap() {
        if (activity is VendorSightSeeingActivity) {
            sight_Seeing_Service_Request = (context as VendorSightSeeingActivity).getSightSeeingPojo()
            imageFile = (context as VendorSightSeeingActivity).imageFile
        }

        try {
            setData(sight_Seeing_Service_Request)
            AppLog.e(TAG, sight_Seeing_Service_Request.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setData(sight_Seeing_Service_Request: Ven_Sight_Seeing_Service_Request) {
        ed_bname.setText(sight_Seeing_Service_Request.business_name)
        ed_bcontact.setText(sight_Seeing_Service_Request.dial_code + " " + sight_Seeing_Service_Request.business_contactno)
        ed_email.setText(sight_Seeing_Service_Request.business_email)
        ed_city.setText(sight_Seeing_Service_Request.business_city_id.cityname)
        ed_area.setText(sight_Seeing_Service_Request.business_region_id.regionname)
        ed_address.setText(sight_Seeing_Service_Request.business_address)
        ed_taxicount.setText(sight_Seeing_Service_Request.total_tour_vehicle)
        ed_seat.setText(sight_Seeing_Service_Request.avg_seat_per_vehicle)
        ed_rate.setText(sight_Seeing_Service_Request.cost)

        if (sight_Seeing_Service_Request.business_tax != null && !sight_Seeing_Service_Request.business_tax.equals("")) {
            hint_tax.visibility = View.VISIBLE
            ed_tax.visibility = View.VISIBLE
            ed_tax.setText(sight_Seeing_Service_Request.business_tax)
        } else {
            hint_tax.visibility = View.GONE
            ed_tax.visibility = View.GONE
        }

        var data = StringBuilder()
        for (regionModel: RegionModel in sight_Seeing_Service_Request.service_region_ids) {
            if (data.length > 0) {
                data.append(", ")
            }
            data.append(regionModel.regionname)
        }
        ed_service.setText(data)
        if (AppUtils.getStatusBoolean(sight_Seeing_Service_Request.all_day)) {
            chk_alldays.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.all_day)
            cons_day.visibility = View.GONE
        } else {
            chk_alldays.visibility = View.GONE
            chk_monday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.mon)
            chk_tuesday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.tue)
            chk_wednesday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.wed)
            chk_thursday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.thu)
            chk_friday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.fri)
            chk_saturday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.sat)
            chk_sunday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.sun)
        }
        var seenList = ArrayList<LocationTimeModel>()
        seenList.add(sight_Seeing_Service_Request.startinfo)
        seenList.addAll(sight_Seeing_Service_Request.sight_seens)
        seenList.add(sight_Seeing_Service_Request.endinfo)
        createAdapterView(seenList)
        ed_desc.setText(sight_Seeing_Service_Request.description)


//        if (imageFile != null)
//            Glide.with(this).load(File(imageFile?.absolutePath)).into(img_upload)

        initViewPager((activity as VendorSightSeeingActivity).mResults, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_seight_final_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        putAllDataToFieldMap()
        initView(view)
    }

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        var SCREEN_COUNT = mResults.size
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

    private fun createAdapterView(seenList: ArrayList<LocationTimeModel>) {
        seightSeeing_sight_V = SeightSeeing_sight_V(activity!!, AppConstants.FROM_REVIEW, seenList)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_sightseeing.layoutManager = manager
        rv_sightseeing.adapter = (seightSeeing_sight_V)
    }

}