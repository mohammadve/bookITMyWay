package com.virtual.customervendor.vendor.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customer_vendor.utill.UploadBussinessImage
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.OfferModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.request.VendorHealthServiceRequest
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.model.response.UserProfilePicResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.activity.VendorDoctorActivity
import com.virtual.customervendor.vendor.ui.activity.VendorHairActivity
import com.virtual.customervendor.vendor.ui.activity.VendorMassagePhysioActivity
import com.virtual.customervendor.vendor.ui.activity.VendorNailsActivity
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_appoint_one_vendor.*
import java.io.File
import java.util.*

class VendorAppOneFragment : Fragment(), View.OnClickListener, UploadBussinessImage.IBussinessImage, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
        if (activity is VendorDoctorActivity) {
            deletefiles.clear()
            deletefiles.add(doctoreServiceRequest.business_images.get(position).url)
            hitDeleteImage(deletefiles, doctoreServiceRequest.business_id.toString()!!)
        }else if (activity is VendorHairActivity) {
            deletefiles.clear()
            deletefiles.add(hairServiceRequest.business_images.get(position).url)
            hitDeleteImage(deletefiles, hairServiceRequest.business_id.toString()!!)
        } else if (activity is VendorNailsActivity) {
            deletefiles.clear()
            deletefiles.add(nailServiceRequest.business_images.get(position).url)
            hitDeleteImage(deletefiles, nailServiceRequest.business_id.toString()!!)
        } else if (activity is VendorMassagePhysioActivity) {
            deletefiles.clear()
            deletefiles.add(massageServiceRequest.business_images.get(position).url)
            hitDeleteImage(deletefiles, massageServiceRequest.business_id.toString()!!)
        }
    }

    override fun onSuccess(userImageUploadResponse: UserProfilePicResponse) {
    }

    override fun onError(e: Throwable) {
    }

    override fun onSubscribe(d: Disposable) {
    }

    var datetime: StringBuilder? = null
    var manager: FragmentManager? = null
    val list: ArrayList<OfferModel> = java.util.ArrayList()
    var count: Int = 0
    var TAG: String = VendorAppOneFragment::class.java.name
    var apiService: ApiInterface? = null
    var doctoreServiceRequest = VendorHealthServiceRequest()
    var dentistRequest = VendorHealthServiceRequest()
    var hairServiceRequest = VendorHealthServiceRequest()
    var nailServiceRequest = VendorHealthServiceRequest()
    var massageServiceRequest = VendorHealthServiceRequest()

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
            R.id.ed_area -> {
                ed_address.requestFocus()
                if (context is VendorDoctorActivity) {
                    (context as VendorDoctorActivity).setDisplayDialog(8, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_CITY)
                } else if (context is VendorHairActivity) {
                    (context as VendorHairActivity).setDisplayDialog(8, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_CITY)
                } else if (context is VendorNailsActivity) {
                    (context as VendorNailsActivity).setDisplayDialog(8, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_CITY)
                } else if (context is VendorMassagePhysioActivity) {
                    (context as VendorMassagePhysioActivity).setDisplayDialog(8, SharedPreferenceManager.getRegisterCountryDetails().data.countryCode!!, AppConstants.FROM_V_TAXI_CITY)
                }
            }
            R.id.ed_city -> {
                ed_address.requestFocus()
                if (context is VendorDoctorActivity) {
                    if (doctoreServiceRequest.business_region_id.regionid != null && !doctoreServiceRequest.business_region_id.regionid.equals("")) {
                        (context as VendorDoctorActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + doctoreServiceRequest.business_region_id.regionid)
                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }

                } else if (context is VendorHairActivity) {

                    if (hairServiceRequest.business_region_id.regionid != null && !hairServiceRequest.business_region_id.regionid.equals("")) {
                        (context as VendorHairActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + hairServiceRequest.business_region_id.regionid)

                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }

                } else if (context is VendorNailsActivity) {

                    if (nailServiceRequest.business_region_id.regionid != null && !nailServiceRequest.business_region_id.regionid.equals("")) {
                        (context as VendorNailsActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + nailServiceRequest.business_region_id.regionid)

                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }
                } else if (context is VendorMassagePhysioActivity) {

                    if (massageServiceRequest.business_region_id.regionid != null && !massageServiceRequest.business_region_id.regionid.equals("")) {
                        (context as VendorMassagePhysioActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + massageServiceRequest.business_region_id.regionid)

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
            if (activity is VendorDoctorActivity) {
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorDoctorActivity).imageFiles = imageFiles
                (activity as VendorDoctorActivity).mResults = mResults
                if ((activity as VendorDoctorActivity).isFromedit()) {
                    (activity as VendorDoctorActivity).uploadPic(imageFiles, doctoreServiceRequest.business_id.toString()!!)
                }
            } else if (activity is VendorHairActivity) {
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorHairActivity).imageFiles = imageFiles
                (activity as VendorHairActivity).mResults = mResults
                if ((activity as VendorHairActivity).isFromedit()) {
                    (activity as VendorHairActivity).uploadPic(imageFiles, hairServiceRequest.business_id.toString()!!)
                }
            } else if (activity is VendorNailsActivity) {
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorNailsActivity).imageFiles = imageFiles
                (activity as VendorNailsActivity).mResults = mResults
                if ((activity as VendorNailsActivity).isFromedit()) {
                    (activity as VendorNailsActivity).uploadPic(imageFiles, nailServiceRequest.business_id.toString()!!)
                }
            } else if (activity is VendorMassagePhysioActivity) {
                for (imgUrl in mResults) {
                    imageFiles.add(File(imgUrl.url))
                }
                (activity as VendorMassagePhysioActivity).imageFiles = imageFiles
                (activity as VendorMassagePhysioActivity).mResults = mResults
                if ((activity as VendorMassagePhysioActivity).isFromedit()) {
                    (activity as VendorMassagePhysioActivity).uploadPic(imageFiles, massageServiceRequest.business_id.toString()!!)
                }
            }
        }
    }


