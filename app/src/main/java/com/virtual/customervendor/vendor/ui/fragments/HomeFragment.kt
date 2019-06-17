package com.virtual.customervendor.vendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.VenderOffersModel
import com.virtual.customervendor.model.response.VenderOffersResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.activity.AddAdvertisementActivity
import com.virtual.customervendor.vendor.ui.activity.DashBoardActivity
import com.virtual.customervendor.vendor.ui.activity.SendOfferActivity
import com.virtual.customervendor.vendor.ui.adapter.HomeAdvertiisementAdaVendor
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home_vendor.*

class HomeFragment : Fragment(), View.OnClickListener {
    var homeAdvertiisementAdaVendor: HomeAdvertiisementAdaVendor? = null
    val list: ArrayList<VenderOffersModel> = java.util.ArrayList()
    var apiService: ApiInterface? = null
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_add -> {
                startActivity(Intent(activity, AddAdvertisementActivity::class.java))
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_home_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = ApiClient.client.create(ApiInterface::class.java)

        initView()
    }

    fun initView() {
        btn_add.setOnClickListener(this)
        createAdapterView()

    }

    override fun onResume() {
        super.onResume()
        getOffersApi()
    }

    private fun createAdapterView() {
        homeAdvertiisementAdaVendor = HomeAdvertiisementAdaVendor(activity!!, list) { offerModel ->
            var intent: Intent = Intent(activity, SendOfferActivity::class.java)
            intent.putExtra(AppConstants.OFFER_DATA, offerModel)
            startActivity(intent)
            SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)

        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_list.layoutManager = manager
        rv_list.adapter = (homeAdvertiisementAdaVendor)
    }

    private fun getOffersApi() {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))
            apiService?.getVenderOffers("Bearer " + SharedPreferenceManager.getAuthToken(), (activity as DashBoardActivity).businessDetail.business_id.toString())?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<VenderOffersResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(userResponse: VenderOffersResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
                                list.clear()
                                list.addAll(userResponse.data)
                                homeAdvertiisementAdaVendor?.notifyDataSetChanged()
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, userResponse.message)
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
    }
}