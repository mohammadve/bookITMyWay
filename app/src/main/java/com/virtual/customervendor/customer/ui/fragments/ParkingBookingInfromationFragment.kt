package com.virtual.customervendor.customer.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.OfferListingActivity
import com.virtual.customervendor.customer.ui.activity.ParkingValetActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.ApplyOfferModel
import com.virtual.customervendor.model.VenderOffersModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.response.ApplyOfferResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking_infromation_parking.*
import java.text.SimpleDateFormat
import java.util.*

class ParkingBookingInfromationFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener, TextWatcher {
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (!ed_fromtime.text.toString().isEmpty())
            if (AppUtill.isTimeGreater(ed_fromdate.text.toString() + " " + ed_fromtime.text.toString())) {
                UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
                ed_fromtime.setText("")
            }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, monthOfYear)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val dat1e = SimpleDateFormat("yyyy-MM-dd").format(c.time)
        ed_fromdate.setText(dat1e)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> activity?.onBackPressed()

            R.id.viewoffer -> {
                var intent: Intent = Intent(activity, OfferListingActivity::class.java)
                intent.putExtra(AppKeys.BUSINESS_ID, serviceDetail.businessData.business_id.toString())
                startActivityForResult(intent, requestCode)
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }

            R.id.offer -> {
                validateField(AppConstants.CAL_OFFER)
            }

            R.id.btn_next -> {
                validateField(AppConstants.CAL_NEXT)
                (activity as ParkingValetActivity).applyOfferModel = ApplyOfferModel("", "", "")
            }

            R.id.ed_fromdate -> {
                var selectedDays = ArrayList<Int>()
                for (i in 1..7) {
                    if (isValidDate(i)) {
                        selectedDays.add(i)
                    }
                }
                val daysArray: Array<Calendar> = AppUtils.getEnabledDays(selectedDays)
                AppUtils.getDate(activity?.fragmentManager, this, daysArray)
            }
            R.id.ed_fromtime -> {
                AppUtils.getTimeNew(ed_fromtime, activity as AppCompatActivity?)
//                AppUtill.getTime(ed_fromtime, activity!!)
            }

            R.id.ed_date -> {
                var selectedDays = ArrayList<Int>()
                for (i in 1..7) {
                    if (isValidDate(i)) {
                        selectedDays.add(i)
                    }
                }
                val daysArray: Array<Calendar> = AppUtils.getEnabledDays(selectedDays)
                AppUtils.getDate(activity?.fragmentManager, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val c = Calendar.getInstance()
                    c.set(Calendar.YEAR, year)
                    c.set(Calendar.MONTH, monthOfYear)
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dat1e = SimpleDateFormat("yyyy-MM-dd").format(c.time)
                    ed_date.setText(dat1e)

                }, daysArray)
            }
            R.id.ed_time -> {
                AppUtils.getTimeNew(ed_time, activity as AppCompatActivity?)
//                AppUtill.getTime(ed_time, activity!!)
            }

        }
    }

    var TAG: String = ParkingBookingInfromationFragment::class.java.name;
    var request: MutableMap<String, String> = mutableMapOf()
    var serviceDetail: VendorServiceDetailModel = VendorServiceDetailModel()
    val requestCode: Int = 101
    var apiService: ApiInterface? = null

    companion object {
        fun newInstance(): ParkingBookingInfromationFragment {
            val fragment = ParkingBookingInfromationFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_infromation_parking, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        apiService = ApiClient.client.create(ApiInterface::class.java)
        request = (activity as ParkingValetActivity).getFieldMap()
        serviceDetail = (activity as ParkingValetActivity).serviceDetail
        btn_next.setOnClickListener(this)
        ed_fromdate.setOnClickListener(this)
        ed_fromtime.setOnClickListener(this)
        ed_date.setOnClickListener(this)
        ed_time.setOnClickListener(this)
        viewoffer.setOnClickListener(this)
        offer.setOnClickListener(this)
        btn_next.setOnClickListener(this)

        ed_fromtime.addTextChangedListener(this)

        ed_bcontact.setText(resources.getString(R.string.estimated) + " " + AppUtils.getRateWithSymbol(serviceDetail.parking_charges) + resources.getString(R.string.star))
    }

    fun validateField(from: String) {
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (ed_licence.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_licence, getString(R.string.field_required))
            return
        }
        if (til_licence.isErrorEnabled()) {
            UiValidator.disableValidationError(til_licence)
        }

        if (ed_fromdate.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_fromdate, getString(R.string.field_required))
            return
        }
        if (til_fromdate.isErrorEnabled()) {
            UiValidator.disableValidationError(til_fromdate)
        }

        if (ed_date.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_date, getString(R.string.field_required))
            return
        }
        if (til_date.isErrorEnabled()) {
            UiValidator.disableValidationError(til_date)
        }

        if (!AppUtill.compareDate(ed_fromdate.getText().toString(), ed_date.getText().toString())) {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
            return
        }


        if (ed_fromtime.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_fromtime, getString(R.string.field_required))
            return
        }
        if (til_fromtime.isErrorEnabled()) {
            UiValidator.disableValidationError(til_fromtime)
        }

        if (ed_time.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_time, getString(R.string.field_required))
            return
        }
        if (til_time.isErrorEnabled()) {
            UiValidator.disableValidationError(til_time)
        }

        if (ed_fromdate.getText().toString().equals(ed_date.getText().toString())) {
            if (!AppUtill.compareTime(ed_fromtime.getText().toString(), ed_time.getText().toString())) {
                UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
                return
            }
        }




        putAllDataToFieldMap()
        if (activity is ParkingValetActivity) {
            if (from.equals(AppConstants.CAL_OFFER)) {
                if (!ed_coupon.text.toString().isEmpty())
                    getOfferPrice()
                else {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.nocoupon_code))
                }
            } else if (from.equals(AppConstants.CAL_NEXT)) {
                (activity as ParkingValetActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
            }
        }
    }

    private fun putAllDataToFieldMap() {
        if (context is ParkingValetActivity) {
            request.put(AppKeys.KEY_LICENCENO, ed_licence.text.toString())
            request.put(AppKeys.KEY_FROMDATE, ed_fromdate.text.toString())
            request.put(AppKeys.KEY_TODATE, ed_date.text.toString())
            request.put(AppKeys.KEY_FROMTIME, ed_fromtime.text.toString())
            request.put(AppKeys.KEY_TOTIME, ed_time.text.toString())
        }
    }

    private fun isValidDate(day: Int): Boolean {
        when (day) {
            1 -> if (serviceDetail.sunday.equals("1"))
                return true
            2 -> if (serviceDetail?.monday.equals("1"))
                return true
            3 -> if (serviceDetail?.tuesday.equals("1"))
                return true
            4 -> if (serviceDetail?.wednesday.equals("1"))
                return true
            5 -> if (serviceDetail?.thursday.equals("1"))
                return true
            6 -> if (serviceDetail?.friday.equals("1"))
                return true
            7 -> if (serviceDetail?.saturday.equals("1"))
                return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            val result = data?.getSerializableExtra(AppKeys.OFFER_RESULT) as VenderOffersModel
            AppLog.e(TAG, result.toString())
            ed_coupon.setText(result.coupon)
        }
    }

    private fun getOfferPrice() {

        var days = AppUtill.getDifferenceDays(request.get(AppKeys.KEY_FROMDATE)!!, request.get(AppKeys.KEY_TODATE)!!)
        AppLog.e(TAG, "DATe" + days)

        var orderprice = String()
        if (days > 0) {
            orderprice = (serviceDetail.parking_charges?.toFloat()?.times(days).toString())
        } else {
            orderprice = (serviceDetail.parking_charges!!)
        }
        val business_id = serviceDetail.businessData.business_id.toString()
        val coupon = ed_coupon.text.toString()
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))
            apiService?.getOfferPrice("Bearer " + SharedPreferenceManager.getAuthToken(), business_id, coupon, orderprice)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<ApplyOfferResponse> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onNext(userResponse: ApplyOfferResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, userResponse.toString())
                            UiValidator.displayMsgSnack(cons, activity, userResponse.message)
                            if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
                                (activity as ParkingValetActivity).applyOfferModel = userResponse.data
                                (activity as ParkingValetActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                UiValidator.displayMsgSnack(cons, activity, e.message)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }
    }

}