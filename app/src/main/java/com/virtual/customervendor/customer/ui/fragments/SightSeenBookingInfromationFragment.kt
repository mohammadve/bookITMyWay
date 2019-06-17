package com.virtual.customervendor.customer.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.BookRideSightSeenActivity
import com.virtual.customervendor.customer.ui.activity.OfferListingActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.PackageModel
import com.virtual.customervendor.model.VenderOffersModel
import com.virtual.customervendor.model.response.ApplyOfferResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking_infromation_sightseen.*
import java.text.SimpleDateFormat

class SightSeenBookingInfromationFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> activity?.onBackPressed()
            R.id.btn_next -> validateField(AppConstants.CAL_NEXT)
            R.id.offer -> validateField(AppConstants.CAL_OFFER)
            R.id.ed_date -> AppUtill.getDate(ed_date, activity!!)
            R.id.viewoffer -> {
                var intent: Intent = Intent(activity, OfferListingActivity::class.java)
                intent.putExtra(AppKeys.BUSINESS_ID, sightseenDetail.business_id.toString())
                startActivityForResult(intent, requestCode)
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }


        }
    }

    var TAG: String = SightSeenBookingInfromationFragment::class.java.name;
    var request: MutableMap<String, String> = mutableMapOf();
    var sightseenDetail = PackageModel()
    val requestCode: Int = 101
    var apiService: ApiInterface? = null

    companion object {
        fun newInstance(): SightSeenBookingInfromationFragment {
            val fragment = SightSeenBookingInfromationFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_infromation_sightseen, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sightseenDetail = (activity as BookRideSightSeenActivity).getSightSeenDetail()
        initView()
    }

    fun initView() {
        apiService = ApiClient.client.create(ApiInterface::class.java)
        btn_next.setOnClickListener(this)
        ed_date.setOnClickListener(this)
        viewoffer.setOnClickListener(this)
        offer.setOnClickListener(this)

        tv_event_name.text = sightseenDetail.package_name
        tv_fees.text = AppUtils.getRateWithSymbol(sightseenDetail.cost) + "/" + resources.getString(R.string.person)
    }

    fun validateField(from: String) {
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (ed_date.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_date, getString(R.string.field_required))
            return
        }
        if (til_date.isErrorEnabled()) {
            UiValidator.disableValidationError(til_date)
        }
        val date = SimpleDateFormat("yyyy-MM-dd").parse(ed_date.getText().toString())
        val dayOfTheWeek = DateFormat.format("EEEE", date) as String // Thursday
        if (dayOfTheWeek.equals("Monday", true) && !sightseenDetail.monday.equals("1")) {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
            return
        }
        if (dayOfTheWeek.equals("Tuesday", true) && !sightseenDetail.tuesday.equals("1")) {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
            return
        }
        if (dayOfTheWeek.equals("Wednesday", true) && !sightseenDetail.wednesday.equals("1")) {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
            return
        }
        if (dayOfTheWeek.equals("Thursday", true) && !sightseenDetail.thursday.equals("1")) {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
            return
        }
        if (dayOfTheWeek.equals("Friday", true) && !sightseenDetail.friday.equals("1")) {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
            return
        }
        if (dayOfTheWeek.equals("Saturday", true) && !sightseenDetail.saturday.equals("1")) {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
            return
        }
        if (dayOfTheWeek.equals("Sunday", true) && !sightseenDetail.sunday.equals("1")) {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
            return
        }

        if (ed_no_of_person.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_no_of_person, getString(R.string.field_required))
            return
        }

        if (!(ed_no_of_person.getText().toString().toInt() > 0)) {
            UiValidator.setValidationError(til_no_of_person, getString(R.string.choose_valid_person))
            return
        }

        if (til_no_of_person.isErrorEnabled()) {
            UiValidator.disableValidationError(til_no_of_person)
        }

        if (ed_instruction.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_instructon, getString(R.string.field_required))
            return
        }
        if (til_instructon.isErrorEnabled()) {
            UiValidator.disableValidationError(til_instructon)
        }




        putAllDataToFieldMap()
        (activity as BookRideSightSeenActivity).fieldmap = request
        if (from.equals(AppConstants.CAL_OFFER)) {
            if (!ed_coupon.text.toString().isEmpty()) {
                getOfferPrice()
            } else {
                UiValidator.displayMsgSnack(cons, activity, getString(R.string.nocoupon_code))
            }

        } else if (from.equals(AppConstants.CAL_NEXT)) {
            (activity as BookRideSightSeenActivity).setDisplayFragment(5, resources.getString(R.string.confirm_booking), false)
        }

    }

    private fun putAllDataToFieldMap() {
        request.put(AppKeys.KEY_TODATE, ed_date.text.toString())
        request.put(AppKeys.KEY_SPACIAL_INSTRUCTION, ed_instruction.text.toString())
        request.put(AppKeys.KEY_NO_OF_PERSON, ed_no_of_person.text.toString())
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
        val orderprice = (sightseenDetail.cost?.toInt()?.times(ed_no_of_person.text.toString().toInt())).toString()
        val business_id = sightseenDetail.business_id.toString()
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
                                (activity as BookRideSightSeenActivity).applyOfferModel = userResponse.data
                                (activity as BookRideSightSeenActivity).setDisplayFragment(5, resources.getString(R.string.confirm_booking), false)
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


}