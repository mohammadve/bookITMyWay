package com.virtual.customervendor.vendor.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.model.request.VendorParkingRequest
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorParkingValetActivity
import kotlinx.android.synthetic.main.fragment_parking_two_vendor.*

class VendorParkingTwoFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
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
            R.id.chk_24time -> {
                handleTime24(isChecked)
            }
        }
    }

    var datetime: String? = null
    var parkingRequest = VendorParkingRequest()
    var TAG: String = VendorParkingTwoFragment::class.java.simpleName

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.ed_starttime -> {
                if (chk_24time.isChecked()) chk_24time.isChecked = false

                AppUtils.getTimeNew(ed_starttime, activity as AppCompatActivity?)

//                AppUtill.getTime(ed_starttime,activity!!)
            }
            R.id.ed_closingtime -> {
                if (chk_24time.isChecked()) chk_24time.isChecked = false
//                AppUtill.getTime(ed_closingtime, activity!!)
                AppUtils.getTimeNew(ed_closingtime, activity as AppCompatActivity?)

            }
            R.id.btn_next -> {
                validateField()
            }
        }
    }

    companion object {
        fun newInstance(from: String): VendorParkingTwoFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorParkingTwoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_parking_two_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parkingRequest = (context as VendorParkingValetActivity).getValetRequestPojo()

        til_rate.hint=""+til_rate.hint+"("+AppUtils.getCurrencySymbol()+")"

        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        ed_starttime.setOnClickListener(this)
        ed_closingtime.setOnClickListener(this)
        chk_alldays.setOnCheckedChangeListener(this)
        chk_monday.setOnCheckedChangeListener(this)
        chk_tuesday.setOnCheckedChangeListener(this)
        chk_wednesday.setOnCheckedChangeListener(this)
        chk_thursday.setOnCheckedChangeListener(this)
        chk_friday.setOnCheckedChangeListener(this)
        chk_saturday.setOnCheckedChangeListener(this)
        chk_sunday.setOnCheckedChangeListener(this)
        chk_24time.setOnCheckedChangeListener(this)
        manageFromEdit()
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

    fun handleTime24(isChecked: Boolean) {
        if (isChecked) {
            ed_starttime.setText("")
            ed_closingtime.setText("")
        }
    }

    private fun putAllDataToFieldMap(parkingRequest: VendorParkingRequest) {

        try {
            parkingRequest.parking_capacity = ed_par_cap.text.toString()
            parkingRequest.parking_charges = ed_rate.text.toString()
            parkingRequest.all_day = (AppUtils.getStatusString(chk_alldays.isChecked))
            parkingRequest.mon = (AppUtils.getStatusString(chk_monday.isChecked))
            parkingRequest.tue = (AppUtils.getStatusString(chk_tuesday.isChecked))
            parkingRequest.wed = (AppUtils.getStatusString(chk_wednesday.isChecked))
            parkingRequest.thu = (AppUtils.getStatusString(chk_thursday.isChecked))
            parkingRequest.fri = (AppUtils.getStatusString(chk_friday.isChecked))
            parkingRequest.sat = (AppUtils.getStatusString(chk_saturday.isChecked))
            parkingRequest.sun = (AppUtils.getStatusString(chk_sunday.isChecked))
            parkingRequest.is_24_hours_open = AppUtils.getStatusString(chk_24time.isChecked)
            parkingRequest.start_time = ed_starttime.text.toString()
            parkingRequest.close_time = ed_closingtime.text.toString()
            parkingRequest.description = ed_desc.text.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getfilledData() {
        try {
            ed_par_cap.setText(parkingRequest.parking_capacity)
            ed_rate.setText(parkingRequest.parking_charges)
            if (AppUtils.getStatusBoolean(parkingRequest.all_day)) {
                chk_alldays.isChecked = true
            } else {
                chk_monday.isChecked = AppUtils.getStatusBoolean(parkingRequest.mon)
                chk_tuesday.isChecked = AppUtils.getStatusBoolean(parkingRequest.tue)
                chk_wednesday.isChecked = AppUtils.getStatusBoolean(parkingRequest.wed)
                chk_thursday.isChecked = AppUtils.getStatusBoolean(parkingRequest.thu)
                chk_friday.isChecked = AppUtils.getStatusBoolean(parkingRequest.fri)
                chk_saturday.isChecked = AppUtils.getStatusBoolean(parkingRequest.sat)
                chk_sunday.isChecked = AppUtils.getStatusBoolean(parkingRequest.sun)
            }

            if (AppUtils.getStatusBoolean(parkingRequest.is_24_hours_open)) {
                chk_24time.isChecked = true
            } else {
                ed_starttime.setText(parkingRequest.start_time)
                ed_closingtime.setText(parkingRequest.close_time)
            }
            ed_desc.setText(parkingRequest.description)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun manageFromEdit() {
        if (activity is VendorParkingValetActivity) {
            if ((activity as VendorParkingValetActivity).isFromedit()) {
                btn_next.setText((activity as VendorParkingValetActivity).getString(R.string.save))
            }
        }
    }

    fun validateField() {

        if (ed_par_cap.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_par_capacity, getString(R.string.field_required))
            return
        }
        if (til_par_capacity.isErrorEnabled()) {
            UiValidator.disableValidationError(til_par_capacity)
        }

        if (ed_rate.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_rate, getString(R.string.field_required))
            return
        }
        if (til_rate.isErrorEnabled()) {
            UiValidator.disableValidationError(til_rate)
        }
        if (!(chk_alldays.isChecked || chk_monday.isChecked || chk_tuesday.isChecked || chk_wednesday.isChecked || chk_thursday.isChecked || chk_friday.isChecked || chk_saturday.isChecked || chk_sunday.isChecked)) {
            UiValidator.displayMsgSnack(nest,activity, getString(R.string.select_days_of_service))
            return
        }
        if (!(chk_24time.isChecked || (!ed_starttime.text.toString().isEmpty() && !ed_closingtime.text.toString().isEmpty()))) {
            UiValidator.displayMsgSnack(nest,activity, getString(R.string.bussines_hours))
            return
        }
        if (!chk_24time.isChecked) {
            if (!AppUtill.compareTime(ed_starttime.text.toString(), ed_closingtime.text.toString())) {
                UiValidator.displayMsgSnack(nest,activity, getString(R.string.choose_valid_time_slot))
                return
            }
        }
        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }

        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (activity is VendorParkingValetActivity) {
            val mFieldMap = (context as VendorParkingValetActivity).getValetRequestPojo()
            putAllDataToFieldMap(mFieldMap)
            if ((activity as VendorParkingValetActivity).isFromedit()) {
                (activity as VendorParkingValetActivity).hitApiEdit(parkingRequest)
            } else {
                (activity as VendorParkingValetActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.review_your_bussiness), false)
            }
        }

    }

}