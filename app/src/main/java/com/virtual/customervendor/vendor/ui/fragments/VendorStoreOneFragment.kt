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
import com.virtual.customervendor.model.request.VendorStoreServiceRequest
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.activity.VendorStoreActivity
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_store_one_vendor.*
import java.io.File
import java.util.*

class VendorStoreOneFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
        if (activity is VendorStoreActivity) {
            deletefiles.clear()
            deletefiles.add(vendorstoreRequest.business_images.get(position).url)
            hitDeleteImage(deletefiles, vendorstoreRequest.business_id.toString()!!)
        }
    }

    var imageFile: File? = null
    var TAG: String = VendorStoreOneFragment::class.java.name
    var vendorstoreRequest = VendorStoreServiceRequest()

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
                if (context is VendorStoreActivity) {
                    if (vendorstoreRequest.business_region_id.regionid != null && !vendorstoreRequest.business_region_id.regionid.equals("")) {

                      AppLog.e("@@@",""+vendorstoreRequest.business_region_id.regionid);
                        (context as VendorStoreActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + vendorstoreRequest.business_region_id.regionid)
                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }
                }
            }
            R.id.ed_region -> {
                ed_address.requestFocus()
                if (context is VendorStoreActivity) {
                    (context as VendorStoreActivity).setDisplayDialog(7, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_SERVICE_AREA)
                }
            }
            R.id.img_upload -> captureMultipleImages()
            R.id.iv_profile -> captureMultipleImages()
            R.id.img_camera -> captureMultipleImages()

        }
    }

    fun capturedImage(mResults: ArrayList<BusinessImage>) {
        initViewPager(mResults, false)
        if (mResults != null) {
            if (activity is VendorStoreActivity) {
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorStoreActivity).imageFiles = imageFiles
                (activity as VendorStoreActivity).mResults = mResults
                if ((activity as VendorStoreActivity).isFromedit()) {
                    (activity as VendorStoreActivity).uploadPic(imageFiles, vendorstoreRequest.business_id.toString()!!)
                }
            }

        }
    }

    companion object {
        fun newInstance(): VendorStoreOneFragment {
            val fragment = VendorStoreOneFragment()
            return fragment
        }
    }

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(activity, mResults, this, fromEdit)
        viewPager?.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
    }


    private fun getfilledData() {
        try {
            ed_bname.setText(vendorstoreRequest.business_name)
            ed_bcontact.setText(vendorstoreRequest.business_contactno)
            ed_email.setText(vendorstoreRequest.business_email)
            ed_city.setText(vendorstoreRequest.business_city_id.cityname)
            ed_region.setText(vendorstoreRequest.business_region_id.regionname)
            ed_address.setText(vendorstoreRequest.business_address)
            ed_code.setText(vendorstoreRequest.dial_code)
            ed_tax.setText(vendorstoreRequest.business_tax)
            if (vendorstoreRequest.business_images != null && vendorstoreRequest.business_images.size > 0) {
                initViewPager(vendorstoreRequest.business_images, true)
            } else if ((activity as VendorStoreActivity).mResults != null) {
                initViewPager((activity as VendorStoreActivity).mResults, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun manageFromEdit() {
        if (activity is VendorStoreActivity) {
            if ((activity as VendorStoreActivity).isFromedit()) {
                btn_next.setText((activity as VendorStoreActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        }
    }

    private fun putAllDataToFieldMap() {
        vendorstoreRequest.business_name = ed_bname.text.toString()
        vendorstoreRequest.business_contactno = ed_bcontact.text.toString()
        vendorstoreRequest.business_email = ed_email.text.toString()
        vendorstoreRequest.business_address = ed_address.text.toString()
        vendorstoreRequest.business_tax = ed_tax.text.toString()
        //  vendorstoreRequest.country_code = ed_code.text.toString()
        if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
            vendorstoreRequest.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
            vendorstoreRequest.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!

        } else {
            vendorstoreRequest.country_code = "IN"
            vendorstoreRequest.dial_code = "91"
        }
//
    }

    fun validateField() {

//        if (ed_bname.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_bname, getString(R.string.rgs_sign_in_empty_username_text))
//            return
//        }
//        if (!UiValidator.isValidUserName(ed_bname.getText().toString())) {
//            UiValidator.setValidationError(til_bname, getString(R.string.rgs_sign_up_invalid_username))
//            return
//        }
//        if (til_bname.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_bname)
//        }
//
//        if (ed_bcontact.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_bcontact, getString(R.string.rgs_sign_in_empty_mobile_text))
//            return
//        }
//        if (!UiValidator.isValidPhoneNumber(ed_bcontact.getText().toString())) {
//            UiValidator.setValidationError(til_bcontact, getString(R.string.rgs_sign_up_invalid_mobileno))
//            return
//        }
//        if (til_bcontact.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_bcontact)
//        }
//
//        if (ed_email.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_bemail, getString(R.string.rgs_sign_up_empty_email))
//            return
//        }
//        if (!UiValidator.isValidEmail(ed_email.getText().toString())) {
//            UiValidator.setValidationError(til_bemail, getString(R.string.rgs_sign_up_invalid_email))
//            return
//        }
//        if (til_bemail.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_bemail)
//        }
//
//
//        if (ed_bname.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_bname, getString(R.string.rgs_sign_in_empty_username_text))
//            return
//        }
//        if (!UiValidator.isValidUserName(ed_bname.getText().toString())) {
//            UiValidator.setValidationError(til_bname, getString(R.string.rgs_sign_up_invalid_username))
//            return
//        }
//        if (til_bname.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_bname)
//        }
//        if (ed_region.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_bregion, getString(R.string.field_required))
//            return
//        }
//
//        if (til_bregion.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_bregion)
//        }
//        if (ed_city.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_bcity, getString(R.string.field_required))
//            return
//        }
//
//        if (til_bcity.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_bcity)
//        }
//
//
//        if (ed_address.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_badd, getString(R.string.field_required))
//            return
//        }
//
//        if (til_badd.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_badd)
//        }

        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (activity is VendorStoreActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorStoreActivity).isFromedit()) {
                (activity as VendorStoreActivity).hitApiEdit(vendorstoreRequest)
            } else {
                (activity as VendorStoreActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_store_one_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (context is VendorStoreActivity) {
            vendorstoreRequest = (context as VendorStoreActivity).getFieldPojo()
            mResults.clear()


            if (vendorstoreRequest.business_images.size > 0) {
                for (img in vendorstoreRequest.business_images) {
                    mResults.add(img.url)
                }
            } else if ((activity as VendorStoreActivity).mResults.size > 0) {
                for (img in (activity as VendorStoreActivity).mResults) {
                    mResults.add(img.url)
                }
            }

//            for (img in vendorstoreRequest.business_images) {
//                mResults.add(img.url)
//            }
        }
        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        ed_city.setOnClickListener(this)
        ed_region.setOnClickListener(this)
        img_upload.setOnClickListener(this)
        iv_profile.setOnClickListener(this)
        img_camera.setOnClickListener(this)

        ed_bcontact.addTextChangedListener(PhoneNumberTextWatcher(ed_bcontact))

        if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
            // fieldMap.put(AppKeys.COUNTRY_CODE, SharedPreferenceManager.getCurrentCountryDetails().data.code!!)
            ed_code.setText("+" + SharedPreferenceManager.getRegisterCountryDetails().data?.code!!)
        } else {
            ed_code.setText("+91")

        }
        manageFromEdit()
    }

    fun updateSelectedCity(bean: CityModel) {
        vendorstoreRequest.business_city_id = bean
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
        if (bean != null)
            ed_city.setText("")
        vendorstoreRequest.business_region_id = bean
        ed_region.setText(bean.regionname)
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
                                if (activity is VendorStoreActivity) {
                                    var taxi_Service_Request = CachingManager.getVendorStoreInfo()
                                    taxi_Service_Request.business_images = userResponse.data
                                    CachingManager.setVendorStoreInfo(taxi_Service_Request)
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
                            if (e != null) {
                            UiValidator.displayMsgSnack(nest, activity!!, e.message)
                            AppLog.e(TAG, e.message)}
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.no_internet_connection))
        }
    }

    private fun captureMultipleImages() {
// start multiple photos selector
        val intent = Intent(activity, ImagesSelectorActivity::class.java)
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, AppConstants.VENDOR_BANNER_IMG_COUNT)
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000)
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false)
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults)
// start the selector
        startActivityForResult(intent, REQUEST_CODE)
    }

}