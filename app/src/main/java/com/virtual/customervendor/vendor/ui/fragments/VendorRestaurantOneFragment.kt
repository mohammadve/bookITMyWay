package com.virtual.customervendor.vendor.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.request.VendorRestaurantServiceModel
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.activity.VendorRestaurantActivity
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_restaurant_one_vendor.*
import java.io.File

class VendorRestaurantOneFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
        deletefiles.clear()
        deletefiles.add(vendorRestaurantServiceModel.business_images.get(position).url)
        hitDeleteImage(deletefiles, vendorRestaurantServiceModel.business_id.toString()!!)
    }

    var imageFile: File? = null
    var TAG: String = VendorRestaurantOneFragment::class.java.name
    var vendorRestaurantServiceModel = VendorRestaurantServiceModel()
    val REQUEST_CODE = 123
    var mResults = ArrayList<String>()
    var imageFiles = ArrayList<File>()
    var deletefiles = ArrayList<String>()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                validateField()
            }

            R.id.ed_city -> {
                ed_address.requestFocus()
                if (context is VendorRestaurantActivity) {

                    if (vendorRestaurantServiceModel.business_region_id.regionid != null && !vendorRestaurantServiceModel.business_region_id.regionid.equals("")) {
                        (context as VendorRestaurantActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + vendorRestaurantServiceModel.business_region_id.regionid)

                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }
                }
            }
            R.id.ed_region -> {
                ed_address.requestFocus()
                if (context is VendorRestaurantActivity) {
                    (context as VendorRestaurantActivity).setDisplayDialog(7, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_SERVICE_AREA)
                }
            }
            R.id.img_upload -> captureMultipleImages()
            R.id.iv_profile -> captureMultipleImages()
            R.id.img_camera -> captureMultipleImages()
        }
    }