//

    companion object {
        fun newInstance(): VendorAppOneFragment {
            val fragment = VendorAppOneFragment()
            return fragment
        }
    }

    private fun putAllDataToFieldMap() {
        try {
            if (context is VendorDoctorActivity) {
                doctoreServiceRequest.business_name = ed_bname.text.toString()
                doctoreServiceRequest.business_contactno = ed_bcontact.text.toString()
                doctoreServiceRequest.business_email = ed_email.text.toString()
                doctoreServiceRequest.business_address = ed_address.text.toString()
                doctoreServiceRequest.business_tax = ed_tax.text.toString()
                if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
                    doctoreServiceRequest.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
                    doctoreServiceRequest.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!

                } else {
                    doctoreServiceRequest.country_code = "IN"
                    doctoreServiceRequest.dial_code = "91"

                }
            }  else if (context is VendorHairActivity) {
                hairServiceRequest.business_name = ed_bname.text.toString()
                hairServiceRequest.business_contactno = ed_bcontact.text.toString()
                hairServiceRequest.business_email = ed_email.text.toString()
                hairServiceRequest.business_address = ed_address.text.toString()
                hairServiceRequest.business_tax = ed_tax.text.toString()
                if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
                    hairServiceRequest.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
                    hairServiceRequest.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!
                } else {
                    hairServiceRequest.country_code = "IN"
                    hairServiceRequest.dial_code = "91"
                }
            } else if (context is VendorNailsActivity) {
                nailServiceRequest.business_name = ed_bname.text.toString()
                nailServiceRequest.business_contactno = ed_bcontact.text.toString()
                nailServiceRequest.business_email = ed_email.text.toString()
                nailServiceRequest.business_address = ed_address.text.toString()
                nailServiceRequest.business_tax = ed_tax.text.toString()
                if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
                    nailServiceRequest.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
                    nailServiceRequest.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!
                } else {
                    nailServiceRequest.country_code = "IN"
                    nailServiceRequest.dial_code = "91"
                }
            } else if (context is VendorMassagePhysioActivity) {
                massageServiceRequest.business_name = ed_bname.text.toString()
                massageServiceRequest.business_contactno = ed_bcontact.text.toString()
                massageServiceRequest.business_email = ed_email.text.toString()
                massageServiceRequest.business_address = ed_address.text.toString()
                massageServiceRequest.business_tax = ed_tax.text.toString()
                if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
                    massageServiceRequest.country_code = SharedPreferenceManager.getRegisterCountryDetails().data?.countryCode!!
                    massageServiceRequest.dial_code = SharedPreferenceManager.getRegisterCountryDetails().data?.code!!

                } else {
                    massageServiceRequest.country_code = "IN"
                    massageServiceRequest.dial_code = "91"
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun getfilledData() {
        if (context is VendorDoctorActivity) {
            doctoreServiceRequest = (context as VendorDoctorActivity).getDoctorFieldPojo()
            mResults.clear()


            if (doctoreServiceRequest.business_images.size > 0) {
                for (img in doctoreServiceRequest.business_images) {
                    mResults.add(img.url)
                }
            } else if ((activity as VendorDoctorActivity).mResults.size > 0) {
                for (img in (activity as VendorDoctorActivity).mResults) {
                    mResults.add(img.url)
                }
            }

//            for (img in doctoreServiceRequest.business_images) {
//                mResults.add(img.url)
//            }
        }
//
        else if (context is VendorHairActivity) {
            hairServiceRequest = (context as VendorHairActivity).getHairFieldPojo()
            mResults.clear()

            if (hairServiceRequest.business_images.size > 0) {
                for (img in hairServiceRequest.business_images) {
                    mResults.add(img.url)
                }
            } else if ((activity as VendorHairActivity).mResults.size > 0) {
                for (img in (activity as VendorHairActivity).mResults) {
                    mResults.add(img.url)
                }
            }

//            for (img in hairServiceRequest.business_images) {
//                mResults.add(img.url)
//            }
        } else if (context is VendorNailsActivity) {
            nailServiceRequest = (context as VendorNailsActivity).getNailFieldPojo()
            mResults.clear()

            if (nailServiceRequest.business_images.size > 0) {
                for (img in nailServiceRequest.business_images) {
                    mResults.add(img.url)
                }
            } else if ((activity as VendorNailsActivity).mResults.size > 0) {
                for (img in (activity as VendorNailsActivity).mResults) {
                    mResults.add(img.url)
                }
            }

//            for (img in nailServiceRequest.business_images) {
//                mResults.add(img.url)
//            }
        } else if (context is VendorMassagePhysioActivity) {
            massageServiceRequest = (context as VendorMassagePhysioActivity).getMassageFieldPojo()
            mResults.clear()


            if (massageServiceRequest.business_images.size > 0) {
                for (img in massageServiceRequest.business_images) {
                    mResults.add(img.url)
                }
            } else if ((activity as VendorMassagePhysioActivity).mResults.size > 0) {
                for (img in (activity as VendorMassagePhysioActivity).mResults) {
                    mResults.add(img.url)
                }
            }

//            for (img in massageServiceRequest.business_images) {
//                mResults.add(img.url)
//            }
        }

        try {
            if (activity is VendorDoctorActivity) {
                ed_bname.setText(doctoreServiceRequest.business_name)
                ed_bcontact.setText(doctoreServiceRequest.business_contactno)
                ed_email.setText(doctoreServiceRequest.business_email)
                ed_city.setText(doctoreServiceRequest.business_city_id.cityname)
                ed_area.setText(doctoreServiceRequest.business_region_id.regionname)
                ed_address.setText(doctoreServiceRequest.business_address)
                ed_code.setText(doctoreServiceRequest.dial_code)

                ed_tax.setText(doctoreServiceRequest.business_tax)
                if (doctoreServiceRequest.business_images != null && doctoreServiceRequest.business_images.size > 0) {
                    initViewPager(doctoreServiceRequest.business_images, true)
                } else if ((activity as VendorDoctorActivity).mResults != null) {
                    initViewPager((activity as VendorDoctorActivity).mResults, false)
                }

            } else if (activity is VendorHairActivity) {
                ed_bname.setText(hairServiceRequest.business_name)
                ed_bcontact.setText(hairServiceRequest.business_contactno)
                ed_email.setText(hairServiceRequest.business_email)
                ed_city.setText(hairServiceRequest.business_city_id.cityname)
                ed_area.setText(hairServiceRequest.business_region_id.regionname)
                ed_address.setText(hairServiceRequest.business_address)
                ed_code.setText(hairServiceRequest.dial_code)

                ed_tax.setText(hairServiceRequest.business_tax)
                if (hairServiceRequest.business_images != null && hairServiceRequest.business_images.size > 0) {
                    initViewPager(hairServiceRequest.business_images, true)
                } else if ((activity as VendorHairActivity).mResults != null) {
                    initViewPager((activity as VendorHairActivity).mResults, false)
                }

            } else if (activity is VendorNailsActivity) {
                ed_bname.setText(nailServiceRequest.business_name)
                ed_bcontact.setText(nailServiceRequest.business_contactno)
                ed_email.setText(nailServiceRequest.business_email)
                ed_city.setText(nailServiceRequest.business_city_id.cityname)
                ed_area.setText(nailServiceRequest.business_region_id.regionname)
                ed_address.setText(nailServiceRequest.business_address)
                ed_code.setText(nailServiceRequest.dial_code)
                ed_tax.setText(nailServiceRequest.business_tax)
                if (nailServiceRequest.business_images != null && nailServiceRequest.business_images.size > 0) {
                    initViewPager(nailServiceRequest.business_images, true)
                } else if ((activity as VendorNailsActivity).mResults != null) {
                    initViewPager((activity as VendorNailsActivity).mResults, false)
                }
            } else if (activity is VendorMassagePhysioActivity) {
                ed_bname.setText(massageServiceRequest.business_name)
                ed_bcontact.setText(massageServiceRequest.business_contactno)
                ed_email.setText(massageServiceRequest.business_email)
                ed_city.setText(massageServiceRequest.business_city_id.cityname)
                ed_area.setText(massageServiceRequest.business_region_id.regionname)
                ed_address.setText(massageServiceRequest.business_address)
                ed_code.setText(massageServiceRequest.dial_code)
                ed_tax.setText(massageServiceRequest.business_tax)
                if (massageServiceRequest.business_images != null && massageServiceRequest.business_images.size > 0) {
                    initViewPager(massageServiceRequest.business_images, true)
                } else if ((activity as VendorMassagePhysioActivity).mResults != null) {
                    initViewPager((activity as VendorMassagePhysioActivity).mResults, false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_appoint_one_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        ed_city.setOnClickListener(this)
        ed_area.setOnClickListener(this)
        img_upload.setOnClickListener(this)
        iv_profile.setOnClickListener(this)
        img_camera.setOnClickListener(this)

        ed_bcontact.addTextChangedListener(PhoneNumberTextWatcher(ed_bcontact))

        if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
            // fieldMap.put(AppKeys.COUNTRY_CODE, SharedPreferenceManager.getRegisterCountryDetails().data.code!!)
            ed_code.setText("+" + SharedPreferenceManager.getRegisterCountryDetails().data?.code!!)
        } else {
            ed_code.setText("+91")

        }

        manageFromEdit()
    }

    fun manageFromEdit() {
        if (activity is VendorDoctorActivity) {
            if ((activity as VendorDoctorActivity).isFromedit()) {
                btn_next.setText((activity as VendorDoctorActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        } else if (activity is VendorHairActivity) {
            if ((activity as VendorHairActivity).isFromedit()) {
                btn_next.setText((activity as VendorHairActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        } else if (activity is VendorNailsActivity) {
            if ((activity as VendorNailsActivity).isFromedit()) {
                btn_next.setText((activity as VendorNailsActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        } else if (activity is VendorMassagePhysioActivity) {
            if ((activity as VendorMassagePhysioActivity).isFromedit()) {
                btn_next.setText((activity as VendorMassagePhysioActivity).getString(R.string.save))
                iv_profile.visibility = View.VISIBLE
                img_camera.visibility = View.GONE
            }
        }
    }

    fun updateSelectedCity(bean: CityModel) {
        if (bean != null) {
            ed_city.setText(bean.cityname)
            if (context is VendorDoctorActivity) {
                doctoreServiceRequest.business_city_id = bean
            } else if (context is VendorHairActivity)
                hairServiceRequest.business_city_id = bean
            else if (context is VendorNailsActivity) {
                nailServiceRequest.business_city_id = bean
            } else if (context is VendorMassagePhysioActivity) {
                massageServiceRequest.business_city_id = bean
            }
        }
    }

    fun updateSelectedRegion(bean: RegionModel) {
        if (bean != null) {
            ed_area.setText(bean.regionname)
            ed_city.setText("")
            if (context is VendorDoctorActivity) {
                doctoreServiceRequest.business_region_id = bean
            } else if (context is VendorHairActivity)
                hairServiceRequest.business_region_id = bean
            else if (context is VendorNailsActivity) {
                nailServiceRequest.business_region_id = bean
            } else if (context is VendorMassagePhysioActivity) {
                massageServiceRequest.business_region_id = bean
            }
        }
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

        if (ed_area.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_barea, getString(R.string.field_required))
            return
        }

        if (til_barea.isErrorEnabled()) {
            UiValidator.disableValidationError(til_barea)
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

        if (activity is VendorDoctorActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorDoctorActivity).isFromedit()) {
                (activity as VendorDoctorActivity).hitApiEdit(doctoreServiceRequest)
            } else {
                (activity as VendorDoctorActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }
        } /*else if (activity is VendorDentistActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorDentistActivity).isFromedit()) {
                (activity as VendorDentistActivity).hitApiEdit(dentistRequest)
            } else {
                (activity as VendorDentistActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }
        }*/ else if (activity is VendorHairActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorHairActivity).isFromedit()) {
                (activity as VendorHairActivity).hitApi(hairServiceRequest)
            } else {
                (activity as VendorHairActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }
        } else if (activity is VendorNailsActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorNailsActivity).isFromedit()) {
                (activity as VendorNailsActivity).hitApi(nailServiceRequest)
            } else {
                (activity as VendorNailsActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
            }
        } else if (activity is VendorMassagePhysioActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorMassagePhysioActivity).isFromedit()) {
                (activity as VendorMassagePhysioActivity).hitApi(massageServiceRequest)
            } else {
                (activity as VendorMassagePhysioActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.service_information), false)
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
                                if (activity is VendorDoctorActivity || activity is VendorHairActivity || activity is VendorNailsActivity || activity is VendorMassagePhysioActivity) {
                                    var taxi_Service_Request = CachingManager.getVendorDoctorInfo()
                                    taxi_Service_Request.business_images = userResponse.data
                                    CachingManager.setVendorDoctorInfo(taxi_Service_Request)
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