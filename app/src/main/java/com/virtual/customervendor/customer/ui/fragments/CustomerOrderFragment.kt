package com.virtual.customervendor.customer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.adapter.OrderadapterCustomer
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CustomerOrderModel
import com.virtual.customervendor.model.response.CustomerOrderResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.customer.ui.activity.CustomerOrderDetailActivity
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_order_customer.*

class CustomerOrderFragment : Fragment(), View.OnClickListener, PagingListeners {

    var orderadapterCustomer: OrderadapterCustomer? = null
    val list: ArrayList<CustomerOrderModel> = java.util.ArrayList()
    var apiService: ApiInterface? = null
    val TAG: String = CustomerOrderFragment::class.java.simpleName


    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.rl_searchView -> {
                performSearch()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_order_customer, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        initView(view)
    }


    fun initView(view: View) {
        rl_searchView.setOnClickListener(this)
        createAdapterView()
    }

    private fun createAdapterView() {
        orderadapterCustomer = OrderadapterCustomer(this, activity!!, list) { orderDeatail ->

            var intent: Intent = Intent(activity!!, CustomerOrderDetailActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.OREDER_DATA, orderDeatail)
            intent.putExtras(bundle)
            startActivity(intent)
            SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)

        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_list.layoutManager = manager
        rv_list.adapter = (orderadapterCustomer)

        getCustomerOrderList(0)

        performSearch()
    }


    fun getCustomerOrderList(offset: Int) {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            apiService?.getCustomerOrderList("Bearer " + SharedPreferenceManager.getAuthToken(), offset)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<CustomerOrderResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: CustomerOrderResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
//                            list.clear()
                                if (detailResponse.data.size > 0) {
                                    list.addAll(detailResponse.data)
                                    orderadapterCustomer?.notifyDataSetChanged()
                                }
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, detailResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t?.message)
    }

    private fun performSearch() {
        et_searchText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val searchText = editable.toString()
                orderadapterCustomer!!.filter.filter(searchText)
            }
        })
    }

    override fun onFinishListener() {

        getCustomerOrderList(list.size)
    }

    override fun onFinishListener(type: Int) {

    }

}