//    override fun capturedImage(uri: Uri?, imageFile: File?) {
//        if (imageFile != null) {
//            this.imageFile = imageFile
//            Glide.with(this).load(File(imageFile.absolutePath)).into(img_upload)
//            if (activity is VendorRestaurantActivity) {
//                (activity as VendorRestaurantActivity).imageFile = imageFile
//                if ((activity as VendorRestaurantActivity).isFromedit()) (activity as VendorRestaurantActivity).uploadPic(imageFile, vendorRestaurantServiceModel.business_id.toString())
//            }
//        }
//    }

    fun capturedImage(mResults: ArrayList<BusinessImage>) {
        initViewPager(mResults, false)
        if (mResults != null) {
            if (activity is VendorRestaurantActivity) {
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorRestaurantActivity).imageFiles = imageFiles
                (activity as VendorRestaurantActivity).mResults = mResults
                if ((activity as VendorRestaurantActivity).isFromedit()) {
                    (activity as VendorRestaurantActivity).uploadPic(imageFiles, vendorRestaurantServiceModel.business_id.toString()!!)
                }
            }
        }
    }


    companion object {
        fun newInstance(): VendorRestaurantOneFragment {
            val fragment = VendorRestaurantOneFragment()
            return fragment
        }
    }

    private fun getfilledData() {
        try {
            ed_bname.setText(vendorRestaurantServiceModel.business_name)
            ed_bcontact.setText(vendorRestaurantServiceModel.business_contactno)
            ed_email.setText(vendorRestaurantServiceModel.business_email)
            ed_city.setText(vendorRestaurantServiceModel.business_city_id.cityname)
            ed_region.setText(vendorRestaurantServiceModel.business_region_id.regionname)
            ed_address.setText(vendorRestaurantServiceModel.business_address)
            ed_tax.setText(vendorRestaurantServiceModel.business_tax)
            ed_code.setText(vendorRestaurantServiceModel.dial_code)

            if (vendorRestaurantServiceModel.business_images != null && vendorRestaurantServiceModel.business_images.size > 0) {
                initViewPager(vendorRestaurantServiceModel.business_images, true)
            } else if ((activity as VendorRestaurantActivity).mResults != null) {
                initViewPager((activity as VendorRestaurantActivity).mResults, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun manageFromEdit() {
        if (activity is VendorRestaurantActivity) {
            if ((activity as VendorRestaurantActivity).isFromedit()) {
                btn_next.setText((activity as VendorRestaurantActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        }
    }

    private fun putAllDataToFieldMap() {
        vendorRestaurantServiceModel.business_name = ed_bname.text.toString()
        vendorRestaurantServiceModel.business_contactno = ed_bcontact.text.toString()
        vendorRestaurantServiceModel.business_email = ed_email.text.toString()
        vendorRestaurantServiceModel.business_address = ed_address.text.toString()
        vendorRestaurantServiceModel.business_tax = ed_tax.text.toString()
        if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
            vendorRestaurantServiceModel.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
            vendorRestaurantServiceModel.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!

        } else {
            vendorRestaurantServiceModel.country_code = "IN"
            vendorRestaurantServiceModel.dial_code = "91"
        }
    }

    fun validateField() {

        if (ed_bname.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_bname, getString(R.string.rgs_sign_in_empty_username_text))
            return
        }
        if (!UiValidator.isValidUserName(ed_bname.getText().toString())) {
            UiValidator.setValidationError(til_bname, getString(R.string.rgs_sign_up_invalid_username))
            return
        }
        if (til_bname.isErrorEnabled()) {
            UiValidator.disableValidationError(til_bname)
        }

        if (ed_bcontact.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_bcontact, getString(R.string.rgs_sign_in_empty_mobile_text))
            return
        }
        if (!UiValidator.isValidPhoneNumber(ed_bcontact.getText().toString())) {
            UiValidator.setValidationError(til_bcontact, getString(R.string.rgs_sign_up_invalid_mobileno))
            return
        }
        if (til_bcontact.isErrorEnabled()) {
            UiValidator.disableValidationError(til_bcontact)
        }

        if (ed_email.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_bemail, getString(R.string.rgs_sign_up_empty_email))
            return
        }
        if (!UiValidator.isValidEmail(ed_email.getText().toString())) {
            UiValidator.setValidationError(til_bemail, getString(R.string.rgs_sign_up_invalid_email))
            return
        }
        if (til_bemail.isErrorEnabled()) {
            UiValidator.disableValidationError(til_bemail)
        }


        if (ed_bname.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_bname, getString(R.string.rgs_sign_in_empty_username_text))
            return
        }
        if (!UiValidator.isValidUserName(ed_bname.getText().toString())) {
            UiValidator.setValidationError(til_bname, getString(R.string.rgs_sign_up_invalid_username))
            return
        }
        if (til_bname.isErrorEnabled()) {
            UiValidator.disableValidationError(til_bname)
        }

        if (ed_city.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_bcity, getString(R.string.field_required))
            return
        }
        if (ed_region.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_bregion, getString(R.string.field_required))
            return
        }
        if (til_bcity.isErrorEnabled()) {
            UiValidator.disableValidationError(til_bcity)
        }



        if (til_bregion.isErrorEnabled()) {
            UiValidator.disableValidationError(til_bregion)
        }
        if (ed_address.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_badd, getString(R.string.field_required))
            return
        }

        if (til_badd.isErrorEnabled()) {
            UiValidator.disableValidationError(til_badd)
        }

        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (activity is VendorRestaurantActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorRestaurantActivity).isFromedit()) {
                (activity as VendorRestaurantActivity).hitApiEdit(vendorRestaurantServiceModel)
            } else {
                (activity as VendorRestaurantActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_restaurant_one_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vendorRestaurantServiceModel = (context as VendorRestaurantActivity).getFieldPojo()
        mResults.clear()
//        for (img in vendorRestaurantServiceModel.business_images) {
//            mResults.add(img.url)
//        }

        if (vendorRestaurantServiceModel.business_images.size > 0) {
            for (img in vendorRestaurantServiceModel.business_images) {
                mResults.add(img.url)
            }
        } else if ((activity as VendorRestaurantActivity).mResults.size > 0) {
            for (img in (activity as VendorRestaurantActivity).mResults) {
                mResults.add(img.url)
            }
        }
        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        iv_profile.setOnClickListener(this)
        ed_city.setOnClickListener(this)
        ed_region.setOnClickListener(this)
        img_upload.setOnClickListener(this)
        img_camera.setOnClickListener(this)

        ed_bcontact.addTextChangedListener(PhoneNumberTextWatcher(ed_bcontact))
        if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
            ed_code.setText("+" + SharedPreferenceManager.getRegisterCountryDetails().data?.code!!)
        } else {
            ed_code.setText("+91")

        }
        manageFromEdit()
    }

    fun updateSelectedCity(bean: CityModel) {
        UiValidator.hideSoftKeyboard(activity)
        vendorRestaurantServiceModel.business_city_id = bean
        ed_city.setText(bean.cityname)
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

    fun updateSelectedServiceArea(bean: RegionModel) {
        UiValidator.hideSoftKeyboard(activity)
        if (bean != null) {
            vendorRestaurantServiceModel.business_region_id = bean
            ed_region.setText(bean.regionname)
            ed_city.setText("")
        }
    }

    private fun captureMultipleImages() {
// start multiple photos selector
        val intent = Intent(activity, ImagesSelectorActivity::class.java)
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, AppConstants.VENDOR_BANNER_IMG_COUNT)
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, AppConstants.VENDOR_BANNER_IMG_COMPRESSION_SIZE)
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false)
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults)
// start the selector
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(activity, mResults, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
    }

    fun hitDeleteImage(deletefiles: ArrayList<String>, businessId: String) {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(activity!!, getString(R.string.please_wait))
            var apiInterface = ApiClient.client.create(ApiInterface::class.java)
            apiInterface?.deleteBusinessImage("Bearer " + SharedPreferenceManager.getAuthToken(), deletefiles, businessId)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<BusinessImagesResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(userResponse: BusinessImagesResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, userResponse.toString())
                            if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                if (activity is VendorRestaurantActivity) {
                                    var taxi_Service_Request = CachingManager.getVendorRestaurantInfo()
                                    taxi_Service_Request.business_images = userResponse.data
                                    CachingManager.setVendorRestaurantInfo(taxi_Service_Request)
                                    mResults.clear()
                                    for (img in userResponse.data) {
                                        mResults.add(img.url)
                                    }
                                    initViewPager(userResponse.data, true)
                                }
                                activity?.setResult(Activity.RESULT_OK)
//                                activity?.finish()

                            } else {
                                UiValidator.displayMsgSnack(nest, activity, userResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            UiValidator.displayMsgSnack(nest, activity!!, e.message)
                            AppLog.e(TAG, e.message)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.no_internet_connection))
        }
    }


}