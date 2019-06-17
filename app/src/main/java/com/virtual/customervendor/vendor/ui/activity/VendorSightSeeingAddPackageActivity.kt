package com.virtual.customervendor.vendor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customview.CustomEditText
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessDetail
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.model.LocationTimeModel
import com.virtual.customervendor.model.response.TaxiServiceResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.utills.ProgressDialogLoader.progressDialogCreation
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_package_vendor.*

class VendorSightSeeingAddPackageActivity : BaseActivity(), View.OnClickListener {
    var TAG: String = VendorSightSeeingAddPackageActivity::class.java.simpleName
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var apiInterface: ApiInterface? = null
    var business_id = BusinessDetail()
    var seenList = ArrayList<LocationTimeModel>()


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_next -> {
                validateField()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_package_vendor)
        business_id = intent.getSerializableExtra(AppKeys.BUSINESS_ID) as BusinessDetail
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()
    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.add_new_event)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        val iv_add = toolbar!!.findViewById(R.id.iv_add) as ImageView
        val iv_edit = toolbar!!.findViewById(R.id.iv_edit) as ImageView
        val iv_delete = toolbar!!.findViewById(R.id.iv_delete) as ImageView
        iv_add.visibility = View.GONE
        iv_edit.visibility = View.GONE
        iv_delete.visibility = View.GONE
        iv_back.setOnClickListener(this)



        btn_next.setOnClickListener(this)
    }

    fun validateField() {
        if (ed_taxicount.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_taxicount, getString(R.string.field_required))
            return
        }
        if (til_taxicount.isErrorEnabled()) {
            UiValidator.disableValidationError(til_taxicount)
        }
        if (ed_seat.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_seat, getString(R.string.field_required))
            return
        }
        if (til_seat.isErrorEnabled()) {
            UiValidator.disableValidationError(til_seat)
        }
        if (ed_rate.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_rate, getString(R.string.field_required))
            return
        }
        if (til_rate.isErrorEnabled()) {
            UiValidator.disableValidationError(til_rate)
        }
        if (ed_service.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_servicearea, getString(R.string.rgs_sign_in_empty_username_text))
            return
        }
        if (til_servicearea.isErrorEnabled()) {
            UiValidator.disableValidationError(til_servicearea)
        }
        if (!(chk_alldays.isChecked || chk_monday.isChecked || chk_tuesday.isChecked || chk_wednesday.isChecked || chk_thursday.isChecked || chk_friday.isChecked || chk_saturday.isChecked || chk_sunday.isChecked)) {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.select_days_of_service))
            return
        }

        if (ed_startloc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_startloc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_startloc)
        }

        if (ed_starttime.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_starttime, getString(R.string.field_required))
            return
        }
        if (til_starttime.isErrorEnabled()) {
            UiValidator.disableValidationError(til_starttime)
        }

        if (ed_closeloc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_closeloc, getString(R.string.field_required))
            return
        }
        if (til_closeloc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_closeloc)
        }

        if (ed_closeingtime.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_closetime, getString(R.string.field_required))
            return
        }
        if (til_closetime.isErrorEnabled()) {
            UiValidator.disableValidationError(til_closetime)
        }

        if (!AppUtill.compareTime(ed_starttime.text.toString(), ed_closeingtime.text.toString())) {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.choose_valid_time_slot))
            return
        }

        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }

        getListSightSeen()

        UiValidator.hideSoftKeyboard(this)


    }

    fun getListSightSeen() {
        AppLog.e(TAG, rv_sightseeing.childCount.toString())
        var locationTimeModel: LocationTimeModel
        if (seenList != null) {
            seenList.clear()
        }
        for (i in 0 until rv_sightseeing.childCount) {
            val data: ConstraintLayout = rv_sightseeing.getChildAt(i) as ConstraintLayout
            if (!data.findViewById<CustomEditText>(R.id.ed_startloc).text.toString().isEmpty()) {
                locationTimeModel = LocationTimeModel()
                locationTimeModel.location = data.findViewById<CustomEditText>(R.id.ed_startloc).text.toString()
                locationTimeModel.time = data.findViewById<CustomEditText>(R.id.ed_sighttime).text.toString()
                seenList.add(locationTimeModel)
            }
        }
//        sight_Seeing_Service_Request.sight_seens = seenList
        AppLog.e(TAG, seenList.toString())
    }


    private fun putAllDataToFieldMap() {
//        vendorEventDetail.name = ed_eventname.text.toString()
//        vendorEventDetail.total_tickets = ed_ticketcount.text.toString()
//        vendorEventDetail.ticket_price = ed_eventprice.text.toString()
//        vendorEventDetail.start_date = ed_startdate.text.toString()
//        vendorEventDetail.end_date = ed_enddate.text.toString()
//        vendorEventDetail.start_time = ed_starttime.text.toString()
//        vendorEventDetail.close_time = ed_closingtime.text.toString()
//        vendorEventDetail.description = ed_desc.text.toString()
//        vendorEventDetail.business_id = business_id.business_id.toString()
//        vendorEventDetail.business_category_id = business_id.category_id.toString()
//        vendorEventDetail.business_subcategory_id = business_id.subcategory_id.toString()
//        vendorEventDetail.action = AppConstants.ACTION_ADD
//
//        hitApiAdd(vendorEventDetail)
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

    }

    public fun hitApiAdd(vendorEventDetail: EventDetail) {
        if (AppUtils.isInternetConnected(this)) {
            progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.addUpdateEvent("Bearer " + SharedPreferenceManager.getAuthToken(), vendorEventDetail)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<TaxiServiceResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(userResponse: TaxiServiceResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {

                            } else {
                                UiValidator.displayMsgSnack(coordinator, this@VendorSightSeeingAddPackageActivity, userResponse.message)
                            }
                            AppLog.e(TAG, userResponse.toString())
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null) {
                                UiValidator.displayMsgSnack(coordinator, this@VendorSightSeeingAddPackageActivity, e.message)
                                AppLog.e(TAG, e.message)
                            }
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }


}
