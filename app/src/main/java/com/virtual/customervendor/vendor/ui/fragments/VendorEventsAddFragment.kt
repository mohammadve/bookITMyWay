package com.virtual.customervendor.vendor.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.virtual.customervendor.R
import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.request.VendorEventServiceRequest
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.CaptureImageUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorEventsActivity
import kotlinx.android.synthetic.main.fragment_events_one_vendor.*
import java.io.File

class VendorEventsAddFragment : Fragment(), View.OnClickListener, CaptureImageUtils.ImageSelectionListener {

    private var captureImageUtils: CaptureImageUtils? = null
    var imageFile: File? = null
    var TAG: String = VendorEventsAddFragment::class.java.name
    var vendorEventServiceRequest = VendorEventServiceRequest()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                validateField()
            }

            R.id.ed_city -> {
                if (context is VendorEventsActivity) {
                    if (vendorEventServiceRequest.business_region_id.regionid != null && !vendorEventServiceRequest.business_region_id.regionid.equals("")) {
                        (context as VendorEventsActivity).setDisplayDialog(6, AppConstants.FROM_V_TAXI_CITY, "" + vendorEventServiceRequest.business_region_id.regionid)
                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }
                }
            }
            R.id.ed_region -> {
                if (context is VendorEventsActivity) {
                    (context as VendorEventsActivity).setDisplayDialog(7, AppConstants.FROM_V_TAXI_SERVICE_AREA, "")
                }
            }
            R.id.img_upload -> {
                captureImageUtils?.openSelectImageFromDialog()
            }
        }
    }

    override fun capturedImage(uri: Uri?, imageFile: File?) {
        if (imageFile != null) {
            this.imageFile = imageFile
//            uploadUserPicOnServer1(imageFile)
            Glide.with(this).load(File(imageFile.absolutePath)).into(img_upload)
            if (activity is VendorEventsActivity) {
//                (activity as VendorEventsActivity).imageFiles = imageFile
            }
        }
    }

    companion object {
        fun newInstance(): VendorEventsAddFragment {
            val fragment = VendorEventsAddFragment()
            return fragment
        }
    }

    private fun getfilledData() {
        try {
            ed_bname.setText(vendorEventServiceRequest.event_name)
            ed_bcontact.setText(vendorEventServiceRequest.business_contactno)
            ed_email.setText(vendorEventServiceRequest.business_email)
            ed_city.setText(vendorEventServiceRequest.business_city_id.cityname)
            ed_region.setText(vendorEventServiceRequest.business_region_id.regionname)
            ed_address.setText(vendorEventServiceRequest.business_address)
//            if ((activity as VendorEventsActivity).bus_imageFile != null)
//                Glide.with(this).load(File((activity as VendorEventsActivity).bus_imageFile?.absolutePath)).into(img_upload)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun putAllDataToFieldMap() {
        vendorEventServiceRequest.event_name = ed_bname.text.toString()
        vendorEventServiceRequest.business_contactno = ed_bcontact.text.toString()
        vendorEventServiceRequest.business_email = ed_email.text.toString()
        vendorEventServiceRequest.business_address = ed_address.text.toString()

//
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

        if (til_bcity.isErrorEnabled()) {
            UiValidator.disableValidationError(til_bcity)
        }

        if (ed_region.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_bregion, getString(R.string.field_required))
            return
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
        if (activity is VendorEventsActivity) {
            putAllDataToFieldMap()
            (activity as VendorEventsActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.event_information), false)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_events_one_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vendorEventServiceRequest = (context as VendorEventsActivity).getEventFieldPojo()
        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        captureImageUtils = CaptureImageUtils(activity, this)
        btn_next.setOnClickListener(this)
        ed_city.setOnClickListener(this)
        ed_region.setOnClickListener(this)
        img_upload.setOnClickListener(this)
    }

    fun updateSelectedCity(bean: CityModel) {
        vendorEventServiceRequest.business_city_id = bean
        ed_city.setText(bean.cityname)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            captureImageUtils?.onRequestPermission(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            captureImageUtils!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun updateSelectedServiceArea(bean: RegionModel) {
        if (bean != null)
            vendorEventServiceRequest.business_region_id = bean
        ed_region.setText(bean.regionname)
    }

}