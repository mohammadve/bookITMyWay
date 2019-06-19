package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customer_vendor.utill.UploadBussinessImage
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessDetail
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.model.response.TaxiServiceResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.utills.ProgressDialogLoader.progressDialogCreation
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_eventsnew_vendor.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.ArrayList

class VendorAddEventActivity : BaseActivity(), View.OnClickListener, ViewPagerItemClicked {
    var TAG: String = VendorAddEventActivity::class.java.simpleName
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var apiInterface: ApiInterface? = null
    var vendorEventDetail = EventDetail()
    var imageFile: File? = null
    var business_id = BusinessDetail()

    val REQUEST_CODE = 123
    var mResults = ArrayList<String>()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null
    var imageFiles = ArrayList<File>()
    var images = ArrayList<BusinessImage>()


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.ed_starttime -> {
                AppUtils.getTimeNew(ed_starttime, this as AppCompatActivity?)
//                AppUtill.getTime(ed_starttime, this)
            }
            R.id.ed_closingtime -> {
                AppUtils.getTimeNew(ed_closingtime, this as AppCompatActivity?)
//                AppUtill.getTime(ed_closingtime, this)
            }
            R.id.ed_startdate -> {
                AppUtill.getDate(ed_startdate, this)
            }
            R.id.ed_enddate -> {
                AppUtill.getDate(ed_enddate, this)
            }
            R.id.btn_next -> {
                validateField()
            }
            R.id.img_upload -> {
                //captureImageUtils?.openSelectImageFromDialog()
                captureMultipleImages()
            }
            R.id.iv_profile -> {
                captureMultipleImages()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_eventsnew_vendor)
        business_id = intent.getSerializableExtra(AppKeys.BUSINESS_ID) as BusinessDetail
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()

