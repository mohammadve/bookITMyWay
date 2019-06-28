package com.virtual.customervendor.customer.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.*
import com.virtual.customervendor.customer.ui.adapter.SelectedServiceAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.*
import com.virtual.customervendor.model.response.ApplyOfferResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_appointment_information.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppointmentInformationFragment : Fragment(), View.OnClickListener {
    var selected_service_adapter: SelectedServiceAdapter? = null
    var selectedTimeSlot: CustomerTimeModel = CustomerTimeModel()
    var servicemenu: ArrayList<ItemPriceModel> = ArrayList<ItemPriceModel>()
    var timeSlotList: ArrayList<CustomerTimeModel> = ArrayList<CustomerTimeModel>()
    fun updateSelectedTime(bean: CustomerTimeModel, cityResponse: ArrayList<CustomerTimeModel>) {
        timeSlotList = cityResponse
        if (bean != null) {
            selectedTimeSlot = bean
            ed_time.setText(bean.slot)

        }
    }

    fun updateSelectedSpeciality(bean: AppointmentReasonModel) {
        if (bean != null) {
            ed_appreason.setText(bean.reason)
            request.put(AppKeys.KEY_APPOINTMENT_REASON, bean.reason!!)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> activity?.onBackPressed()

            R.id.ed_appreason -> {
                if (activity is BookAppointmentDoctorActivity) {
                    (activity as BookAppointmentDoctorActivity).setDisplayDialog(10, AppConstants.FROM_V_TAXI_SERVICE_AREA)
                }
            }


            R.id.viewoffer -> {
                var intent: Intent = Intent(activity, OfferListingActivity::class.java)
                intent.putExtra(AppKeys.BUSINESS_ID, businessDetailModel.businessData.business_id.toString())
                startActivityForResult(intent, requestCode)
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
            R.id.btn_next -> validateField(AppConstants.CAL_NEXT)
            R.id.offer -> validateField(AppConstants.CAL_OFFER)
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
                if (!ed_date.text.toString().equals("")) {
                    if (activity is BookAppointmentDoctorActivity)
                        (activity as BookAppointmentDoctorActivity).showTimeSelectionDialog(ed_date.text.toString())
                    else if (activity is BookAppointmentHairActivity)
                        (activity as BookAppointmentHairActivity).showTimeSelectionDialog(ed_date.text.toString())
                    else if (activity is BookAppointmentNailActivity)
                        (activity as BookAppointmentNailActivity).showTimeSelectionDialog(ed_date.text.toString())
                    else if (activity is BookAppointmentMassageActivity)
                        (activity as BookAppointmentMassageActivity).showTimeSelectionDialog(ed_date.text.toString())
                } else {
                    UiValidator.displayMsgSnack(cons, activity!!, resources.getString(R.string.choose_date_first))
                }
            }
            R.id.ed_services -> {
                if (activity is BookAppointmentHairActivity)
                    (activity as BookAppointmentHairActivity).showServiceSelectionDialogMulti(ed_services.text.toString())
                else if (activity is BookAppointmentNailActivity)
                    (activity as BookAppointmentNailActivity).showServiceSelectionDialogMulti(ed_services.text.toString())
                else if (activity is BookAppointmentMassageActivity)
                    (activity as BookAppointmentMassageActivity).showServiceSelectionDialogMulti(ed_services.text.toString())

            }

        }
    }

    var TAG: String = AppointmentInformationFragment::class.java.name;
    var request: MutableMap<String, String> = mutableMapOf();
    var businessDetailModel = VendorServiceDetailModel()
    val requestCode: Int = 101
    var apiService: ApiInterface? = null

    companion object {
        fun newInstance(): AppointmentInformationFragment {
            val fragment = AppointmentInformationFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_appointment_information, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity is BookAppointmentDoctorActivity) {
            til_appreason.visibility = View.VISIBLE
            ed_services.visibility = View.GONE

            businessDetailModel = (activity as BookAppointmentDoctorActivity).getbusinessDetailModel()
            request = (activity as BookAppointmentDoctorActivity).getFieldMap()
        } else if (activity is BookAppointmentHairActivity) {
            businessDetailModel = (activity as BookAppointmentHairActivity).getbusinessDetailModel()
            request = (activity as BookAppointmentHairActivity).getFieldMap()
            ed_services.visibility = View.VISIBLE
        } else if (activity is BookAppointmentMassageActivity) {
            businessDetailModel = (activity as BookAppointmentMassageActivity).getbusinessDetailModel()
            request = (activity as BookAppointmentMassageActivity).getFieldMap()
            ed_services.visibility = View.VISIBLE
        } else if (activity is BookAppointmentNailActivity) {
            businessDetailModel = (activity as BookAppointmentNailActivity).getbusinessDetailModel()
            request = (activity as BookAppointmentNailActivity).getFieldMap()
            ed_services.visibility = View.VISIBLE

        }
        initView()
    }

    fun initView() {
        apiService = ApiClient.client.create(ApiInterface::class.java)
        btn_next.setOnClickListener(this)
        ed_date.setOnClickListener(this)
        ed_time.setOnClickListener(this)
        ed_services.setOnClickListener(this)
        viewoffer.setOnClickListener(this)
        offer.setOnClickListener(this)
        ed_appreason.setOnClickListener(this)
        tv_doctor_name.text = businessDetailModel.businessData.business_name
        tv_fees.text = AppUtils.getRateWithSymbol(businessDetailModel.fees_per_visit) + "/Visit"


        ed_time.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!ed_time.text.toString().isEmpty())

                    if (activity !is BookAppointmentDoctorActivity && servicemenu.size > 1) {


                        if (AppUtill.isTimeGreater(ed_date.text.toString() + " " + ed_time.text.toString())) {
                            UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
                            ed_time.setText("")
                        } else {


                            var pos = 10000
                            var x = 0
                            while (x < timeSlotList.size) {

                                if (timeSlotList.get(x).slot.equals(selectedTimeSlot.slot)) {
                                    pos = x
                                    break
                                }
                                x++
                            }

                            var isTimeSlotFree = false

                            var i = 0
                            while (i < servicemenu.size) {

                                if ((pos + i) == timeSlotList.size) {
                                    isTimeSlotFree = false
                                    break
                                }

                                if (timeSlotList.get(pos + i) != null) {

                                    if (timeSlotList.get(pos + i).remain > 0) {
                                        isTimeSlotFree = true
                                        servicemenu.get(i).serviceTime = timeSlotList.get(pos + i).slot
                                    } else {
                                        isTimeSlotFree = false
                                        break
                                    }


                                }

                                i++
                            }


                            if (!isTimeSlotFree) {
                                UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
                                ed_time.setText("")
                            } else {
                                if (activity is BookAppointmentHairActivity) {
                                    (activity as BookAppointmentHairActivity).serviceSelectedItems = servicemenu

                                } else if (activity is BookAppointmentMassageActivity) {
                                    (activity as BookAppointmentMassageActivity).serviceSelectedItems = servicemenu

                                } else if (activity is BookAppointmentNailActivity) {
                                    (activity as BookAppointmentNailActivity).serviceSelectedItems = servicemenu
                                }
                            }

                        }

                    } else {
                        if (AppUtill.isTimeGreater(ed_date.text.toString() + " " + ed_time.text.toString())) {
                            UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
                            ed_time.setText("")
                        }
                    }

            }
        })

        selected_service_adapter = SelectedServiceAdapter(activity!!, servicemenu) { serviceModel ->
            // serviceModelList = serviceModel


        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        rv_selected_service?.layoutManager = manager
        rv_selected_service?.adapter = selected_service_adapter
    }

    fun validateField(from: String) {
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)

        if (activity is BookAppointmentDoctorActivity) {
            if (ed_appreason.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_appreason, getString(R.string.field_required))
                return
            }
            if (til_appreason.isErrorEnabled()) {
                UiValidator.disableValidationError(til_appreason)
            }
        }


        if (servicemenu.size == 0) {
            UiValidator.setValidationError(servicees, getString(R.string.field_required))
            return
        }

        if (ed_date.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_date, getString(R.string.field_required))
            return
        }
        if (til_date.isErrorEnabled()) {
            UiValidator.disableValidationError(til_date)
        }

        if (ed_time.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_time, getString(R.string.field_required))
            return
        }
        if (til_time.isErrorEnabled()) {
            UiValidator.disableValidationError(til_time)
        }





        putAllDataToFieldMap()
        if (activity is BookAppointmentDoctorActivity) {
            if (from.equals(AppConstants.CAL_OFFER)) {
                if (!ed_coupon.text.toString().isEmpty())
                    getOfferPrice()
                else {
                    UiValidator.displayMsgSnack(nest, activity, resources.getString(R.string.nocoupon_code))
                }
            } else if (from.equals(AppConstants.CAL_NEXT)) {
                (activity as BookAppointmentDoctorActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
            }
        } else if (activity is BookAppointmentHairActivity) {
            if (from.equals(AppConstants.CAL_OFFER)) {
                if (!ed_coupon.text.toString().isEmpty())
                    getOfferPrice()
                else {
                    UiValidator.displayMsgSnack(nest, activity, resources.getString(R.string.nocoupon_code))
                }
            } else if (from.equals(AppConstants.CAL_NEXT)) {
                (activity as BookAppointmentHairActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
            }
        } else if (activity is BookAppointmentMassageActivity) {
            if (from.equals(AppConstants.CAL_OFFER)) {
                if (!ed_coupon.text.toString().isEmpty())
                    getOfferPrice() else {
                    UiValidator.displayMsgSnack(nest, activity, resources.getString(R.string.nocoupon_code))
                }
            } else if (from.equals(AppConstants.CAL_NEXT)) {
                (activity as BookAppointmentMassageActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
            }
        } else if (activity is BookAppointmentNailActivity) {
            if (from.equals(AppConstants.CAL_OFFER)) {
                if (!ed_coupon.text.toString().isEmpty()) {
                    getOfferPrice()
                } else {
                    UiValidator.displayMsgSnack(nest, activity, resources.getString(R.string.nocoupon_code))
                }
            } else if (from.equals(AppConstants.CAL_NEXT)) {
                (activity as BookAppointmentNailActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
            }
        }
    }

    private fun putAllDataToFieldMap() {
        if (context is BookAppointmentDoctorActivity
                || context is BookAppointmentHairActivity
                || context is BookAppointmentMassageActivity
                || context is BookAppointmentNailActivity) {
            request.put(AppKeys.KEY_TODATE, ed_date.text.toString())
            request.put(AppKeys.KEY_TOTIME, ed_time.text.toString())
        }
    }

    private fun isValidDate(day: Int): Boolean {
        when (day) {
            1 -> if (businessDetailModel.sunday.equals("1"))
                return true
            2 -> if (businessDetailModel?.monday.equals("1"))
                return true
            3 -> if (businessDetailModel?.tuesday.equals("1"))
                return true
            4 -> if (businessDetailModel?.wednesday.equals("1"))
                return true
            5 -> if (businessDetailModel?.thursday.equals("1"))
                return true
            6 -> if (businessDetailModel?.friday.equals("1"))
                return true
            7 -> if (businessDetailModel?.saturday.equals("1"))
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
        var orderprice: String? = ""
        if (activity is BookAppointmentDoctorActivity) {
            orderprice = (businessDetailModel.fees_per_visit)

        } else if (activity is BookAppointmentNailActivity) {
            var tempPrice=0f

            for (temp in (activity as BookAppointmentNailActivity).serviceSelectedItems) {

                 tempPrice = tempPrice + temp.itemPrice.toFloat()

            }
            orderprice = tempPrice.toString()

        } else if (activity is BookAppointmentHairActivity) {
            var tempPrice=0f
            for (temp in (activity as BookAppointmentHairActivity).serviceSelectedItems) {

                 tempPrice = tempPrice + temp.itemPrice.toFloat()

            }
            orderprice = tempPrice.toString()
        } else if (activity is BookAppointmentMassageActivity) {
            var tempPrice=0f

            for (temp in (activity as BookAppointmentMassageActivity).serviceSelectedItems) {

                 tempPrice = tempPrice + temp.itemPrice.toFloat()

            }
            orderprice = tempPrice.toString()
        }


        val business_id = businessDetailModel.businessData.business_id.toString()
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
                                if (activity is BookAppointmentDoctorActivity) {
                                    (activity as BookAppointmentDoctorActivity).applyOfferModel = userResponse.data
                                    (activity as BookAppointmentDoctorActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
                                } else if (activity is BookAppointmentHairActivity) {
                                    (activity as BookAppointmentHairActivity).applyOfferModel = userResponse.data
                                    (activity as BookAppointmentHairActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
                                } else if (activity is BookAppointmentMassageActivity) {
                                    (activity as BookAppointmentMassageActivity).applyOfferModel = userResponse.data
                                    (activity as BookAppointmentMassageActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
                                } else if (activity is BookAppointmentNailActivity) {
                                    (activity as BookAppointmentNailActivity).applyOfferModel = userResponse.data
                                    (activity as BookAppointmentNailActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
                                }
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, userResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                UiValidator.displayMsgSnack(cons, activity, e.message)
                        }

                        override fun onComplete() {}
                    })

        } else {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }
    }

    fun updateSelectedServiceList(bean: ArrayList<ItemPriceModel>) {
        if (servicemenu.size > 0) {
            //  UiValidator.displayMsgSnack(cons, activity, "Please sel")
            ed_time.setText("")
        }

        servicemenu = bean
        // selected_service_adapter?.notifyDataSetChanged()
        if (activity is BookAppointmentHairActivity) {
            (activity as BookAppointmentHairActivity).serviceSelectedItems = servicemenu

        } else if (activity is BookAppointmentMassageActivity) {
            (activity as BookAppointmentMassageActivity).serviceSelectedItems = servicemenu

        } else if (activity is BookAppointmentNailActivity) {
            (activity as BookAppointmentNailActivity).serviceSelectedItems = servicemenu
        }

        selected_service_adapter = SelectedServiceAdapter(activity!!, servicemenu) { serviceModel ->
            // serviceModelList = serviceModel


        }

        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        rv_selected_service?.layoutManager = manager
        rv_selected_service?.adapter = selected_service_adapter
        if (servicemenu.size > 0) {
            if (servicees.isErrorEnabled()) {
                UiValidator.disableValidationError(servicees)
            }
        }

        var totalCost = 0f
        for (item in servicemenu) {
            totalCost = totalCost + item.itemPrice.toFloat()

        }


        if (activity is BookAppointmentHairActivity) {
            (activity as BookAppointmentHairActivity).getbusinessDetailModel().fees_per_visit = totalCost.toString()
        } else if (activity is BookAppointmentMassageActivity) {
            (activity as BookAppointmentHairActivity).getbusinessDetailModel().fees_per_visit = totalCost.toString()
        } else if (activity is BookAppointmentNailActivity) {
            (activity as BookAppointmentHairActivity).getbusinessDetailModel().fees_per_visit = totalCost.toString()
        }


    }

}