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
import com.virtual.customervendor.customer.ui.activity.ParkingValetActivity
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
import kotlinx.android.synthetic.main.fragment_booking_final_parking.*


class ParkingBookingFragmentFinal : Fragment(), View.OnClickListener {
    //Changes By Himanshu Start
    private val ACTIVITY_REQUEST_CODE: Int = 1112
    var selectedId: Int = 0
    var days: Int = 0
    var taxAmt = String()
    var totalAmt = String()
    //Changes By Himanshu ends

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_next -> {
                //Changes By Himanshu Start

                selectedId = radioGroup_payment_option_parking.checkedRadioButtonId
                if (selectedId == R.id.rb_pay_now_parking || selectedId == R.id.rb_pay_later_parking) {
                    if (selectedId == R.id.rb_pay_now_parking) {
                        request.put(AppKeys.PAYMENT_TYPE, "ONLINE")

                        var intent: Intent = Intent(context, PaymentActivity::class.java)
                        (activity as AppCompatActivity).startActivityForResult(intent, ACTIVITY_REQUEST_CODE)

                    } else {
                        request.put(AppKeys.PAYMENT_TYPE, "PAYLATER")
                        request.put(AppKeys.OFFER_ID, "" + applyOfferModel.offer_id)
                        request.put(AppKeys.OFFER_PRICE, "" + applyOfferModel.offer_price)
                        if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("")) {
                            request.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)
                        } else {
                            if (days > 1) {
                                request.put(AppKeys.ORDER_PRICE, (serviceDetail.parking_charges?.toFloat()?.times(days).toString()))
                            } else {
                                request.put(AppKeys.ORDER_PRICE, "" + serviceDetail.parking_charges)
                            }
                        }
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

        request.put(AppKeys.CARD_TOKEN, "" + card_token)

        request.put(AppKeys.OFFER_ID, "" + applyOfferModel.offer_id)
        request.put(AppKeys.OFFER_PRICE, "" + applyOfferModel.offer_price)
        if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("")) {
            request.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)
        } else {
            if (days > 1) {
                request.put(AppKeys.ORDER_PRICE, (serviceDetail.parking_charges?.toFloat()?.times(days).toString()))
            } else {
                request.put(AppKeys.ORDER_PRICE, "" + serviceDetail.parking_charges)
            }
        }

        hitApi()


    }

    var TAG: String = ParkingBookingFragmentFinal::class.java.name
    var request: MutableMap<String, String> = mutableMapOf()
    var parkingInfo: CustomerBookingListModel = CustomerBookingListModel()
    var datetime: StringBuilder? = null
    var apiService: ApiInterface? = null
    var serviceDetail: VendorServiceDetailModel = VendorServiceDetailModel()
    var applyOfferModel = ApplyOfferModel()


    companion object {
        fun newInstance(): ParkingBookingFragmentFinal {
            val fragment = ParkingBookingFragmentFinal()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_final_parking, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        request = (activity as ParkingValetActivity).getFieldMap()
        parkingInfo = (activity as ParkingValetActivity).parkingInfo
        serviceDetail = (activity as ParkingValetActivity).serviceDetail
        applyOfferModel = (activity as ParkingValetActivity).applyOfferModel

        days = AppUtill.getDifferenceDays(request.get(AppKeys.KEY_FROMDATE)!!, request.get(AppKeys.KEY_TODATE)!!)
        setData()
    }

    fun setData() {
        txtBusiness.setText(parkingInfo.business_name)
        txtcity.setText(parkingInfo.cityname)
        txtregion.setText(parkingInfo.contact_number)
        ed_jou.setText(request.get(AppKeys.KEY_LICENCENO))
        ed_jou.setText(request.get(AppKeys.KEY_LICENCENO))
        ed_date.setText(request.get(AppKeys.KEY_FROMDATE) + " | " + request.get(AppKeys.KEY_FROMTIME))
        ed_todate.setText(request.get(AppKeys.KEY_TODATE) + " | " + request.get(AppKeys.KEY_TOTIME))

        if (applyOfferModel != null && !applyOfferModel.order_price.isEmpty()) {
            ed_orderprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.order_price))
            ed_offerprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.offer_price))

            totalAmt = AppUtill.calculateTotalAmt(serviceDetail.businessData.business_tax!!, applyOfferModel.offer_price)
            taxAmt = AppUtill.calculateTaxAmt(serviceDetail.businessData.business_tax!!, applyOfferModel.offer_price)
        } else {
            if (days > 1) {
                ed_orderprice.setText(AppUtils.getRateWithSymbol((serviceDetail.parking_charges?.toFloat()?.times(days).toString())))
                ed_offerprice.setText(AppUtils.getRateWithSymbol((serviceDetail.parking_charges?.toFloat()?.times(days).toString())))

                totalAmt = AppUtill.calculateTotalAmt(serviceDetail.businessData.business_tax!!, serviceDetail.parking_charges?.toFloat()?.times(days).toString())
                taxAmt = AppUtill.calculateTaxAmt(serviceDetail.businessData.business_tax!!, serviceDetail.parking_charges?.toFloat()?.times(days).toString())
            } else {
                ed_orderprice.setText(AppUtils.getRateWithSymbol(serviceDetail.parking_charges))
                ed_offerprice.setText(AppUtils.getRateWithSymbol(serviceDetail.parking_charges))

                totalAmt = AppUtill.calculateTotalAmt(serviceDetail.businessData.business_tax!!, serviceDetail.parking_charges!!)
                taxAmt = AppUtill.calculateTaxAmt(serviceDetail.businessData.business_tax!!, serviceDetail.parking_charges!!)
            }
        }

        ed_tax.setText(AppUtils.getRateWithSymbol(taxAmt))
        ed_toatlprice.setText(AppUtils.getRateWithSymbol(totalAmt))

        request.put(AppKeys.TOTAL_PRICE, totalAmt)
        request.put(AppKeys.TAX, serviceDetail.businessData.business_tax!!)

        if (parkingInfo.business_image != null && !parkingInfo.business_image.equals(""))
            Glide.with(activity!!).load(parkingInfo.business_image).into(imgBusiness)


    }

    fun hitApi() {
        request.put(AppKeys.SERVICE_ID, parkingInfo.service_id.toString())
        request.put(AppKeys.BUSINESS_ID, parkingInfo.business_id.toString())
        request.put(AppKeys.CATEGORY_ID, AppConstants.CAT_PARKING_VALET.toString())
        request.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_PARKING_VALET.toString())
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
                                //Changes By Himanshu Start

                                (activity as ParkingValetActivity).orderNo = apiResponseRestaurant.data.order_id!!
                                (activity as ParkingValetActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)

                                //Changes By Himanshu ends
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