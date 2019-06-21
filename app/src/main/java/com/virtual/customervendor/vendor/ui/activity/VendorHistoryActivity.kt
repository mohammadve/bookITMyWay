package com.virtual.customervendor.vendor.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding.widget.RxTextView
import com.virtual.customer_vendor.utill.AppUtill
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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class VendorHistoryActivity : BaseActivity(), View.OnClickListener, PagingListeners {
    var orderAdapter: OrderadapterVendor? = null
    val TAG: String = VendorHistoryActivity::class.java.simpleName
    var apiService: ApiInterface? = null
    val list: ArrayList<CustomerOrderModel> = java.util.ArrayList()
    var businessId: String = String()
    var category_id: String = String()
    var subcategory_id: String = String()
    var isShowNoData: Boolean = true
    var filterType = ""
    var fromDate = ""
    var toDate = ""

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.ll_FilterView -> {
                showPopUpMenuForFilter(v)

            }
        }
    }

    private fun showPopUpMenuForFilter(v: View) {

        var popup: PopupMenu = PopupMenu(this, v)
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.filter_popup_menu, popup.getMenu());


        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.m_all -> {
                    filterType = ""
                    et_filterText.setText("All")

                    getVendorOrderList(businessId, 0)
                    //Toast.makeText(activity, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                }
                R.id.m_today -> {
                    filterType = "ondate"
                    val c = Calendar.getInstance()
                    c.add(Calendar.DAY_OF_MONTH, -1)
                    SimpleDateFormat("yyyy-MM-dd").format(c.time)

                    fromDate =    SimpleDateFormat("yyyy-MM-dd").format(c.time)
                    toDate =   SimpleDateFormat("yyyy-MM-dd").format(c.time)
                    et_filterText.setText(item.title)


                    getVendorOrderList(businessId, 0)
                }
                R.id.m_week -> {
                    filterType = "onweek"
                    et_filterText.setText(item.title)

                    getVendorOrderList(businessId, 0)
                }
                R.id.m_monthly -> {
                    filterType = "onmonth"
                    et_filterText.setText(item.title)

                    getVendorOrderList(businessId, 0)
                }
                R.id.m_custom -> {
                    filterType = "ondate"
                    showDatePickerDialog()

                }

            }
            true
        })




        popup.show()
    }

    private fun showDatePickerDialog() {
        val delayDialog = Dialog(this)
        delayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        delayDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        delayDialog.setContentView(R.layout.filter_date_pick_layout)
        delayDialog.setCancelable(false)
        delayDialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


        val tv_txt_ok = delayDialog.findViewById<TextView>(R.id.tv_txt_ok)
        val tv_from_date = delayDialog.findViewById<TextView>(R.id.tv_from_date)
        val tv_to_date = delayDialog.findViewById<TextView>(R.id.tv_to_date)
        val tv_txt_cancel = delayDialog.findViewById<TextView>(R.id.tv_txt_cancel)

        tv_txt_ok.setOnClickListener {

            if (tv_from_date.text.toString().trim().isEmpty() || tv_to_date.text.toString().trim().isEmpty()) {
                UiValidator.displayMsgSnack(coordinator, this, "Please select date first")

            } else {
                fromDate = tv_from_date.text.toString().trim()
                toDate = tv_to_date.text.toString().trim()
                et_filterText.setText(fromDate + " to " + toDate)
                getVendorOrderList(businessId, 0)
                delayDialog.dismiss()

            }

        }
        tv_txt_cancel.setOnClickListener {
            delayDialog.dismiss()

        }
        tv_from_date.setOnClickListener {
            if (!tv_to_date.text.toString().trim().isBlank()) {
                val c = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf.parse(tv_to_date.text.toString().trim())
                c.time = date


                AppUtill.getBackDateForFilter(tv_from_date, this, SimpleDateFormat("yyyy-MM-dd").format(c.time))
            } else {
                val c = Calendar.getInstance()
                c.add(Calendar.DAY_OF_MONTH, -1)
                AppUtill.getBackDateForFilter(tv_from_date, this, SimpleDateFormat("yyyy-MM-dd").format(c.time))
            }


        }
        tv_to_date.setOnClickListener {
            AppUtill.getBackDateForFilter(tv_to_date, this, null)

        }
        delayDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_history)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        init()
        createAdapterView()

    }

    fun init() {
        var toolbar = findViewById(R.id.toolbar) as AppBarLayout
        var mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.order_history)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        ll_FilterView.setOnClickListener(this)

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


            val obs = RxTextView.textChanges(et_searchText)
                    //.filter { charSequence -> charSequence.length > 0 }
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .map<String> { charSequence -> charSequence.toString() }
            obs.subscribe { string ->


                runOnUiThread {
                    //                        progress_serch_order.visibility = View.VISIBLE
                    ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))

                    //  ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
                }

                if (!filterType.equals("ondate")) {
                    fromDate = ""
                    toDate = ""
                }
                apiService?.getBusinessOrderList("Bearer " + SharedPreferenceManager.getAuthToken(), businessID, offset,
                        SharedPreferenceManager.getCategoryId(), SharedPreferenceManager.getSubcategoryId(),
                        "past", string, filterType, fromDate, toDate)


                        ?.subscribeOn(Schedulers.io())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(object : Observer<CustomerOrderResponse> {
                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(detailResponse: CustomerOrderResponse) {
                                AppLog.e(TAG, detailResponse.toString())
                                ProgressDialogLoader.progressDialogDismiss()
                                if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {

                                    if (offset == 0) {
                                        list.clear()
                                        orderAdapter?.notifyDataSetChanged()
                                    }
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
                                    if (isShowNoData) {
                                        if (offset == 0) {
                                            nodatafound.visibility = View.VISIBLE
                                        }
                                    }


                                }
                            }

                            override fun onError(e: Throwable) {
                                handleError(e)
                                ProgressDialogLoader.progressDialogDismiss()

                                if (isShowNoData)
                                    nodatafound.visibility = View.VISIBLE

                            }

                            override fun onComplete() {
                            }
                        })
            }
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    private fun performSearch() {
/*        et_searchText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val searchText = editable.toString()
                orderAdapter!!.filter.filter(searchText)
            }
        })*/
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

        if (list.size > 4) {
            getVendorOrderList(businessId, list.size)
        }
    }

    override fun onFinishListener(type: Int) {

    }

}
