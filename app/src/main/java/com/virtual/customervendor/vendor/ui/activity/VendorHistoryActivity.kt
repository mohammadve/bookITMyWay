package com.virtual.customervendor.vendor.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.adapter.OrderadapterVendor
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CustomerOrderModel
import com.virtual.customervendor.model.response.CustomerOrderResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_vendor_history.*

class VendorHistoryActivity : BaseActivity(), View.OnClickListener, PagingListeners {
    var orderAdapter: OrderadapterVendor? = null
    val TAG: String = VendorHistoryActivity::class.java.simpleName
    var apiService: ApiInterface? = null
    val list: ArrayList<CustomerOrderModel> = java.util.ArrayList()
    var businessId: String = String()
    var category_id: String = String()
    var subcategory_id: String = String()
    var isShowNoData: Boolean = true


    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_history)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        init()
        createAdapterView()

    }

    fun init() {
      var   toolbar = findViewById(R.id.toolbar) as AppBarLayout
      var  mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.order_history)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)

    }
    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    private fun createAdapterView() {
        orderAdapter = OrderadapterVendor(this, this, list) { orderDeatail ->

            var intent: Intent = Intent(this, VendorOrderDetailActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.OREDER_DATA, orderDeatail)
            intent.putExtras(bundle)
            startActivityForResult(intent, 113)
            SlideAnimationUtill.slideNextAnimation(this as AppCompatActivity)

        }
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_list.layoutManager = manager
        rv_list.adapter = (orderAdapter)

        getBusinessID()
    }

    fun getVendorOrderList(businessID: String, offset: Int) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService?.getBusinessOrderList("Bearer " + SharedPreferenceManager.getAuthToken(), businessID, offset, SharedPreferenceManager.getCategoryId(), SharedPreferenceManager.getSubcategoryId(), "past")


                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<CustomerOrderResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: CustomerOrderResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                if (detailResponse.data.size > 0) {
                                    nodatafound.visibility = View.GONE
                                    list.addAll(detailResponse.data)
                                    orderAdapter?.notifyDataSetChanged()
                                    isShowNoData = false
                                } else {
                                    if (isShowNoData)
                                        nodatafound.visibility = View.VISIBLE
                                }
                            } else {
                                UiValidator.displayMsgSnack(coordinator, this as AppCompatActivity, detailResponse.message)
                                if (isShowNoData)
                                    nodatafound.visibility = View.VISIBLE

                            }
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                            if (isShowNoData)
                                nodatafound.visibility = View.VISIBLE

                        }

                        override fun onComplete() {
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    private fun performSearch() {
        et_searchText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val searchText = editable.toString()
                orderAdapter!!.filter.filter(searchText)
            }
        })
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
        AppLog.e(TAG, t?.message)
    }

    fun getBusinessID() {

        when (SharedPreferenceManager.getCategoryId()) {

            AppConstants.CAT_TRANSPORTATION -> {
                when (SharedPreferenceManager.getSubcategoryId()) {
                    AppConstants.SUBCAT_TRANS_TAXI -> businessId = SharedPreferenceManager.getBussinessID()
                    AppConstants.SUBCAT_TRANS_LIMO -> businessId = SharedPreferenceManager.getBussinessID()
                    AppConstants.SUBCAT_TRANS_TOURBUS -> businessId = SharedPreferenceManager.getBussinessID()
                    AppConstants.SUBCAT_TRANS_SIGHTSEEING -> businessId = SharedPreferenceManager.getBussinessID()
                }
            }
            AppConstants.CAT_RESTAURANT_DINNIG -> {
                when (SharedPreferenceManager.getSubcategoryId()) {
                    AppConstants.SUBCAT_RESTAURANT_DINNIG -> businessId = SharedPreferenceManager.getBussinessID()
                }
            }
            AppConstants.CAT_HEALTH_BODYCARE -> {
                when (SharedPreferenceManager.getSubcategoryId()) {
                    AppConstants.SUBCAT_HEALTH_DOCTOR -> businessId = SharedPreferenceManager.getBussinessID()
                    AppConstants.SUBCAT_HEALTH_HAIR -> businessId = SharedPreferenceManager.getBussinessID()
                    AppConstants.SUBCAT_HEALTH_NAIL -> businessId = SharedPreferenceManager.getBussinessID()
                    AppConstants.SUBCAT_HEALTH_PHYSIO -> businessId = SharedPreferenceManager.getBussinessID()
                    AppConstants.SUBCAT_HEALTH_OTHER -> businessId = SharedPreferenceManager.getBussinessID()
                }
            }
            AppConstants.CAT_PARKING_VALET -> {
                when (SharedPreferenceManager.getSubcategoryId()) {
                    AppConstants.SUBCAT_PARKING_VALET -> businessId = SharedPreferenceManager.getBussinessID()
                }
            }


            AppConstants.CAT_EVENT_TICKET -> {
                when (SharedPreferenceManager.getSubcategoryId()) {
                    AppConstants.SUBCAT_EVENT_TICKET -> businessId = SharedPreferenceManager.getBussinessID()
                }
            }
            AppConstants.CAT_STORE_SELLER -> {
                when (SharedPreferenceManager.getSubcategoryId()) {
                    AppConstants.SUBCAT_STORE_SELLER -> businessId = SharedPreferenceManager.getBussinessID()
                }
            }
        }
        getVendorOrderList(businessId, 0)

        performSearch()
    }

    override fun onFinishListener() {
        getVendorOrderList(businessId, list.size)
    }

    override fun onFinishListener(type: Int) {

    }

}
