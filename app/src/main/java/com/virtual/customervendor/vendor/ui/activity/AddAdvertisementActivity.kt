package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
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
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.model.response.CommonResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_advertisement_vendor.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddAdvertisementActivity : BaseActivity(), View.OnClickListener, ViewPagerItemClicked {
    var datetime: StringBuilder? = null
    var TAG: String = AddAdvertisementActivity::class.java.name
    var apiService: ApiInterface? = null
    var apirequest: MutableMap<String, String> = mutableMapOf()
    val REQUEST_CODE = 123
    var mResults = ArrayList<String>()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null
    var imageFiles = ArrayList<File>()
    var deletefiles = ArrayList<String>()
    var images = ArrayList<BusinessImage>()
    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_create -> {
//                startActivity(Intent(this, SendOfferActivity::class.java))
                validateField()
            }
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.ed_startdate -> {
                AppUtill.getDate(ed_startdate, this)
            }
            R.id.ed_enddate -> {
                AppUtill.getDate(ed_enddate, this)
            }
            R.id.img_upload -> {
                //captureImageUtils?.openSelectImageFromDialog()
                captureMultipleImages()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_advertisement_vendor)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        init()
        setToolBar()
    }

    fun init() {
        btn_create.setOnClickListener(this)
        img_upload.setOnClickListener(this)
        ed_startdate.setOnClickListener(this)
        ed_enddate.setOnClickListener(this)

    }

    fun validateField() {

        if (ed_title.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_title, getString(R.string.field_required))
            return
        }
        if (til_title.isErrorEnabled()) {
            UiValidator.disableValidationError(til_title)
        }

        if (ed_price.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_price, getString(R.string.field_required))
            return
        }
        if (til_price.isErrorEnabled()) {
            UiValidator.disableValidationError(til_price)
        }

        if (ed_startdate.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_startdtae, getString(R.string.field_required))
            return
        }
        if (til_startdtae.isErrorEnabled()) {
            UiValidator.disableValidationError(til_startdtae)
        }

        if (ed_enddate.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_enddate, getString(R.string.field_required))
            return
        }
        if (til_enddate.isErrorEnabled()) {
            UiValidator.disableValidationError(til_enddate)
        }

        if (ed_coupen.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_coupen, getString(R.string.field_required))
            return
        }
        if (til_coupen.isErrorEnabled()) {
            UiValidator.disableValidationError(til_coupen)
        }

        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }
        val startDate = SimpleDateFormat("yyyy-MM-dd").parse(ed_startdate.getText().toString())
        val endDate = SimpleDateFormat("yyyy-MM-dd").parse(ed_enddate.getText().toString())

        if (startDate.after(endDate)) {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.please_select_correct_dates))
            return
        }
        hitApi()
    }

    fun setToolBar() {
        val appbar: AppBarLayout = findViewById<AppBarLayout>(R.id.toolbar)
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = appbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.create_offer)
        val iv_back = appbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun hitApi() {
        apirequest.clear();
        apirequest.put(AppKeys.BUSINESS_ID, SharedPreferenceManager.getBussinessID())
        apirequest.put(AppKeys.KEY_TITLE, ed_title.text.toString())
        apirequest.put(AppKeys.KEY_PRICE, ed_price.text.toString())
        apirequest.put(AppKeys.KEY_STARTDATE, ed_startdate.text.toString())
        apirequest.put(AppKeys.KEY_ENDDATE, ed_enddate.text.toString())
        apirequest.put(AppKeys.KEY_DESCRIPTION, ed_desc.text.toString())
        apirequest.put(AppKeys.KEY_COUPON, ed_coupen.text.toString())
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService!!.createOffers("Bearer " + SharedPreferenceManager.getAuthToken(), apirequest)!!.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CommonResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(apiResponseRestaurant: CommonResponse) {
                            AppLog.e(TAG, apiResponseRestaurant.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (apiResponseRestaurant.status.equals(AppConstants.KEY_SUCCESS)) {
                                if (imageFiles.size > 0)
//                                finish()
                                    uploadPic(imageFiles, apiResponseRestaurant.offer_id)
                                else {
                                    finish()
                                }
                            } else {
                                UiValidator.displayMsgSnack(coordinator, this@AddAdvertisementActivity, apiResponseRestaurant.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            if (e != null)
                            AppLog.e(TAG, e.message)
                            ProgressDialogLoader.progressDialogDismiss()
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
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
                imageList.add(MultipartBody.Part.createFormData(AppKeys.VENDOR_OFFER_IMAGES, imageFile.name, requestFile))
            }
        }

        val anInterface = ApiClient.client.create(ApiInterface::class.java)
        anInterface.uploadOfferImages("Bearer " + SharedPreferenceManager.getAuthToken(), offerID, imageList).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<BusinessImagesResponse> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onSuccess(userImageUploadResponse: BusinessImagesResponse) {
                        ProgressDialogLoader.progressDialogDismiss()
                        if (userImageUploadResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                            finish()
                        } else {
                            UiValidator.displayMsgSnack(coordinator, this@AddAdvertisementActivity, userImageUploadResponse.message)
                        }
                        AppLog.e(UploadBussinessImage.TAG, userImageUploadResponse.toString())
                    }

                    override fun onError(e: Throwable) {
                        ProgressDialogLoader.progressDialogDismiss()
                        if (e != null)
                        AppLog.e(UploadBussinessImage.TAG, e.toString())
                    }
                })
    }

    private fun initViewPager(fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(this@AddAdvertisementActivity, images, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(this@AddAdvertisementActivity!!, mResults.size, layoutDots, viewPager)
    }

    override fun onPagerItemClicked(position: Int) {
        images.removeAt(position)
        homeSliderAdapter?.notifyDataSetChanged()
    }

}