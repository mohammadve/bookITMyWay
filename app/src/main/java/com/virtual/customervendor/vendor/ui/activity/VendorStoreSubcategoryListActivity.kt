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
import com.bumptech.glide.Glide
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.ItemPriceStoreModel
import com.virtual.customervendor.model.ProductCategoryModel
import com.virtual.customervendor.model.response.StoreListingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.VendorStoreSubCategoryAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_store_subcategory.*

class VendorStoreSubcategoryListActivity : BaseActivity(), View.OnClickListener, VendorStoreSubCategoryAdapter.UpdateItems {

    override fun deleteItem(itempriceModel: ItemPriceStoreModel) {
    }

    override fun updateItem(itemPriceModel: ItemPriceStoreModel) {
            if (store_cat_id == AppConstants.STORE_CAT_SEAT_SERVICE.toInt()) {
                var intent: Intent = Intent(this!!, VendorListEditStoreItemsActivity::class.java)
                var bundle = Bundle()
                bundle.putSerializable(AppConstants.OREDER_DATA, itemPriceModel)
                intent.putExtras(bundle)
                startActivityForResult(intent, 113)
            }else if(store_cat_id == AppConstants.STORE_CAT_CUSTOM.toInt()){
                var intent: Intent = Intent(this!!, VendorListEditStoreCustomItemActivity::class.java)
                var bundle = Bundle()
                bundle.putSerializable(AppConstants.OREDER_DATA, itemPriceModel)
                intent.putExtras(bundle)
                startActivityForResult(intent, 113)
            }

            SlideAnimationUtill.slideNextAnimation(this@VendorStoreSubcategoryListActivity)

    }

    var storeSubCategoryAdapter: VendorStoreSubCategoryAdapter? = null
    var TAG: String = VendorStoreSubcategoryListActivity::class.java.simpleName
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var apiInterface: ApiInterface? = null
    var productModel = ProductCategoryModel()
    var store_cat_id: Int = 0


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_add -> {
                if (store_cat_id.toString() == AppConstants.STORE_CAT_SEAT_SERVICE) {
                    var intent: Intent = Intent(this, VendorAddStoreItemsActivity::class.java)
                    var bundle = Bundle()
                    bundle.putSerializable(AppConstants.OREDER_DATA, productModel)
                    intent.putExtras(bundle)
                    startActivityForResult(intent, 112)
                    SlideAnimationUtill.slideNextAnimation(this)
                } else if (store_cat_id.toString() == AppConstants.STORE_CAT_CLOTHING) {
                    var intent: Intent = Intent(this, VendorAddStoreItemsClothsActivity::class.java)
                    var bundle = Bundle()
                    bundle.putSerializable(AppConstants.OREDER_DATA, productModel)
                    intent.putExtras(bundle)
                    startActivityForResult(intent, 112)
                    SlideAnimationUtill.slideNextAnimation(this)
                } else if (store_cat_id.toString() == AppConstants.STORE_CAT_CUSTOM) {
                    var intent: Intent = Intent(this, VendorAddStoreItemsOtherActivity::class.java)
                    var bundle = Bundle()
                    bundle.putSerializable(AppConstants.OREDER_DATA, productModel)
                    intent.putExtras(bundle)
                    startActivityForResult(intent, 112)
                    SlideAnimationUtill.slideNextAnimation(this)
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_subcategory)
        UiValidator.hideSoftKeyboard(this)
        productModel = intent.extras.get(AppConstants.OREDER_DATA) as ProductCategoryModel
        store_cat_id = intent.extras.get(AppConstants.STORE_CAT_ID) as Int

        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()
        if (productModel != null) {
            Glide.with(this).load(productModel.newimage!!).into(img_upload)
            hitApi()
        }

    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.store_items)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)

        val iv_add = toolbar!!.findViewById(R.id.iv_add) as ImageView
        iv_add.visibility = View.VISIBLE
        iv_add.setOnClickListener(this)

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
            apiInterface?.getStoreItemListing("Bearer " + SharedPreferenceManager.getAuthToken(), SharedPreferenceManager.getServiceId(), productModel.id.toInt(),store_cat_id)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<StoreListingResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: StoreListingResponse) {
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

    private fun handleResults(eventListingResponse: StoreListingResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (eventListingResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            if (eventListingResponse.itemlisting.size > 0) {
                emptyString.visibility = View.GONE
                createAdapterEvents(eventListingResponse.itemlisting)
            } else {
                emptyString.visibility = View.VISIBLE
            }
        } else {
            UiValidator.displayMsgSnack(coordinator, this, eventListingResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t.message)
    }

    private fun createAdapterEvents(eventlisting: ArrayList<ItemPriceStoreModel>) {
        storeSubCategoryAdapter = VendorStoreSubCategoryAdapter(this, eventlisting,this)

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_event.layoutManager = manager
        rv_event.adapter = (storeSubCategoryAdapter)
        rv_event.isNestedScrollingEnabled = false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 112 && resultCode == Activity.RESULT_OK) {
            hitApi()
        } else if (requestCode == 113 && resultCode == Activity.RESULT_OK) {
            hitApi()
        }
    }
}
