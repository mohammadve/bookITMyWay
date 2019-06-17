package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessDetail
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.model.response.EventListingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.VendorEventsAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_events_vendor.*
import java.io.Serializable

class VendorListEventActivity : BaseActivity(), View.OnClickListener {
    var eventsAdapter: VendorEventsAdapter? = null

    var TAG: String = VendorListEventActivity::class.java.simpleName
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var apiInterface: ApiInterface? = null
    var businessDetail = BusinessDetail()


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_add -> {
                var intent: Intent = Intent(this, VendorAddEventActivity::class.java)
                intent.putExtra(AppKeys.BUSINESS_ID, businessDetail as Serializable)
                startActivityForResult(intent, 112)
                SlideAnimationUtill.slideNextAnimation(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_events_vendor)
        businessDetail = intent.extras.get(AppConstants.BUSINESS_DATA) as BusinessDetail
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()

        /* if (businessDetail != null) {
             hitApi()
         }*/
    }

    override fun onResume() {
        super.onResume()
        if (businessDetail != null) {
            hitApi()
        }
    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.my_events)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        val iv_add = toolbar!!.findViewById(R.id.iv_add) as ImageView
        val iv_edit = toolbar!!.findViewById(R.id.iv_edit) as ImageView
        val iv_delete = toolbar!!.findViewById(R.id.iv_delete) as ImageView
        iv_add.visibility = View.VISIBLE
        iv_edit.visibility = View.GONE
        iv_delete.visibility = View.GONE
        iv_back.setOnClickListener(this)
        iv_add.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 112 && resultCode == Activity.RESULT_OK) {
            hitApi()
        }
    }

    fun hitApi() {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        apiInterface?.getEventListing("Bearer " + SharedPreferenceManager.getAuthToken(), businessDetail.business_id!!, AppConstants.USER_VENDOR)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<EventListingResponse> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(detailResponse: EventListingResponse) {
                        AppLog.e(TAG, detailResponse.toString())
                        handleResults(detailResponse)
                    }

                    override fun onError(e: Throwable) {
                        handleError(e)
                    }

                    override fun onComplete() {}
                })
    }

    private fun handleResults(eventListingResponse: EventListingResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (eventListingResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            createAdapterEvents(eventListingResponse.eventlisting)
        } else {
            UiValidator.displayMsgSnack(coordinator, this, eventListingResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t.message)
    }

    private fun createAdapterEvents(eventlisting: ArrayList<EventDetail>) {
        eventsAdapter = VendorEventsAdapter(this!!, eventlisting) { offerModel ->
            var intent: Intent = Intent(this, VendorEventsDetailActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.BUSINESS_DATA, offerModel)
            intent.putExtras(bundle)
            startActivityForResult(intent, 112)
            SlideAnimationUtill.slideNextAnimation(this)
        }
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_event.layoutManager = manager
        rv_event.adapter = (eventsAdapter)
    }
}
