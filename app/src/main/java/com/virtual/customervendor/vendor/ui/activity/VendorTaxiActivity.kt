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
import com.virtual.customervendor.model.request.Ven_Taxi_Service_Request
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.model.response.TaxiServiceResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.fragments.SuccesBookingFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorTaxiFinalFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorTaxiOneFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorTaxiTwoFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_taxi_vendor.*
import java.io.File

class VendorTaxiActivity : BaseActivity(), View.OnClickListener, CityDialogFragment.citySelectionInterface, RegionDialogFragmentMulti.MultiRegionSelectionInterface, RegionDialogFragmentSingle.SingleRegionSelectionInterface, UploadBussinessImage.IBussinessImage1 {
    var datetime: String? = null
    var manager: FragmentManager? = null
    var TAG: String = VendorTaxiActivity::class.java.simpleName
    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var mTitleFrag: String? = null
    var cityDialogFragment: CityDialogFragment? = null
    var regionDialogFragmentMulti: RegionDialogFragmentMulti? = null
    var regionDialogFragmentsingle: RegionDialogFragmentSingle? = null
    var taxi_Service_Request = Ven_Taxi_Service_Request()
    var apiInterface: ApiInterface? = null
    var isFromEdit: Boolean = false
    var fragmentCount: Int = 0
    var imageFiles: ArrayList<File>? = null
    //    var deleteFiles: ArrayList<String>? = null
    var mResults = ArrayList<BusinessImage>()

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

    override fun done(bean: ArrayList<RegionModel>, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        if (regionDialogFragmentMulti != null) {
            regionDialogFragmentMulti!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorTaxiTwoFragment) fragment.updateSelectedServiceArea(bean)
            }
        }
    }

    override fun regionSelectionInterface(bean: RegionModel, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        UiValidator.hideSoftKeyboard(this@VendorTaxiActivity)
        if (regionDialogFragmentsingle != null) {
            regionDialogFragmentsingle!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorTaxiOneFragment) fragment.updateSelectedRegion(bean)
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
        setContentView(R.layout.activity_taxi_vendor)
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
        mTitle?.text = getString(R.string.setup_your_bussiness)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        if (isFromEdit) {
            taxi_Service_Request = CachingManager.getVendorTaxiInfo()
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
            1 -> replaceFragment(VendorTaxiOneFragment.newInstance(), removeStack)
            2 -> {
                mTitleFrag = title
                replaceFragment(VendorTaxiTwoFragment.newInstance(title), removeStack)
            }
            3 -> {
                mTitleFrag = title
                replaceFragment(VendorTaxiFinalFragment.newInstance(title), removeStack)
            }
            4 -> {
                replaceFragment(SuccesBookingFragment.newInstance(), removeStack)
            }
        }
    }


    fun setDisplayDialog(number: Int, title: String, regionId: String) {
        when (number) {
            6 -> showCitySelectionDialog(title, regionId)
            7 -> showRegionSelectionDialogMMutli(title)
            8 -> showRegionSelectionDialog(title)
        }

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
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorTaxiTwoFragment -> {
                if (isFromEdit) {
                    finish()
                } else {
                    setTitleBar(resources.getString(R.string.bussiness_information))
                    fragmentManager!!.popBackStackImmediate()
                }
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorTaxiFinalFragment -> {
                setTitleBar(resources.getString(R.string.service_information))
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is SuccesBookingFragment -> finish()
        }
        SlideAnimationUtill.slideBackAnimation(this)
    }

    private fun showCitySelectionDialog(from: String, regionId: String) {
        cityDialogFragment = CityDialogFragment.newInstance(regionId)
        manager = supportFragmentManager
        cityDialogFragment!!.show(manager, "My Dialog")
    }

    private fun showRegionSelectionDialogMMutli(from: String) {
        regionDialogFragmentMulti = RegionDialogFragmentMulti.newInstance(from)
        manager = supportFragmentManager
        regionDialogFragmentMulti!!.show(manager, "My Dialog")
    }

    private fun showRegionSelectionDialog(from: String) {
        regionDialogFragmentsingle = RegionDialogFragmentSingle.newInstance(from, "")
        manager = supportFragmentManager
        regionDialogFragmentsingle!!.show(manager, "My Dialog")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
        when {
            fragment is VendorTaxiOneFragment -> fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun getTaxiFieldPojo(): Ven_Taxi_Service_Request {
        return taxi_Service_Request
    }

    fun isFromedit(): Boolean {
        return isFromEdit
    }

    fun hitApi(taxi_Service_Request: Ven_Taxi_Service_Request) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        taxi_Service_Request.business_subcategory_id = AppConstants.SUBCAT_TRANS_TAXI.toString()
        taxi_Service_Request.business_category_id = AppConstants.CAT_TRANSPORTATION.toString()
        apiInterface?.taxiServiceSetUp("Bearer " + SharedPreferenceManager.getAuthToken(), taxi_Service_Request)
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
    }

    fun hitApiEdit(taxi_Service_Request: Ven_Taxi_Service_Request) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        taxi_Service_Request.action = AppConstants.ACTION_EDIT
        apiInterface?.taxiServiceSetUp("Bearer " + SharedPreferenceManager.getAuthToken(), taxi_Service_Request)
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
    }

    private fun handleResults(userResponse: TaxiServiceResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            CachingManager.setVendorTaxiInfo(taxi_Service_Request)
            if (isFromEdit) {
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                if (imageFiles != null) {
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
            AppLog.e(TAG, t.message)
        }
    }

    public fun uploadPic(files: ArrayList<File>, bussinessId: String) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        if (files != null && files.size > 0) {
            files.subList(0, taxi_Service_Request.business_images.size).clear()
        }
        UploadBussinessImage.uploadPicsOnServer(files, bussinessId, this)

    }

    override fun onSuccess(userImageUploadResponse: BusinessImagesResponse) {
        AppLog.e(TAG, userImageUploadResponse.toString())
        ProgressDialogLoader.progressDialogDismiss()
        if (userImageUploadResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            var taxi_Service_Request = CachingManager.getVendorTaxiInfo()
            taxi_Service_Request.business_images = userImageUploadResponse.data
            CachingManager.setVendorTaxiInfo(taxi_Service_Request)
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