        til_eventprice.hint = "" + til_eventprice.hint + "(" + AppUtils.getCurrencySymbol() + ")"
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
        ed_starttime.setOnClickListener(this)
        ed_closingtime.setOnClickListener(this)
        ed_startdate.setOnClickListener(this)
        ed_enddate.setOnClickListener(this)
        iv_upload.setOnClickListener(this)
        iv_profile.setOnClickListener(this)
    }

    fun validateField() {

        if (ed_eventname.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_eventname, getString(R.string.field_required))
            return
        }
        if (til_eventname.isErrorEnabled()) {
            UiValidator.disableValidationError(til_eventname)
        }

        if (ed_ticketcount.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_ticketcount, getString(R.string.field_required))
            return
        }
        if (til_ticketcount.isErrorEnabled()) {
            UiValidator.disableValidationError(til_ticketcount)
        }
        if (ed_eventprice.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_eventprice, getString(R.string.field_required))
            return
        }
        if (til_eventprice.isErrorEnabled()) {
            UiValidator.disableValidationError(til_eventprice)
        }

        if (ed_startdate.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_startdate, getString(R.string.field_required))
            return
        }
        if (til_startdate.isErrorEnabled()) {
            UiValidator.disableValidationError(til_startdate)
        }

        if (ed_enddate.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_enddate, getString(R.string.field_required))
            return
        }
        if (til_enddate.isErrorEnabled()) {
            UiValidator.disableValidationError(til_enddate)
        }

        if (!AppUtill.compareDate(ed_startdate.getText().toString(), ed_enddate.getText().toString())) {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.choose_valid_time_slot))
            return
        }


        if (ed_starttime.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_starttime, getString(R.string.field_required))
            return
        }
        if (til_starttime.isErrorEnabled()) {
            UiValidator.disableValidationError(til_starttime)
        }

        if (ed_closingtime.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_closetime, getString(R.string.field_required))
            return
        }
        if (til_closetime.isErrorEnabled()) {
            UiValidator.disableValidationError(til_closetime)
        }

        if (AppUtill.compareDateEquals(ed_startdate.getText().toString(), ed_enddate.getText().toString())) {
            if (!AppUtill.compareTime(ed_starttime.getText().toString(), ed_closingtime.getText().toString())) {
                UiValidator.displayMsgSnack(coordinator, this, getString(R.string.choose_valid_time_slot))
                return
            }
        }

        if (ed_venue.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_venue, getString(R.string.field_required))
            return
        }
        if (til_venue.isErrorEnabled()) {
            UiValidator.disableValidationError(til_venue)
        }

        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }
        UiValidator.hideSoftKeyboard(this)

        putAllDataToFieldMap()
    }


    private fun putAllDataToFieldMap() {
        vendorEventDetail.name = ed_eventname.text.toString()

        vendorEventDetail.total_tickets = ed_ticketcount.text.toString().toInt()
        vendorEventDetail.ticket_price = ed_eventprice.text.toString()
        vendorEventDetail.start_date = ed_startdate.text.toString()
        vendorEventDetail.end_date = ed_enddate.text.toString()
        vendorEventDetail.start_time = ed_starttime.text.toString()
        vendorEventDetail.close_time = ed_closingtime.text.toString()
        vendorEventDetail.description = ed_desc.text.toString()
        vendorEventDetail.business_id = business_id.business_id.toString()
        vendorEventDetail.business_category_id = business_id.category_id.toString()
        vendorEventDetail.business_subcategory_id = business_id.subcategory_id.toString()
        vendorEventDetail.action = AppConstants.ACTION_ADD
        vendorEventDetail.venue = ed_venue.text.toString()

        hitApiAdd(vendorEventDetail)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
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
                                if (imageFiles.size > 0)
                                    uploadPic(imageFiles, userResponse.data.service_id)
                                else {
                                    setResult(Activity.RESULT_OK)
                                    finish()
                                }
                            } else {
                                UiValidator.displayMsgSnack(coordinator, this@VendorAddEventActivity, userResponse.message)
                            }
                            AppLog.e(TAG, userResponse.toString())
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null) {
                                UiValidator.displayMsgSnack(coordinator, this@VendorAddEventActivity, e.message)
                                AppLog.e(TAG, e.message)
                            }
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }


    private fun captureMultipleImages() {
        val intent = Intent(this, ImagesSelectorActivity::class.java)
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, AppConstants.VENDOR_BANNER_IMG_COMPRESSION_SIZE)
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false)
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CODE) {
            if (resultCode === Activity.RESULT_OK) {
                mResults = data!!.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS)

                var imgList = ArrayList<BusinessImage>()
                for (url in mResults) {
                    imgList.add(BusinessImage(url, ""))
                }
                capturedImage(imgList)

                AppLog.e(TAG, mResults.toString())
            }
        }
    }

    fun capturedImage(mResults: ArrayList<BusinessImage>) {
        images.clear()
        imageFiles.clear()
        images = mResults
        initViewPager(false)
        if (mResults != null) {
            for (imgUrl in mResults) {
                imageFiles.add(File(imgUrl.url))
            }


        }
    }

    fun uploadPic(addfile: ArrayList<File>, offerID: String) {
        var imageList = java.util.ArrayList<MultipartBody.Part>()
//            var deleteimageList = java.util.ArrayList<MultipartBody.Part>()
        if (addfile != null) {

            for (imageFile in addfile) {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
                imageList.add(MultipartBody.Part.createFormData(AppKeys.VENDOR_EVENTIMAGE, imageFile.name, requestFile))
            }
        }

        val anInterface = ApiClient.client.create(ApiInterface::class.java)
        anInterface.updateEventImage("Bearer " + SharedPreferenceManager.getAuthToken(), offerID, imageList).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<BusinessImagesResponse> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onSuccess(userImageUploadResponse: BusinessImagesResponse) {
                        ProgressDialogLoader.progressDialogDismiss()
                        if (userImageUploadResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                            finish()
                            UiValidator.displayMsgSnack(coordinator, this@VendorAddEventActivity, userImageUploadResponse.message)
                        }
                        AppLog.e(UploadBussinessImage.TAG, userImageUploadResponse.toString())
                    }

                    override fun onError(e: Throwable) {
                        ProgressDialogLoader.progressDialogDismiss()
                        if (e != null) {
                            AppLog.e(UploadBussinessImage.TAG, e.toString())
                        }
                    }
                })
    }

    private fun initViewPager(fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(this@VendorAddEventActivity, images, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(this@VendorAddEventActivity!!, mResults.size, layoutDots, viewPager)
    }

    override fun onPagerItemClicked(position: Int) {

    }
}
