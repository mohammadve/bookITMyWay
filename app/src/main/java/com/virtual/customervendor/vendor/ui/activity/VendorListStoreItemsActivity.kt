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
import com.virtual.customervendor.model.ProductCategoryModel
import com.virtual.customervendor.model.response.ProductCategoryResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.StoreCategoryAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_storeitems_vendor.*

class VendorListStoreItemsActivity : BaseActivity(), View.OnClickListener {
    var storeCategoryAdapter: StoreCategoryAdapter? = null

    var TAG: String = VendorListStoreItemsActivity::class.java.simpleName
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
                /*


                var intent: Intent = Intent(this, ClothingCategoryModel::class.java)
                intent.putExtra(AppKeys.SERVICE_ID, businessDetail.service_id)
                startActivityForResult(intent, 112)
                SlideAnimationUtill.slideNextAnimation(this)*/

                var intent: Intent = Intent(this, VendorAddStoreItemsClothsActivity::class.java)
                intent.putExtra(AppKeys.SERVICE_ID, businessDetail.service_id)
                startActivityForResult(intent, 112)
                SlideAnimationUtill.slideNextAnimation(this)

            }
            R.id.iv_edit -> {
                var intent: Intent = Intent(this, VendorListEditStoreItemsActivity::class.java)
                startActivityForResult(intent, 112)
                SlideAnimationUtill.slideNextAnimation(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_storeitems_vendor)
        UiValidator.hideSoftKeyboard(this)
        businessDetail = intent.extras.get(AppConstants.BUSINESS_DATA) as BusinessDetail
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()

        if (businessDetail != null) {
            hitApi()
        }
    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = businessDetail.business_name
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
    }


    fun hitApi() {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.getProductCategory("Bearer " + SharedPreferenceManager.getAuthToken(),businessDetail.service_id!!)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<ProductCategoryResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: ProductCategoryResponse) {
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

    private fun handleResults(eventListingResponse: ProductCategoryResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (eventListingResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            createAdapterEvents(eventListingResponse.data)
        } else {
            UiValidator.displayMsgSnack(coordinator, this, eventListingResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t.message)
    }

    private fun createAdapterEvents(eventlisting: ArrayList<ProductCategoryModel>) {
        storeCategoryAdapter = StoreCategoryAdapter(this, eventlisting) {productModel ->
            var intent: Intent = Intent(this!!, VendorStoreSubcategoryListActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.OREDER_DATA, productModel)
            intent.putExtras(bundle)
            startActivity(intent)
            SlideAnimationUtill.slideNextAnimation(this@VendorListStoreItemsActivity)

        }
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_event.layoutManager = manager
        rv_event.adapter = (storeCategoryAdapter)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 112 && resultCode == Activity.RESULT_OK) {
            hitApi()
        }
    }
}
