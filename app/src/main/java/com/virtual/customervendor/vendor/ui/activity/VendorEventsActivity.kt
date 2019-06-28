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
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.request.VendorBusinessDetailRequest
import com.virtual.customervendor.model.request.VendorEventServiceRequest
import com.virtual.customervendor.model.response.BusinessDetailUpdateResponse
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.model.response.TaxiServiceResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.fragments.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_restaurant_vendor.*
import java.io.File

class VendorEventsActivity : BaseActivity(), View.OnClickListener, CityDialogFragment.citySelectionInterface, RegionDialogFragmentSingle.SingleRegionSelectionInterface, UploadBussinessImage.IBussinessImage1, UploadBussinessImage.IEvevtServiceImage {
    var datetime: String? = null
    var manager: FragmentManager? = null
    var TAG: String = VendorEventsActivity::class.java.simpleName
    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var mTitleFrag: String? = null
    var cityDialogFragment: CityDialogFragment? = null
    var regionDialogFragmentsingle: RegionDialogFragmentSingle? = null
    var vendorEventServiceRequest = VendorEventServiceRequest()
    var apiInterface: ApiInterface? = null
    var ser_imageFile = ArrayList<File>()
    var strImages = java.util.ArrayList<String>()
    var isFromEdit: Boolean = false
    var fragmentCount: Int = 0
    var eventId: String = String()


    var imageFiles: ArrayList<File>? = null
    //    var deleteFiles: ArrayList<String>? = null
    var mResults = ArrayList<BusinessImage>()

