package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.dialogFragment.CityDialogFragment
import com.virtual.customervendor.customer.ui.dialogFragment.RegionDialogFragmentMulti
import com.virtual.customervendor.customer.ui.dialogFragment.RegionDialogFragmentSingle
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.PackageModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.response.TaxiServiceResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.utills.ProgressDialogLoader.progressDialogCreation
import com.virtual.customervendor.vendor.ui.fragments.VendorPackageDetailFragment
import com.virtual.customervendor.vendor.ui.fragments.VendorPackageEditFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_events_detail_vendor.*
import kotlinx.android.synthetic.main.custom_toolbar_appbar_event.*

class VendorPackageDetailActivity : BaseActivity(), View.OnClickListener, RegionDialogFragmentMulti.MultiRegionSelectionInterface {
    var datetime: String? = null
    var manager: FragmentManager? = null
    var TAG: String = VendorPackageDetailActivity::class.java.simpleName
    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var mTitleFrag: String? = null
    var cityDialogFragment: CityDialogFragment? = null
    var regionDialogFragmentsingle: RegionDialogFragmentSingle? = null
    var apiInterface: ApiInterface? = null
    var vendorEventServiceRequest = PackageModel()
    var isFromAdd: Boolean = false
    var fragmentCount: Int = 0
    var regionDialogFragmentMulti: RegionDialogFragmentMulti? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_delete -> {
                hitApiDelete(vendorEventServiceRequest)
            }
            R.id.iv_edit -> {
                setDisplayFragment(2, resources.getString(R.string.edit_package), false)
            }
        }
    }

    override fun done(bean: ArrayList<RegionModel>, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        if (regionDialogFragmentMulti != null) {
            regionDialogFragmentMulti!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is VendorPackageEditFragment) fragment.updateSelectedServiceArea(bean)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_detail_vendor)

        isFromAdd = intent.getBooleanExtra(AppConstants.FROM_ADD, false)
        if (!isFromAdd) {
            vendorEventServiceRequest = intent.extras.getSerializable(AppConstants.BUSINESS_DATA) as PackageModel
        }
        fragmentCount = intent.getIntExtra(AppConstants.FRAGMENT_NUMBER, 0)
        AppLog.e(TAG, "isFromAdd= " + isFromAdd + " fragmentCount= " + fragmentCount)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()
    }

    fun isFromAdd1(): Boolean {
        return isFromAdd
    }


    fun init() {
        frameLayout = findViewById(R.id.flContentnew) as FrameLayout
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        iv_delete.setOnClickListener(this)
        iv_edit.setOnClickListener(this)

        if (isFromAdd) {
            setDisplayFragment(2, resources.getString(R.string.package_), false)
        } else
            setDisplayFragment(1, resources.getString(R.string.package_), false)
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
                replaceFragment(VendorPackageDetailFragment.newInstance(), removeStack)
            }
            2 -> {
                mTitleFrag = title
                iv_delete.visibility = View.GONE
                iv_edit.visibility = View.GONE
                replaceFragment(VendorPackageEditFragment.newInstance(title), removeStack)
            }
        }
    }


    fun setDisplayDialog(number: Int, title: String) {
        when (number) {
            7 -> showNationalitySelectionDialogMMutli(title)
        }
    }

    private fun showNationalitySelectionDialogMMutli(from: String) {
        regionDialogFragmentMulti = RegionDialogFragmentMulti.newInstance(from)
        manager = supportFragmentManager
        regionDialogFragmentMulti!!.show(manager, "My Dialog")
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
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorPackageDetailFragment -> finish()
            fragmentManager!!.findFragmentById(R.id.flContentnew) is VendorPackageEditFragment -> {
                if (isFromAdd1()) {
                    finish()
                } else {
                    setTitleBar(resources.getString(R.string.package_))
                    iv_delete.visibility = View.VISIBLE
                    iv_edit.visibility = View.VISIBLE
                    fragmentManager!!.popBackStackImmediate()
                }
            }
        }
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun getEventFieldPojo(): PackageModel {
        return vendorEventServiceRequest
    }


    public fun hitApiEdit(action: String) {
        if (AppUtils.isInternetConnected(this)) {
            progressDialogCreation(this, getString(R.string.please_wait))
            vendorEventServiceRequest.action = action
            vendorEventServiceRequest.business_category_id = AppConstants.CAT_TRANSPORTATION.toString()
            vendorEventServiceRequest.business_subcategory_id = AppConstants.SUBCAT_TRANS_SIGHTSEEING.toString()
            vendorEventServiceRequest.business_id = SharedPreferenceManager.getBussinessID()
            apiInterface?.addUpdateSightSeen("Bearer " + SharedPreferenceManager.getAuthToken(), vendorEventServiceRequest)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<TaxiServiceResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(userResponse: TaxiServiceResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                setResult(Activity.RESULT_OK)
                                finish()
                            } else {
                                UiValidator.displayMsgSnack(coordinator, this@VendorPackageDetailActivity, userResponse.message)
                            }
                            AppLog.e(TAG, userResponse.toString())
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null){
                            UiValidator.displayMsgSnack(coordinator, this@VendorPackageDetailActivity, e.message)
                            AppLog.e(TAG, e.message)}
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    public fun hitApiDelete(vendorEventDetail: PackageModel) {
        if (AppUtils.isInternetConnected(this)) {
            progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.deleteSightSeen("Bearer " + SharedPreferenceManager.getAuthToken(), vendorEventDetail.service_id)
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
                                UiValidator.displayMsgSnack(coordinator, this@VendorPackageDetailActivity, userResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null){
                            UiValidator.displayMsgSnack(coordinator, this@VendorPackageDetailActivity, e.message)
                            AppLog.e(TAG, e.message)}
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }


}
