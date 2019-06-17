package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessDetail
import com.virtual.customervendor.model.PackageModel
import com.virtual.customervendor.model.response.PackageListResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.VendorPackagesAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sightseen_packages_vendor.*
import kotlinx.android.synthetic.main.custom_toolbar_appbar_event.*
import java.io.Serializable

class VendorSightSeenPackages : BaseActivity(), View.OnClickListener {

    var vendorPackagesAdapter: VendorPackagesAdapter? = null
    var packagelist: ArrayList<PackageModel> = ArrayList()

    var TAG: String = VendorSightSeenPackages::class.java.simpleName
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var apiInterface: ApiInterface? = null
    var businessDetail = BusinessDetail()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_add -> {
                var intent: Intent = Intent(this, VendorPackageDetailActivity::class.java)
                intent.putExtra(AppKeys.BUSINESS_ID, businessDetail as Serializable)
                intent.putExtra(AppConstants.FROM_ADD, true)
                intent.putExtra(AppConstants.FRAGMENT_NUMBER, 2)
                startActivityForResult(intent, 112)
                SlideAnimationUtill.slideNextAnimation(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sightseen_packages_vendor)
        businessDetail = intent.extras.get(AppConstants.BUSINESS_DATA) as BusinessDetail
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        UiValidator.hideSoftKeyboard(this)
        init()
        hitApi()
    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.packages)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        iv_add.setOnClickListener(this)
        iv_add.visibility = View.VISIBLE
        createAdapterEvents()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun hitApi() {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.getPackageList("Bearer " + SharedPreferenceManager.getAuthToken(), SharedPreferenceManager.getBussinessID().toInt())
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<PackageListResponse> {
                        override fun onSubscribe(d: Disposable) {                        }

                        override fun onNext(detailResponse: PackageListResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            handleResults(detailResponse)
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


    private fun handleResults(restaurantmenu: PackageListResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (restaurantmenu.status.equals(AppConstants.KEY_SUCCESS)) {
            if (restaurantmenu.eventlisting != null && restaurantmenu.eventlisting.size > 0) {
                packagelist.clear()
                packagelist.addAll(restaurantmenu.eventlisting)
                vendorPackagesAdapter?.notifyDataSetChanged()
            }
        } else {
            UiValidator.displayMsgSnack(coordinator, this, restaurantmenu.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
        AppLog.e(TAG, t.message)
    }

    private fun createAdapterEvents() {
        vendorPackagesAdapter = VendorPackagesAdapter(this, packagelist) { packageModel ->
            var intent: Intent = Intent(this, VendorPackageDetailActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.BUSINESS_DATA, packageModel)
            intent.putExtras(bundle)
            startActivityForResult(intent, 112)
            SlideAnimationUtill.slideNextAnimation(this)
        }
        rv_packages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_packages.adapter = vendorPackagesAdapter
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AppLog.e("vhhhgdfkgjhdkl", requestCode.toString() + " " + resultCode)
        if (requestCode == 112 && resultCode == Activity.RESULT_OK) {
            hitApi()
        }
    }
}
