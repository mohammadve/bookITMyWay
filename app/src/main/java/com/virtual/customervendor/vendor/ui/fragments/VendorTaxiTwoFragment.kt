package com.virtual.customervendor.vendor.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.model.OfferModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.request.Ven_Taxi_Service_Request
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorLimoActivity
import com.virtual.customervendor.vendor.ui.activity.VendorTaxiActivity
import com.virtual.customervendor.vendor.ui.activity.VendorTourBusActivity
import kotlinx.android.synthetic.main.fragment_taxi_two_vendor.*

class VendorTaxiTwoFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    var datetime: String? = null
    var manager: FragmentManager? = null
    val list: ArrayList<OfferModel> = java.util.ArrayList()
    var count: Int = 0
    var TAG: String = VendorTaxiTwoFragment::class.java.simpleName
    var taxi_Service_Request = Ven_Taxi_Service_Request()
    var limo_Service_Request = Ven_Taxi_Service_Request()
    var tourbus_Service_Request = Ven_Taxi_Service_Request()
    var dataId = StringBuilder()

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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.ed_starttime -> {
                if (chk_24time.isChecked()) chk_24time.isChecked = false
                AppUtils.getTimeNew(ed_starttime, activity as AppCompatActivity?)

            }
            R.id.ed_closingtime -> {
                if (chk_24time.isChecked()) chk_24time.isChecked = false
                AppUtils.getTimeNew(ed_closingtime, activity as AppCompatActivity?)

            }
            R.id.ed_service -> {
                if (context is VendorTaxiActivity) {
                    (context as VendorTaxiActivity).setDisplayDialog(7, AppConstants.FROM_V_TAXI_SERVICE_AREA, "")
                } else if (context is VendorLimoActivity) {
                    (context as VendorLimoActivity).setDisplayDialog(7, AppConstants.FROM_V_TAXI_SERVICE_AREA, "")
                } else if (context is VendorTourBusActivity) {
                    (context as VendorTourBusActivity).setDisplayDialog(7, AppConstants.FROM_V_TAXI_SERVICE_AREA, "")
                }
            }
            R.id.btn_next -> {
                validateField()
            }
        }
    }

    private fun getfilledData() {
        if (context is VendorTaxiActivity)
            taxi_Service_Request = (context as VendorTaxiActivity).getTaxiFieldPojo()
        if (context is VendorLimoActivity)
            limo_Service_Request = (context as VendorLimoActivity).getLimoFieldPojo()
        if (context is VendorTourBusActivity)
            tourbus_Service_Request = (context as VendorTourBusActivity).getTourBusFieldPojo()

        try {
            if (activity is VendorTaxiActivity) {
                ed_taxicount.setText(taxi_Service_Request.total_taxi)
                ed_seat.setText(taxi_Service_Request.avg_seat_per_taxi)
                ed_rate.setText(taxi_Service_Request.rate_per_km)

                val data = StringBuilder()
                for (regionModel: RegionModel in taxi_Service_Request.service_region_ids) {
                    if (data.length > 0) {
                        data.append(", ")
                    }
                    data.append(regionModel.regionname)
                }
                ed_service.setText(data.toString())
                chk_alldays.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.all_day)
                chk_monday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.mon)
                chk_tuesday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.tue)
                chk_wednesday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.wed)
                chk_thursday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.thu)
                chk_friday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.fri)
                chk_saturday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.sat)
                chk_sunday.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.sun)
                chk_24time.isChecked = AppUtils.getStatusBoolean(taxi_Service_Request.is_24_hours_open)
                ed_starttime.setText(taxi_Service_Request.start_time)
                ed_closingtime.setText(taxi_Service_Request.close_time)
                ed_desc.setText(taxi_Service_Request.description)
            } else if (activity is VendorLimoActivity) {

                ed_taxicount.setText(limo_Service_Request.total_taxi)
                ed_seat.setText(limo_Service_Request.avg_seat_per_taxi)
                ed_rate.setText(limo_Service_Request.rate_per_km)
                val data = StringBuilder()
                for (regionModel: RegionModel in limo_Service_Request.service_region_ids) {
                    if (data.length > 0) {
                        data.append(", ")
                    }
                    data.append(regionModel.regionname)
                }
                ed_service.setText(data.toString())

                chk_alldays.isChecked = AppUtils.getStatusBoolean(limo_Service_Request.all_day)
                chk_monday.isChecked = AppUtils.getStatusBoolean(limo_Service_Request.mon)
                chk_tuesday.isChecked = AppUtils.getStatusBoolean(limo_Service_Request.tue)
                chk_wednesday.isChecked = AppUtils.getStatusBoolean(limo_Service_Request.wed)
                chk_thursday.isChecked = AppUtils.getStatusBoolean(limo_Service_Request.thu)
                chk_friday.isChecked = AppUtils.getStatusBoolean(limo_Service_Request.fri)
                chk_saturday.isChecked = AppUtils.getStatusBoolean(limo_Service_Request.sat)
                chk_sunday.isChecked = AppUtils.getStatusBoolean(limo_Service_Request.sun)
                chk_24time.isChecked = AppUtils.getStatusBoolean(limo_Service_Request.is_24_hours_open)
                ed_starttime.setText(limo_Service_Request.start_time)
                ed_closingtime.setText(limo_Service_Request.close_time)
                ed_desc.setText(limo_Service_Request.description)

            } else if (activity is VendorTourBusActivity) {
                ed_taxicount.setText(tourbus_Service_Request.total_taxi)
                ed_seat.setText(tourbus_Service_Request.avg_seat_per_taxi)
                ed_rate.setText(tourbus_Service_Request.rate_per_km)

                val data = StringBuilder()
                for (regionModel: RegionModel in tourbus_Service_Request.service_region_ids) {
                    if (data.length > 0) {
                        data.append(", ")
                    }
                    data.append(regionModel.regionname)
                }
                ed_service.setText(data.toString())

                chk_alldays.isChecked = AppUtils.getStatusBoolean(tourbus_Service_Request.all_day)
                chk_monday.isChecked = AppUtils.getStatusBoolean(tourbus_Service_Request.mon)
                chk_tuesday.isChecked = AppUtils.getStatusBoolean(tourbus_Service_Request.tue)
                chk_wednesday.isChecked = AppUtils.getStatusBoolean(tourbus_Service_Request.wed)
                chk_thursday.isChecked = AppUtils.getStatusBoolean(tourbus_Service_Request.thu)
                chk_friday.isChecked = AppUtils.getStatusBoolean(tourbus_Service_Request.fri)
                chk_saturday.isChecked = AppUtils.getStatusBoolean(tourbus_Service_Request.sat)
                chk_sunday.isChecked = AppUtils.getStatusBoolean(tourbus_Service_Request.sun)
                chk_24time.isChecked = AppUtils.getStatusBoolean(tourbus_Service_Request.is_24_hours_open)
                ed_starttime.setText(tourbus_Service_Request.start_time)
                ed_closingtime.setText(tourbus_Service_Request.close_time)
                ed_desc.setText(tourbus_Service_Request.description)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance(from: String): VendorTaxiTwoFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorTaxiTwoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_taxi_two_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        til_rate.hint = "" + til_rate.hint + "(" + AppUtils.getCurrencySymbol() + ")"

        getfilledData()
        manageFromEdit()
        initView()

    }

    fun initView() {
        btn_next.setOnClickListener(this)
        ed_starttime.setOnClickListener(this)
        ed_closingtime.setOnClickListener(this)
        ed_service.setOnClickListener(this)
        chk_alldays.setOnCheckedChangeListener(this)
        chk_monday.setOnCheckedChangeListener(this)
        chk_tuesday.setOnCheckedChangeListener(this)
        chk_wednesday.setOnCheckedChangeListener(this)
        chk_thursday.setOnCheckedChangeListener(this)
        chk_friday.setOnCheckedChangeListener(this)
        chk_saturday.setOnCheckedChangeListener(this)
        chk_sunday.setOnCheckedChangeListener(this)
        chk_24time.setOnCheckedChangeListener(this)
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

    fun updateSelectedServiceArea(bean: ArrayList<RegionModel>) {
        val data = StringBuilder()

        val filledList: ArrayList<RegionModel> = ArrayList()

        for (temp in bean) {
            if (temp.isSelected) {
                filledList.add(temp)
                if (data.length > 0) {
                    data.append(", ")
                    dataId.append(", ")
                }
                data.append(temp.regionname)
                dataId.append(temp.regionid)
            }
        }
        if (activity is VendorTaxiActivity)
            taxi_Service_Request.service_region_ids = filledList
        else if (activity is VendorLimoActivity)
            limo_Service_Request.service_region_ids = filledList
        else if (activity is VendorTourBusActivity)
            tourbus_Service_Request.service_region_ids = filledList

        ed_service.setText(data.toString())
    }

    private fun putAllDataToFieldMap(taxi_Service_Request: Ven_Taxi_Service_Request) {
        try {
            taxi_Service_Request.total_taxi = ed_taxicount.text.toString()
            taxi_Service_Request.avg_seat_per_taxi = ed_seat.text.toString()
            taxi_Service_Request.rate_per_km = ed_rate.text.toString()
            taxi_Service_Request.all_day = AppUtils.getStatusString(chk_alldays.isChecked)

            taxi_Service_Request.mon = AppUtils.getStatusString(chk_monday.isChecked)
            taxi_Service_Request.tue = AppUtils.getStatusString(chk_tuesday.isChecked)
            taxi_Service_Request.wed = AppUtils.getStatusString(chk_wednesday.isChecked)
            taxi_Service_Request.thu = AppUtils.getStatusString(chk_thursday.isChecked)
            taxi_Service_Request.fri = AppUtils.getStatusString(chk_friday.isChecked)
            taxi_Service_Request.sat = AppUtils.getStatusString(chk_saturday.isChecked)
            taxi_Service_Request.sun = AppUtils.getStatusString(chk_sunday.isChecked)
            taxi_Service_Request.is_24_hours_open = AppUtils.getStatusString(chk_24time.isChecked)
            taxi_Service_Request.start_time = ed_starttime.text.toString()
            taxi_Service_Request.close_time = ed_closingtime.text.toString()
            taxi_Service_Request.description = ed_desc.text.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun manageFromEdit() {
        if (activity is VendorTaxiActivity) {
            if ((activity as VendorTaxiActivity).isFromedit()) {
                btn_next.setText((activity as VendorTaxiActivity).getString(R.string.save))
            }
        } else if (activity is VendorLimoActivity) {
            if ((activity as VendorLimoActivity).isFromedit()) {
                btn_next.setText((activity as VendorLimoActivity).getString(R.string.save))
            }
        } else if (activity is VendorTourBusActivity) {
            if ((activity as VendorTourBusActivity).isFromedit()) {
                btn_next.setText((activity as VendorTourBusActivity).getString(R.string.save))
            }
        }
    }

    fun validateField() {
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)

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
        if (!(chk_24time.isChecked || (!ed_starttime.text.toString().isEmpty() && !ed_closingtime.text.toString().isEmpty()))) {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.bussines_hours))
            return
        }
        if (!chk_24time.isChecked) {
            if (!AppUtill.compareTime(ed_starttime.text.toString(), ed_closingtime.text.toString())) {
                UiValidator.displayMsgSnack(nest, activity, getString(R.string.choose_valid_time_slot))
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



        if (activity is VendorTaxiActivity) {
            putAllDataToFieldMap(taxi_Service_Request)
            if ((activity as VendorTaxiActivity).isFromedit()) {
                (activity as VendorTaxiActivity).hitApiEdit(taxi_Service_Request)
            } else {
                (activity as VendorTaxiActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.review_your_bussiness), false)
            }
        } else if (activity is VendorLimoActivity) {
            putAllDataToFieldMap(limo_Service_Request)
            if ((activity as VendorLimoActivity).isFromedit()) {
                (activity as VendorLimoActivity).hitApiEdit(limo_Service_Request)
            } else {
                (activity as VendorLimoActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.review_your_bussiness), false)
            }
        } else if (activity is VendorTourBusActivity) {
            putAllDataToFieldMap(tourbus_Service_Request)
            if ((activity as VendorTourBusActivity).isFromedit()) {
                (activity as VendorTourBusActivity).hitApiEdit(tourbus_Service_Request)
            } else {
                (activity as VendorTourBusActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.review_your_bussiness), false)
            }
        }
    }


}