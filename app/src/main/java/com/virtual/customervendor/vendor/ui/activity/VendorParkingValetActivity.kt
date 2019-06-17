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
import com.virtual.customervendor.model.request.VendorParkingRequest
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.model.response.TaxiServiceResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.fragments.SuccesBookingFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorParkingOneFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorParkingReviewFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorParkingTwoFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_parking_valet_vendor.*
import java.io.File

class VendorParkingValetActivity : BaseActivity(), View.OnClickListener, CityDialogFragment.citySelectionInterface, RegionDialogFragmentSingle.SingleRegionSelectionInterface, UploadBussinessImage.IBussinessImage1 {
    override fun onSuccess(userImageUploadResponse: BusinessImagesResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userImageUploadResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            vendorParkingRequest.business_images = userImageUploadResponse.data
            CachingManager.setVendorParkingInfo(vendorParkingRequest)
            if (!isFromEdit) {
                setDisplayFragment(4, resources.getString(R.string.succesful), false)
            } else {
                setResult(Activity.RESULT_OK)
            }
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userImageUploadResponse.message)
        }
    }

    override fun onError(e: Throwable) {
        handleError(e)
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun regionSelectionInterface(bean: RegionModel, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        if (regionDialogFragmentsingle != null) {
            regionDialogFragmentsingle!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorParkingOneFragment) fragment.updateSelectedServiceArea(bean)
            }
        }
    }

    override fun selectedCityUpdate(bean: CityModel, fromWhere: String?) {
        AppLog.e(TAG, bean.cityname)
        if (cityDialogFragment != null) {
            cityDialogFragment!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorParkingOneFragment) fragment.updateSelectedCity(bean)
            }
        }
    }


    var manager: FragmentManager? = null
    var TAG: String = VendorParkingValetActivity::class.java.simpleName
    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var mTitleFrag: String? = null
    var cityDialogFragment: CityDialogFragment? = null
    var regionDialogFragmentsingle: RegionDialogFragmentSingle? = null
    var vendorParkingRequest = VendorParkingRequest()
    var apiInterface: ApiInterface? = null
    var imageFile: File? = null
    var isFromEdit: Boolean = false
    var fragmentCount: Int = 0
    var imageFiles: ArrayList<File>? = null
    //    var deleteFiles: ArrayList<String>? = null
    var mResults = ArrayList<BusinessImage>()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_valet_vendor)
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
            vendorParkingRequest = CachingManager.getVendorParkingInfo()
            if (fragmentCount == 1) {
                setDisplayFragment(fragmentCount, resources.getString(R.string.bussiness_information), false)
            } else if (fragmentCount == 2) {
                setDisplayFragment(fragmentCount, resources.getString(R.string.service_information), false)
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
        mTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left1, R.anim.slide_from_left, R.anim.slide_to_right).replace(frameLayout!!.id, fragment).commit()
    }

    fun setDisplayFragment(number: Int, title: String, removeStack: Boolean) {
        setTitleBar(title)
        when (number) {
            1 -> replaceFragment(VendorParkingOneFragment.newInstance(), removeStack)
            2 -> {
                mTitleFrag = title
                replaceFragment(VendorParkingTwoFragment.newInstance(title), removeStack)
            }
            3 -> {
                mTitleFrag = title
                replaceFragment(VendorParkingReviewFragment.newInstance(title), removeStack)
            }
            4 -> replaceFragment(SuccesBookingFragment.newInstance(), removeStack)
        }
    }

    fun setDisplayDialog(number: Int, title: String, region_id: String) {
        when (number) {
            6 -> showCitySelectionDialog(title, region_id)
            7 -> showRegionSelectionDialogSingle(title)
        }

    }

    fun isFromedit(): Boolean {
        return isFromEdit
    }


    override fun onBackPressed() {
        handleBackPress()
    }

    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
    }

    fun handleBackPress() {
        when {
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorParkingOneFragment -> finish()
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorParkingTwoFragment -> {
                if (isFromEdit) {
                    finish()
                } else {
                    setTitleBar(resources.getString(R.string.bussiness_information))
                    fragmentManager!!.popBackStackImmediate()
                }
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorParkingReviewFragment -> {
                setTitleBar(resources.getString(R.string.service_information))
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is SuccesBookingFragment -> finish()
        }
        SlideAnimationUtill.slideBackAnimation(this)
    }

    private fun showCitySelectionDialog(from: String, region_id: String) {
        cityDialogFragment = CityDialogFragment.newInstance(region_id)
        manager = supportFragmentManager
        cityDialogFragment!!.show(manager, "My Dialog")
    }

    private fun showRegionSelectionDialogSingle(from: String) {
        regionDialogFragmentsingle = RegionDialogFragmentSingle.newInstance(from, "")
        manager = supportFragmentManager
        regionDialogFragmentsingle!!.show(manager, "My Dialog")
    }

    fun getValetRequestPojo(): VendorParkingRequest {
        return vendorParkingRequest
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
        when {
            fragment is VendorParkingOneFragment -> fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun hitApi(parking_Service_Request: VendorParkingRequest) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            parking_Service_Request.business_subcategory_id = AppConstants.SUBCAT_PARKING_VALET.toString()
            parking_Service_Request.business_category_id = AppConstants.CAT_PARKING_VALET.toString()
            apiInterface?.parkingServiceSetUp("Bearer " + SharedPreferenceManager.getAuthToken(), parking_Service_Request)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<TaxiServiceResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(userResponse: TaxiServiceResponse) {
                            AppLog.e(TAG, userResponse.toString())
                            handleResults(userResponse)
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    fun hitApiEdit(parking_Service_Request: VendorParkingRequest) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            parking_Service_Request.action = AppConstants.ACTION_EDIT
            apiInterface?.parkingServiceSetUp("Bearer " + SharedPreferenceManager.getAuthToken(), parking_Service_Request)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<TaxiServiceResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(userResponse: TaxiServiceResponse) {
                            AppLog.e(TAG, userResponse.toString())
                            handleResultsEdit(userResponse)
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }


    private fun handleResultsEdit(userResponse: TaxiServiceResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            vendorParkingRequest.business_id = userResponse.data.business_id.toInt()
            vendorParkingRequest.service_id = userResponse.data.service_id
            CachingManager.setVendorParkingInfo(vendorParkingRequest)
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
        }
    }

    private fun handleResults(userResponse: TaxiServiceResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            CachingManager.setVendorParkingInfo(vendorParkingRequest)
            if (imageFiles != null) {
                uploadPic(imageFiles!!, userResponse.data.business_id)
            } else {
                setDisplayFragment(4, resources.getString(R.string.succesful), false)
            }
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null) {
            UiValidator.displayMsgSnack(coordinator, this, t.message)
            AppLog.e(TAG, t.message)
        }
    }

    public fun uploadPic(files: ArrayList<File>, bussinessId: String) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        if (files != null && files.size > 0) {
            files.subList(0, vendorParkingRequest.business_images.size).clear()
        }
        UploadBussinessImage.uploadPicsOnServer(files, bussinessId, this)

    }


}
