package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CustomerOrderModel
import com.virtual.customervendor.model.OrderDetailModel
import com.virtual.customervendor.model.StoreCartModel
import com.virtual.customervendor.model.response.BusinessOrderDetailResponse
import com.virtual.customervendor.model.response.CancelOrderResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.StoreItemsViewAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_order_detail_vendor.*
import java.text.SimpleDateFormat
import java.util.*

class VendorOrderDetailActivity : BaseActivity(), View.OnClickListener {
    var datetime: String? = null
    var TAG: String = VendorOrderDetailActivity::class.java.simpleName
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var mTitleFrag: String? = null
    var orderdetailModel = OrderDetailModel()
    var apiService: ApiInterface? = null
    var storeItemsAdapter: StoreItemsViewAdapter? = null
    var orderid: String? = String()
    var currentDate = String()
    var barcode :Boolean = false

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> onBackPressed()
            R.id.btn_cancel -> {
                cancelOrder(orderdetailModel.order_id!!)
            }
            R.id.btn_next -> {
                if (orderdetailModel.checkin.equals("0")) {
                    if (orderdetailModel.category_id == AppConstants.CAT_EVENT_TICKET.toString() || orderdetailModel.category_id == AppConstants.CAT_STORE_SELLER.toString()) {
                        if (chk_term.isChecked)
                            setCheckInOutStatus(orderdetailModel.order_id!!, AppConstants.STATUS_CHECK_IN)
                        else {
                            UiValidator.displayMsgSnack(coordinator, this@VendorOrderDetailActivity, (resources.getString(R.string.please_select_payment_option)))
                        }
                    } else {
                        setCheckInOutStatus(orderdetailModel.order_id!!, AppConstants.STATUS_CHECK_IN)
                    }
                } else if (orderdetailModel.checkout.equals("0")) {
                    if (chk_term.isChecked)
                        setCheckInOutStatus(orderdetailModel.order_id!!, AppConstants.STATUS_CHECK_OUT)
                    else {
                        UiValidator.displayMsgSnack(coordinator, this@VendorOrderDetailActivity, (resources.getString(R.string.please_select_payment_option)))
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_vendor)

        orderid = intent.getStringExtra("orderid")
        var orderModel = intent.extras.getSerializable(AppConstants.OREDER_DATA) as? CustomerOrderModel
        var isComming_from_notification = intent.getBooleanExtra("isComming_from_notification", false)
        barcode= intent.getBooleanExtra("barcode", false)

        apiService = ApiClient.client.create(ApiInterface::class.java)
        currentDate = (SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis())))

        init()

