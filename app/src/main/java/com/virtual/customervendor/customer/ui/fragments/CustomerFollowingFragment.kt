package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.adapter.CustomerfollowingAdapter
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessFollowingModel
import com.virtual.customervendor.model.response.BusinessFollowingResponse
import com.virtual.customervendor.model.response.FollowUnfollowResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_followers_customer.*

class CustomerFollowingFragment : Fragment(), View.OnClickListener, PagingListeners {
    val TAG: String = CustomerFollowingFragment::class.java.simpleName
    var orderAdapter: CustomerfollowingAdapter? = null
    var list = ArrayList<BusinessFollowingModel>()
    var apiService: ApiInterface? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_followers_customer, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        initView(view)
    }

    fun initView(view: View) {
        orderAdapter = CustomerfollowingAdapter(this, activity as AppCompatActivity, list) { model, position ->
            followUnfollowApi("0", model.business_id!!, position)
        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_follower.layoutManager = manager
        rv_follower.adapter = (orderAdapter)

        getBusinessFollowerList(0)
    }

    fun getBusinessFollowerList(offset: Int) {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            apiService?.getBusinessFollowingList("Bearer " + SharedPreferenceManager.getAuthToken(), offset)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())

                    ?.subscribe(object : Observer<BusinessFollowingResponse> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onNext(detailResponse: BusinessFollowingResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
//                            list.clear()
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


    fun followUnfollowApi(status: String, business_id: String, positon: Int) {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            apiService?.folloUnfollow("Bearer " + SharedPreferenceManager.getAuthToken(), business_id, status)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<FollowUnfollowResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: FollowUnfollowResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                UiValidator.displayMsgSnack(cons, activity, detailResponse.message)
                                list.removeAt(positon)
                                orderAdapter?.notifyDataSetChanged()
                                handleResult()
                            } else UiValidator.displayMsgSnack(cons, activity, detailResponse.message)
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

    private fun handleResult() {
        if (!(list.size > 0)) {
            nodatafound.visibility = View.VISIBLE
            rv_follower.visibility = View.GONE
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t?.message)
    }

    override fun onFinishListener() {
//        getBusinessFollowerList(list.size)
    }

    override fun onFinishListener(type: Int) {
    }

}