package com.virtual.customervendor.vendor.ui.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
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
import com.virtual.customervendor.model.OfferModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.request.Ven_Sight_Seeing_Service_Request
import com.virtual.customervendor.model.request.Ven_Taxi_Service_Request
import com.virtual.customervendor.model.request.VendorBusinessDetailRequest
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.activity.VendorLimoActivity
import com.virtual.customervendor.vendor.ui.activity.VendorSightSeeingActivity
import com.virtual.customervendor.vendor.ui.activity.VendorTaxiActivity
import com.virtual.customervendor.vendor.ui.activity.VendorTourBusActivity
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_taxi_one_vendor.*
import java.io.File

class VendorTaxiOneFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
        if (activity is VendorTaxiActivity) {
            deletefiles.clear()
            deletefiles.add(taxi_Service_Request.business_images.get(position).url)
            hitDeleteImage(deletefiles, taxi_Service_Request.business_id!!)
        } else if (activity is VendorLimoActivity) {
            deletefiles.clear()
            deletefiles.add(limo_Service_Request.business_images.get(position).url)
            hitDeleteImage(deletefiles, limo_Service_Request.business_id!!)
        } else if (activity is VendorTourBusActivity) {
            deletefiles.clear()
            deletefiles.add(tourbus_Service_Request.business_images.get(position).url)
            hitDeleteImage(deletefiles, tourbus_Service_Request.business_id!!)
        } else if (activity is VendorSightSeeingActivity) {
            deletefiles.clear()
            deletefiles.add(sight_Seeing_Service_Request.business_images.get(position).url)
            hitDeleteImage(deletefiles, sight_Seeing_Service_Request.business_id!!)
        }
    }

    var datetime: StringBuilder? = null
    var manager: FragmentManager? = null
    val list: ArrayList<OfferModel> = java.util.ArrayList()
    var count: Int = 0
    //    var imageFile: File? = null
    var TAG: String = VendorTaxiOneFragment::class.java.name
    var apiService: ApiInterface? = null
    var taxi_Service_Request = Ven_Taxi_Service_Request()
    var limo_Service_Request = Ven_Taxi_Service_Request()
    var tourbus_Service_Request = Ven_Taxi_Service_Request()
    var sight_Seeing_Service_Request = Ven_Sight_Seeing_Service_Request()
    val REQUEST_CODE = 123
    var mResults = ArrayList<String>()
    var imageFiles = ArrayList<File>()
    var deletefiles = ArrayList<String>()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null
    var mView: View? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                validateField()
            }
            R.id.ed_area -> {
                ed_address.requestFocus()

                if (context is VendorTaxiActivity) {
                    (context as VendorTaxiActivity).setDisplayDialog(8, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_CITY)
                } else if (context is VendorLimoActivity) {
                    (context as VendorLimoActivity).setDisplayDialog(8, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_CITY)
                } else if (context is VendorTourBusActivity) {
                    (context as VendorTourBusActivity).setDisplayDialog(8, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_CITY)
                } else if (context is VendorSightSeeingActivity) {
                    (context as VendorSightSeeingActivity).setDisplayDialog(8, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_CITY)
                }
            }
            R.id.ed_city -> {
                ed_address.requestFocus()

                if (context is VendorTaxiActivity) {
                    if (taxi_Service_Request.business_region_id.regionid != null && !taxi_Service_Request.business_region_id.regionid.equals("")) {

                        (context as VendorTaxiActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + taxi_Service_Request.business_region_id.regionid)
                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }


                } else if (context is VendorLimoActivity) {
                    if (limo_Service_Request.business_region_id.regionid != null && !limo_Service_Request.business_region_id.regionid.equals("")) {

                        (context as VendorLimoActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + limo_Service_Request.business_region_id.regionid)

                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }

                } else if (context is VendorTourBusActivity) {
                    if (tourbus_Service_Request.business_region_id.regionid != null && !tourbus_Service_Request.business_region_id.regionid.equals("")) {

                        (context as VendorTourBusActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + tourbus_Service_Request.business_region_id.regionid)


                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }
                } else if (context is VendorSightSeeingActivity) {
                    if (sight_Seeing_Service_Request.business_region_id.regionid != null && !sight_Seeing_Service_Request.business_region_id.regionid.equals("")) {

                        (context as VendorSightSeeingActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + sight_Seeing_Service_Request.business_region_id.regionid)

                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }
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
            if (activity is VendorTaxiActivity) {
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorTaxiActivity).imageFiles = imageFiles
                (activity as VendorTaxiActivity).mResults = mResults
                if ((activity as VendorTaxiActivity).isFromedit()) {
                    (activity as VendorTaxiActivity).uploadPic(imageFiles, taxi_Service_Request.business_id!!)
                }
            } else if (activity is VendorLimoActivity) {
//                (activity as VendorLimoActivity).imageFile = imageFile
//                if ((activity as VendorLimoActivity).isFromedit()) (activity as VendorLimoActivity).uploadPic(imageFile, limo_Service_Request.business_id!!)
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorLimoActivity).imageFiles = imageFiles
                (activity as VendorLimoActivity).mResults = mResults
                if ((activity as VendorLimoActivity).isFromedit()) {
                    (activity as VendorLimoActivity).uploadPic(imageFiles, limo_Service_Request.business_id!!)
                }
            } else if (activity is VendorTourBusActivity) {
//                (activity as VendorTourBusActivity).imageFile = imageFile
//                if ((activity as VendorTourBusActivity).isFromedit()) (activity as VendorTourBusActivity).uploadPic(imageFile, tourbus_Service_Request.business_id!!)
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorTourBusActivity).imageFiles = imageFiles
                (activity as VendorTourBusActivity).mResults = mResults
                if ((activity as VendorTourBusActivity).isFromedit()) {
                    (activity as VendorTourBusActivity).uploadPic(imageFiles, tourbus_Service_Request.business_id!!)
                }
            } else if (activity is VendorSightSeeingActivity) {
//                (activity as VendorSightSeeingActivity).imageFile = imageFile
//                if ((activity as VendorSightSeeingActivity).isFromedit()) (activity as VendorSightSeeingActivity).uploadPic(imageFile, sight_Seeing_Service_Request.business_id!!)
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorSightSeeingActivity).imageFiles = imageFiles
                (activity as VendorSightSeeingActivity).mResults = mResults
                if ((activity as VendorSightSeeingActivity).isFromedit()) {
                    (activity as VendorSightSeeingActivity).uploadPic(imageFiles, sight_Seeing_Service_Request.business_id!!)
                }
            }
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

    companion object {
        fun newInstance(): VendorTaxiOneFragment {
            val fragment = VendorTaxiOneFragment()
            return fragment
        }
    }

    private fun putAllDataToFieldMap() {
        try {
            if (context is VendorTaxiActivity) {
                taxi_Service_Request.business_name = ed_bname.text.toString()
                taxi_Service_Request.business_contactno = ed_bcontact.text.toString()
                taxi_Service_Request.business_email = ed_email.text.toString()
                taxi_Service_Request.business_address = ed_address.text.toString()
                taxi_Service_Request.business_tax = ed_tax.text.toString()
                if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
                    taxi_Service_Request.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
                    taxi_Service_Request.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!

                } else {
                    taxi_Service_Request.country_code = "IN"
                    taxi_Service_Request.dial_code = "91"

                }
            } else if (context is VendorLimoActivity) {
                limo_Service_Request.business_name = ed_bname.text.toString()
                limo_Service_Request.business_contactno = ed_bcontact.text.toString()
                limo_Service_Request.business_email = ed_email.text.toString()
                limo_Service_Request.business_address = ed_address.text.toString()
                limo_Service_Request.business_tax = ed_tax.text.toString()
                if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
                    limo_Service_Request.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
                    limo_Service_Request.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!

                } else {
                    limo_Service_Request.country_code = "IN"
                    limo_Service_Request.dial_code = "91"

                }
            } else if (context is VendorTourBusActivity) {
                tourbus_Service_Request.business_name = ed_bname.text.toString()
                tourbus_Service_Request.business_contactno = ed_bcontact.text.toString()
                tourbus_Service_Request.business_email = ed_email.text.toString()
                tourbus_Service_Request.business_address = ed_address.text.toString()
                tourbus_Service_Request.business_tax = ed_tax.text.toString()
                if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
                    tourbus_Service_Request.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
                    tourbus_Service_Request.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!

                } else {
                    tourbus_Service_Request.country_code = "IN"
                    tourbus_Service_Request.dial_code = "91"

                }
            } else if (context is VendorSightSeeingActivity) {
                sight_Seeing_Service_Request.business_name = ed_bname.text.toString()
                sight_Seeing_Service_Request.business_contactno = ed_bcontact.text.toString()
                sight_Seeing_Service_Request.business_email = ed_email.text.toString()
                sight_Seeing_Service_Request.business_address = ed_address.text.toString()
                sight_Seeing_Service_Request.business_tax = ed_tax.text.toString()
                if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
                    sight_Seeing_Service_Request.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
                    sight_Seeing_Service_Request.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!

                } else {
                    sight_Seeing_Service_Request.country_code = "IN"
                    sight_Seeing_Service_Request.dial_code = "91"

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getfilledData() {
        if (context is VendorTaxiActivity) {
            taxi_Service_Request = (context as VendorTaxiActivity).getTaxiFieldPojo()
            mResults.clear()
            if (taxi_Service_Request.business_images.size > 0) {
                for (img in taxi_Service_Request.business_images) {
                    mResults.add(img.url)
                }
            } else if ((activity as VendorTaxiActivity).mResults.size > 0) {
                for (img in (activity as VendorTaxiActivity).mResults) {
                    mResults.add(img.url)
                }
            }
        }
        if (context is VendorLimoActivity) {
            limo_Service_Request = (context as VendorLimoActivity).getLimoFieldPojo()
            mResults.clear()

            if (limo_Service_Request.business_images.size > 0) {
                for (img in limo_Service_Request.business_images) {
                    mResults.add(img.url)
                }
            } else if ((activity as VendorLimoActivity).mResults.size > 0) {
                for (img in (activity as VendorLimoActivity).mResults) {
                    mResults.add(img.url)
                }
            }

//            for (img in limo_Service_Request.business_images) {
//                mResults.add(img.url)
//            }
        }
        if (context is VendorTourBusActivity) {
            tourbus_Service_Request = (context as VendorTourBusActivity).getTourBusFieldPojo()
            mResults.clear()

            if (tourbus_Service_Request.business_images.size > 0) {
                for (img in tourbus_Service_Request.business_images) {
                    mResults.add(img.url)
                }
            } else if ((activity as VendorTourBusActivity).mResults.size > 0) {
                for (img in (activity as VendorTourBusActivity).mResults) {
                    mResults.add(img.url)
                }
            }
//            for (img in tourbus_Service_Request.business_images) {
//                mResults.add(img.url)
//            }
        }
        if (context is VendorSightSeeingActivity) {
            sight_Seeing_Service_Request = (context as VendorSightSeeingActivity).getSightSeeingPojo()
            mResults.clear()

            if (sight_Seeing_Service_Request.business_images.size > 0) {
                for (img in sight_Seeing_Service_Request.business_images) {
                    mResults.add(img.url)
                }
            } else if ((activity as VendorSightSeeingActivity).mResults.size > 0) {
                for (img in (activity as VendorSightSeeingActivity).mResults) {
                    mResults.add(img.url)
                }
            }

//            for (img in sight_Seeing_Service_Request.business_images) {
//                mResults.add(img.url)
//            }
        }

        try {
            if (activity is VendorTaxiActivity) {
                ed_bname.setText(taxi_Service_Request.business_name)
                ed_bcontact.setText(taxi_Service_Request.business_contactno)
                ed_email.setText(taxi_Service_Request.business_email)
                ed_city.setText(taxi_Service_Request.business_city_id.cityname)
                ed_area.setText(taxi_Service_Request.business_region_id.regionname)
                ed_address.setText(taxi_Service_Request.business_address)
                ed_code.setText(taxi_Service_Request.country_code)
                ed_tax.setText(taxi_Service_Request.business_tax)

                if (taxi_Service_Request.business_images != null && taxi_Service_Request.business_images.size > 0) {
                    initViewPager(taxi_Service_Request.business_images, true)
                } else if ((activity as VendorTaxiActivity).mResults != null) {
                    initViewPager((activity as VendorTaxiActivity).mResults, false)
                }

            } else if (activity is VendorLimoActivity) {
                ed_bname.setText(limo_Service_Request.business_name)
                ed_bcontact.setText(limo_Service_Request.business_contactno)
                ed_email.setText(limo_Service_Request.business_email)
                ed_city.setText(limo_Service_Request.business_city_id.cityname)
                ed_area.setText(limo_Service_Request.business_region_id.regionname)
                ed_address.setText(limo_Service_Request.business_address)
                ed_code.setText(limo_Service_Request.country_code)
                ed_tax.setText(limo_Service_Request.business_tax)

                if (limo_Service_Request.business_images != null && limo_Service_Request.business_images.size > 0) {
                    initViewPager(limo_Service_Request.business_images, true)
                } else if ((activity as VendorLimoActivity).mResults != null) {
                    initViewPager((activity as VendorLimoActivity).mResults, false)
                }
            } else if (activity is VendorTourBusActivity) {
                ed_bname.setText(tourbus_Service_Request.business_name)
                ed_bcontact.setText(tourbus_Service_Request.business_contactno)
                ed_email.setText(tourbus_Service_Request.business_email)
                ed_city.setText(tourbus_Service_Request.business_city_id.cityname)
                ed_area.setText(tourbus_Service_Request.business_region_id.regionname)
                ed_address.setText(tourbus_Service_Request.business_address)
                ed_code.setText(tourbus_Service_Request.country_code)
                ed_tax.setText(tourbus_Service_Request.business_tax)

                if (tourbus_Service_Request.business_images != null && tourbus_Service_Request.business_images.size > 0) {
                    initViewPager(tourbus_Service_Request.business_images, true)
                } else if ((activity as VendorTourBusActivity).mResults != null) {
                    initViewPager((activity as VendorTourBusActivity).mResults, false)
                }

            } else if (activity is VendorSightSeeingActivity) {
                ed_bname.setText(sight_Seeing_Service_Request.business_name)
                ed_bcontact.setText(sight_Seeing_Service_Request.business_contactno)
                ed_email.setText(sight_Seeing_Service_Request.business_email)
                ed_city.setText(sight_Seeing_Service_Request.business_city_id.cityname)
                ed_area.setText(sight_Seeing_Service_Request.business_region_id.regionname)
                ed_address.setText(sight_Seeing_Service_Request.business_address)
                ed_code.setText(sight_Seeing_Service_Request.dial_code)
                ed_tax.setText(sight_Seeing_Service_Request.business_tax)

                if (sight_Seeing_Service_Request.business_images != null && sight_Seeing_Service_Request.business_images.size > 0) {
                    initViewPager(sight_Seeing_Service_Request.business_images, true)
                } else if ((activity as VendorSightSeeingActivity).mResults != null) {
                    initViewPager((activity as VendorSightSeeingActivity).mResults, false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_taxi_one_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        iv_profile.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        ed_city.setOnClickListener(this)
        ed_area.setOnClickListener(this)
        img_upload.setOnClickListener(this)
        img_camera.setOnClickListener(this)
        viewPager.setOnClickListener(this)

        ed_bcontact.addTextChangedListener(PhoneNumberTextWatcher(ed_bcontact))

        if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
            ed_code.setText("+" + SharedPreferenceManager.getRegisterCountryDetails().data?.code!!)
        } else {
            ed_code.setText("+91")
        }
        manageFromEdit()
    }

    fun manageFromEdit() {
        if (activity is VendorTaxiActivity) {
            if ((activity as VendorTaxiActivity).isFromedit()) {
                btn_next.setText((activity as VendorTaxiActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        } else if (activity is VendorLimoActivity) {
            if ((activity as VendorLimoActivity).isFromedit()) {
                btn_next.setText((activity as VendorLimoActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        } else if (activity is VendorTourBusActivity) {
            if ((activity as VendorTourBusActivity).isFromedit()) {
                btn_next.setText((activity as VendorTourBusActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        } else if (activity is VendorSightSeeingActivity) {
            if ((activity as VendorSightSeeingActivity).isFromedit()) {
                btn_next.setText((activity as VendorSightSeeingActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        }
    }

    fun updateSelectedCity(bean: CityModel) {
        if (bean != null) {
            ed_city.setText(bean.cityname)
            if (context is VendorTaxiActivity) {
                taxi_Service_Request.business_city_id = bean
            } else if (context is VendorLimoActivity)
                limo_Service_Request.business_city_id = bean
            else if (context is VendorTourBusActivity)
                tourbus_Service_Request.business_city_id = bean
            else if (context is VendorSightSeeingActivity) {
                sight_Seeing_Service_Request.business_city_id = bean
            }
        }
    }

    fun updateSelectedRegion(bean: RegionModel) {
        if (bean != null) {
            ed_city.setText("")
            ed_area.setText(bean.regionname)
            if (context is VendorTaxiActivity) {
                taxi_Service_Request.business_region_id = bean
            } else if (context is VendorLimoActivity)
                limo_Service_Request.business_region_id = bean
            else if (context is VendorTourBusActivity)
                tourbus_Service_Request.business_region_id = bean
            else if (context is VendorSightSeeingActivity) {
                sight_Seeing_Service_Request.business_region_id = bean
            }
        }
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

    fun validateField() {
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)

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


        if (ed_area.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_barea, getString(R.string.field_required))
            return
        }

        if (til_barea.isErrorEnabled()) {
            UiValidator.disableValidationError(til_barea)
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



        if (activity is VendorTaxiActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorTaxiActivity).isFromedit()) {
                (activity as VendorTaxiActivity).hitApiEdit(taxi_Service_Request)
            } else {
                (activity as VendorTaxiActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }
        } else if (activity is VendorLimoActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorLimoActivity).isFromedit()) {
                (activity as VendorLimoActivity).hitApiEdit(limo_Service_Request)
            } else {
                (activity as VendorLimoActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }
        } else if (activity is VendorTourBusActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorTourBusActivity).isFromedit()) {
                (activity as VendorTourBusActivity).hitApiEdit(tourbus_Service_Request)
            } else {
                (activity as VendorTourBusActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }
        } else if (activity is VendorSightSeeingActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorSightSeeingActivity).isFromedit()) {

                var businessDetailRequest = VendorBusinessDetailRequest()

                businessDetailRequest.business_id = sight_Seeing_Service_Request.business_id
                businessDetailRequest.business_name = sight_Seeing_Service_Request.business_name
                businessDetailRequest.business_contactno = sight_Seeing_Service_Request.business_contactno
                businessDetailRequest.business_email = sight_Seeing_Service_Request.business_email
                businessDetailRequest.business_city_id = sight_Seeing_Service_Request.business_city_id
                businessDetailRequest.business_region_id = sight_Seeing_Service_Request.business_region_id
                businessDetailRequest.business_address = sight_Seeing_Service_Request.business_address
                businessDetailRequest.country_code = sight_Seeing_Service_Request.country_code
                businessDetailRequest.dial_code = sight_Seeing_Service_Request.dial_code
                businessDetailRequest.business_tax = sight_Seeing_Service_Request.business_tax

                (activity as VendorSightSeeingActivity).hitApiEdit(businessDetailRequest)
            } else {
                (activity as VendorSightSeeingActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }

        }
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
                                if (activity is VendorTaxiActivity || activity is VendorLimoActivity || activity is VendorTourBusActivity) {
                                    var taxi_Service_Request = CachingManager.getVendorTaxiInfo()
                                    taxi_Service_Request.business_images = userResponse.data
                                    CachingManager.setVendorTaxiInfo(taxi_Service_Request)
                                    initViewPager(userResponse.data, true)
                                } else if (activity is VendorSightSeeingActivity) {
                                    var taxi_Service_Request = CachingManager.getVendorSightSeenInfo()
                                    taxi_Service_Request.business_images = userResponse.data
                                    CachingManager.setVendorSightSeenInfo(taxi_Service_Request)
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