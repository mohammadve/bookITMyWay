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
import com.virtual.customervendor.customer.ui.dialogFragment.RegionDialogFragmentMulti
import com.virtual.customervendor.customer.ui.dialogFragment.RegionDialogFragmentSingle
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.request.Ven_Sight_Seeing_Service_Request
import com.virtual.customervendor.model.request.VendorBusinessDetailRequest
import com.virtual.customervendor.model.response.BusinessDetailUpdateResponse
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.model.response.TaxiServiceResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.fragments.SuccesBookingFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorSightSeeingFinalFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorSightSeeingTwoFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorTaxiOneFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_taxi_vendor.*
import java.io.File

class VendorSightSeeingActivity : BaseActivity(), View.OnClickListener, CityDialogFragment.citySelectionInterface, RegionDialogFragmentMulti.MultiRegionSelectionInterface, RegionDialogFragmentSingle.SingleRegionSelectionInterface, UploadBussinessImage.IBussinessImage1 {
    override fun done(bean: ArrayList<RegionModel>, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        if (regionDialogFragmentMulti != null) {
            regionDialogFragmentMulti!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorSightSeeingTwoFragment) fragment.updateSelectedServiceArea(bean)
            }
        }
    }

    override fun selectedCityUpdate(bean: CityModel, fromWhere: String?) {
        AppLog.e(TAG, bean.cityname)
        if (cityDialogFragment != null) {
            cityDialogFragment!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorTaxiOneFragment) fragment.updateSelectedCity(bean)
            }
        }
    }

    override fun regionSelectionInterface(bean: RegionModel, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        if (regionDialogFragmentsingle != null) {
            regionDialogFragmentsingle!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorTaxiOneFragment) fragment.updateSelectedRegion(bean)
            }
        }
    }

    var datetime: String? = null
    var manager: FragmentManager? = null
    var TAG: String = VendorSightSeeingActivity::class.java.simpleName
    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var mTitleFrag: String? = null
    var cityDialogFragment: CityDialogFragment? = null
    var regionDialogFragmentMulti: RegionDialogFragmentMulti? = null
    var regionDialogFragmentsingle: RegionDialogFragmentSingle? = null
    var sight_Seeing_Service_Request = Ven_Sight_Seeing_Service_Request()
    var isFromEdit: Boolean = false
    var fragmentCount: Int = 0
    var apiInterface: ApiInterface? = null
    var imageFile: File? = null
    var imageFiles: java.util.ArrayList<File>? = null
    //    var deleteFiles: java.util.ArrayList<String>? = null
    var mResults = java.util.ArrayList<BusinessImage>()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    fun isFromedit(): Boolean {
        return isFromEdit
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taxi_vendor)
//        setToolBar()
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
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        mTitle?.text = getString(R.string.setup_your_bussiness)
        iv_back.setOnClickListener(this)


        if (isFromEdit) {
            sight_Seeing_Service_Request = CachingManager.getVendorSightSeenInfo()
            if (fragmentCount == 1) {
                setDisplayFragment(fragmentCount, resources.getString(R.string.editbussiness_information), false)
            } else if (fragmentCount == 2) {
                setDisplayFragment(fragmentCount, resources.getString(R.string.editservice_information), false)
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
            1 -> replaceFragment(VendorTaxiOneFragment.newInstance(), false)
            2 -> {
                mTitleFrag = title
                replaceFragment(VendorSightSeeingTwoFragment.newInstance(title), false)
            }
            3 -> {
                mTitleFrag = title
                replaceFragment(VendorSightSeeingFinalFragment.newInstance(title), false)
            }
            4 -> replaceFragment(SuccesBookingFragment.newInstance(), false)
        }
    }

    fun setDisplayDialog(number: Int, title: String, regionId: String) {
        when (number) {
            6 -> showNationalitySelectionDialog(title, regionId)
            7 -> showNationalitySelectionDialogMMutli(title)
            8 -> showRegionSelectionDialog(title)
        }
    }

    private fun showRegionSelectionDialog(from: String) {
        regionDialogFragmentsingle = RegionDialogFragmentSingle.newInstance(from, "")
        manager = supportFragmentManager
        regionDialogFragmentsingle!!.show(manager, "My Dialog")
    }

    override fun onBackPressed() {
        handleBackPress()
    }

    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
    }

    fun handleBackPress() {
        when {
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorTaxiOneFragment -> finish()
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorSightSeeingTwoFragment -> {
                if (isFromEdit) {
                    finish()
                } else {
                    setTitleBar(resources.getString(R.string.bussiness_information))
                    fragmentManager!!.popBackStackImmediate()
                }
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorSightSeeingFinalFragment -> {
                if (isFromEdit) {
                    finish()
                } else {
                    setTitleBar(resources.getString(R.string.service_information))
                    fragmentManager!!.popBackStackImmediate()
                }
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is SuccesBookingFragment -> finish()
        }
        SlideAnimationUtill.slideBackAnimation(this)
    }


    private fun showNationalitySelectionDialog(from: String, regionId: String) {
        cityDialogFragment = CityDialogFragment.newInstance(regionId)
        manager = supportFragmentManager
        cityDialogFragment!!.show(manager, "My Dialog")
    }


    private fun showNationalitySelectionDialogMMutli(from: String) {
        regionDialogFragmentMulti = RegionDialogFragmentMulti.newInstance(from)
        manager = supportFragmentManager
        regionDialogFragmentMulti!!.show(manager, "My Dialog")
    }

    fun getSightSeeingPojo(): Ven_Sight_Seeing_Service_Request {
        return sight_Seeing_Service_Request
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
        when {
            fragment is VendorTaxiOneFragment -> fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    fun hitApi(sight_Seeing_Service_Request: Ven_Sight_Seeing_Service_Request) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        sight_Seeing_Service_Request.business_subcategory_id = AppConstants.SUBCAT_TRANS_SIGHTSEEING.toString()
        sight_Seeing_Service_Request.business_category_id = AppConstants.CAT_TRANSPORTATION.toString()
        apiInterface?.sightSeenServiceSetUp("Bearer " + SharedPreferenceManager.getAuthToken(), sight_Seeing_Service_Request)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : io.reactivex.Observer<TaxiServiceResponse> {
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
            var vendorsight: Ven_Sight_Seeing_Service_Request = CachingManager.getVendorSightSeenInfo()
            vendorsight.business_id = businessDetailRequest.business_id
            vendorsight.business_name = businessDetailRequest.business_name!!
            vendorsight.business_contactno = businessDetailRequest.business_contactno!!
            vendorsight.business_email = businessDetailRequest.business_email!!
            vendorsight.business_city_id = businessDetailRequest.business_city_id
            vendorsight.business_region_id = businessDetailRequest.business_region_id
            vendorsight.business_address = businessDetailRequest.business_address!!
            CachingManager.setVendorSightSeenInfo(vendorsight)
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
        }
    }

    private fun handleResults(userResponse: TaxiServiceResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            CachingManager.setVendorSightSeenInfo(sight_Seeing_Service_Request)
            if (isFromEdit) {
                setResult(Activity.RESULT_OK)
                finish()
            } else
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

    public fun uploadPic(files: java.util.ArrayList<File>, bussinessId: String) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        if (files != null && files.size > 0) {
            files.subList(0, sight_Seeing_Service_Request.business_images.size).clear()
        }
        UploadBussinessImage.uploadPicsOnServer(files, bussinessId, this)
    }

    override fun onSuccess(userImageUploadResponse: BusinessImagesResponse) {
        AppLog.e(TAG, userImageUploadResponse.toString())
        ProgressDialogLoader.progressDialogDismiss()
        if (userImageUploadResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            var taxi_Service_Request = CachingManager.getVendorSightSeenInfo()
            taxi_Service_Request.business_images = userImageUploadResponse.data
            CachingManager.setVendorSightSeenInfo(taxi_Service_Request)
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
        AppLog.e(TAG, d.toString())
    }
}
