
package com.virtual.customervendor.vendor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.model.response.CategoryResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import kotlinx.android.synthetic.main.activity_become_vendor.*

class BecomeVendorActivity : BaseActivity(), View.OnClickListener {
    var TAG: String = BecomeVendorActivity::class.java.name
    var apiService: ApiInterface? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_table -> {
                callActivity(AppConstants.CAT_RESTAURANT_DINNIG.toString(), VendorRestaurantActivity())
            }
            R.id.btn_valet -> {
                callActivity(AppConstants.CAT_PARKING_VALET.toString(), VendorParkingValetActivity())
            }
            R.id.btn_ride -> {
                callActivity(AppConstants.CAT_TRANSPORTATION.toString(), VendorTransportServiceActivity())
            }
            R.id.btn_appoint -> {
                callActivity(AppConstants.CAT_HEALTH_BODYCARE.toString(), VendorAppointmentsServiceActivity())
            }
            R.id.btn_purchase -> {
                callActivity(AppConstants.CAT_STORE_SELLER.toString(), VendorStoreActivity())
            }
            R.id.btn_ticketevent -> {
                callActivity(AppConstants.CAT_EVENT_TICKET.toString(), VendorEventsActivity())
            }
        }
    }

    fun callActivity(keyValue: String, activity1: AppCompatActivity) {
        var intent: Intent = Intent(this, activity1.javaClass)
        intent.putExtra(AppKeys.KEY_CATEGORY, keyValue)
        startActivity(intent)
        SlideAnimationUtill.slideNextAnimation(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_become_vendor)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        initView()
        setToolBar()
    }


    fun initView() {
//        fetchCategory()
//        btn_table.setTag()
        btn_table.setOnClickListener(this)
        btn_ride.setOnClickListener(this)
        btn_appoint.setOnClickListener(this)
        btn_purchase.setOnClickListener(this)
        btn_valet.setOnClickListener(this)
        btn_ticketevent.setOnClickListener(this)
    }

//    fun fetchCategory() {
//        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
//        apiService!!.category.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<CategoryResponse> {
//                    override fun onSubscribe(d: Disposable) {
//                        AppLog.e(TAG, "onSubscribe")
//                    }
//
//                    override fun onNext(userResponse: CategoryResponse) {
//                        handleResults(userResponse)
//                        AppLog.e(TAG, userResponse.toString())
//                    }
//
//                    override fun onError(e: Throwable) {
//                        AppLog.e(TAG, e.message)
//                        handleError(e)
//                    }
//
//                    override fun onComplete() {
//                        AppLog.e(TAG, "onComplete: ")
//                    }
//                })
//
//
//    }

    private fun handleResults(categoryResponse: CategoryResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (categoryResponse.status.equals(AppConstants.KEY_SUCCESS)) {

        } else {
            UiValidator.displayMsgSnack(main_cord, this, categoryResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        AppLog.e(TAG, "UserDetails Registration Error" + t.toString())
    }

    fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.choose_service_area)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }

}