package com.virtual.customervendor.customer.ui.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
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
import kotlinx.android.synthetic.main.activity_order_detail_customer.*
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import org.json.JSONObject


class CustomerOrderDetailActivity : BaseActivity(), View.OnClickListener {
    var datetime: String? = null
    var TAG: String = CustomerOrderDetailActivity::class.java.simpleName
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var mTitleFrag: String? = null
    var apiInterface: ApiInterface? = null

    var orderdetailModel = OrderDetailModel()
    var storeItemsAdapter: StoreItemsViewAdapter? = null
    var currentDate = String()
    var orderid: String? = String()


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }


            R.id.btn_cancel -> {
                cancelOrder(orderdetailModel.order_id!!)
            }
            R.id.btn_next -> {
                hitNext(orderdetailModel.order_id!!, AppConstants.STATUS_I_AM_WAITING)
            }
            R.id.here -> {
                hitNext(orderdetailModel.order_id!!, AppConstants.STATUS_I_AM_HERE)
            }
            R.id.btn_arrived -> {
                hitNext(orderdetailModel.order_id!!, AppConstants.STATUS_I_AM_ARRIVED)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_customer)
        var orderModel = intent.extras.getSerializable(AppConstants.OREDER_DATA) as? CustomerOrderModel
        var isComming_from_notification = intent.getBooleanExtra("isComming_from_notification", false)

        orderid = intent.getStringExtra("orderid")

        apiInterface = ApiClient.client.create(ApiInterface::class.java)

        currentDate = (SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis())))



        if (orderid != null && !orderid?.isEmpty()!!) {
            getVendorDetail(orderid!!, "notify")
        } else if (isComming_from_notification) {
            getVendorDetail(orderModel?.order_id!!, "notify")
        } else {
            getVendorDetail(orderModel?.order_id!!, null)
        }
        init()
    }


    fun convertToDate(receivedDate: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.parse(receivedDate)
    }

    fun setData() {
        ordernumber.setText(orderdetailModel.order_id)
//        ed_datetime.setText(resources.getString(R.string.date) + " : " + orderdetailModel.date)
//        ed_bookedtime.setText(resources.getString(R.string.time) + " : " + orderdetailModel.time)

        ven_mobilenum.setText("+" + orderdetailModel.vendor_countrycode + " " + orderdetailModel.vendor_phone)
        email.setText("" + orderdetailModel.vendor_email)

        ed_user.setText("" + orderdetailModel.business_name)
        ed_noofperson.setText(orderdetailModel.serviceinformation.expect_guest)
        ed_total.setText(AppUtils.getRateWithSymbol(orderdetailModel.price))


        if (orderdetailModel.payment_type.equals(AppConstants.PAY_ONLINE)) {
            paymentstatus.setText(resources.getString(R.string.prepaid))
        } else if (orderdetailModel.payment_type.equals(AppConstants.PAY_LATER)) {
            paymentstatus.setText(resources.getString(R.string.postpaid))
        }


        if (orderdetailModel.status.equals(AppConstants.PAYMENT_PAID)) {
            paymentstatus.setText(resources.getString(R.string.prepaid))
        } else if (orderdetailModel.status.equals(AppConstants.PAYMENT_PENDING)) {
            paymentstatus.setText(resources.getString(R.string.postpaid))
        } else if (orderdetailModel.status.equals(AppConstants.PAYMENT_CANCEL)) {
            paymentstatus.setText(resources.getString(R.string.cancelled))
            btn_cancel.setText(resources.getString(R.string.cancelled))
            setButtonBg(btn_cancel)
        }


//        if (orderdetailModel.serviceinformation.from_date_new!!?.equals(currentDate) || convertToDate(orderdetailModel.serviceinformation.from_date_new!!).after(convertToDate(currentDate))) {
//            btn_cancel.visibility = View.VISIBLE
//        }

        if (convertToDate(orderdetailModel.serviceinformation.from_date_new!!).before(convertToDate(currentDate))) {
            setButtonBg(btn_cancel)
        }

        ed_bookingdatetime.setText(resources.getString(R.string.date) + " : " + orderdetailModel.serviceinformation.from_date)
        ed_bookingtime.setText(resources.getString(R.string.time) + " : " + orderdetailModel.serviceinformation.from_time)

        if (orderdetailModel.subcategory_name != null && !orderdetailModel.subcategory_name.equals("")) {
            ed_booking_for.setText(orderdetailModel.subcategory_name)
        } else {
            ed_booking_for.setText(orderdetailModel.category_name)
        }
        if (!(orderdetailModel.category_id == AppConstants.CAT_TRANSPORTATION.toString() && (orderdetailModel.subcategory_id == AppConstants.SUBCAT_TRANS_SIGHTSEEING.toString()))
                && !(orderdetailModel.category_id == AppConstants.CAT_STORE_SELLER.toString()) && !(orderdetailModel.category_id == AppConstants.CAT_EVENT_TICKET.toString())) {
            if (orderdetailModel.serviceinformation.from_date_new!!?.equals(currentDate) && orderdetailModel?.vendor_alert != null) {

//                if (orderdetailModel?.vendor_alert.equals("0")) {
                btn_next.visibility = View.VISIBLE
                here.visibility = View.VISIBLE
                btn_arrived.visibility = View.VISIBLE
                /*  } else*/

                if (orderdetailModel.status.equals(AppConstants.PAYMENT_CANCEL)) {
                    setButtonBg(btn_arrived)
                    setButtonBg(here)
                    setButtonBg(btn_next)
                } else {
                    if (orderdetailModel?.vendor_alert.equals(AppConstants.STATUS_I_AM_ARRIVED)) {
                        setButtonBg(btn_arrived)
                    } else if (orderdetailModel?.vendor_alert.equals(AppConstants.STATUS_I_AM_HERE)) {
                        setButtonBg(btn_arrived)
                        setButtonBg(here)
                    } else if (orderdetailModel?.vendor_alert.equals(AppConstants.STATUS_I_AM_WAITING)) {
                        setButtonBg(btn_arrived)
                        setButtonBg(here)
                        setButtonBg(btn_next)
                    }
                }


            }
        }



        if (orderdetailModel.category_id == AppConstants.CAT_HEALTH_BODYCARE.toString()) {
//            btn_next.visibility = View.VISIBLE
            hint_orderno.setText(resources.getText(R.string.appointment_number))
            hint_date.visibility = View.GONE
            ed_noofperson.visibility = View.GONE

            ed_bookingtime.visibility = View.VISIBLE
            ed_bookingdatetime.setText(resources.getString(R.string.date) + " : " + orderdetailModel.serviceinformation.book_date)
            ed_bookingtime.setText(resources.getString(R.string.time) + " : " + orderdetailModel.serviceinformation.book_time)

        } else if (orderdetailModel.category_id == AppConstants.CAT_TRANSPORTATION.toString() &&
                (orderdetailModel.subcategory_id == AppConstants.SUBCAT_TRANS_TAXI.toString() ||
                        orderdetailModel.subcategory_id == AppConstants.SUBCAT_TRANS_LIMO.toString() ||
                        orderdetailModel.subcategory_id == AppConstants.SUBCAT_TRANS_TOURBUS.toString())) {
            ed_to.visibility = View.VISIBLE
            ed_bookingtime.visibility = View.VISIBLE

            hint_special.visibility = View.VISIBLE
            ed_specialins.visibility = View.VISIBLE

            hint_date.setText(resources.getText(R.string.journey))

            ed_noofperson.setText(resources.getString(R.string.from) + " : " + orderdetailModel.serviceinformation.from_location)
            ed_to.setText(resources.getString(R.string.too) + " : " + orderdetailModel.serviceinformation.to_location)

            ed_bookingdatetime.setText(resources.getString(R.string.date) + " : " + orderdetailModel.serviceinformation.from_date)
            ed_bookingtime.setText(resources.getString(R.string.time) + " : " + orderdetailModel.serviceinformation.from_time)

            ed_specialins.setText(orderdetailModel.serviceinformation.special_instruction)

        } else if (orderdetailModel.category_id == AppConstants.CAT_TRANSPORTATION.toString() && (orderdetailModel.subcategory_id == AppConstants.SUBCAT_TRANS_SIGHTSEEING.toString())) {
            btn_next.visibility = View.GONE
            hint_special.visibility = View.VISIBLE
            ed_specialins.visibility = View.VISIBLE

            ed_noofperson.setText(orderdetailModel.serviceinformation.number_of_tourist)
            ed_bookingdatetime.setText(resources.getString(R.string.date) + " : " + orderdetailModel.serviceinformation.booking_date)

            ed_specialins.setText(orderdetailModel.serviceinformation.special_instruction)
        } else if (orderdetailModel.category_id == AppConstants.CAT_PARKING_VALET.toString()) {

            ed_bookingtime.visibility = View.VISIBLE
            hint_date.setText(resources.getText(R.string.licence_plate))
            ed_noofperson.setText(orderdetailModel.serviceinformation.licence_plate)
            ed_bookingdatetime.setText(resources.getString(R.string.date) + " : " + orderdetailModel.serviceinformation.from_date)
            ed_bookingtime.setText(resources.getString(R.string.time) + " : " + orderdetailModel.serviceinformation.from_time)

        } else if (orderdetailModel.category_id == AppConstants.CAT_EVENT_TICKET.toString()) {
            btn_next.visibility = View.GONE
            ed_bookingtime.visibility = View.VISIBLE
            ed_noofperson.setText(orderdetailModel.serviceinformation.number_of_person)
            ed_bookingdatetime.setText(resources.getString(R.string.date) + " : " + orderdetailModel.serviceinformation.book_date)
            ed_bookingtime.setText(resources.getString(R.string.time) + " : " + orderdetailModel.serviceinformation.book_time)

            if (orderdetailModel.serviceinformation?.barcode != null && !(orderdetailModel.serviceinformation?.barcode)?.isEmpty()!!) {
                image.visibility = View.VISIBLE
                generateQRCode(orderdetailModel.serviceinformation?.barcode!!)
            }

        } else if (orderdetailModel.category_id == AppConstants.CAT_STORE_SELLER.toString()) {
            btn_next.visibility = View.GONE
            hint_date.visibility = View.GONE
            ed_noofperson.visibility = View.GONE

            booking_view.visibility = View.GONE
            hint_bookingdate.visibility = View.GONE
            ed_bookingdatetime.visibility = View.GONE
            ed_bookingtime.visibility = View.GONE

            if (orderdetailModel.storeItem.size > 0) {
                createAdapterEvents(orderdetailModel.storeItem)
            }

        } else if (orderdetailModel.category_id == AppConstants.CAT_RESTAURANT_DINNIG.toString()) {

            ed_bookingtime.visibility = View.VISIBLE
            ed_bookingdatetime.setText(resources.getString(R.string.date) + " : " + orderdetailModel.serviceinformation.book_date)
            ed_bookingtime.setText(resources.getString(R.string.time) + " : " + orderdetailModel.serviceinformation.book_time)


        }
    }

    private fun createAdapterEvents(eventlisting: ArrayList<StoreCartModel>) {
        storeItemsAdapter = StoreItemsViewAdapter(this, AppConstants.FROM_REVIEW, eventlisting)
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_order.layoutManager = manager
        rv_order.adapter = (storeItemsAdapter)
    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.orders)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        here.setOnClickListener(this)
        btn_arrived.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun getVendorDetail(orderid: String, notify: String?) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.getBusinessOrderDetail("Bearer " + SharedPreferenceManager.getAuthToken(), orderid, notify)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<BusinessOrderDetailResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: BusinessOrderDetailResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            UiValidator.displayMsgSnack(coordinator, this@CustomerOrderDetailActivity, detailResponse.message)
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

    fun hitNext(orderid: String, alert_status: String) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.customerInformVendor("Bearer " + SharedPreferenceManager.getAuthToken(), orderid, alert_status)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<BusinessOrderDetailResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: BusinessOrderDetailResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                UiValidator.displayMsgSnack(coordinator, this@CustomerOrderDetailActivity, detailResponse.message)
                                finish()
                            } else {
                                UiValidator.displayMsgSnack(coordinator, this@CustomerOrderDetailActivity, detailResponse.message)
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

    fun cancelOrder(orderid: String) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.cancelOrder("Bearer " + SharedPreferenceManager.getAuthToken(), orderid)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<CancelOrderResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: CancelOrderResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            UiValidator.displayMsgSnack(coordinator, this@CustomerOrderDetailActivity, detailResponse.message)
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

    fun generateQRCode(barcode: String) {
        var multiFormatWriter = MultiFormatWriter();
        try {
            var bitMatrix = multiFormatWriter.encode(barcode, BarcodeFormat.QR_CODE, 250, 250);
            var barcodeEncoder = BarcodeEncoder();
            var bitmap = barcodeEncoder.createBitmap(bitMatrix);
            image.setImageBitmap(bitmap);
        } catch (e: WriterException) {
            e.printStackTrace();
        }
    }

}
