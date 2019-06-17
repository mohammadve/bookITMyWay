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
import com.virtual.customervendor.customer.ui.activity.EventsActivity
import com.virtual.customervendor.customer.ui.activity.OfferListingActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.model.VenderOffersModel
import com.virtual.customervendor.model.response.ApplyOfferResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking_event.*

class EventBookingFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                validateField(AppConstants.CAL_NEXT)
            }

            R.id.offer -> {
                validateField(AppConstants.CAL_OFFER)
            }

            R.id.viewoffer -> {
                var intent: Intent = Intent(activity, OfferListingActivity::class.java)
                intent.putExtra(AppKeys.BUSINESS_ID, eventDetail.business_id.toString())
                startActivityForResult(intent, requestCode)
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
        }
    }

    var TAG: String = EventBookingFragment::class.java.name;
    var request: MutableMap<String, String> = mutableMapOf();
    var eventDetail = EventDetail()
    val requestCode: Int = 101
    var apiService: ApiInterface? = null


    companion object {
        fun newInstance(): EventBookingFragment {
            val fragment = EventBookingFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_event, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventDetail = (activity as EventsActivity).eventDetail()
        initView()
    }

    fun initView() {
        apiService = ApiClient.client.create(ApiInterface::class.java)
        btn_next.setOnClickListener(this)
        ed_date.setOnClickListener(this)
        offer.setOnClickListener(this)
        viewoffer.setOnClickListener(this)

        tv_event_name.text = eventDetail.name
        tv_fees.text = AppUtils.getRateWithSymbol(eventDetail.ticket_price) + resources.getString(R.string.ticket)
        ed_date.setText(eventDetail.start_date)
        ed_enddate.setText(eventDetail.end_date)
    }

    fun validateField(from: String) {
        if (ed_no_of_person.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_no_of_person, getString(R.string.field_required))
            return
        }
        if (til_no_of_person.isErrorEnabled()) {
            UiValidator.disableValidationError(til_no_of_person)
        }
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)


        putAllDataToFieldMap()
        (activity as EventsActivity).fieldmap = request
        if (from.equals(AppConstants.CAL_OFFER)) {
            if (!ed_coupon.text.toString().isEmpty()) {
                getOfferPrice()
            } else {
                UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.nocoupon_code))
            }
        } else if (from.equals(AppConstants.CAL_NEXT)) {
            (activity as EventsActivity).setDisplayFragment(5, resources.getString(R.string.confirm_booking), false)

        }

    }

    private fun getOfferPrice() {
        val orderprice = (eventDetail.ticket_price)
        val business_id = eventDetail.business_id
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
                                (activity as EventsActivity).applyOfferModel = userResponse.data
                                (activity as EventsActivity).setDisplayFragment(5, resources.getString(R.string.confirm_booking), false)
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


    private fun putAllDataToFieldMap() {
        request.put(AppKeys.KEY_TODATE, ed_date.text.toString())
        request.put(AppKeys.KEY_TOTIME, ed_enddate.text.toString())
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


}