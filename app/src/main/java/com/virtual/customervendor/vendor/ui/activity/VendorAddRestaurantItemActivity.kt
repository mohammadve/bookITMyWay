package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.View
import android.view.Window
import android.widget.TextView
import com.bumptech.glide.Glide
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.response.StoreListingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_rest_item_vendor.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.ArrayList

class VendorAddRestaurantItemActivity : BaseActivity(), View.OnClickListener {

    var TAG: String = VendorAddRestaurantItemActivity::class.java.simpleName
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var apiInterface: ApiInterface? = null
    val REQUEST_CODE = 123
    var mResults = ArrayList<String>()
    var imageFiles = ArrayList<File>()
    var item_type = String()


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_next -> {
                validate()
            }
            R.id.img_item -> {
                captureMultipleImages()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_rest_item_vendor)
        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.hide()
        }
        item_type = intent.extras.get("from") as String
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()
        AppLog.e("kljgdflg0", item_type)
    }

    fun init() {
        btn_next.setOnClickListener(this)
        img_item.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    private fun captureMultipleImages() {
// start multiple photos selector
        val intent = Intent(this, ImagesSelectorActivity::class.java)
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1)
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, AppConstants.VENDOR_BANNER_IMG_COMPRESSION_SIZE)
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false)
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults)
// start the selector
        startActivityForResult(intent, REQUEST_CODE)
    }


    fun validate() {
        if (ed_itemname.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_itemname, getString(R.string.field_required))
            return
        }
        if (til_itemname.isErrorEnabled()) {
            UiValidator.disableValidationError(til_itemname)
        }

        if (ed_price.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_price, getString(R.string.field_required))
            return
        }
        if (til_price.isErrorEnabled()) {
            UiValidator.disableValidationError(til_price)
        }


        if (imageFiles != null && imageFiles.size > 0)
            hitApi() else {
            UiValidator.displayMsg(this, resources.getString(R.string.choose_image))
        }
    }


    fun hitApi() {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))

            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFiles.get(0))
            // MultipartBody.Part is used to send also the actual file name
            val img = MultipartBody.Part.createFormData("item_image[]", imageFiles.get(0).name, requestFile)

            var map = HashMap<String, RequestBody>()


            var item_name = RequestBody.create(MediaType.parse("text/plain"), ed_itemname.text.toString())
            map.put("item_name", item_name)

            var item_price = RequestBody.create(MediaType.parse("text/plain"), ed_price.text.toString())
            map.put("item_price", item_price)


            var service_id = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceManager.getServiceId().toString())
            map.put("service_id", service_id)

            var item_type1 = RequestBody.create(MediaType.parse("text/plain"), item_type)
            map.put("item_type", item_type1)




            apiInterface?.restaurantItemAdd("Bearer " + SharedPreferenceManager.getAuthToken(), map, img)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<StoreListingResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: StoreListingResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            handleResults(detailResponse)
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsg(this, getString(R.string.no_internet_connection))
        }
    }


    private fun handleResults(eventListingResponse: StoreListingResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (eventListingResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            UiValidator.displayMsgSnack(start_lay, this, eventListingResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t.message)
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
        for (imgUrl in mResults) {
            imageFiles.add(File(imgUrl.url))
        }
        img_camera.visibility = View.GONE
        Glide.with(this).load(imageFiles.get(0)).into(img_item)
    }

}
