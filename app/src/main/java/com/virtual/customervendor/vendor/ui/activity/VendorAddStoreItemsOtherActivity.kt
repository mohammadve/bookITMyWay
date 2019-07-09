package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.ProductCategoryModel
import com.virtual.customervendor.model.response.StoreListingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_vendor_add_store_items_other.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class VendorAddStoreItemsOtherActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener, View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var TAG: String = VendorAddStoreItemsOtherActivity::class.java.simpleName
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var apiInterface: ApiInterface? = null
    var imageFiles = java.util.ArrayList<File>()
    val REQUEST_CODE = 123
    var mResults = java.util.ArrayList<String>()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null
    var productModel = ProductCategoryModel()

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_save -> {
                validate()
            }
            R.id.img_camera -> {
                captureMultipleImages()
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        if (p1) {
            til_bookprice.visibility = View.VISIBLE
        } else {
            til_bookprice.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_add_store_items_other)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        productModel = intent.extras.get(AppConstants.OREDER_DATA) as ProductCategoryModel

        initView()
    }

    fun initView() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.add_item)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)

        img_camera.setOnClickListener(this)

        cb_add_to_relaese.setOnCheckedChangeListener(this)
        btn_save.setOnClickListener(this)
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

        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }
        hitApi(imageFiles)
    }


    fun hitApi(addfile: ArrayList<File>) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))

            var imageList = java.util.ArrayList<MultipartBody.Part>()
//            var deleteimageList = java.util.ArrayList<MultipartBody.Part>()
            if (addfile != null) {

                for (imageFile in addfile) {
                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
                    imageList.add(MultipartBody.Part.createFormData(AppKeys.STORE_PRODUCT_IMAGES, imageFile.name, requestFile))
                }
            }

            var map = HashMap<String, RequestBody>()

            var action = RequestBody.create(MediaType.parse("text/plain"), AppConstants.ACTION_ADD)
            map.put("action", action)

            var item_name = RequestBody.create(MediaType.parse("text/plain"), ed_itemname.text.toString())
            map.put("item_name", item_name)

            var item_price = RequestBody.create(MediaType.parse("text/plain"), ed_price.text.toString())
            map.put("item_price", item_price)

            var item_description = RequestBody.create(MediaType.parse("text/plain"), ed_desc.text.toString())
            map.put("product_description", item_description)

            var product_category = RequestBody.create(MediaType.parse("text/plain"), productModel.id)
            map.put("product_category", product_category)

            var service_id = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceManager.getServiceId().toString())
            map.put("service_id", service_id)



            apiInterface?.storeItemAddDeleteEdit("Bearer " + SharedPreferenceManager.getAuthToken(), map, imageList)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(mainThread())
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
            UiValidator.displayMsgSnack(start_lay, this, getString(R.string.no_internet_connection))
        }
    }

    private fun handleResults(eventListingResponse: StoreListingResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (eventListingResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            setResult(Activity.RESULT_OK)
            finish()
        }
        UiValidator.displayMsgSnack(start_lay, this, eventListingResponse.message)
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null) {
            AppLog.e(TAG, t.message)
            UiValidator.displayMsgSnack(start_lay, this, t.message)
        }
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CODE) {
            if (resultCode === Activity.RESULT_OK) {
                mResults = data!!.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS)

                var imgList = java.util.ArrayList<BusinessImage>()
                for (url in mResults) {
                    imgList.add(BusinessImage(url, ""))
                }
                capturedImage(imgList)

                AppLog.e(TAG, mResults.toString())
            }
        }
    }
    fun capturedImage(mResults: ArrayList<BusinessImage>) {
        initViewPager(mResults, false)
        for (imgUrl in mResults) {
            imageFiles.add(File(imgUrl.url))
        }
    }

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(this, mResults, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(this, mResults.size, layoutDots, viewPager)
    }


}
