package com.virtual.customervendor.customer.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.OfferListingActivity
import com.virtual.customervendor.customer.ui.activity.TableBookingActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CustomerTimeModel
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
import kotlinx.android.synthetic.main.fragment_booking_infromation_restaurant.*
import java.text.SimpleDateFormat
import java.util.*
import android.text.Editable
import android.text.TextWatcher
import com.virtual.customer_vendor.utill.AppUtill


class RestaurantBookingInfromationFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener {
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                validateField(AppConstants.CAL_NEXT)
            }

            R.id.viewoffer -> {
                var intent: Intent = Intent(activity, OfferListingActivity::class.java)
                intent.putExtra(AppKeys.BUSINESS_ID, detailResponse.businessData.business_id.toString())
                startActivityForResult(intent, requestCode)
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
            R.id.offer -> {
                validateField(AppConstants.CAL_OFFER)
            }

            R.id.ed_fromdate -> {
                //   AppUtill.getDate(ed_fromdate, activity!!)

                //Changes By Himanshu Starts

                var selectedDays = ArrayList<Int>()
                for (i in 1..7) {
                    if (isValidDate(i)) {
                        selectedDays.add(i)
                    }
                }

                val daysArray: Array<Calendar> = AppUtils.getEnabledDays(selectedDays)
                AppUtils.getDate(activity?.fragmentManager, this, daysArray)
                //Changes By Himanshu Starts

            }
            R.id.ed_fromtime -> {
                if (!ed_fromdate.text.toString().equals("")) {
                    (activity as TableBookingActivity).showTimeSelectionDialog(ed_fromdate.text.toString())
                } else {
                    UiValidator.displayMsgSnack(cons, activity!!, resources.getString(R.string.choose_date_first))
                }
            }
        }
    }


    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, monthOfYear)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val dat1e = SimpleDateFormat("yyyy-MM-dd").format(c.time)
        ed_fromdate.setText(dat1e)
    }

    fun updateSelectedTime(bean: CustomerTimeModel) {
        if (bean != null) {
            timedata = bean
            if (ed_licence.text.isEmpty()) {
                ed_fromtime.setText(bean.slot)
//                UiValidator.displayMsgSnack(nest, activity, resources.getString(R.string.choose_no_of_persons))
            } else if (!(ed_licence.text.toString().toInt() > bean?.remain?.toInt()!!)) {
                ed_fromtime.setText(bean.slot)
            } else {
                UiValidator.displayMsgSnack(nest, activity, resources.getString(R.string.no_enoughseats))
            }

        }
    }

    var TAG: String = RestaurantBookingInfromationFragment::class.java.name;
    var request: MutableMap<String, String> = mutableMapOf();
    var detailResponse: VendorServiceDetailModel = VendorServiceDetailModel()
    var apiService: ApiInterface? = null
    val requestCode: Int = 101
    var timedata = CustomerTimeModel()

    companion object {
        fun newInstance(): RestaurantBookingInfromationFragment {
            val fragment = RestaurantBookingInfromationFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_infromation_restaurant, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    fun initView() {
        apiService = ApiClient.client.create(ApiInterface::class.java)
        request = (activity as TableBookingActivity).getFieldMap()
        detailResponse = (activity as TableBookingActivity).detailResponse
        btn_next.setOnClickListener(this)
        ed_fromdate.setOnClickListener(this)
        ed_fromtime.setOnClickListener(this)
        viewoffer.setOnClickListener(this)
        offer.setOnClickListener(this)
        ed_bcontact.setText(resources.getString(R.string.estimated) + " " + AppUtils.getRateWithSymbol(detailResponse.cost_per_guest) + " /" + resources.getString(R.string.per_guest) + resources.getString(R.string.star))


        ed_fromtime.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!ed_fromtime.text.toString().isEmpty())
                    if (AppUtill.isTimeGreater(ed_fromdate.text.toString() + " " + ed_fromtime.text.toString())) {
                        UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
                        ed_fromtime.setText("")
                    }
            }
        })
    }

    fun validateField(from: String) {
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (ed_licence.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_licence, getString(R.string.field_required))
            return
        }
        if (!(ed_licence.getText().toString().toInt() > 0)) {
            UiValidator.setValidationError(til_licence, getString(R.string.choose_valid_person))
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

        if (ed_fromtime.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_fromtime, getString(R.string.field_required))
            return
        }
        if (til_fromtime.isErrorEnabled()) {
            UiValidator.disableValidationError(til_fromtime)
        }

        if ((ed_licence.text.toString().toInt() > timedata?.remain?.toInt()!!)) {
            UiValidator.displayMsgSnack(nest, activity, resources.getString(R.string.no_enoughseats))
            ed_fromtime.setText("")
            ed_licence.setText("")
            return
        }
        putAllDataToFieldMap()
        if (activity is TableBookingActivity) {
            if (from.equals(AppConstants.CAL_OFFER)) {
                if (!ed_coupon.text.toString().isEmpty())
                    getOfferPrice()
                else {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.nocoupon_code))
                }
            } else if (from.equals(AppConstants.CAL_NEXT)) {
                (activity as TableBookingActivity).setDisplayFragment(5, resources.getString(R.string.confirm_booking), false)
            }
        }
    }

    private fun putAllDataToFieldMap() {
        if (context is TableBookingActivity) {
            request.put(AppKeys.KEY_EXPECTEDGUEST, ed_licence.text.toString())
            request.put(AppKeys.KEY_BOOKDATE, ed_fromdate.text.toString())
            request.put(AppKeys.KEY_BOOKTIME, ed_fromtime.text.toString())
            request.put(AppKeys.ORDER_PRICE, (detailResponse.cost_per_guest?.toDouble()?.times(ed_licence.text.toString().toDouble())).toString())
        }
    }

    private fun isValidDate(day: Int): Boolean {
        when (day) {
            1 -> if (detailResponse.sunday.equals("1"))
                return true
            2 -> if (detailResponse?.monday.equals("1"))
                return true
            3 -> if (detailResponse?.tuesday.equals("1"))
                return true
            4 -> if (detailResponse?.wednesday.equals("1"))
                return true
            5 -> if (detailResponse?.thursday.equals("1"))
                return true
            6 -> if (detailResponse?.friday.equals("1"))
                return true
            7 -> if (detailResponse?.saturday.equals("1"))
                return true
        }
        return false
    }


    private fun getOfferPrice() {
        val orderprice = (detailResponse.cost_per_guest?.toDouble()?.times(ed_licence.text.toString().toDouble())).toString()
        val business_id = detailResponse.businessData.business_id.toString()
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
                            if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
                                UiValidator.displayMsgSnack(cons, activity, userResponse.message)
                                (activity as TableBookingActivity).applyOfferModel = userResponse.data
                                (activity as TableBookingActivity).setDisplayFragment(5, resources.getString(R.string.confirm_booking), false)
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, userResponse.message)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            val result = data?.getSerializableExtra(AppKeys.OFFER_RESULT) as VenderOffersModel
            AppLog.e(TAG, result.toString())
            ed_coupon.setText(result.coupon)
        }
    }
}