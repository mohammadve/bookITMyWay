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
import com.virtual.customervendor.customer.ui.activity.TableBookingActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.ApplyOfferModel
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.response.CustomerBookingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking_final_restaurant.*

class RestaurantBookingFragmentFinal : Fragment(), View.OnClickListener {
    private val ACTIVITY_REQUEST_CODE: Int = 1111
    var selectedId: Int = 0
    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_next -> {
                selectedId = radioGroup_payment_option.checkedRadioButtonId
                if (selectedId == R.id.rb_pay_now || selectedId == R.id.rb_pay_later) {
                    if (selectedId == R.id.rb_pay_now) {
                        request.put(AppKeys.PAYMENT_TYPE, "ONLINE")
                        var intent: Intent = Intent(context, PaymentActivity::class.java)
                        (activity as AppCompatActivity).startActivityForResult(intent, ACTIVITY_REQUEST_CODE)
                    } else {
                        request.put(AppKeys.PAYMENT_TYPE, "PAYLATER")
                        request.put(AppKeys.OFFER_ID, "" + applyOfferModel.offer_id)
                        request.put(AppKeys.OFFER_PRICE, "" + applyOfferModel.offer_price)
                        if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("") && !applyOfferModel.order_price.equals("null", true)) {
                            request.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)
                        } else {
//                            request.put(AppKeys.ORDER_PRICE, "" + detailResponse.cost_per_guest)
                        }
                        hitApi()
                    }
                } else {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.please_select_payment_option))
                }
            }
        }


    }

    fun hitAPIForConfirm(card_token: String) {

        request.put(AppKeys.PAYMENT_TYPE, "ONLINE")

        request.put(AppKeys.CARD_TOKEN, "" + card_token)

        request.put(AppKeys.OFFER_ID, "" + applyOfferModel.offer_id)
        request.put(AppKeys.OFFER_PRICE, "" + applyOfferModel.offer_price)
        if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("") && !applyOfferModel.order_price.equals("null", true)) {
            request.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)
        } else {
            request.put(AppKeys.ORDER_PRICE, "" + detailResponse.cost_per_guest)

        }

        hitApi()


    }

    var TAG: String = RestaurantBookingFragmentFinal::class.java.name
    var request: MutableMap<String, String> = mutableMapOf()
    var parkingInfo: CustomerBookingListModel = CustomerBookingListModel()
    var datetime: StringBuilder? = null
    var apiService: ApiInterface? = null
    var detailResponse: VendorServiceDetailModel = VendorServiceDetailModel()
    var applyOfferModel = ApplyOfferModel()
    var taxAmt = String()
    var totalAmt = String()


    companion object {
        fun newInstance(): RestaurantBookingFragmentFinal {
            val fragment = RestaurantBookingFragmentFinal()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_final_restaurant, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        request = (activity as TableBookingActivity).getFieldMap()
        parkingInfo = (activity as TableBookingActivity).customerBookingListModel
        detailResponse = (activity as TableBookingActivity).detailResponse
        applyOfferModel = (activity as TableBookingActivity).applyOfferModel
        setData()
    }

    fun setData() {
        txtBusiness.setText(parkingInfo.business_name)
        txtregion.setText(parkingInfo.contact_number)
        txtcity.setText(parkingInfo.cityname)
        ed_jou.setText(request.get(AppKeys.KEY_EXPECTEDGUEST))
        ed_date.setText(resources.getString(R.string.date) + " : " + request.get(AppKeys.KEY_BOOKDATE))
        ed_time.setText(resources.getString(R.string.time) + " : " + request.get(AppKeys.KEY_BOOKTIME))

        if (applyOfferModel != null && !applyOfferModel.order_price.isEmpty()) {
            ed_orderprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.order_price))
            ed_offerprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.offer_price))

            totalAmt = AppUtill.calculateTotalAmt(detailResponse.businessData.business_tax!!, applyOfferModel.offer_price)
            taxAmt = AppUtill.calculateTaxAmt(detailResponse.businessData.business_tax!!, applyOfferModel.offer_price)

        } else {
            ed_orderprice.setText(AppUtils.getRateWithSymbol(request.get(AppKeys.ORDER_PRICE)))
            ed_offerprice.setText(AppUtils.getRateWithSymbol(request.get(AppKeys.ORDER_PRICE)))

            totalAmt = AppUtill.calculateTotalAmt(detailResponse.businessData.business_tax!!, request.get(AppKeys.ORDER_PRICE)!!)
            taxAmt = AppUtill.calculateTaxAmt(detailResponse.businessData.business_tax!!, request.get(AppKeys.ORDER_PRICE)!!)
        }

        request.put(AppKeys.TOTAL_PRICE, totalAmt)
        request.put(AppKeys.TAX, detailResponse.businessData.business_tax!!)

        ed_tax.setText(AppUtils.getRateWithSymbol(taxAmt))
        ed_toatlprice.setText(AppUtils.getRateWithSymbol(totalAmt))

        if (parkingInfo.business_image != null && !parkingInfo.business_image.equals(""))
            Glide.with(activity!!).load(parkingInfo.business_image).into(imgBusiness)
    }

    fun hitApi() {
        request.put(AppKeys.SERVICE_ID, parkingInfo.service_id.toString())
        request.put(AppKeys.BUSINESS_ID, parkingInfo.business_id.toString())
        request.put(AppKeys.CATEGORY_ID, AppConstants.CAT_RESTAURANT_DINNIG.toString())
        request.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_RESTAURANT_DINNIG.toString())
        AppLog.e(TAG, request.toString())
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            apiService!!.initiateOrder("Bearer " + SharedPreferenceManager.getAuthToken(), request)!!.subscribeOn(Schedulers.io())
                    .observeOn(mainThread())
                    .subscribe(object : Observer<CustomerBookingResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(apiResponseRestaurant: CustomerBookingResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, apiResponseRestaurant.toString())
                            if (apiResponseRestaurant.status.equals(AppConstants.KEY_SUCCESS)) {
                                (activity as TableBookingActivity).orderNo = apiResponseRestaurant.data.order_id!!
                                (activity as TableBookingActivity).setDisplayFragment(6, resources.getString(R.string.booking_confirmed), false)
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, apiResponseRestaurant.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                AppLog.e(TAG, e.message)
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