    override fun regionSelectionInterface(bean: RegionModel, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        if (regionDialogFragmentsingle != null) {
            regionDialogFragmentsingle!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorEventsOneFragment) fragment.updateSelectedServiceArea(bean)
            }
        }
    }


    override fun selectedCityUpdate(bean: CityModel, fromWhere: String?) {
        AppLog.e(TAG, bean.cityname)
        if (cityDialogFragment != null) {
            cityDialogFragment!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorEventsOneFragment) fragment.updateSelectedCity(bean)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_vendor)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        isFromEdit = intent.getBooleanExtra(AppConstants.FROM_EDIT, false)
        fragmentCount = intent.getIntExtra(AppConstants.FRAGMENT_NUMBER, 0)
        AppLog.e(TAG, "isFromEdit= " + isFromEdit + " fragmentCount= " + fragmentCount)
        init()
    }

    fun init() {
        frameLayout = findViewById(R.id.flContentnew) as FrameLayout
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.setup_your_bussiness)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        if (isFromEdit) {
            vendorEventServiceRequest = CachingManager.getVendorEventInfo()
            if (fragmentCount == 1) {
                setDisplayFragment(fragmentCount, resources.getString(R.string.editbussiness_information), false)
            }
        } else {
            setDisplayFragment(1, resources.getString(R.string.bussiness_information), false)
        }
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
            1 -> replaceFragment(VendorEventsOneFragment.newInstance(), removeStack)
            2 -> {
                mTitleFrag = title
                replaceFragment(VendorEventTwoFragment.newInstance(title), removeStack)
            }
            3 -> {
                mTitleFrag = title
                replaceFragment(VendorEventReviewFragment.newInstance(title), removeStack)
            }
            4 -> replaceFragment(SuccesBookingFragment.newInstance(), removeStack)
        }
    }

    fun setDisplayDialog(number: Int, title: String, region_id: String) {
        when (number) {
            6 -> showCitySelectionDialog(region_id)
            7 -> showRegionSelectionDialogSingle(title)
        }

    }

    override fun onBackPressed() {
        handleBackPress()
    }

    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
    }

    fun isFromedit(): Boolean {
        return isFromEdit
    }

    fun handleBackPress() {
        when {
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorEventsOneFragment -> finish()
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorEventTwoFragment -> {
                setTitleBar(resources.getString(R.string.bussiness_information))
                fragmentManager!!.popBackStackImmediate()
            }

            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorEventReviewFragment -> {
                setTitleBar(resources.getString(R.string.event_information))
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is SuccesBookingFragment -> finish()
        }
        SlideAnimationUtill.slideBackAnimation(this)
    }

    private fun showCitySelectionDialog(region_id: String) {
        cityDialogFragment = CityDialogFragment.newInstance(region_id)
        manager = supportFragmentManager
        cityDialogFragment!!.show(manager, "My Dialog")
    }

    private fun showRegionSelectionDialogSingle(from: String) {
        regionDialogFragmentsingle = RegionDialogFragmentSingle.newInstance(from, "")
        manager = supportFragmentManager
        regionDialogFragmentsingle!!.show(manager, "My Dialog")
    }


    fun getEventFieldPojo(): VendorEventServiceRequest {
        return vendorEventServiceRequest
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
        when {
            fragment is VendorEventsOneFragment -> fragment.onActivityResult(requestCode, resultCode, data)
            fragment is VendorEventTwoFragment -> fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    public fun hitApi() {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            vendorEventServiceRequest.business_category_id = AppConstants.CAT_EVENT_TICKET
            vendorEventServiceRequest.business_subcategory_id = AppConstants.SUBCAT_EVENT_TICKET
            apiInterface?.eventServiceSetUp("Bearer " + SharedPreferenceManager.getAuthToken(), vendorEventServiceRequest)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<TaxiServiceResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(userResponse: TaxiServiceResponse) {
                            handleResults(userResponse)
                            AppLog.e(TAG, userResponse.toString())
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    public fun hitApiEdit(businessDetailRequest: VendorBusinessDetailRequest) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.updateBusinessDetail("Bearer " + SharedPreferenceManager.getAuthToken(), businessDetailRequest)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<BusinessDetailUpdateResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(userResponse: BusinessDetailUpdateResponse) {
                            handleResultEdit(userResponse, businessDetailRequest)
                            AppLog.e(TAG, userResponse.toString())
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }


    private fun handleResultEdit(userResponse: BusinessDetailUpdateResponse, businessDetailRequest: VendorBusinessDetailRequest) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            var vendorEventService: VendorEventServiceRequest = CachingManager.getVendorEventInfo()
            vendorEventService.business_id = businessDetailRequest.business_id
            vendorEventService.business_name = businessDetailRequest.business_name!!
            vendorEventService.business_contactno = businessDetailRequest.business_contactno!!
            vendorEventService.business_email = businessDetailRequest.business_email!!
            vendorEventService.business_city_id = businessDetailRequest.business_city_id
            vendorEventService.business_region_id = businessDetailRequest.business_region_id
            vendorEventService.business_address = businessDetailRequest.business_address!!
            CachingManager.setVendorEventInfo(vendorEventService)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }


    private fun handleResults(userResponse: TaxiServiceResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            CachingManager.setVendorEventInfo(vendorEventServiceRequest)
            if (isFromEdit) {
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                if (imageFiles != null) {
                    eventId = userResponse.data.service_id
                    uploadPic(imageFiles!!, userResponse.data.business_id)
                } else {
                    setDisplayFragment(4, resources.getString(R.string.succesful), false)
                }
            }
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null) {
            UiValidator.displayMsgSnack(coordinator, this, t.message)
            AppLog.e(TAG, t?.message)
        }
    }

    public fun uploadPic(files: ArrayList<File>, bussinessId: String) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        if (files != null && files.size > 0) {
            files.subList(0, vendorEventServiceRequest.business_images.size).clear()
        }
        UploadBussinessImage.uploadPicsOnServer(files, bussinessId, this)

    }

    public fun uploadPicEvent(files: ArrayList<File>, serviceId: String) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        UploadBussinessImage.uploadEventImageOnServer(files, serviceId, this)
    }

    override fun onSuccessEvent(userImageUploadResponse: BusinessImagesResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userImageUploadResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            setDisplayFragment(4, resources.getString(R.string.succesful), false)
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

    override fun onSuccess(userImageUploadResponse: BusinessImagesResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        AppLog.e(TAG, userImageUploadResponse.toString())
        if (userImageUploadResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            var taxi_Service_Request = CachingManager.getVendorEventInfo()
            taxi_Service_Request.business_images = userImageUploadResponse.data
            CachingManager.setVendorEventInfo(taxi_Service_Request)
            if (!isFromEdit) {
                if (ser_imageFile.size > 0) {
                    uploadPicEvent(ser_imageFile!!, eventId)
                } else {
                    setDisplayFragment(4, resources.getString(R.string.succesful), false)
                }
            } else {
                setResult(Activity.RESULT_OK)
            }
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userImageUploadResponse.message)
        }
    }

    override fun onError(e: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        AppLog.e(TAG, e.toString())
    }

    override fun onSubscribe(d: Disposable) {
        AppLog.e(TAG, d.toString())
    }


}
