package com.virtual.customervendor.customer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.PaymentActivity
import com.virtual.customervendor.customer.ui.activity.BookAppointmentDoctorActivity
import com.virtual.customervendor.customer.ui.activity.BookAppointmentHairActivity
import com.virtual.customervendor.customer.ui.activity.BookAppointmentMassageActivity
import com.virtual.customervendor.customer.ui.activity.BookAppointmentNailActivity
import com.virtual.customervendor.customer.ui.adapter.SelectedServiceAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.ApplyOfferModel
import com.virtual.customervendor.model.ItemPriceModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.response.CustomerBookingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_appointment_final.*


class AppointmentFinalFragment : Fragment(), View.OnClickListener {
    //Changes By Himanshu Starts

    private lateinit var selected_service_adapter: SelectedServiceAdapter
    private val ACTIVITY_REQUEST_CODE: Int = 1114
    var selectedId: Int = 0
    var taxAmt = String()
    var totalAmt = String()
    var selectedServiceMenu: ArrayList<ItemPriceModel> = ArrayList<ItemPriceModel>()

    //Changes By Himanshu ends
    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_next -> {

                //Changes By Himanshu Starts

                selectedId = radioGroup_payment_option_appointment.checkedRadioButtonId
                if ((activity is BookAppointmentDoctorActivity) || (activity is BookAppointmentNailActivity)) {
                    selectedId = R.id.rb_pay_later_appointment
                }
                if (selectedId == R.id.rb_pay_now_appointment || selectedId == R.id.rb_pay_later_appointment) {

                    if (selectedId == R.id.rb_pay_now_appointment) {
                        var intent: Intent = Intent(context, PaymentActivity::class.java)
                        (activity as AppCompatActivity).startActivityForResult(intent, ACTIVITY_REQUEST_CODE)

                    } else {


                        apirequest.clear();
                        apirequest.put(AppKeys.PAYMENT_TYPE, "PAYLATER")
                        apirequest.put(AppKeys.OFFER_ID, "" + applyOfferModel.offer_id)
                        apirequest.put(AppKeys.OFFER_PRICE, "" + applyOfferModel.offer_price)
                        if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("") && !applyOfferModel.order_price.equals("null", true)) {
                            apirequest.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)

                        } else {
                            apirequest.put(AppKeys.ORDER_PRICE, "" + info.fees_per_visit)

                        }

                        apirequest.put(AppKeys.TOTAL_PRICE, totalAmt)
                        apirequest.put(AppKeys.TAX, info.businessData.business_tax!!)
                        hitApi()

                    }
                } else {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.please_select_payment_option))
