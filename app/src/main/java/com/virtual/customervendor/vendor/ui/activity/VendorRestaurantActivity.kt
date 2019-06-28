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
import com.virtual.customervendor.model.ItemPriceModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.request.VendorRestaurantServiceModel
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

class VendorRestaurantActivity : BaseActivity(), View.OnClickListener, CityDialogFragment.citySelectionInterface, RegionDialogFragmentSingle.SingleRegionSelectionInterface, UploadBussinessImage.IBussinessImage1 {


    var datetime: String? = null
    var manager: FragmentManager? = null
    var TAG: String = VendorRestaurantActivity::class.java.simpleName
    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var mTitleFrag: String? = null
    var cityDialogFragment: CityDialogFragment? = null
    var regionDialogFragmentsingle: RegionDialogFragmentSingle? = null
    var foodList: ArrayList<ItemPriceModel> = ArrayList()
    var drinkList: ArrayList<ItemPriceModel> = ArrayList()
    var dessertList: ArrayList<ItemPriceModel> = ArrayList()
    var vendorRestaurantServiceModel = VendorRestaurantServiceModel()
    var apiInterface: ApiInterface? = null
    var imageFile: File? = null
    var isFromEdit: Boolean = false
    var fragmentCount: Int = 0
    var imageFiles: ArrayList<File>? = null
    //    var deleteFiles: ArrayList<String>? = null
    var mResults = ArrayList<BusinessImage>()

