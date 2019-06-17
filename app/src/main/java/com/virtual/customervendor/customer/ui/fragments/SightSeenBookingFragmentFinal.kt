package com.virtual.customervendor.customer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.PaymentActivity
import com.virtual.customervendor.customer.ui.activity.BookRideSightSeenActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.ApplyOfferModel
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.PackageModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.response.CustomerBookingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking_final_sightseen.*

class SightSeenBookingFragmentFinal : Fragment(), View.OnClickListener {
    //Changes By Himanshu Starts

    private val ACTIVITY_REQUEST_CODE: Int = 1118
    var selectedId: Int = 0
    //Changes By Himanshu ends

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_next -> {
                //Changes By Himanshu Starts


                selectedId = radioGroup_payment_option_sight_seeing.checkedRadioButtonId
                if (selectedId == R.id.rb_pay_now_sight_seeing || selectedId == R.id.rb_pay_later_sight_seeing) {
                    if (selectedId == R.id.rb_pay_now_sight_seeing) {

                        var intent: Intent = Intent(context, PaymentActivity::class.java)
                        (activity as AppCompatActivity).startActivityForResult(intent, ACTIVITY_REQUEST_CODE)
                    } else {
                        apirequest.clear();
                        apirequest.put(AppKeys.PAYMENT_TYPE, "PAYLATER")
                        apirequest.put(AppKeys.OFFER_ID, "" + applyOfferModel.offer_id)
                        apirequest.put(AppKeys.OFFER_PRICE, "" + applyOfferModel.offer_price)
                        if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("")) {
                            apirequest.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)

                        } else {
                            apirequest.put(AppKeys.ORDER_PRICE, "" + (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.cost!!.toInt()))
                        }

                        apirequest.put(AppKeys.TOTAL_PRICE, totalAmt)
                        apirequest.put(AppKeys.TAX, businessDetailModel.businessData.business_tax!!)
                        hitApi()
                    }

                } else {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.please_select_payment_option))
                }
                //Changes By Himanshu ends
            }
        }
    }


    fun hitAPIForConfirm(card_token: String) {
        apirequest.clear();
        apirequest.put(AppKeys.CARD_TOKEN, "" + card_token)
        apirequest.put(AppKeys.PAYMENT_TYPE, "ONLINE")
        apirequest.put(AppKeys.OFFER_ID, "" + applyOfferModel.offer_id)
        apirequest.put(AppKeys.OFFER_PRICE, "" + applyOfferModel.offer_price)
        if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("")) {
            apirequest.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)

        } else {
            apirequest.put(AppKeys.ORDER_PRICE, "" + (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.cost!!.toInt()))

        }

        apirequest.put(AppKeys.TOTAL_PRICE, totalAmt)
        apirequest.put(AppKeys.TAX, businessDetailModel.businessData.business_tax!!)
        hitApi()


    }


    var TAG: String = SightSeenBookingFragmentFinal::class.java.name
    var request: MutableMap<String, String> = mutableMapOf()
    var apirequest: MutableMap<String, String> = mutableMapOf()
    var info: CustomerBookingListModel = CustomerBookingListModel()
    var eventInfo: PackageModel = PackageModel()
    var datetime: StringBuilder? = null
    var apiService: ApiInterface? = null
    var applyOfferModel = ApplyOfferModel()
    var businessDetailModel = VendorServiceDetailModel()
    var taxAmt = String()
    var totalAmt = String()

    companion object {
        fun newInstance(): SightSeenBookingFragmentFinal {
            val fragment = SightSeenBookingFragmentFinal()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_final_sightseen, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        info = (activity as BookRideSightSeenActivity).getcustomerBookingListModel()
        eventInfo = (activity as BookRideSightSeenActivity).getSightSeenDetail()
        request = (activity as BookRideSightSeenActivity).getFieldMap()
        applyOfferModel = (activity as BookRideSightSeenActivity).applyOfferModel
        businessDetailModel = (activity as BookRideSightSeenActivity).businessDetailModel
        setData()
    }

    fun setData() {
        txtBusiness.setText(info.business_name)
        txtregion.setText(info.contact_number)
        txtcity.setText(info.cityname)
        if (info.business_image != null && !info.business_image.equals(""))
            Glide.with(activity!!).load(info.business_image).into(imgBusiness)
        tv_event_name.text = eventInfo.package_name

        tv_from.text = eventInfo.start_location
        tv_to.text = eventInfo.end_location

        tv_des.text = eventInfo.description
        tv_date_time_value.setText(request.get(AppKeys.KEY_TODATE))
        tv_no_of_person_value.text = request.get(AppKeys.KEY_NO_OF_PERSON)
        tv_special_instructions_value.text = request.get(AppKeys.KEY_SPACIAL_INSTRUCTION)

        if (applyOfferModel != null && !applyOfferModel.order_price.isEmpty()) {
            ed_orderprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.order_price))
            ed_offerprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.offer_price))

            totalAmt = AppUtill.calculateTotalAmt(businessDetailModel.businessData.business_tax!!, applyOfferModel.offer_price)
            taxAmt = AppUtill.calculateTaxAmt(businessDetailModel.businessData.business_tax!!, applyOfferModel.offer_price)
        } else {
            ed_orderprice.setText(AppUtils.getRateWithSymbol("" + (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.cost!!.toDouble())))
            ed_offerprice.setText(AppUtils.getRateWithSymbol("" + (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.cost!!.toDouble())))

            totalAmt = AppUtill.calculateTotalAmt(businessDetailModel.businessData.business_tax!!, (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.cost!!.toDouble()).toString())
            taxAmt = AppUtill.calculateTaxAmt(businessDetailModel.businessData.business_tax!!, (request.get(AppKeys.KEY_NO_OF_PERSON)!!.toInt() * eventInfo.cost!!.toDouble()).toString())
        }




        ed_tax.setText(AppUtils.getRateWithSymbol(taxAmt))
        ed_toatlprice.setText(AppUtils.getRateWithSymbol(totalAmt))
    }

    fun hitApi() {
        apirequest.put(AppKeys.SERVICE_ID, info.service_id.toString())
        apirequest.put(AppKeys.BUSINESS_ID, info.business_id.toString())
        apirequest.put(AppKeys.CATEGORY_ID, AppConstants.CAT_TRANSPORTATION.toString())
        apirequest.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_TRANS_SIGHTSEEING.toString())
        apirequest.put(AppKeys.KEY_BOOKING_DATE, request.get(AppKeys.KEY_TODATE)!!)
        apirequest.put(AppKeys.KEY_SPACIAL_INSTRUCTION, request.get(AppKeys.KEY_SPACIAL_INSTRUCTION)!!)
        apirequest.put(AppKeys.KEY_NO_OF_TOURIST, request.get(AppKeys.KEY_NO_OF_PERSON)!!)
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
                                (activity as BookRideSightSeenActivity).fieldmap.put(AppKeys.KEY_TRANSACTION_ID, apiResponseRestaurant.data.order_id.toString())
                                (activity as BookRideSightSeenActivity).setDisplayFragment(6, resources.getString(R.string.booking_confirmed), false)
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, apiResponseRestaurant.message)
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
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }

    }

}

