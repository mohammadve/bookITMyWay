package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.PurchaseItemsActivity
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.StoreItemLocationModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.utills.*
import kotlinx.android.synthetic.main.fragment_delivery_information.*

class StoreDeliveryInformationFragment : Fragment(), View.OnClickListener {

    var TAG: String = StoreDeliveryInformationFragment::class.java.name
    var serviceDetail = VendorServiceDetailModel()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_next -> {
                validateField()
            }
            R.id.ed_servicearea -> {
                (activity as PurchaseItemsActivity).setDisplayDialogServiceArea(7, "")
            }

        }
    }

    companion object {
        fun newInstance(): StoreDeliveryInformationFragment {
            val fragment = StoreDeliveryInformationFragment()
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_delivery_information, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        serviceDetail = (activity as PurchaseItemsActivity).businessDetailModel

        AppLog.e(TAG, serviceDetail.store_category_id + " " + serviceDetail.storecategory)

        btn_next.setOnClickListener(this)

        if (serviceDetail.store_category_id.equals(AppConstants.STORE_CAT_SEAT_SERVICE)) {
            til_servicearea.visibility = View.VISIBLE
            til_section.visibility = View.VISIBLE
            til_row.visibility = View.VISIBLE
            til_seat.visibility = View.VISIBLE

            ed_servicearea.setOnClickListener(this)
        } else {
            til_address1.visibility = View.VISIBLE
            til_address2.visibility = View.VISIBLE
            til_city.visibility = View.VISIBLE
            til_region.visibility = View.VISIBLE
            til_zip_code.visibility = View.VISIBLE
            start_lay.visibility = View.VISIBLE

            ed_phone_number.addTextChangedListener(PhoneNumberTextWatcher(ed_phone_number))

            if (SharedPreferenceManager.getRegisterCountryDetails() != null) {
                ed_code.setText("+" + CachingManager.getCurrentCountry().code)
            } else {
                ed_code.setText("+91")
            }
        }


    }

    fun updateSelectedRegion(bean: StoreItemLocationModel) {
        if (bean != null) {
            ed_servicearea.setText(bean.location_name)
        }
    }

    fun validateField() {
        if (serviceDetail.store_category_id.equals(AppConstants.STORE_CAT_SEAT_SERVICE)) {
            if (ed_servicearea.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_servicearea, getString(R.string.field_required))
                return
            }
            if (til_servicearea.isErrorEnabled()) {
                UiValidator.disableValidationError(til_servicearea)
            }
            if (ed_section.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_section, getString(R.string.field_required))
                return
            }
            if (til_section.isErrorEnabled()) {
                UiValidator.disableValidationError(til_section)
            }
            if (ed_row.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_row, getString(R.string.field_required))
                return
            }
            if (til_row.isErrorEnabled()) {
                UiValidator.disableValidationError(til_row)
            }
            if (ed_seat.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_seat, getString(R.string.field_required))
                return
            }
            if (til_seat.isErrorEnabled()) {
                UiValidator.disableValidationError(til_seat)
            }

            (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_SERVICEAREA, ed_servicearea.getText().toString())
            (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_SECTION, ed_section.getText().toString())
            (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_ROW, ed_row.getText().toString())
            (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_SEAT, ed_seat.getText().toString())
        } else {
            if (ed_address1.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_address1, getString(R.string.field_required))
                return
            }
            if (til_address1.isErrorEnabled()) {
                UiValidator.disableValidationError(til_address1)
            }

            if (ed_address2.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_address2, getString(R.string.field_required))
                return
            }
            if (til_address2.isErrorEnabled()) {
                UiValidator.disableValidationError(til_address2)
            }
            if (ed_city.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_city, getString(R.string.field_required))
                return
            }
            if (til_city.isErrorEnabled()) {
                UiValidator.disableValidationError(til_city)
            }

            if (ed_region.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_region, getString(R.string.field_required))
                return
            }
            if (til_region.isErrorEnabled()) {
                UiValidator.disableValidationError(til_region)
            }

            if (ed_zip_code.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_zip_code, getString(R.string.field_required))
                return
            }
            if (til_zip_code.isErrorEnabled()) {
                UiValidator.disableValidationError(til_zip_code)
            }

            if (ed_phone_number.getText().toString().isEmpty()) {
                UiValidator.setValidationError(til_phone_number, getString(R.string.field_required))
                return
            }
            if (til_phone_number.isErrorEnabled()) {
                UiValidator.disableValidationError(til_phone_number)
            }

            (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_ADDRESS1, ed_address1.getText().toString())
            (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_ADDRESS2, ed_address2.getText().toString())
            (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_CITY, ed_city.getText().toString())
            (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_REGION, ed_region.getText().toString())
            (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_ZIP_CODE, ed_zip_code.getText().toString())
            (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_PHONE_NUMBER, ed_phone_number.getText().toString())
        }

        (activity as PurchaseItemsActivity).setDisplayFragment(8, resources.getString(R.string.confirm_order), false)
    }
}