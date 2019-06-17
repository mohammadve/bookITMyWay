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
import com.virtual.customervendor.customer.ui.activity.*
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.ApplyOfferModel
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.response.CustomerBookingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking_final.*

class BookingFragmentFinal : Fragment(), View.OnClickListener {
    //Changes By Himanshu Starts

    private val ACTIVITY_REQUEST_CODE: Int = 1113
    var selectedId: Int = 0
    //Changes By Himanshu ends

    override fun onClick(v: View?) {
        //Changes By Himanshu Starts


        selectedId = radioGroup_payment_option_taxi.checkedRadioButtonId
        if (selectedId == R.id.rb_pay_now_taxi || selectedId == R.id.rb_pay_later_taxi) {
            if (selectedId == R.id.rb_pay_now_taxi) {
                var intent: Intent = Intent(context, PaymentActivity::class.java)
                (activity as AppCompatActivity).startActivityForResult(intent, ACTIVITY_REQUEST_CODE)
            } else {
                request.put(AppKeys.PAYMENT_TYPE, "PAYLATER")
                request.put(AppKeys.OFFER_ID, "" + applyOfferModel.offer_id)
                request.put(AppKeys.OFFER_PRICE, "" + applyOfferModel.offer_price)
                if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("") && !applyOfferModel.order_price.equals("null", true)) {
                    request.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)

                } else {
                    request.put(AppKeys.ORDER_PRICE, (vendorServiceDetailModel.rate_per_km?.toDouble()?.times(this!!.totalDistance!!).toString()))
                }
                AppLog.e(TAG, request.toString())
                hitforOrder()
            }

        } else {
            UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.please_select_payment_option))
        }
        //Changes By Himanshu ends


    }

    fun hitAPIForConfirm(card_token: String) {

        request.put(AppKeys.PAYMENT_TYPE, "ONLINE")

        request.put(AppKeys.CARD_TOKEN, "" + card_token)

        request.put(AppKeys.OFFER_ID, "" + applyOfferModel.offer_id)
        request.put(AppKeys.OFFER_PRICE, "" + applyOfferModel.offer_price)
        if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("") && !applyOfferModel.order_price.equals("null", true)) {
            request.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)

        } else {
            request.put(AppKeys.ORDER_PRICE, (vendorServiceDetailModel.rate_per_km?.toDouble()?.times(this!!.totalDistance!!).toString()))

        }
        hitforOrder()
    }

    var TAG: String = BookingFragmentFinal::class.java.name;
    var request: MutableMap<String, String> = mutableMapOf();
    var datetime: StringBuilder? = null
    var apiService: ApiInterface? = null
    var parkingInfo: CustomerBookingListModel = CustomerBookingListModel()
    var vendorServiceDetailModel = VendorServiceDetailModel()
    var applyOfferModel = ApplyOfferModel()
    var totalDistance: Float? = null
    var taxAmt = String()
    var totalAmt = String()


    companion object {
        fun newInstance(): BookingFragmentFinal {
            val fragment = BookingFragmentFinal()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_final, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity is BookRideTaxiActivity) {
            request = (activity as BookRideTaxiActivity).getFieldMap()
            parkingInfo = (activity as BookRideTaxiActivity).customerBookingListModel
            vendorServiceDetailModel = (activity as BookRideTaxiActivity).serviceDetailModel
            applyOfferModel = (activity as BookRideTaxiActivity).applyOfferModel
            totalDistance = (activity as BookRideTaxiActivity).totalDistance
            setData()
        } else if (activity is BookRideLimoActivity) {
            request = (activity as BookRideLimoActivity).getFieldMap()
            parkingInfo = (activity as BookRideLimoActivity).customerBookingListModel
            vendorServiceDetailModel = (activity as BookRideLimoActivity).serviceDetailModel
            applyOfferModel = (activity as BookRideLimoActivity).applyOfferModel
            totalDistance = (activity as BookRideLimoActivity).totalDistance
            setData()
        } else if (activity is BookRideTourBusActivity) {
            request = (activity as BookRideTourBusActivity).getFieldMap()
            parkingInfo = (activity as BookRideTourBusActivity).customerBookingListModel
            vendorServiceDetailModel = (activity as BookRideTourBusActivity).serviceDetailModel
            applyOfferModel = (activity as BookRideTourBusActivity).applyOfferModel
            totalDistance = (activity as BookRideTourBusActivity).totalDistance
            setData()
        }

        btn_next.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
    }

    fun setData() {
        txtBusiness.setText(parkingInfo.business_name)
        txtregion.setText(parkingInfo.contact_number)
        txtcity.setText(parkingInfo.cityname)
        ed_jou.setText(request.get(AppKeys.KEY_FROM_LOCATION))
        ed_tojou.setText(request.get(AppKeys.KEY_TO_LOCATION))

        ed_date.setText(request.get(AppKeys.KEY_FROMDATE))
        ed_time.setText(request.get(AppKeys.KEY_FROMTIME))

        ed_desc.setText(request.get(AppKeys.KEY_SPACIAL_INSTRUCTION))


        if (applyOfferModel != null && !applyOfferModel.order_price.isEmpty()) {
            ed_orderprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.order_price))
            ed_offerprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.offer_price))

            totalAmt = AppUtill.calculateTotalAmt(vendorServiceDetailModel.businessData.business_tax!!, applyOfferModel.offer_price)
            taxAmt = AppUtill.calculateTaxAmt(vendorServiceDetailModel.businessData.business_tax!!, applyOfferModel.offer_price)
        } else {
            ed_orderprice.setText(AppUtils.getRateWithSymbol("" + vendorServiceDetailModel.rate_per_km?.toDouble()?.times(this!!.totalDistance!!)))
            ed_offerprice.setText(AppUtils.getRateWithSymbol("" + vendorServiceDetailModel.rate_per_km?.toDouble()?.times(this!!.totalDistance!!)))

            totalAmt = AppUtill.calculateTotalAmt(vendorServiceDetailModel.businessData.business_tax!!, vendorServiceDetailModel.rate_per_km?.toDouble()?.times(this!!.totalDistance!!).toString())
            taxAmt = AppUtill.calculateTaxAmt(vendorServiceDetailModel.businessData.business_tax!!, vendorServiceDetailModel.rate_per_km?.toDouble()?.times(this!!.totalDistance!!).toString())
        }

        txtdistance.text = "(" + totalDistance.toString() + resources.getString(R.string.km) + ")"

        request.put(AppKeys.TOTAL_PRICE, totalAmt)
        request.put(AppKeys.TAX, vendorServiceDetailModel.businessData.business_tax!!)

        ed_tax.setText(AppUtils.getRateWithSymbol(taxAmt))
        ed_toatlprice.setText(AppUtils.getRateWithSymbol(totalAmt))

        if (parkingInfo.business_image != null && !parkingInfo.business_image.equals(""))
            Glide.with(activity!!).load(parkingInfo.business_image).into(imgBusiness)
    }


    fun hitforOrder() {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            AppLog.e(TAG, request.toString())
            apiService!!.initiateOrder("Bearer " + SharedPreferenceManager.getAuthToken(), request)!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CustomerBookingResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(apiResponseRestaurant: CustomerBookingResponse) {
                            AppLog.e(TAG, apiResponseRestaurant.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (apiResponseRestaurant.status.equals(AppConstants.KEY_SUCCESS)) {
                                //Changes By Himanshu Starts

                                if (activity is BookRideTaxiActivity) {
                                    (activity as BookRideTaxiActivity).orderNo = apiResponseRestaurant.data.order_id!!
                                    (activity as BookRideTaxiActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)
                                } else if (activity is BookRideLimoActivity) {
                                    (activity as BookRideLimoActivity).orderNo = apiResponseRestaurant.data.order_id!!
                                    (activity as BookRideLimoActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)
                                } else if (activity is BookRideTourBusActivity) {
                                    (activity as BookRideTourBusActivity).orderNo = apiResponseRestaurant.data.order_id!!
                                    (activity as BookRideTourBusActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)
                                } else if (activity is BookAppointmentDoctorActivity) {
                                    (activity as BookAppointmentDoctorActivity).orderNo = apiResponseRestaurant.data.order_id!!
                                    (activity as BookAppointmentDoctorActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)
                                }
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