        if (orderid != null && !orderid?.isEmpty()!!) {
            getVendorDetail(orderid!!, "notify")
        } else if (isComming_from_notification) {
            getVendorDetail(orderModel?.order_id!!, "notify")
        } else {
            getVendorDetail(orderModel?.order_id!!, null)
        }
    }

    fun setData() {
        ordernumber.setText(orderdetailModel.order_id)
        ed_datetime.setText(resources.getString(R.string.date) + " : " + orderdetailModel.serviceinformation.book_date)
        ed_time.setText(resources.getString(R.string.time) + " : " + orderdetailModel.serviceinformation.book_time)
        ed_user.setText(orderdetailModel.customer_name)

        ven_mobilenum.setText("+" + orderdetailModel.customer_countrycode + " " + orderdetailModel.customer_phone)
        email.setText("" + orderdetailModel.customer_email)

        ed_noofperson.setText(orderdetailModel.serviceinformation.expect_guest)
        ed_total.setText(AppUtils.getRegisterRateWithSymbol(orderdetailModel.price))



        if (orderdetailModel.checkin.equals("0")) {

            chk_term.visibility = View.GONE
            if (orderdetailModel.category_id == AppConstants.CAT_EVENT_TICKET.toString()) {
                chk_term.visibility = View.VISIBLE
                btn_next.setText(resources.getString(R.string.done))
            } else if (orderdetailModel.category_id == AppConstants.CAT_STORE_SELLER.toString()) {
                chk_term.visibility = View.VISIBLE
                btn_next.setText(resources.getString(R.string.order_complete))
            } else {
                btn_next.setText(resources.getString(R.string.check_in))
            }
        } else if (orderdetailModel.checkout.equals("0")) {

            chk_term.visibility = View.VISIBLE
            if (orderdetailModel.category_id == AppConstants.CAT_EVENT_TICKET.toString()) {
                btn_next.setText(resources.getString(R.string.done))
            } else if (orderdetailModel.category_id == AppConstants.CAT_STORE_SELLER.toString()) {
                btn_next.setText(resources.getString(R.string.order_complete))
            } else {
                btn_next.setText(resources.getString(R.string.check_out))
            }

        } else {
            btn_next.visibility = View.GONE
            chk_term.visibility = View.GONE
        }

        if (orderdetailModel.status.equals(AppConstants.PAYMENT_PAID)) {
            chk_term.setChecked(true)
        }

        if (orderdetailModel.status.equals(AppConstants.PAYMENT_PAID)) {
            paymentstatus.setText(resources.getString(R.string.prepaid))
        } else if (orderdetailModel.status.equals(AppConstants.PAYMENT_PENDING)) {
            paymentstatus.setText(resources.getString(R.string.postpaid))
        } else if (orderdetailModel.status.equals(AppConstants.PAYMENT_CANCEL)) {
            paymentstatus.setText(resources.getString(R.string.cancelled))
            btn_cancel.setText(resources.getString(R.string.cancelled))
            setButtonBg(btn_cancel)
            btn_next.visibility = View.GONE
            chk_term.visibility = View.GONE
        }




        if (convertToDate(orderdetailModel.serviceinformation.from_date_new!!).before(convertToDate(currentDate))) {
            btn_next.visibility = View.GONE
            chk_term.visibility = View.GONE
            setButtonBg(btn_cancel)
        }

        if(barcode){
            btn_cancel.visibility = View.GONE
        }

        if (convertToDate(orderdetailModel.serviceinformation.from_date_new!!).equals(currentDate)) {
            btn_next.visibility = View.VISIBLE
            chk_term.visibility = View.VISIBLE
        }else{
            btn_next.visibility = View.GONE
            chk_term.visibility = View.GONE
        }

        if (orderdetailModel.category_id == AppConstants.CAT_HEALTH_BODYCARE.toString()) {
            hint_orderno.setText(resources.getText(R.string.appointment_number))
            hint_date.visibility = View.GONE
            ed_noofperson.visibility = View.GONE
        } else if (orderdetailModel.category_id == AppConstants.CAT_TRANSPORTATION.toString() &&
                (orderdetailModel.subcategory_id == AppConstants.SUBCAT_TRANS_TAXI.toString() ||
                        orderdetailModel.subcategory_id == AppConstants.SUBCAT_TRANS_LIMO.toString() ||
                        orderdetailModel.subcategory_id == AppConstants.SUBCAT_TRANS_TOURBUS.toString())) {
            ed_datetime.visibility = View.VISIBLE
            ed_journeyto.visibility = View.VISIBLE

            hint_special.visibility = View.VISIBLE
            ed_specialins.visibility = View.VISIBLE

            ed_specialins.setText(orderdetailModel.serviceinformation.special_instruction)

            hint_date.setText(resources.getText(R.string.journey))
            ed_noofperson.setText(resources.getString(R.string.from) + " : " + orderdetailModel.serviceinformation.from_location)
            ed_journeyto.setText(resources.getString(R.string.too) + " : " + orderdetailModel.serviceinformation.to_location)

            ed_datetime.setText(resources.getString(R.string.date) + " : " + orderdetailModel.serviceinformation.from_date)
            ed_time.setText(resources.getString(R.string.time) + " : " + orderdetailModel.serviceinformation.from_time)
        } else if (orderdetailModel.category_id == AppConstants.CAT_TRANSPORTATION.toString() && (orderdetailModel.subcategory_id == AppConstants.SUBCAT_TRANS_SIGHTSEEING.toString())) {

            hint_special.visibility = View.VISIBLE
            ed_specialins.visibility = View.VISIBLE

            ed_specialins.setText(orderdetailModel.serviceinformation.special_instruction)

            ed_datetime.setText(orderdetailModel.serviceinformation.booking_date)
            ed_noofperson.setText(orderdetailModel.serviceinformation.number_of_tourist)
            ed_time.visibility = View.GONE
            hint_datetime.setText(resources.getText(R.string.bookingdate))

        } else if (orderdetailModel.category_id == AppConstants.CAT_PARKING_VALET.toString()) {
            hint_date.setText(resources.getText(R.string.licence_plate))
//            to.visibility = View.VISIBLE
//            ed_todate.visibility = View.VISIBLE
            ed_datetime.setText(resources.getString(R.string.from) + " : " + orderdetailModel.serviceinformation.from_date + " " + orderdetailModel.serviceinformation.from_time)
            ed_time.setText(resources.getString(R.string.too) + " : " + orderdetailModel.serviceinformation.to_date + " " + orderdetailModel.serviceinformation.to_time)
//            ed_todate.setText(orderdetailModel.serviceinformation.to_date + " " + orderdetailModel.serviceinformation.to_time)
            ed_noofperson.setText(orderdetailModel.serviceinformation.licence_plate)
        } else if (orderdetailModel.category_id == AppConstants.CAT_EVENT_TICKET.toString()) {
//            hint_date.setText(resources.getText(R.string.licence_plate))
            ed_noofperson.setText(orderdetailModel.serviceinformation.number_of_person)
        } else if (orderdetailModel.category_id == AppConstants.CAT_STORE_SELLER.toString()) {
            hint_date.visibility = View.GONE
            ed_noofperson.visibility = View.GONE
            ed_datetime.visibility = View.GONE
            hint_datetime.visibility = View.GONE
            ed_time.visibility = View.GONE

            if (orderdetailModel.storeItem.size > 0) {
                hint_item.visibility = View.VISIBLE
                rv_order.visibility = View.VISIBLE
                createAdapterEvents(orderdetailModel.storeItem)
            }
        }

//        if (orderdetailModel.serviceinformation.from_date_new!!?.equals(currentDate) || convertToDate(orderdetailModel.serviceinformation.from_date_new!!).after(convertToDate(currentDate))) {
//            btn_cancel.visibility = View.VISIBLE
//        }
    }


    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.order_detail)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
    }

    fun setButtonBg(view: View) {

        view.setClickable(false)
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_click_deactivate))
        } else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            view.setBackground(getResources().getDrawable(R.drawable.btn_bg_click_deactivate))
        } else {
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_bg_click_deactivate))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun getVendorDetail(orderid: String, notify: String?) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService?.getBusinessOrderDetail("Bearer " + SharedPreferenceManager.getAuthToken(), orderid, notify)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<BusinessOrderDetailResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: BusinessOrderDetailResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            UiValidator.displayMsgSnack(coordinator, this@VendorOrderDetailActivity, detailResponse.message)
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                orderdetailModel = detailResponse.data
                                setData()
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                AppLog.e(TAG, e?.message)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    fun setCheckInOutStatus(orderid: String, status: String) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService?.updateCheckInOutStatus("Bearer " + SharedPreferenceManager.getAuthToken(), orderid, status)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<BusinessOrderDetailResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: BusinessOrderDetailResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                UiValidator.displayMsgSnack(coordinator, this@VendorOrderDetailActivity, detailResponse.message)
                                setResult(Activity.RESULT_OK)
                                finish()
                            } else {
                                UiValidator.displayMsgSnack(coordinator, this@VendorOrderDetailActivity, detailResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()

                            if (e != null)
                                AppLog.e(TAG, e?.message)
                        }

                        override fun onComplete() {
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    fun convertToDate(receivedDate: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.parse(receivedDate)
    }

    fun cancelOrder(orderid: String) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService?.cancelOrder("Bearer " + SharedPreferenceManager.getAuthToken(), orderid)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<CancelOrderResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: CancelOrderResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            UiValidator.displayMsgSnack(coordinator, this@VendorOrderDetailActivity, detailResponse.message)
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                finish()
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                AppLog.e(TAG, e?.message)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }


    private fun createAdapterEvents(eventlisting: ArrayList<StoreCartModel>) {
        storeItemsAdapter = StoreItemsViewAdapter(this, AppConstants.FROM_REVIEW, eventlisting)
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_order.layoutManager = manager
        rv_order.adapter = (storeItemsAdapter)
    }
}
