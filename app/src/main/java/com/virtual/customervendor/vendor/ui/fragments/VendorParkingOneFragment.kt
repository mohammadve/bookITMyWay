package com.virtual.customervendor.vendor.ui.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
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
import com.virtual.customervendor.model.request.VendorParkingRequest
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.activity.*
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_parking_one_vendor.*
import java.io.File

class VendorParkingOneFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
        deletefiles.clear()
        deletefiles.add(parkingRequest.business_images.get(position).url)
        hitDeleteImage(deletefiles, parkingRequest.business_id.toString()!!)
    }

    var imageFile: File? = null
    var TAG: String = VendorParkingOneFragment::class.java.name
    var parkingRequest = VendorParkingRequest()
    val REQUEST_CODE = 123
    var mResults = ArrayList<String>()
    var imageFiles = ArrayList<File>()
    var deletefiles = ArrayList<String>()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> activity?.onBackPressed()
            R.id.btn_next -> validateField()
            R.id.ed_city -> {
                ed_address.requestFocus()
                if (context is VendorParkingValetActivity) {
                    if (parkingRequest.business_region_id.regionid != null && !parkingRequest.business_region_id.regionid.equals("")) {
                        (context as VendorParkingValetActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + parkingRequest.business_region_id.regionid)
                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }
                }
            }
            R.id.ed_region -> {
                ed_address.requestFocus()
                if (context is VendorParkingValetActivity) {
                    (context as VendorParkingValetActivity).setDisplayDialog(7, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_SERVICE_AREA)
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
            if (activity is VendorParkingValetActivity) {
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorParkingValetActivity).imageFiles = imageFiles
                (activity as VendorParkingValetActivity).mResults = mResults
                if ((activity as VendorParkingValetActivity).isFromedit()) {
                    (activity as VendorParkingValetActivity).uploadPic(imageFiles, parkingRequest.business_id.toString()!!)
                }
            }

        }
    }

    companion object {
        fun newInstance(): VendorParkingOneFragment {
            val fragment = VendorParkingOneFragment()
            return fragment
        }
    }

    private fun getfilledData() {
        try {
            ed_bname.setText(parkingRequest.business_name)
            ed_bcontact.setText(parkingRequest.business_contactno)
            ed_email.setText(parkingRequest.business_email)
            ed_city.setText(parkingRequest.business_city_id.cityname)
            ed_region.setText(parkingRequest.business_region_id.regionname)
            ed_address.setText(parkingRequest.business_address)
            ed_code.setText(parkingRequest.dial_code)
            ed_tax.setText(parkingRequest.business_tax)

            if (parkingRequest.business_images != null && parkingRequest.business_images.size > 0) {
                initViewPager(parkingRequest.business_images, true)
            } else if ((activity as VendorParkingValetActivity).mResults != null) {
                initViewPager((activity as VendorParkingValetActivity).mResults, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun putAllDataToFieldMap(parkingRequest: VendorParkingRequest) {
        try {
            parkingRequest.business_name = ed_bname.text.toString()
            parkingRequest.business_contactno = ed_bcontact.text.toString()
            parkingRequest.business_email = ed_email.text.toString()
            parkingRequest.business_address = ed_address.text.toString()
            parkingRequest.business_tax = ed_tax.text.toString()
            if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
                parkingRequest.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
                parkingRequest.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!

            } else {
                parkingRequest.country_code = "IN"
                parkingRequest.dial_code = "91"

            }
        } catch (e: Exception) {
            e.printStackTrace()
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
        if (ed_region.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_bregion, getString(R.string.field_required))
            return
        }

        if (til_bregion.isErrorEnabled()) {
            UiValidator.disableValidationError(til_bregion)
        }
        if (ed_city.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_bcity, getString(R.string.field_required))
            return
        }
        if (til_bcity.isErrorEnabled()) {
            UiValidator.disableValidationError(til_bcity)
        }


        if (ed_address.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_badd, getString(R.string.field_required))
            return
        }

        if (til_badd.isErrorEnabled()) {
            UiValidator.disableValidationError(til_badd)
        }

        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (activity is VendorParkingValetActivity) {
            putAllDataToFieldMap(parkingRequest)
            if ((activity as VendorParkingValetActivity).isFromedit()) {
                (activity as VendorParkingValetActivity).hitApiEdit(parkingRequest)
            } else {
                (activity as VendorParkingValetActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }
        }


    }

    fun manageFromEdit() {
        if (activity is VendorParkingValetActivity) {
            if ((activity as VendorParkingValetActivity).isFromedit()) {
                btn_next.setText((activity as VendorParkingValetActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_parking_one_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parkingRequest = (context as VendorParkingValetActivity).getValetRequestPojo()
        mResults.clear()
        if (parkingRequest.business_images.size > 0) {
            for (img in parkingRequest.business_images) {
                mResults.add(img.url)
            }
        } else if ((activity as VendorParkingValetActivity).mResults.size > 0) {
            for (img in (activity as VendorParkingValetActivity).mResults) {
                mResults.add(img.url)
            }
        }
        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        iv_profile.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        ed_city.setOnClickListener(this)
        ed_region.setOnClickListener(this)
        img_upload.setOnClickListener(this)
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
        ed_city.setText(bean.cityname)
        parkingRequest.business_city_id = bean
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CODE) {
            if (resultCode === RESULT_OK) {
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
        ed_city.setText("")
        ed_region.setText(bean.regionname)
        parkingRequest.business_region_id = bean
    }

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(activity, mResults, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
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
                                if (activity is VendorParkingValetActivity) {
                                    var taxi_Service_Request = CachingManager.getVendorParkingInfo()
                                    taxi_Service_Request.business_images = userResponse.data
                                    CachingManager.setVendorParkingInfo(taxi_Service_Request)

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
                                AppLog.e(TAG, e.message)
                            }
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.no_internet_connection))
        }
    }


}