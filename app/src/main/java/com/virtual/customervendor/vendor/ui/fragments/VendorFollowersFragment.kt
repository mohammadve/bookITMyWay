package com.virtual.customervendor.vendor.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessFollowerModel
import com.virtual.customervendor.model.response.BusinessFollowerResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.VendorfollowerAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_followers_vendor.*

class VendorFollowersFragment : Fragment(), View.OnClickListener, PagingListeners {


    val TAG: String = VendorFollowersFragment::class.java.simpleName
    var orderAdapter: VendorfollowerAdapter? = null
    var list = ArrayList<BusinessFollowerModel>()
    var apiService: ApiInterface? = null
    var businessId: String = String()
    override fun onClick(v: View?) {
        when (v!!.id) {
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_followers_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        initView(view)
    }

    fun initView(view: View) {
        orderAdapter = VendorfollowerAdapter(this, activity as AppCompatActivity, list)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_follower.layoutManager = manager
        rv_follower.adapter = (orderAdapter)
        getBusinessID()
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
        getBusinessFollowerList(businessId, 0)
    }

    fun getBusinessFollowerList(business_id: String, offset: Int) {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            apiService?.getBusinessFollowerList("Bearer " + SharedPreferenceManager.getAuthToken(), business_id, offset)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<BusinessFollowerResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: BusinessFollowerResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {

                                if (detailResponse.data.size > 0) {
                                    list.addAll(detailResponse.data)
                                    orderAdapter?.notifyDataSetChanged()
                                    rv_follower.visibility = View.VISIBLE
                                    nodatafound.visibility = View.GONE
                                }
                                if (!(list.size > 0)) {
                                    nodatafound.visibility = View.VISIBLE
                                    rv_follower.visibility = View.GONE
                                }
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

    override fun onFinishListener() {
        getBusinessFollowerList(businessId, list.size)
    }

    override fun onFinishListener(type: Int) {

    }
}