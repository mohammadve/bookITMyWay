package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.PurchaseItemsActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.*
import com.virtual.customervendor.model.response.StoreListingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.StoreSubCategoryAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_store_product_items.*

class StoreProductListingFragment : Fragment() {

    companion object {
        fun newInstance(): StoreProductListingFragment {
            val fragment = StoreProductListingFragment()
            return fragment
        }
    }

    var productCategoryModel = ProductCategoryModel()
    var TAG: String = StoreProductListingFragment::class.java.name;
    var apiService: ApiInterface? = null
    var storeSubCategoryAdapter: StoreSubCategoryAdapter? = null
    var businessDetailModel: VendorServiceDetailModel = VendorServiceDetailModel()
    var count = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_store_product_items, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiClient.client.create(ApiInterface::class.java)
        businessDetailModel = (activity as PurchaseItemsActivity).businessDetailModel

        productCategoryModel = (activity as PurchaseItemsActivity).productCategoryModel
        if (productCategoryModel != null) {
            getStoreItems()
            Glide.with(this).load(productCategoryModel.newimage).into(img_upload)
        }
        (activity as PurchaseItemsActivity).setCartVisible(true)
//        (activity as PurchaseItemsActivity).addedItemsList.clear()

        (activity as PurchaseItemsActivity).setCartValueVisible(true)

    }


    fun getStoreItems() {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))
            apiService?.getStoreItemListing("Bearer " + SharedPreferenceManager.getAuthToken(), businessDetailModel.service_id?.toInt()!!, productCategoryModel.id.toInt())
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
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }
    }


    private fun handleResults(eventListingResponse: StoreListingResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (eventListingResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            createAdapterEvents(eventListingResponse.itemlisting)
        } else {
            UiValidator.displayMsgSnack(cons, activity, eventListingResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t.message)
    }

    private fun createAdapterEvents(eventlisting: ArrayList<ItemPriceStoreModel>) {
        storeSubCategoryAdapter = StoreSubCategoryAdapter(activity as PurchaseItemsActivity, eventlisting) { itemPriceModel ->

            (activity as PurchaseItemsActivity).itemPriceModel = itemPriceModel
            (activity as PurchaseItemsActivity).setDisplayFragment(5, itemPriceModel.item_name, false)
            (activity as PurchaseItemsActivity).productName = itemPriceModel.item_name
        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_items.layoutManager = manager
        rv_items.adapter = (storeSubCategoryAdapter)
        rv_items.isNestedScrollingEnabled = false
    }

//    fun callActivity(model: ItemPriceStoreModel) {
//        count++
//        val storeCartModel: StoreCartModel = StoreCartModel()
//        storeCartModel.item_id = "" + model.item_id
//        storeCartModel.item_name = model.item_name
//        storeCartModel.price = model.item_price
//        storeCartModel.quantity = "1"
//        (activity as PurchaseItemsActivity).setCartValueVisible(true, count.toString())
//        (activity as PurchaseItemsActivity).addedItemsList.add(storeCartModel)
//    }


}