//                    Toast.makeText(activity, "Please select payment option", Toast.LENGTH_SHORT).show()
                }


            }
        }
    }

    fun hitAPIForConfirm(card_token: String) {
        apirequest.clear();
        apirequest.put(AppKeys.PAYMENT_TYPE, "ONLINE")

        apirequest.put(AppKeys.CARD_TOKEN, "" + card_token)

        apirequest.put(AppKeys.OFFER_ID, "" + applyOfferModel.offer_id)
        apirequest.put(AppKeys.OFFER_PRICE, "" + applyOfferModel.offer_price)
        if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("") && !applyOfferModel.order_price.equals("null", true)) {
            apirequest.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)

        } else {
            apirequest.put(AppKeys.ORDER_PRICE, "" + info.fees_per_visit)

        }

        apirequest.put(AppKeys.TOTAL_PRICE, totalAmt)
        apirequest.put(AppKeys.TAX, info.businessData.business_tax!!)

        hitApi()


    }

    //Changes By Himanshu ends
    var TAG: String = AppointmentFinalFragment::class.java.name
    var request: MutableMap<String, String> = mutableMapOf()
    var apirequest: MutableMap<String, String> = mutableMapOf()
    var info: VendorServiceDetailModel = VendorServiceDetailModel()
    var datetime: StringBuilder? = null
    var apiService: ApiInterface? = null
    var sunCategoryId: Int? = null
    var applyOfferModel = ApplyOfferModel()


    companion object {
        fun newInstance(): AppointmentFinalFragment {
            val fragment = AppointmentFinalFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_appointment_final, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)

        if (activity is BookAppointmentDoctorActivity) {
            info = (activity as BookAppointmentDoctorActivity).getbusinessDetailModel()
            request = (activity as BookAppointmentDoctorActivity).getFieldMap()
            sunCategoryId = AppConstants.SUBCAT_HEALTH_DOCTOR
            applyOfferModel = (activity as BookAppointmentDoctorActivity).applyOfferModel

            PaymentOption_appointment.visibility = View.GONE
            radioGroup_payment_option_appointment.visibility = View.GONE
            radio.visibility = View.GONE

        } else if (activity is BookAppointmentHairActivity) {
            info = (activity as BookAppointmentHairActivity).getbusinessDetailModel()
            request = (activity as BookAppointmentHairActivity).getFieldMap()
            sunCategoryId = AppConstants.SUBCAT_HEALTH_HAIR
            applyOfferModel = (activity as BookAppointmentHairActivity).applyOfferModel

            selected_service_adapter = SelectedServiceAdapter(activity!!, (activity as BookAppointmentHairActivity).serviceSelectedItems) { serviceModel ->
                // serviceModelList = serviceModel
            }
            val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            rv_selected_service?.layoutManager = manager
            rv_selected_service?.adapter = selected_service_adapter

            selectedServiceMenu = (activity as BookAppointmentHairActivity).serviceSelectedItems


        } else if (activity is BookAppointmentMassageActivity) {
            info = (activity as BookAppointmentMassageActivity).getbusinessDetailModel()
            request = (activity as BookAppointmentMassageActivity).getFieldMap()
            sunCategoryId = AppConstants.SUBCAT_HEALTH_PHYSIO
            applyOfferModel = (activity as BookAppointmentMassageActivity).applyOfferModel

            selected_service_adapter = SelectedServiceAdapter(activity!!, (activity as BookAppointmentMassageActivity).serviceSelectedItems) { serviceModel ->
                // serviceModelList = serviceModel
            }
            val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            rv_selected_service?.layoutManager = manager
            rv_selected_service?.adapter = selected_service_adapter
            selectedServiceMenu = (activity as BookAppointmentMassageActivity).serviceSelectedItems


        } else if (activity is BookAppointmentNailActivity) {
            info = (activity as BookAppointmentNailActivity).getbusinessDetailModel()
            request = (activity as BookAppointmentNailActivity).getFieldMap()
            sunCategoryId = AppConstants.SUBCAT_HEALTH_NAIL
            applyOfferModel = (activity as BookAppointmentNailActivity).applyOfferModel

            PaymentOption_appointment.visibility = View.GONE
            radio.visibility = View.GONE
            radioGroup_payment_option_appointment.visibility = View.GONE


            selected_service_adapter = SelectedServiceAdapter(activity!!, (activity as BookAppointmentNailActivity).serviceSelectedItems) { serviceModel ->
                // serviceModelList = serviceModel
            }
            val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            rv_selected_service?.layoutManager = manager
            rv_selected_service?.adapter = selected_service_adapter

            selectedServiceMenu = (activity as BookAppointmentNailActivity).serviceSelectedItems


        }
        setData()
    }

    fun setData() {
        txtBusiness.setText(info.businessData.business_name)
        txtregion.setText(info.businessData.contact_number)


        txtcity.setText(info.businessData.regions.regionname)

        ed_date.setText(request.get(AppKeys.KEY_TODATE))

        ed_time.setText(request.get(AppKeys.KEY_TOTIME))


        if (applyOfferModel != null && !applyOfferModel.order_price.isEmpty()) {
            ed_orderprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.order_price))
            ed_offerprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.offer_price))

            totalAmt = AppUtill.calculateTotalAmt(info.businessData.business_tax!!, applyOfferModel.offer_price)
            taxAmt = AppUtill.calculateTaxAmt(info.businessData.business_tax!!, applyOfferModel.offer_price)
        } else {
            ed_orderprice.setText(AppUtils.getRateWithSymbol(info.fees_per_visit))
            ed_offerprice.setText(AppUtils.getRateWithSymbol(info.fees_per_visit))

            totalAmt = AppUtill.calculateTotalAmt(info.businessData.business_tax!!, info.fees_per_visit!!)
            taxAmt = AppUtill.calculateTaxAmt(info.businessData.business_tax!!, info.fees_per_visit!!)
        }

        ed_tax.setText(AppUtils.getRateWithSymbol(taxAmt))
        ed_toatlprice.setText(AppUtils.getRateWithSymbol(totalAmt))

        if (info.businessData.business_image != null && !info.businessData.business_image.equals(""))
            Glide.with(activity!!).load(info.businessData.business_image).into(imgBusiness)
    }

    fun hitApi() {

        if (activity is BookAppointmentHairActivity || activity is BookAppointmentMassageActivity || activity is BookAppointmentNailActivity) {
            var serviceStrings =""
            var serviceTimingStrings =""
            for (item in selectedServiceMenu) {
                serviceStrings= serviceStrings+","+ item.itemName
                serviceTimingStrings=serviceTimingStrings+","+ item.serviceTime

            }

            serviceStrings=   serviceStrings.substring(1,serviceStrings.length)
            serviceTimingStrings=   serviceTimingStrings.substring(1,serviceTimingStrings.length)

            var serviceJson = Gson().toJson(serviceStrings)
            var serviceTimeJson = Gson().toJson(serviceTimingStrings)
            apirequest.put(AppKeys.SERVICES_NAME, serviceJson)
            apirequest.put(AppKeys.SERVICES_TIME, serviceTimeJson)

        }


        apirequest.put(AppKeys.SERVICE_ID, info.service_id.toString())
        apirequest.put(AppKeys.BUSINESS_ID, info.businessData.business_id.toString())
        apirequest.put(AppKeys.CATEGORY_ID, AppConstants.CAT_HEALTH_BODYCARE.toString())
        apirequest.put(AppKeys.SUBCATEGORY_ID, sunCategoryId.toString())
        apirequest.put(AppKeys.KEY_BOOK_DATE, request.get(AppKeys.KEY_TODATE)!!)
        apirequest.put(AppKeys.KEY_BOOK_TIME, request.get(AppKeys.KEY_TOTIME)!!)
        if (activity is BookAppointmentDoctorActivity)
            apirequest.put(AppKeys.KEY_APPOINTMENT_REASON, request.get(AppKeys.KEY_APPOINTMENT_REASON)!!)

        AppLog.e(TAG, apirequest.toString())
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
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
                                //Changes By Himanshu Starts

                                if (activity is BookAppointmentDoctorActivity) {

                                    (activity as BookAppointmentDoctorActivity).orderNo = apiResponseRestaurant.data.order_id!!
                                    (activity as BookAppointmentDoctorActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)

                                } else if (activity is BookAppointmentHairActivity) {

                                    (activity as BookAppointmentHairActivity).fieldmap.put(AppKeys.KEY_TRANSACTION_ID, apiResponseRestaurant.data.order_id.toString())
                                    (activity as BookAppointmentHairActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)

                                } else if (activity is BookAppointmentMassageActivity) {

                                    (activity as BookAppointmentMassageActivity).fieldmap.put(AppKeys.KEY_TRANSACTION_ID, apiResponseRestaurant.data.order_id.toString())
                                    (activity as BookAppointmentMassageActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)

                                } else if (activity is BookAppointmentNailActivity) {
                                    (activity as BookAppointmentNailActivity).fieldmap.put(AppKeys.KEY_TRANSACTION_ID, apiResponseRestaurant.data.order_id.toString())
                                    (activity as BookAppointmentNailActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)
                                }


                                //Changes By Himanshu Ends
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