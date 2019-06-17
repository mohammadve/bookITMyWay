package com.virtual.customervendor.customer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.PaymentActivity
import com.virtual.customervendor.customer.ui.activity.EventsActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.ApplyOfferModel
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.response.CustomerBookingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_event_booking_confirm.*

class EventBookingConfirmFragment : Fragment(), View.OnClickListener {
    //Changes By Himanshu Starts

    private val ACTIVITY_REQUEST_CODE: Int = 1116
    var selectedId: Int = 0
    //Changes By Himanshu ends

    override fun onClick(v: View?) {
        //Changes By Himanshu Starts

        selectedId = radioGroup_payment_option_event.checkedRadioButtonId
        if (selectedId == R.id.rb_pay_now_event || selectedId == R.id.rb_pay_later_event) {
            if (selectedId == R.id.rb_pay_now_event) {
                var intent: Intent = Intent(context, PaymentActivity::class.java)
                (activity as AppCompatActivity).startActivityForResult(intent, ACTIVITY_REQUEST_CODE)
            } else {
                apirequest.clear()
                apirequest.put(AppKeys.PAYMENT_TYPE, "PAYLATER")
                apirequest.put(AppKeys.OFFER_ID, applyOfferModel.offer_id)
                apirequest.put(AppKeys.OFFER_PRICE, applyOfferModel.offer_price)

                apirequest.put(AppKeys.ORDER_PRICE, "" + (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.ticket_price!!.toDouble()))

                apirequest.put(AppKeys.TOTAL_PRICE, totalAmt)
                apirequest.put(AppKeys.TAX, detailResponse.businessData.business_tax!!)

                hitApi()
            }
        } else {
            Toast.makeText(activity, "Please select payment option", Toast.LENGTH_SHORT).show()
        }
        //Changes By Himanshu ends

    }


    fun hitAPIForConfirm(card_token: String) {
        apirequest.clear()
        apirequest.put(AppKeys.PAYMENT_TYPE, "ONLINE")

        apirequest.put(AppKeys.CARD_TOKEN, "" + card_token)

        apirequest.put(AppKeys.OFFER_ID, applyOfferModel.offer_id)
        apirequest.put(AppKeys.OFFER_PRICE, applyOfferModel.offer_price)

        apirequest.put(AppKeys.ORDER_PRICE, "" + (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.ticket_price!!.toInt()))

        apirequest.put(AppKeys.TOTAL_PRICE, totalAmt)
        apirequest.put(AppKeys.TAX, detailResponse.businessData.business_tax!!)
        hitApi()


    }

    var TAG: String = EventBookingConfirmFragment::class.java.name
    var request: MutableMap<String, String> = mutableMapOf()
    var apirequest: MutableMap<String, String> = mutableMapOf()
    var info: CustomerBookingListModel = CustomerBookingListModel()
    var eventInfo: EventDetail = EventDetail()
    var datetime: StringBuilder? = null
    var apiService: ApiInterface? = null
    var sunCategoryId: Int? = null
    var applyOfferModel = ApplyOfferModel()
    var taxAmt = String()
    var totalAmt = String()
    var detailResponse = VendorServiceDetailModel()


    companion object {
        fun newInstance(): EventBookingConfirmFragment {
            val fragment = EventBookingConfirmFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_event_booking_confirm, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        info = (activity as EventsActivity).getcustomerBookingListModel()
        eventInfo = (activity as EventsActivity).eventDetail()
        request = (activity as EventsActivity).getFieldMap()
        applyOfferModel = (activity as EventsActivity).applyOfferModel
        detailResponse = (activity as EventsActivity).businessDetailModel
        setData()
    }

    fun setData() {
        txtBusiness.setText(info.business_name)
        txtregion.setText(info.contact_number)
        txtcity.setText(info.cityname)
        if (info.business_image != null && !info.business_image.equals(""))
            Glide.with(activity!!).load(info.business_image).into(imgBusiness)
        tv_event_name.text = eventInfo.name

        tv_startdate.text = eventInfo.start_date!!.split(" ")[0]
        tv_enddate.text = eventInfo.end_date!!.split(" ")[0]

        tv_des.text = eventInfo.description

        tv_starttime.text = eventInfo.start_time
        tv_endtime.text = eventInfo.close_time

        tv_no_of_person_value.text = request.get(AppKeys.KEY_NO_OF_PERSON)


        if (applyOfferModel != null && !applyOfferModel.order_price.isEmpty()) {
            ed_orderprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.order_price))
            ed_offerprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.offer_price))

            totalAmt = AppUtill.calculateTotalAmt(detailResponse.businessData.business_tax!!, applyOfferModel.offer_price)
            taxAmt = AppUtill.calculateTaxAmt(detailResponse.businessData.business_tax!!, applyOfferModel.offer_price)
        } else {
            ed_orderprice.setText(AppUtils.getRateWithSymbol("" + (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.ticket_price!!.toDouble())))
            ed_offerprice.setText(AppUtils.getRateWithSymbol("" + (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.ticket_price!!.toDouble())))

            totalAmt = AppUtill.calculateTotalAmt(detailResponse.businessData.business_tax!!, (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.ticket_price!!.toDouble()).toString())
            taxAmt = AppUtill.calculateTaxAmt(detailResponse.businessData.business_tax!!, (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.ticket_price!!.toDouble()).toString())
        }


        ed_tax.setText(AppUtils.getRateWithSymbol(taxAmt))
        ed_toatlprice.setText(AppUtils.getRateWithSymbol(totalAmt))
    }

    fun hitApi() {
        apirequest.put(AppKeys.SERVICE_ID, eventInfo.service_id.toString())
        apirequest.put(AppKeys.BUSINESS_ID, info.business_id.toString())
        apirequest.put(AppKeys.CATEGORY_ID, AppConstants.CAT_EVENT_TICKET.toString())
        apirequest.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_EVENT_TICKET.toString())
        apirequest.put(AppKeys.KEY_BOOK_DATE, request.get(AppKeys.KEY_TODATE)!!)
        apirequest.put(AppKeys.KEY_BOOK_TIME, request.get(AppKeys.KEY_TOTIME)!!)
        apirequest.put(AppKeys.KEY_NO_OF_PERSON, request.get(AppKeys.KEY_NO_OF_PERSON)!!)
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            AppLog.e(TAG, apirequest.toString())
            apiService!!.initiateOrder("Bearer " + SharedPreferenceManager.getAuthToken(), apirequest)!!.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CustomerBookingResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(apiResponseRestaurant: CustomerBookingResponse) {
                            AppLog.e(TAG, apiResponseRestaurant.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (apiResponseRestaurant.status.equals(AppConstants.KEY_SUCCESS)) {
                                (activity as EventsActivity).fieldmap.put(AppKeys.KEY_TRANSACTION_ID, apiResponseRestaurant.data.order_id.toString())
                                (activity as EventsActivity).setDisplayFragment(6, resources.getString(R.string.booking_confirmed), false)
                            } else {
                                UiValidator.displayMsgSnack(nest, activity, apiResponseRestaurant.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            if (e != null)
                                AppLog.e(TAG, e.message)
                            ProgressDialogLoader.progressDialogDismiss()
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.no_internet_connection))
        }

    }

}