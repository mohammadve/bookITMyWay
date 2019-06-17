package com.virtual.customervendor.vendor.ui.fragments

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customview.CustomEditText
import com.virtual.customervendor.model.LocationTimeModel
import com.virtual.customervendor.model.PackageModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorPackageDetailActivity
import com.virtual.customervendor.vendor.ui.adapter.SeightSeeing_sight_V
import kotlinx.android.synthetic.main.fragment_package_edit_vendor.*

class VendorPackageEditFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView!!.id) {
            R.id.chk_alldays -> {
                handleAlldays(isChecked)
            }
            R.id.chk_monday -> {
                handleDays(isChecked)
            }
            R.id.chk_tuesday -> {
                handleDays(isChecked)
            }
            R.id.chk_wednesday -> {
                handleDays(isChecked)
            }
            R.id.chk_thursday -> {
                handleDays(isChecked)
            }
            R.id.chk_friday -> {
                handleDays(isChecked)
            }
            R.id.chk_saturday -> {
                handleDays(isChecked)
            }
            R.id.chk_sunday -> {
                handleDays(isChecked)
            }
        }
    }

    var datetime: String? = null
    var manager: FragmentManager? = null
    var count: Int = 0
    var TAG: String = VendorPackageEditFragment::class.java.simpleName
    var seightSeeing_sight_V: SeightSeeing_sight_V? = null
    var sight_Seeing_Service_Request = PackageModel()
    var seenList = ArrayList<LocationTimeModel>()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.ed_starttime -> {
                AppUtils.getTimeNew(ed_starttime, activity as AppCompatActivity?)
//                AppUtill.getTime(ed_starttime, activity!!)
            }
            R.id.ed_closeingtime -> {
                AppUtils.getTimeNew(ed_closeingtime, activity as AppCompatActivity?)
//                AppUtill.getTime(ed_closeingtime, activity!!)
            }
            R.id.ed_service -> {
                (context as VendorPackageDetailActivity).setDisplayDialog(7, AppConstants.FROM_V_TAXI_SERVICE_AREA)
            }
            R.id.addmore -> {
                performAddSight()
            }
            R.id.btn_next -> {
                validateField()
            }
        }
    }

    companion object {
        fun newInstance(from: String): VendorPackageEditFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorPackageEditFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_package_edit_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)

        ed_rate.hint = "" + ed_rate.hint + "(" + AppUtils.getCurrencySymbol() + ")"

        if (activity is VendorPackageDetailActivity) {
            sight_Seeing_Service_Request = (context as VendorPackageDetailActivity).vendorEventServiceRequest
        }
        if (!(activity as VendorPackageDetailActivity).isFromAdd1())
            getfilledData() else {
            createAdapterView()
        }
        initView(view)
    }

    private fun getfilledData() {
        try {
            if (activity is VendorPackageDetailActivity) {
                ed_packagename.setText(sight_Seeing_Service_Request.package_name)
                ed_taxicount.setText(sight_Seeing_Service_Request.total_tour_vehicle)
                ed_seat.setText(sight_Seeing_Service_Request.avg_seat_per_vehicle)
                ed_rate.setText(sight_Seeing_Service_Request.cost)

                var data = StringBuilder()
                for (regionModel: RegionModel in sight_Seeing_Service_Request.service_region_ids) {
                    if (data.length > 0) {
                        data.append(", ")
                    }
                    data.append(regionModel.regionname)
                }
                ed_service.setText(data.toString())
                chk_alldays.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.all_day)
                chk_monday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.monday)
                chk_tuesday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.tuesday)
                chk_wednesday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.wednesday)
                chk_thursday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.thursday)
                chk_friday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.friday)
                chk_saturday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.saturday)
                chk_sunday.isChecked = AppUtils.getStatusBoolean(sight_Seeing_Service_Request.sunday)
                ed_desc.setText(sight_Seeing_Service_Request.description)
                ed_startloc.setText(sight_Seeing_Service_Request.start_location)
                ed_starttime.setText(sight_Seeing_Service_Request.start_time)
                ed_closeloc.setText(sight_Seeing_Service_Request.end_location)
                ed_closeingtime.setText(sight_Seeing_Service_Request.end_time)
                if (sight_Seeing_Service_Request.sight_seens.size > 0) {
                    seenList = sight_Seeing_Service_Request.sight_seens
                    seightSeeing_sight_V = SeightSeeing_sight_V(activity!!, AppConstants.FROM_ADD, seenList)
                    val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    rv_sightseeing.layoutManager = manager
                    rv_sightseeing.adapter = (seightSeeing_sight_V)
                } else {
                    createAdapterView()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun initView(view: View) {
//        createAdapterView()
        btn_next.setOnClickListener(this)
        ed_starttime.setOnClickListener(this)
        ed_closeingtime.setOnClickListener(this)
        ed_service.setOnClickListener(this)
        chk_alldays.setOnCheckedChangeListener(this)
        chk_monday.setOnCheckedChangeListener(this)
        chk_tuesday.setOnCheckedChangeListener(this)
        chk_wednesday.setOnCheckedChangeListener(this)
        chk_thursday.setOnCheckedChangeListener(this)
        chk_friday.setOnCheckedChangeListener(this)
        chk_saturday.setOnCheckedChangeListener(this)
        chk_sunday.setOnCheckedChangeListener(this)
        addmore.setOnClickListener(this)
    }

    fun handleAlldays(isChecked: Boolean) {
        if (isChecked) {
            if (chk_monday.isChecked()) chk_monday.isChecked = false
            if (chk_tuesday.isChecked()) chk_tuesday.isChecked = false
            if (chk_wednesday.isChecked()) chk_wednesday.isChecked = false
            if (chk_thursday.isChecked()) chk_thursday.isChecked = false
            if (chk_friday.isChecked()) chk_friday.isChecked = false
            if (chk_saturday.isChecked()) chk_saturday.isChecked = false
            if (chk_sunday.isChecked()) chk_sunday.isChecked = false
        }
    }

    fun handleDays(isChecked: Boolean) {
        if (isChecked) {
            if (chk_alldays.isChecked()) chk_alldays.isChecked = false
        }
    }


    fun updateSelectedServiceArea(bean: ArrayList<RegionModel>) {
        var data = StringBuilder()
        var filledList: ArrayList<RegionModel> = ArrayList()

        for (temp in bean) {
            if (temp.isSelected) {
                filledList.add(temp)
                if (data.length > 0) {
                    data.append(", ")
                }
                data.append(temp.regionname)
            }
        }
        sight_Seeing_Service_Request.service_region_ids = filledList
        ed_service.setText(data.toString())
    }

    private fun putAllDataToFieldMap() {
        try {
            sight_Seeing_Service_Request.package_name = ed_packagename.text.toString()
            sight_Seeing_Service_Request.total_tour_vehicle = ed_taxicount.text.toString()
            sight_Seeing_Service_Request.avg_seat_per_vehicle = ed_seat.text.toString()
            sight_Seeing_Service_Request.cost = ed_rate.text.toString()

            sight_Seeing_Service_Request.all_day = AppUtils.getStatusString(chk_alldays.isChecked)
            sight_Seeing_Service_Request.monday = AppUtils.getStatusString(chk_monday.isChecked)
            sight_Seeing_Service_Request.tuesday = AppUtils.getStatusString(chk_tuesday.isChecked)
            sight_Seeing_Service_Request.wednesday = AppUtils.getStatusString(chk_wednesday.isChecked)
            sight_Seeing_Service_Request.thursday = AppUtils.getStatusString(chk_thursday.isChecked)
            sight_Seeing_Service_Request.friday = AppUtils.getStatusString(chk_friday.isChecked)
            sight_Seeing_Service_Request.saturday = AppUtils.getStatusString(chk_saturday.isChecked)
            sight_Seeing_Service_Request.sunday = AppUtils.getStatusString(chk_sunday.isChecked)

            sight_Seeing_Service_Request.startinfo = LocationTimeModel(ed_startloc.text.toString(), ed_starttime.text.toString())
            sight_Seeing_Service_Request.sight_seens = seenList
            sight_Seeing_Service_Request.endinfo = LocationTimeModel(ed_closeloc.text.toString(), ed_closeingtime.text.toString())
            sight_Seeing_Service_Request.description = ed_desc.text.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun performAddSight() {
        if (seenList.size < 5) {
            seenList.add(LocationTimeModel())
            seightSeeing_sight_V?.notifyItemInserted(seenList.size - 1)
        }
    }

    private fun createAdapterView() {
        seenList.add(LocationTimeModel())
        seenList.add(LocationTimeModel())
        seightSeeing_sight_V = SeightSeeing_sight_V(activity!!, AppConstants.FROM_ADD, seenList)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_sightseeing.layoutManager = manager
        rv_sightseeing.adapter = (seightSeeing_sight_V)
    }

    fun validateField() {
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (ed_packagename.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_packagename, getString(R.string.field_required))
            return
        }
        if (til_packagename.isErrorEnabled()) {
            UiValidator.disableValidationError(til_packagename)
        }
        if (ed_taxicount.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_taxicount, getString(R.string.field_required))
            return
        }
        if (til_taxicount.isErrorEnabled()) {
            UiValidator.disableValidationError(til_taxicount)
        }
        if (ed_seat.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_seat, getString(R.string.field_required))
            return
        }
        if (til_seat.isErrorEnabled()) {
            UiValidator.disableValidationError(til_seat)
        }
        if (ed_rate.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_rate, getString(R.string.field_required))
            return
        }
        if (til_rate.isErrorEnabled()) {
            UiValidator.disableValidationError(til_rate)
        }
        if (ed_service.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_servicearea, getString(R.string.rgs_sign_in_empty_username_text))
            return
        }
        if (til_servicearea.isErrorEnabled()) {
            UiValidator.disableValidationError(til_servicearea)
        }
        if (!(chk_alldays.isChecked || chk_monday.isChecked || chk_tuesday.isChecked || chk_wednesday.isChecked || chk_thursday.isChecked || chk_friday.isChecked || chk_saturday.isChecked || chk_sunday.isChecked)) {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_days_of_service))
            return
        }

        if (ed_startloc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_startloc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_startloc)
        }

        if (ed_starttime.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_starttime, getString(R.string.field_required))
            return
        }
        if (til_starttime.isErrorEnabled()) {
            UiValidator.disableValidationError(til_starttime)
        }

        if (ed_closeloc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_closeloc, getString(R.string.field_required))
            return
        }
        if (til_closeloc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_closeloc)
        }

        if (ed_closeingtime.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_closetime, getString(R.string.field_required))
            return
        }
        if (til_closetime.isErrorEnabled()) {
            UiValidator.disableValidationError(til_closetime)
        }
        getListSightSeen()