    override fun regionSelectionInterface(bean: RegionModel, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        if (regionDialogFragmentsingle != null) {
            regionDialogFragmentsingle!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorRestaurantOneFragment) fragment.updateSelectedServiceArea(bean)
            }
        }
    }

    fun addListData() {
        foodList.add(ItemPriceModel())
        foodList.add(ItemPriceModel())

        drinkList.add(ItemPriceModel())
        drinkList.add(ItemPriceModel())

        dessertList.add(ItemPriceModel())
        dessertList.add(ItemPriceModel())
    }

    override fun selectedCityUpdate(bean: CityModel, fromWhere: String?) {
        AppLog.e(TAG, bean.cityname)
        if (cityDialogFragment != null) {
            cityDialogFragment!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorRestaurantOneFragment) fragment.updateSelectedCity(bean)
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
        setContentView(R.layout.activity_restaurant_vendor)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        addListData()
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
            vendorRestaurantServiceModel = CachingManager.getVendorRestaurantInfo()
            if (fragmentCount == 1) {
                setDisplayFragment(fragmentCount, resources.getString(R.string.bussiness_information), false)
            } else if (fragmentCount == 2) {
                setDisplayFragment(fragmentCount, resources.getString(R.string.service_information), false)
            } else if (fragmentCount == 3) {
                setDisplayFragment(3, resources.getString(R.string.set_your_menu), false)
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
        mTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left1, R.anim.slide_from_left, R.anim.slide_to_right).replace(frameLayout!!.id, fragment!!).commitAllowingStateLoss()
    }

    fun setDisplayFragment(number: Int, title: String, removeStack: Boolean) {
        setTitleBar(title)
        when (number) {
            1 -> replaceFragment(VendorRestaurantOneFragment.newInstance(), removeStack)
            2 -> {
                mTitleFrag = title
                replaceFragment(VendorRestaurantTwoFragment.newInstance(title), removeStack)
            }
            3 -> {
                mTitleFrag = title
                replaceFragment(VendorRestaurantReviewFragment.newInstance(title), removeStack)
            }
            4 -> replaceFragment(SuccesBookingFragment.newInstance(), removeStack)
        }
    }

    fun setDisplayDialog(number: Int, title: String, regionId: String) {
        when (number) {
            6 -> showCitySelectionDialog(title, regionId)
            7 -> showRegionSelectionDialogSingle(title)
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
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorRestaurantOneFragment -> finish()
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorRestaurantTwoFragment -> {

                if (isFromEdit) {
                    finish()
                } else {
                    setTitleBar(resources.getString(R.string.bussiness_information))
                    fragmentManager!!.popBackStackImmediate()
                }
            }
//            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorRestaurantThreeFragment -> {
//                if (isFromEdit) {
//                    finish()
//                } else {
//                    setTitleBar(resources.getString(R.string.service_information))
//                    fragmentManager!!.popBackStackImmediate()
//                }
//            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorRestaurantReviewFragment -> {
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

    private fun showRegionSelectionDialogSingle(from: String) {
        regionDialogFragmentsingle = RegionDialogFragmentSingle.newInstance(from, "")
        manager = supportFragmentManager
        regionDialogFragmentsingle!!.show(manager, "My Dialog")
    }


    fun getFieldPojo(): VendorRestaurantServiceModel {
        return vendorRestaurantServiceModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
        when {
            fragment is VendorRestaurantOneFragment -> fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun isFromedit(): Boolean {
        return isFromEdit
    }

    public fun hitApi() {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        vendorRestaurantServiceModel.business_subcategory_id = AppConstants.SUBCAT_RESTAURANT_DINNIG.toString()
        vendorRestaurantServiceModel.business_category_id = AppConstants.CAT_RESTAURANT_DINNIG.toString()
        apiInterface?.restaurantServiceSetUp("Bearer " + SharedPreferenceManager.getAuthToken(), vendorRestaurantServiceModel)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<TaxiServiceResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(userResponse: TaxiServiceResponse) {
                        handleResults(userResponse)
                        AppLog.e(TAG, userResponse.toString())
                    }

                    override fun onError(e: Throwable) {
                        handleError(e)
                    }

                    override fun onComplete() {}
                })
    }


    public fun hitApiEdit(vendorRestaurantServiceModel: VendorRestaurantServiceModel) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        vendorRestaurantServiceModel.action = AppConstants.ACTION_EDIT
        apiInterface?.restaurantServiceSetUp("Bearer " + SharedPreferenceManager.getAuthToken(), vendorRestaurantServiceModel)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<TaxiServiceResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(userResponse: TaxiServiceResponse) {
                        handleResultsEdit(userResponse)
                        AppLog.e(TAG, userResponse.toString())
                    }

                    override fun onError(e: Throwable) {
                        handleError(e)
                    }

                    override fun onComplete() {}
                })
    }

    private fun handleResultsEdit(userResponse: TaxiServiceResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            vendorRestaurantServiceModel.business_id = userResponse.data.business_id.toInt()
            vendorRestaurantServiceModel.service_id = userResponse.data.service_id
            CachingManager.setVendorRestaurantInfo(vendorRestaurantServiceModel)
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
        }
    }

    private fun handleResults(userResponse: TaxiServiceResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            vendorRestaurantServiceModel.business_id = userResponse.data.business_id.toInt()
            vendorRestaurantServiceModel.service_id = userResponse.data.service_id
            CachingManager.setVendorRestaurantInfo(vendorRestaurantServiceModel)
            if (imageFiles != null) {
                UploadBussinessImage.uploadPicsOnServer(imageFiles!!, userResponse.data.business_id, this)
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

    override fun onSuccess(userImageUploadResponse: BusinessImagesResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userImageUploadResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            vendorRestaurantServiceModel.business_images = userImageUploadResponse.data
            CachingManager.setVendorRestaurantInfo(vendorRestaurantServiceModel)
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
        ProgressDialogLoader.progressDialogDismiss()
        UiValidator.displayMsgSnack(coordinator, this, e.message)
        AppLog.e(TAG, e.message)
    }

    override fun onSubscribe(d: Disposable) {
    }

    public fun uploadPic(files: ArrayList<File>, bussinessId: String) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        if (files != null && files.size > 0) {
            files.subList(0, vendorRestaurantServiceModel.business_images.size).clear()
        }
        UploadBussinessImage.uploadPicsOnServer(files, bussinessId, this)

    }

}
