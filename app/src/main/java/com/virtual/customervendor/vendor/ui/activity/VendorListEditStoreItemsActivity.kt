package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.ItemPriceStoreModel
import com.virtual.customervendor.model.request.StoreItemAlterRequest
import com.virtual.customervendor.model.response.StoreListingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.StoreUpdateItemsAdapter
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity__store_edit_items_vendor.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class VendorListEditStoreItemsActivity : BaseActivity(), View.OnClickListener, StoreUpdateItemsAdapter.UpdateItems, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    override fun deleteItem(itempriceModel: ItemPriceStoreModel, adapterPosition: Int) {
        hitApiEditUpdate(itempriceModel, AppConstants.ACTION_DELETE, adapterPosition)
    }

    override fun updateItem(itempriceModel: ItemPriceStoreModel, adapterPosition: Int) {
//        hitApiEditUpdate(itempriceModel, AppConstants.ACTION_EDIT)
    }

    var storeAddItemsAdapter: StoreUpdateItemsAdapter? = null
    var TAG: String = VendorListEditStoreItemsActivity::class.java.simpleName
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var apiInterface: ApiInterface? = null
    var storeItemAlterRequest = StoreItemAlterRequest()
    var eventlisting = ArrayList<ItemPriceStoreModel>()
    val REQUEST_CODE = 123
    var mResults = java.util.ArrayList<String>()
    var imageFiles = java.util.ArrayList<File>()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null
    var itempriceModel = ItemPriceStoreModel()


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_delete -> {
                onBackPressed()
            }
            R.id.iv_edit -> {
                btn_save.isClickable = true
            }
            R.id.btn_save -> {
                validate()
            }
            R.id.img_camera -> {
                captureMultipleImages()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__store_edit_items_vendor)

        itempriceModel = intent.extras.get(AppConstants.OREDER_DATA) as ItemPriceStoreModel

        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()
    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.store_items)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)

        val iv_edit = toolbar!!.findViewById(R.id.iv_edit) as ImageView
        val iv_del = toolbar!!.findViewById(R.id.iv_delete) as ImageView

        iv_del.visibility = View.GONE
        iv_edit.visibility = View.VISIBLE
        iv_del.setOnClickListener(this)
        iv_edit.setOnClickListener(this)
        img_camera.setOnClickListener(this)
        btn_save.setOnClickListener(this)

        if (itempriceModel != null) {
            ed_itemname.setText(itempriceModel.item_name)
            ed_price.setText(itempriceModel.item_price)
            ed_desc.setText(itempriceModel.item_description)
            ed_addone.setText(itempriceModel.add_on_one)
            ed_addtwo.setText(itempriceModel.add_on_two)
            if (itempriceModel.item_image.size > 0)
                Glide.with(this).load(itempriceModel.item_image.get(0)).into(img_upload)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
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

            var action = RequestBody.create(MediaType.parse("text/plain"), AppConstants.ACTION_EDIT)
            map.put("action", action)

            var item_id = RequestBody.create(MediaType.parse("text/plain"), itempriceModel.item_id.toString())
            map.put("item_id", item_id)

            var item_name = RequestBody.create(MediaType.parse("text/plain"), ed_itemname.text.toString())
            map.put("item_name", item_name)

            var item_price = RequestBody.create(MediaType.parse("text/plain"), ed_price.text.toString())
            map.put("item_price", item_price)

            var item_description = RequestBody.create(MediaType.parse("text/plain"), ed_desc.text.toString())
            map.put("product_description", item_description)

            var product_category = RequestBody.create(MediaType.parse("text/plain"), itempriceModel.product_category)
            map.put("product_category", product_category)

            var service_id = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceManager.getServiceId().toString())
            map.put("service_id", service_id)

            if (!ed_addone.text.toString().isEmpty()) {
                var addonOne = RequestBody.create(MediaType.parse("text/plain"), ed_addone.text.toString())
                map.put("add_on_one", addonOne)
            }

            if (!ed_addtwo.text.toString().isEmpty()) {
                var addonTwo = RequestBody.create(MediaType.parse("text/plain"), ed_addtwo.text.toString())
                map.put("add_on_two", addonTwo)
            }


            apiInterface?.storeItemAddDeleteEdit("Bearer " + SharedPreferenceManager.getAuthToken(), map, imageList)
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
            UiValidator.displayMsgSnack(start_lay, this, getString(R.string.no_internet_connection))
        }
    }

    private fun handleResults(eventListingResponse: StoreListingResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (eventListingResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            setResult(Activity.RESULT_OK)
            finish()
        }
        UiValidator.displayMsgSnack(coordinator, this, eventListingResponse.message)
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null) {
            AppLog.e(TAG, t.message)
            UiValidator.displayMsgSnack(coordinator, this, t.message)
        }
    }


    fun hitApiEditUpdate(itempriceModel: ItemPriceStoreModel, action: String, adapterPosition: Int) {
        AppLog.e(TAG, itempriceModel.toString() + action)
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        storeItemAlterRequest.action = action
        storeItemAlterRequest.item_id = itempriceModel.item_id
        storeItemAlterRequest.item_name = itempriceModel.item_name
        storeItemAlterRequest.item_price = itempriceModel.item_price
        storeItemAlterRequest.service_id = SharedPreferenceManager.getServiceId().toString()

//        apiInterface?.storeItemAddDeleteEdit("Bearer " + SharedPreferenceManager.getAuthToken(), storeItemAlterRequest)
//                ?.subscribeOn(Schedulers.io())
//                ?.observeOn(AndroidSchedulers.mainThread())
//                ?.subscribe(object : Observer<StoreListingResponse> {
//                    override fun onSubscribe(d: Disposable) {
//                    }
//
//                    override fun onNext(detailResponse: StoreListingResponse) {
//                        ProgressDialogLoader.progressDialogDismiss()
//                        AppLog.e(TAG, detailResponse.toString())
//
//                        if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
//                            setResult(Activity.RESULT_OK)
//                            if (action.equals(AppConstants.ACTION_DELETE)) {
//                                eventlisting.removeAt(adapterPosition)
//                                storeAddItemsAdapter!!.notifyItemRemoved(adapterPosition)
//                            } else if (action.equals(AppConstants.ACTION_EDIT)) {
//                                eventlisting.set(adapterPosition, itempriceModel)
//                                storeAddItemsAdapter!!.notifyItemRemoved(adapterPosition)
//                            }
//                        } else {
//                            UiValidator.displayMsgSnack(coordinator, this@VendorListEditStoreItemsActivity, detailResponse.message)
//                        }
//                    }
//
//                    override fun onError(e: Throwable) {
//                        handleError(e)
//                    }
//
//                    override fun onComplete() {}
//                })
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
