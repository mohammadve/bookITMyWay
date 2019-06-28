package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customer_vendor.utill.UploadBussinessImage
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.dialogFragment.CityDialogFragment
import com.virtual.customervendor.customer.ui.dialogFragment.RegionDialogFragmentSingle
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.model.response.TaxiServiceResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.utills.ProgressDialogLoader.progressDialogCreation
import com.virtual.customervendor.vendor.ui.fragments.VendorEventTwoFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorEventsDetailFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_events_detail_vendor.*
import kotlinx.android.synthetic.main.custom_toolbar_appbar_event.*
import java.io.File

class VendorEventsDetailActivity : BaseActivity(), View.OnClickListener, UploadBussinessImage.IEvevtServiceImage {
    var datetime: String? = null
    var manager: FragmentManager? = null
    var TAG: String = VendorEventsDetailActivity::class.java.simpleName
    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var mTitleFrag: String? = null
    var cityDialogFragment: CityDialogFragment? = null
    var regionDialogFragmentsingle: RegionDialogFragmentSingle? = null
    var apiInterface: ApiInterface? = null
    var ser_imageFile: File? = null
    var vendorEventServiceRequest = EventDetail()
    var isFromEdit: Boolean = true
    var fragmentCount: Int = 0

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_delete -> {
                hitApiDelete(vendorEventServiceRequest)
            }
            R.id.iv_edit -> {
                setDisplayFragment(2, resources.getString(R.string.edit_event), false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_detail_vendor)
        vendorEventServiceRequest = intent.extras.getSerializable(AppConstants.BUSINESS_DATA) as EventDetail
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()
    }

    fun isFromedit(): Boolean {
        return isFromEdit
    }


    fun init() {
        frameLayout = findViewById(R.id.flContentnew) as FrameLayout
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.setup_your_bussiness)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        iv_delete.setOnClickListener(this)
        iv_edit.setOnClickListener(this)
        setDisplayFragment(1, resources.getString(R.string.event), false)
    }

    private fun replaceFragment(fragmentClass: Fragment, removeStack: Boolean) {
        var fragment: Fragment? = null
        try {
            fragment = fragmentClass
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Insert the fragment by replacing any existing fragment
        fragmentManager = supportFragmentManager
        val mTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        mTransaction.addToBackStack(null)
        if (removeStack) {
            if (supportFragmentManager.fragments != null && supportFragmentManager.fragments.size > 1) {
                for (i in 1 until supportFragmentManager.fragments.size) {
                    supportFragmentManager.beginTransaction().remove(supportFragmentManager.fragments[i]).commit()
                }
            }
        }
        mTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left1, R.anim.slide_from_left, R.anim.slide_to_right).replace(frameLayout!!.id, fragment!!).commit()
    }

    fun setDisplayFragment(number: Int, title: String, removeStack: Boolean) {
        setTitleBar(title)
        when (number) {
            1 -> {
                iv_delete.visibility = View.VISIBLE
                iv_edit.visibility = View.VISIBLE
                replaceFragment(VendorEventsDetailFragment.newInstance(), removeStack)
            }
            2 -> {
                mTitleFrag = title
                iv_delete.visibility = View.GONE
                iv_edit.visibility = View.GONE
                replaceFragment(VendorEventTwoFragment.newInstance(title), removeStack)
            }
        }
    }


    override fun onBackPressed() {
        handleBackPress()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
    }

    fun handleBackPress() {
        when {
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorEventsDetailFragment -> finish()
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorEventTwoFragment -> {
                setTitleBar(resources.getString(R.string.event))
                iv_delete.visibility = View.VISIBLE
                iv_edit.visibility = View.VISIBLE
                fragmentManager!!.popBackStackImmediate()
            }
        }
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun getEventFieldPojo(): EventDetail {
        return vendorEventServiceRequest
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
        when {
            fragment is VendorEventTwoFragment -> fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    public fun hitApiEdit() {
        if (AppUtils.isInternetConnected(this)) {
            progressDialogCreation(this, getString(R.string.please_wait))
            vendorEventServiceRequest.action = AppConstants.ACTION_EDIT
            vendorEventServiceRequest.business_category_id = AppConstants.CAT_EVENT_TICKET.toString()
            vendorEventServiceRequest.business_subcategory_id = AppConstants.SUBCAT_EVENT_TICKET.toString()
            apiInterface?.addUpdateEvent("Bearer " + SharedPreferenceManager.getAuthToken(), vendorEventServiceRequest)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<TaxiServiceResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(userResponse: TaxiServiceResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                setDisplayFragment(1, resources.getString(R.string.event), false)
                            } else {
                                UiValidator.displayMsgSnack(coordinator, this@VendorEventsDetailActivity, userResponse.message)
                            }
                            AppLog.e(TAG, userResponse.toString())
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null) {
                                UiValidator.displayMsgSnack(coordinator, this@VendorEventsDetailActivity, e.message)
                                AppLog.e(TAG, e.message)
                            }
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    public fun hitApiDelete(vendorEventDetail: EventDetail) {
        if (AppUtils.isInternetConnected(this)) {
            progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.deleteEvent("Bearer " + SharedPreferenceManager.getAuthToken(), vendorEventDetail.service_id)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(mainThread())
                    ?.subscribe(object : Observer<TaxiServiceResponse> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onNext(userResponse: TaxiServiceResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, userResponse.toString())
                            if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                setResult(Activity.RESULT_OK)
                                finish()
                            } else {
                                UiValidator.displayMsgSnack(coordinator, this@VendorEventsDetailActivity, userResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null) {
                                UiValidator.displayMsgSnack(coordinator, this@VendorEventsDetailActivity, e.message)
                                AppLog.e(TAG, e.message)
                            }
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    public fun uploadEventPic(files: ArrayList<File>, eventid: String) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        if (files != null && files.size > 0) {
            files.subList(0, vendorEventServiceRequest.event_images!!.size).clear()
        }
        UploadBussinessImage.uploadEventImageOnServer(files, eventid, this)
    }

    override fun onSuccessEvent(userImageUploadResponse: BusinessImagesResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userImageUploadResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            vendorEventServiceRequest.event_images = userImageUploadResponse.data
            setResult(Activity.RESULT_OK)
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userImageUploadResponse.message)
        }
    }

    override fun onErrorEvent(e: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        AppLog.e(TAG, e.toString())
    }

    override fun onSubscribeEvent(d: Disposable) {
        AppLog.e(TAG, d.toString())
    }

}
