package com.virtual.customervendor.vendor.ui.activity

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.adapter.NotificationadapterCustomer
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.NotificationModel
import com.virtual.customervendor.model.response.NotificationResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.fragments.VendorOrdersFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_notification_vendor.*

class VendorNotificationActivity : BaseActivity(), View.OnClickListener, PagingListeners {
    override fun onFinishListener() {
        getVendorOrderList(list.size)
    }

    override fun onFinishListener(type: Int) {
    }

    var notificationadapterCustomer: NotificationadapterCustomer? = null
    val list: ArrayList<NotificationModel> = java.util.ArrayList()
    val TAG: String = VendorOrdersFragment::class.java.simpleName
    var apiService: ApiInterface? = null
    var apirequest: MutableMap<String, String> = mutableMapOf()


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_vendor)
        init()
        setToolBar()
    }

    fun init() {
        apiService = ApiClient.client.create(ApiInterface::class.java)
        createAdapterView()
    }


    fun setToolBar() {
        val appbar: AppBarLayout = findViewById<AppBarLayout>(R.id.toolbar)
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = appbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.notification)
        val iv_back = appbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }

    private fun createAdapterView() {
        notificationadapterCustomer = NotificationadapterCustomer(this, this, list) { offerModel ->
        }
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_notification.layoutManager = manager
        rv_notification.adapter = (notificationadapterCustomer)


        getVendorOrderList(0)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun getVendorOrderList(offset: Int) {
        apirequest.put(AppKeys.OFFSET, offset.toString())
        apirequest.put(AppKeys.USER_TYPE, AppConstants.USER_VENDOR)
        apirequest.put(AppKeys.OFFER_BUSINESS_ID, SharedPreferenceManager.getBussinessID())
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService?.getNotificationList("Bearer " + SharedPreferenceManager.getAuthToken(), apirequest)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<NotificationResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: NotificationResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                list.clear()
                                list.addAll(detailResponse.data)
                                notificationadapterCustomer?.notifyDataSetChanged()
                            } else {
                                UiValidator.displayMsgSnack(cord, this@VendorNotificationActivity, detailResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                AppLog.e(TAG, e?.message)
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(cord, this, getString(R.string.no_internet_connection))
        }
    }


}