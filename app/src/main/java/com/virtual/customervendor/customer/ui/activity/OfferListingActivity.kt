package com.virtual.customervendor.customer.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.VenderOffersModel
import com.virtual.customervendor.model.response.VenderOffersResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.OfferAdapterCustomer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_offer_listing.*
import java.util.*

class OfferListingActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    var apiService: ApiInterface? = null
    var offerAdapterCustomer: OfferAdapterCustomer? = null
    var toolbar: Toolbar? = null
    var mTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_listing)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        val business_id = intent.extras.getString(AppKeys.BUSINESS_ID)
        init()
        getOffersApi(business_id)
    }

    fun init() {
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        mTitle!!.text = resources.getString(R.string.offers)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

//    fun setTitleBar(titleName: String) {
//        mTitle!!.text = titleName
//    }


    private fun getOffersApi(business_id: String) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService?.getMyOffers("Bearer " + SharedPreferenceManager.getAuthToken(), business_id)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<VenderOffersResponse> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onNext(userResponse: VenderOffersResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
                                if (userResponse.data.size > 0) {
                                    text.visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                    createAdapterView(userResponse.data)
                                } else {
                                    text.visibility = View.VISIBLE
                                    recyclerView.visibility = View.GONE
                                }
                            } else {
                                UiValidator.displayMsgSnack(cons, this@OfferListingActivity, userResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                            UiValidator.displayMsgSnack(cons, this@OfferListingActivity, e.message)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cons, this, getString(R.string.no_internet_connection))
        }
    }

    private fun createAdapterView(list: ArrayList<VenderOffersModel>) {
        offerAdapterCustomer = OfferAdapterCustomer(this, list) { offerModel ->
            val result = Intent()
            result.putExtra(AppKeys.OFFER_RESULT, offerModel)
            setResult(Activity.RESULT_OK, result)
            finish()
            SlideAnimationUtill.slideBackAnimation(this as AppCompatActivity)
        }
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = manager
        recyclerView.adapter = (offerAdapterCustomer)
    }

}
