package com.virtual.customervendor.vendor.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.google.zxing.integration.android.IntentIntegrator
import com.jakewharton.rxbinding.widget.RxTextView
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.adapter.OrderadapterVendor
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CustomerOrderModel
import com.virtual.customervendor.model.response.BarCodeResponse
import com.virtual.customervendor.model.response.CustomerOrderResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.activity.VendorOrderDetailActivity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_orderslist_vendor.*
import org.json.JSONException
import java.util.concurrent.TimeUnit

class VendorOrdersFragment : Fragment(), View.OnClickListener, PagingListeners {


    var orderAdapter: OrderadapterVendor? = null
    val TAG: String = VendorOrdersFragment::class.java.simpleName
    var apiService: ApiInterface? = null
    val list: ArrayList<CustomerOrderModel> = java.util.ArrayList()
    var businessId: String = String()
    var category_id: String = String()
    var subcategory_id: String = String()
    var isShowNoData: Boolean = true
    var filterType = ""
    var fromDate = ""
    var toDate = ""
    private var qrScan: IntentIntegrator? = null
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imageclick -> {
                qrScan = IntentIntegrator(activity!!);
                qrScan!!.initiateScan();
            }
            R.id.ll_FilterView -> {
                showPopUpMenuForFilter(v);
            }
        }
    }

    private fun showPopUpMenuForFilter(v: View) {

        var popup: PopupMenu = PopupMenu(context!!, v)
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
                    filterType = "custom"
                    showDatePickerDialog()

                }

            }
            true
        })




        popup.show()
    }

    private fun showDatePickerDialog() {
        val delayDialog = Dialog(activity)
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
                UiValidator.displayMsgSnack(coordinator, activity, "Please select date first")

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
            AppUtill.getDateForFilter(tv_from_date, activity!!, null)
        }
        tv_to_date.setOnClickListener {
            if (tv_from_date.text.toString().trim().isEmpty()) {
                UiValidator.displayMsgSnack(coordinator, activity, "Select From date first")
            } else {
                AppUtill.getDateForFilter(tv_to_date, activity!!, tv_from_date.text.toString().trim())
            }

        }
        delayDialog.show()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_orderslist_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        initView(view)
    }

    fun initView(view: View) {
        imageclick.setOnClickListener(this)
        ll_FilterView.setOnClickListener(this)

        createAdapterView()
    }


    private fun createAdapterView() {
        orderAdapter = OrderadapterVendor(this, activity!!, list) { orderDeatail ->
            var intent: Intent = Intent(activity!!, VendorOrderDetailActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.OREDER_DATA, orderDeatail)
            intent.putExtras(bundle)
            startActivityForResult(intent, 113)
            SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_list.layoutManager = manager
        rv_list.adapter = (orderAdapter)

        getBusinessID()

        if (SharedPreferenceManager.getCategoryId() == AppConstants.CAT_EVENT_TICKET) {
            imageclick.visibility = View.VISIBLE
            et_searchText.hint = resources.getString(R.string.search_by_order_no_or_event_name)
        } else {
            imageclick.visibility = View.GONE
            et_searchText.hint = resources.getString(R.string.search_by_order_no_or_customer_name)

        }
    }

    private fun performSearch() {
        et_searchText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                // searchText = editable.toString()
//                orderAdapter!!.filter.filter(searchText)
                // getVendorOrderList(businessId, 0,searchText)
            }
        })
    }

    fun getVendorOrderList(businessID: String, offset: Int) {
        if (AppUtils.isInternetConnected(activity)) {

            val obs = RxTextView.textChanges(et_searchText)
                    //.filter { charSequence -> charSequence.length > 0 }
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .map<String> { charSequence -> charSequence.toString() }
            obs.subscribe { string ->


                activity?.runOnUiThread {
                    progress_serch_order.visibility = View.VISIBLE
                    //  ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
                }

                if (!filterType.equals("custom")) {
                    fromDate = ""
                    toDate = ""
                }


                apiService?.getBusinessOrderList("Bearer " + SharedPreferenceManager.getAuthToken(), businessID,
                        offset, SharedPreferenceManager.getCategoryId(), SharedPreferenceManager.getSubcategoryId(),
                        "current_orders", string, filterType, fromDate, toDate)

                        ?.subscribeOn(Schedulers.io())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(object : Observer<CustomerOrderResponse> {
                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(detailResponse: CustomerOrderResponse) {
                                AppLog.e(TAG, detailResponse.toString())
                                ProgressDialogLoader.progressDialogDismiss()
                                progress_serch_order.visibility = View.GONE

                                if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                    if (offset == 0) {
                                        list.clear()
                                        orderAdapter?.notifyDataSetChanged()
                                    }
                                    if (detailResponse.data.size > 0) {

//                                    if (nodata.visibility == View.VISIBLE)
                                        nodata.visibility = View.GONE
                                        list.addAll(detailResponse.data)
                                        orderAdapter?.notifyDataSetChanged()
                                        isShowNoData = false
                                    } else {
//                                    if (isShowNoData)
                                        if (offset == 0) {
                                            nodata.visibility = View.VISIBLE
                                        }
//
                                    }

                                } else {
                                    UiValidator.displayMsgSnack(coordinator, activity, detailResponse.message)
                                    if (isShowNoData)
                                        nodata.visibility = View.VISIBLE

                                }
                            }

                            override fun onError(e: Throwable) {
                                progress_serch_order.visibility = View.GONE

                                handleError(e)
                                if (isShowNoData)
                                    nodata.visibility = View.VISIBLE

                            }

                            override fun onComplete() {
                            }
                        })


            }


        } else {
            UiValidator.displayMsgSnack(coordinator, activity, getString(R.string.no_internet_connection))
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 113 && resultCode == Activity.RESULT_OK) {
            AppLog.e("Tsear", "onActivityResult.toString()")
            list.clear()
            getVendorOrderList(businessId, 0)
        } else {
            var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                //if qrcode has nothing in it
                if (result.getContents() == null) {
                    UiValidator.displayMsgSnack(coordinator, activity, getString(R.string.no_data_found))

                } else {
                    //if qr contains data
                    try {
                        //converting the data to json
                        AppLog.e(TAG, result.getContents())
                        submitBarCode(result.getContents())
                    } catch (e: JSONException) {
                        e.printStackTrace();
                        //if control comes here
                        //that means the encoded format not matches
                        //in this case you can display whatever data is available on the qrcode
                        //to a toast
                        UiValidator.displayMsgSnack(coordinator, activity, getString(R.string.no_data_found))
                    }
                }
            }
        }
    }

    fun submitBarCode(barcode: String) {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            apiService?.getBarCodeDetail("Bearer " + SharedPreferenceManager.getAuthToken(), barcode)

                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<BarCodeResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: BarCodeResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            UiValidator.displayMsgSnack(coordinator, activity, detailResponse.message)
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                if (detailResponse.data.status == "1") {
                                    UiValidator.displayMsgSnack(coordinator, activity, getString(R.string.already_used))
                                } else if (detailResponse.data.status == "0") {
                                    var intent: Intent = Intent(activity!!, VendorOrderDetailActivity::class.java)
                                    intent.putExtra("orderid", detailResponse.data!!.order_id)
                                    intent.putExtra("barcode", true);
                                    startActivity(intent)
                                }

                            } else {

                            }
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)

                        }

                        override fun onComplete() {
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, activity, getString(R.string.no_internet_connection))
        }
    }


}