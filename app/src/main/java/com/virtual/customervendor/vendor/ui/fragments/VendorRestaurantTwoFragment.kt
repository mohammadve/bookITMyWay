package com.virtual.customervendor.vendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.virtual.customervendor.R
import com.virtual.customervendor.model.request.VendorRestaurantServiceModel
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorRestaurantActivity
import kotlinx.android.synthetic.main.fragment_restaurant_two_vendor.*
import android.widget.RadioButton
import com.virtual.customervendor.commonActivity.TimeManagerActivity
import com.virtual.customervendor.model.DayAviliability


class VendorRestaurantTwoFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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
    var TAG: String = VendorRestaurantTwoFragment::class.java.simpleName
    var vendorRestaurantServiceModel = VendorRestaurantServiceModel()
    var dateTime: ArrayList<DayAviliability> = java.util.ArrayList()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
//            R.id.ed_starttime -> {
//                if (chk_24time.isChecked()) chk_24time.isChecked = false
//                AppUtils.getTimeNew(ed_starttime, activity as AppCompatActivity?)
//            }
//            R.id.ed_closingtime -> {
//                if (chk_24time.isChecked()) chk_24time.isChecked = false
//                AppUtils.getTimeNew(ed_closingtime, activity as AppCompatActivity?)
//
//            }
            R.id.btn_next -> {
                validateField()
            }

            R.id.txtDays -> {
                var intent = Intent(activity, TimeManagerActivity::class.java)
                intent.putExtra(TimeManagerActivity.KEY_Multi_Slots,false)
                if(vendorRestaurantServiceModel.dateTime.size>0)
                    intent.putExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST,vendorRestaurantServiceModel.dateTime)
                startActivityForResult(intent,TimeManagerActivity.REQUEST_CODE)
            }
        }
    }

    companion object {
        fun newInstance(from: String): VendorRestaurantTwoFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorRestaurantTwoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_restaurant_two_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vendorRestaurantServiceModel = (context as VendorRestaurantActivity).getFieldPojo()

        til_avgcost.hint = "" + til_avgcost.hint + "(" + AppUtils.getCurrencySymbol() + ")"


        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        txtDays.setOnClickListener(this)
//        ed_starttime.setOnClickListener(this)
//        ed_closingtime.setOnClickListener(this)
//        chk_alldays.setOnCheckedChangeListener(this)
//        chk_monday.setOnCheckedChangeListener(this)
//        chk_tuesday.setOnCheckedChangeListener(this)
//        chk_wednesday.setOnCheckedChangeListener(this)
//        chk_thursday.setOnCheckedChangeListener(this)
//        chk_friday.setOnCheckedChangeListener(this)
//        chk_saturday.setOnCheckedChangeListener(this)
//        chk_sunday.setOnCheckedChangeListener(this)
//        chk_24time.setOnCheckedChangeListener(this)
        manageFromEdit()
    }

    fun handleAlldays(isChecked: Boolean) {
        if (isChecked) {
//            if (chk_monday.isChecked()) chk_monday.isChecked = false
//            if (chk_tuesday.isChecked()) chk_tuesday.isChecked = false
//            if (chk_wednesday.isChecked()) chk_wednesday.isChecked = false
//            if (chk_thursday.isChecked()) chk_thursday.isChecked = false
//            if (chk_friday.isChecked()) chk_friday.isChecked = false
//            if (chk_saturday.isChecked()) chk_saturday.isChecked = false
//            if (chk_sunday.isChecked()) chk_sunday.isChecked = false
        }
    }

    fun handleDays(isChecked: Boolean) {
        if (isChecked) {
//            if (chk_alldays.isChecked()) chk_alldays.isChecked = false
        }
    }

    fun handleTime24(isChecked: Boolean) {
        if (isChecked) {
//            ed_starttime.setText("")
//            ed_closingtime.setText("")
        }
    }

    private fun putAllDataToFieldMap() {
        val rb = cons_timeslot.findViewById(cons_timeslot.getCheckedRadioButtonId()) as RadioButton

        vendorRestaurantServiceModel.total_table = ed_table_cap.text.toString()
        vendorRestaurantServiceModel.seat_per_table = ed_av_seat_count.text.toString()
//        vendorRestaurantServiceModel.all_day = AppUtils.getStatusString(chk_alldays.isChecked)
//        vendorRestaurantServiceModel.mon = AppUtils.getStatusString(chk_monday.isChecked)
//        vendorRestaurantServiceModel.tue = AppUtils.getStatusString(chk_tuesday.isChecked)
//        vendorRestaurantServiceModel.wed = AppUtils.getStatusString(chk_wednesday.isChecked)
//        vendorRestaurantServiceModel.thu = AppUtils.getStatusString(chk_thursday.isChecked)
//        vendorRestaurantServiceModel.fri = AppUtils.getStatusString(chk_friday.isChecked)
//        vendorRestaurantServiceModel.sat = AppUtils.getStatusString(chk_saturday.isChecked)
//        vendorRestaurantServiceModel.sun = AppUtils.getStatusString(chk_sunday.isChecked)
//        vendorRestaurantServiceModel.is_24_hours_open = AppUtils.getStatusString(chk_24time.isChecked)
//        vendorRestaurantServiceModel.start_time = ed_starttime.text.toString()
//        vendorRestaurantServiceModel.close_time = ed_closingtime.text.toString()
        vendorRestaurantServiceModel.cost_per_guest = ed_avcost.text.toString()
        vendorRestaurantServiceModel.time_slot = rb.text.toString()
        vendorRestaurantServiceModel.description = ed_desc.text.toString()
    }

    private fun getfilledData() {
        try {
            if (vendorRestaurantServiceModel != null) {
                ed_table_cap.setText(vendorRestaurantServiceModel.total_table)
                ed_av_seat_count.setText(vendorRestaurantServiceModel.seat_per_table)
                ed_avcost.setText(vendorRestaurantServiceModel.cost_per_guest)

                if (vendorRestaurantServiceModel.time_slot != null) {
                    if (vendorRestaurantServiceModel.time_slot.equals(resources.getString(R.string.fourfive))) {
                        radioButton1.isChecked = true
                    } else if (vendorRestaurantServiceModel.time_slot.equals(resources.getString(R.string.onezero))) {
                        radioButton2.isChecked = true
                    } else if (vendorRestaurantServiceModel.time_slot.equals(resources.getString(R.string.onethreezero))) {
                        radioButton3.isChecked = true
                    } else if (vendorRestaurantServiceModel.time_slot.equals(resources.getString(R.string.twozero))) {
                        radioButton4.isChecked = true
                    }
                }
                if (AppUtils.getStatusBoolean(vendorRestaurantServiceModel.all_day)) {
//                    chk_alldays.isChecked = true
                } else {
//                    chk_monday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.mon)
//                    chk_tuesday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.tue)
//                    chk_wednesday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.wed)
//                    chk_thursday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.thu)
//                    chk_friday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.fri)
//                    chk_saturday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.sat)
//                    chk_sunday.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.sun)
                }

//                if (AppUtils.getStatusBoolean(vendorRestaurantServiceModel.is_24_hours_open)) {
//                    chk_24time.isChecked = AppUtils.getStatusBoolean(vendorRestaurantServiceModel.is_24_hours_open)
//                } else {
//                    ed_starttime.setText(vendorRestaurantServiceModel.start_time)
//                    ed_closingtime.setText(vendorRestaurantServiceModel.close_time)
//                }
//                ed_closingtime.setText(vendorRestaurantServiceModel.close_time)
                ed_desc.setText(vendorRestaurantServiceModel.description)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun validateField() {

        if (ed_table_cap.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_table_capacity, getString(R.string.field_required))
            return
        }
        if (til_table_capacity.isErrorEnabled()) {
            UiValidator.disableValidationError(til_table_capacity)
        }

        if (ed_av_seat_count.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_seat_count, getString(R.string.field_required))
            return
        }
        if (til_seat_count.isErrorEnabled()) {
            UiValidator.disableValidationError(til_seat_count)
        }

        if (ed_avcost.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_avgcost, getString(R.string.field_required))
            return
        }
        if (til_avgcost.isErrorEnabled()) {
            UiValidator.disableValidationError(til_avgcost)
        }

        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }

        if(!isValidTimeSlots(vendorRestaurantServiceModel.dateTime)) {
            UiValidator.displayMsg(context, "Please enter a valid time slots")
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }


        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (activity is VendorRestaurantActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorRestaurantActivity).isFromedit()) {
                (activity as VendorRestaurantActivity).hitApiEdit(vendorRestaurantServiceModel)
            } else {
                (activity as VendorRestaurantActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.review_your_bussiness), false)
            }
        }
    }


    private fun isValidTimeSlots(taxi_Service_Request: ArrayList<DayAviliability>): Boolean {
        for (aviliability in taxi_Service_Request){
            if(aviliability.isSeleted && aviliability.slots.size>0)
                if(aviliability.slots[0].startTime.length>0 && aviliability.slots[0].stopTime.length>0 )
                    return true
        }
        return false
    }

    fun manageFromEdit() {
        if (activity is VendorRestaurantActivity) {
            if ((activity as VendorRestaurantActivity).isFromedit()) {
                btn_next.setText((activity as VendorRestaurantActivity).getString(R.string.save))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==TimeManagerActivity.REQUEST_CODE && resultCode==TimeManagerActivity.RESULT_CODE){
            dateTime=data?.getSerializableExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST) as ArrayList<DayAviliability>
            vendorRestaurantServiceModel.dateTime=dateTime
            vendorRestaurantServiceModel.all_day= data?.getIntExtra(TimeManagerActivity.KEY_ALL_DAY_SAME,0).toString()
        }

    }

}