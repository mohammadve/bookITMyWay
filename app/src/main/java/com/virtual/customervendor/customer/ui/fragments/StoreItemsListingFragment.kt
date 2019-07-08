package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.PurchaseItemsActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CustomerBookingListModel
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
import kotlinx.android.synthetic.main.fragment_store_items.*

class StoreItemsListingFragment : Fragment() {

    companion object {
        fun newInstance(): StoreItemsListingFragment {
            val fragment = StoreItemsListingFragment()
            return fragment
        }
    }

    var TAG: String = StoreItemsListingFragment::class.java.name;
    var apiService: ApiInterface? = null
    var customerBookingListModel = CustomerBookingListModel()
    var storeCategoryAdapter: StoreCategoryAdapter? = null
    var count = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_store_items, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiClient.client.create(ApiInterface::class.java)
        customerBookingListModel = (activity as PurchaseItemsActivity).getcustomerBookingListModel()
        if (customerBookingListModel != null)
            getStoreItems()
        (activity as PurchaseItemsActivity).setCartVisible(false)
        (activity as PurchaseItemsActivity).setCartValueVisible(false)
    }



    fun getStoreItems() {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))
            apiService?.getProductCategory("Bearer " + SharedPreferenceManager.getAuthToken(),customerBookingListModel.service_id?.toInt()!!)
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
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }
    }


    private fun handleResults(eventListingResponse: ProductCategoryResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (eventListingResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            createAdapterEvents(eventListingResponse.data)
        } else {
            UiValidator.displayMsgSnack(cons, activity, eventListingResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t.message)
    }

    private fun createAdapterEvents(eventlisting: ArrayList<ProductCategoryModel>) {
        storeCategoryAdapter = StoreCategoryAdapter(activity as PurchaseItemsActivity, eventlisting) { productModel ->
            (activity as PurchaseItemsActivity).productCategoryModel = productModel
            (activity as PurchaseItemsActivity).setDisplayFragment(4, productModel.name, false)
            (activity as PurchaseItemsActivity).categoryName = productModel.name
            //            var intent: Intent = Intent(this!!, VendorStoreSubcategoryListActivity::class.java)
//            var bundle = Bundle()
//            bundle.putSerializable(AppConstants.OREDER_DATA, productModel)
//            intent.putExtras(bundle)
//            startActivity(intent)
            SlideAnimationUtill.slideNextAnimation(activity as PurchaseItemsActivity)

        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_items.layoutManager = manager
        rv_items.adapter = (storeCategoryAdapter)
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