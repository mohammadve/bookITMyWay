package com.virtual.customervendor.customer.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.OfferListingActivity
import com.virtual.customervendor.customer.ui.activity.PurchaseItemsActivity
import com.virtual.customervendor.customer.ui.adapter.StoreItemsConfirmOrderAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.ItemPriceStoreModel
import com.virtual.customervendor.model.VenderOffersModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.response.ApplyOfferResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cart.*

class StoreCartFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(): StoreCartFragment {
            val fragment = StoreCartFragment()
            return fragment
        }
    }

    var TAG: String = StoreCartFragment::class.java.name;
    var apiService: ApiInterface? = null
    var storeItemsAdapter: StoreItemsConfirmOrderAdapter? = null
    var add = 0
    val requestCode: Int = 101
    var serviceDetail: VendorServiceDetailModel = VendorServiceDetailModel()
    var addedItemsListNew: ArrayList<ItemPriceStoreModel> = ArrayList<ItemPriceStoreModel>()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_next -> {
                (activity as PurchaseItemsActivity).setDisplayFragment(7, resources.getString(R.string.delivery_information), false)
            }
            R.id.offer -> {
                if (!ed_coupon.text.toString().isEmpty()) {
                    getOfferPrice()
                } else {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.nocoupon_code))
                }
            }
            R.id.viewoffer -> {
                var intent: Intent = Intent(activity, OfferListingActivity::class.java)
                intent.putExtra(AppKeys.BUSINESS_ID, serviceDetail.businessData.business_id.toString())
                startActivityForResult(intent, requestCode)
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_cart, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)
        viewoffer.setOnClickListener(this)
        offer.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        serviceDetail = (activity as PurchaseItemsActivity).businessDetailModel
        addedItemsListNew = (activity as PurchaseItemsActivity).getAddedCartItems()
        createAdapterEvents()
        callActivity()
    }


    private fun createAdapterEvents() {
        storeItemsAdapter = StoreItemsConfirmOrderAdapter(activity!!,addedItemsListNew)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_items.layoutManager = manager
        rv_items.adapter = (storeItemsAdapter)
    }

    fun callActivity() {
        add = 0
        for (storeCartModel in addedItemsListNew) {
            add = add + (storeCartModel.item_price!!.toDouble() * storeCartModel.quantity).toInt()
        }
        tv_total_value.text = AppUtils.getRateWithSymbol("" + add)

        (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_TOTAL_PRICE, "" + add)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            val result = data?.getSerializableExtra(AppKeys.OFFER_RESULT) as VenderOffersModel
            AppLog.e(TAG, result.toString())
            ed_coupon.setText(result.coupon)
        }
    }

    private fun getOfferPrice() {
        val orderprice = add.toString()
        val business_id = serviceDetail.businessData.business_id.toString()
        val coupon = ed_coupon.text.toString()
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))
            apiService?.getOfferPrice("Bearer " + SharedPreferenceManager.getAuthToken(), business_id, coupon, orderprice)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<ApplyOfferResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(userResponse: ApplyOfferResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, userResponse.toString())
                            if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
                                UiValidator.displayMsgSnack(cons, activity, userResponse.message)
                                (activity as PurchaseItemsActivity).applyOfferModel = userResponse.data
                                (activity as PurchaseItemsActivity).setDisplayFragment(7, resources.getString(R.string.delivery_information), false)
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, userResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()

                            if (e != null)
                                UiValidator.displayMsgSnack(cons, activity, e.message)
                        }

                        override fun onComplete() {
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }
    }


}