//        if (!AppUtill.compareTime(ed_starttime.text.toString(), ed_closeingtime.text.toString())) {
//            UiValidator.displayMsgSnack(nest, activity, getString(R.string.choose_valid_time_slot))
//            return
//        }

        if (!compareListItems(ed_starttime.getText().toString(), seenList, ed_closeingtime.getText().toString())) {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.choose_valid_time_slot))
            return
        }

        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }

        getListSightSeen()

        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (activity is VendorPackageDetailActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorPackageDetailActivity).isFromAdd1()) {
                (activity as VendorPackageDetailActivity).hitApiEdit(AppConstants.ACTION_ADD)
            } else {
                (activity as VendorPackageDetailActivity).hitApiEdit(AppConstants.ACTION_EDIT)
            }
        }
    }

    fun getListSightSeen() {
        AppLog.e(TAG, rv_sightseeing.childCount.toString())
        var locationTimeModel: LocationTimeModel
        if (seenList != null) {
            seenList.clear()
        }
        for (i in 0 until rv_sightseeing.childCount) {
            val data: ConstraintLayout = rv_sightseeing.getChildAt(i) as ConstraintLayout
            if (!data.findViewById<CustomEditText>(R.id.ed_startloc).text.toString().isEmpty()) {
                locationTimeModel = LocationTimeModel()
                locationTimeModel.location = data.findViewById<CustomEditText>(R.id.ed_startloc).text.toString()
                locationTimeModel.time = data.findViewById<CustomEditText>(R.id.ed_sighttime).text.toString()
                seenList.add(locationTimeModel)
            }
        }
        sight_Seeing_Service_Request.sight_seens = seenList
        AppLog.e(TAG, seenList.toString())
    }

    fun compareListItems(startTime: String, seenList: ArrayList<LocationTimeModel>, endTime: String): Boolean {
        if (seenList.size > 0) {
            if (AppUtill.compareTime(startTime, seenList.get(0).time)) {
                for (i in 0 until seenList.size - 1) {
                    if (!AppUtill.compareTime(seenList.get(i).time, seenList.get(i + 1).time)) {
                        return false
                    }
                }
                if (AppUtill.compareTime(seenList.get(seenList.size - 1).time, endTime)) {
                    return true
                } else
                    return false
            } else {
                return false
            }
        } else {
            if (AppUtill.compareTime(startTime, endTime)) {
                return true
            } else {
                return false
            }
        }
    }

}