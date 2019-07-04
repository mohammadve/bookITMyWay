package com.virtual.customervendor.vendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.TimeManagerActivity
import com.virtual.customervendor.model.DayAviliability
import com.virtual.customervendor.model.request.VendorParkingRequest
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.*
import kotlinx.android.synthetic.main.fragment_parking_two_vendor.*

class VendorParkingTwoFragment : Fragment(), View.OnClickListener {
        var datetime: String? = null
    var parkingRequest = VendorParkingRequest()
    var TAG: String = VendorParkingTwoFragment::class.java.simpleName

    private lateinit var dateTime: ArrayList<DayAviliability>
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }

            R.id.txtDays -> {
                var intent = Intent(activity, TimeManagerActivity::class.java)
                intent.putExtra(TimeManagerActivity.KEY_Multi_Slots, false)
                if(parkingRequest.dateTime.size>0)
                    intent.putExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST,parkingRequest.dateTime)
                startActivityForResult(intent, TimeManagerActivity.REQUEST_CODE)
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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TimeManagerActivity.REQUEST_CODE && resultCode == TimeManagerActivity.RESULT_CODE) {
            dateTime = data?.getSerializableExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST) as ArrayList<DayAviliability>
            var isAllDay: String = data?.getIntExtra(TimeManagerActivity.KEY_ALL_DAY_SAME, 0).toString()
            parkingRequest.dateTime = dateTime
            parkingRequest.all_day = isAllDay
        }
    }

    fun initView(view: View) {

        btn_next.setOnClickListener(this)

        txtDays.setOnClickListener(this)

        manageFromEdit()
    }


    private fun putAllDataToFieldMap(parkingRequest: VendorParkingRequest) {

        try {
            parkingRequest.parking_capacity = ed_par_cap.text.toString()
            parkingRequest.parking_charges = ed_rate.text.toString()

            parkingRequest.description = ed_desc.text.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getfilledData() {
        try {
            ed_par_cap.setText(parkingRequest.parking_capacity)
            ed_rate.setText(parkingRequest.parking_charges)
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
        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }

        if(!isValidTimeSlots(parkingRequest.dateTime)) {
            UiValidator.displayMsg(context, "Please enter a valid time slots")
            return
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
    fun isValidTimeSlots(taxi_Service_Request: ArrayList<DayAviliability>): Boolean {
        for (aviliability in taxi_Service_Request){
            if(aviliability.isSeleted && aviliability.slots.size>0)
                if(aviliability.slots[0].startTime.length>0 && aviliability.slots[0].stopTime.length>0 )
                    return true
        }
        return false
    }
}