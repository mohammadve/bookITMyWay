package com.virtual.customervendor.vendor.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessFollowerModel
import com.virtual.customervendor.model.VenderOffersModel
import com.virtual.customervendor.model.response.BusinessFollowerResponse
import com.virtual.customervendor.model.response.CommonResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.SendOfferCustomerAdapterVendor
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_send_offer_vendor.*

class SendOfferActivity : BaseActivity(), View.OnClickListener, PagingListeners {
    var TAG: String = SendOfferActivity::class.java.name
    var offerModel: VenderOffersModel? = null
    var sendOfferCustomerAdapterVendor: SendOfferCustomerAdapterVendor? = null
    var list = ArrayList<BusinessFollowerModel>()
    var apiService: ApiInterface? = null
    var follower_list = ""
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_send -> {
                for (item in list) {
                    if (item.checked!!) {
                        if (follower_list.length == 0)
                            follower_list = item.user_id!!
                        else
                            follower_list = follower_list + "," + item.user_id!!
                    }
                }
                if (follower_list.length > 0)
                    sendOffer(offerModel!!.offer_id, follower_list)
                else
                    UiValidator.displayMsgSnack(cons, this, resources.getString(R.string.no_follower_is_selected))
            }
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.select_all -> {
                if (select_all.isChecked) {
                    select_all.setChecked(false)
                    select_all.setCheckMarkDrawable(R.drawable.checkbox_unselect)
                    var i = 0
                    while (i < list.size) {
                        list.get(i).checked = false
                        i++
                    }
                    sendOfferCustomerAdapterVendor?.notifyDataSetChanged()
                } else {
                    select_all.setChecked(true)
                    select_all.setCheckMarkDrawable(R.drawable.checkbox_sel)
                    var i = 0
                    while (i < list.size) {
                        list.get(i).checked = true
                        i++
                    }
                    sendOfferCustomerAdapterVendor?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_offer_vendor)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        offerModel = intent.extras.get(AppConstants.OFFER_DATA) as VenderOffersModel
        AppLog.e(TAG, offerModel.toString())
        init()
        setToolBar()
        getBusinessFollowerList(offerModel!!.business_id, 0)
    }

    fun init() {
        txt_title.text = offerModel?.title
        txt_desc.text = offerModel?.description
        txt_validty.text = offerModel?.startdate + " to " + offerModel?.enddate
        if (offerModel?.offer_images?.size!! > 0)
            Glide.with(this).load(offerModel?.offer_images?.get(0)?.media).apply(RequestOptions.circleCropTransform().placeholder(R.drawable.place_holder_list)).into(img_loc)
        createAdapterView()
        btn_send.setOnClickListener(this)
    }


    fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.send_offer)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)

        select_all.setOnClickListener(this)
    }

    private fun createAdapterView() {
        sendOfferCustomerAdapterVendor = SendOfferCustomerAdapterVendor(this, this, list) { pos, checked ->
            list.get(pos).checked = checked
            sendOfferCustomerAdapterVendor?.notifyDataSetChanged()
        }
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_list.layoutManager = manager
        rv_list.adapter = (sendOfferCustomerAdapterVendor)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun getBusinessFollowerList(business_id: String, offset: Int) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
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
                                    nodatafound.visibility = View.GONE
                                    btn_send.visibility = View.VISIBLE
//                                txt_follower.visibility = View.VISIBLE
//                                select_all.visibility = View.VISIBLE
                                    list.addAll(detailResponse.data)
                                    sendOfferCustomerAdapterVendor?.notifyDataSetChanged()
                                } else {
                                    nodatafound.visibility = View.VISIBLE
                                    btn_send.visibility = View.GONE
//                                txt_follower.visibility = View.GONE
//                                select_all.visibility = View.GONE
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
            UiValidator.displayMsgSnack(cons, this, getString(R.string.no_internet_connection))
        }
    }

    fun sendOffer(offer_id: String, follower_list: String) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService?.sendOffer("Bearer " + SharedPreferenceManager.getAuthToken(), offer_id, follower_list)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<CommonResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: CommonResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                finish()
                            }
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cons, this, getString(R.string.no_internet_connection))
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
    }

    override fun onFinishListener() {
//        getBusinessFollowerList(offerModel!!.business_id, list.size)
    }

    override fun onFinishListener(type: Int) {

    }
}