package com.virtual.customervendor.customer.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.dialogFragment.CountryDialogFragment
import com.virtual.customervendor.customer.ui.dialogFragment.RegionDialogFragmentSingle
import com.virtual.customervendor.customer.ui.fragments.*
import com.virtual.customervendor.model.*
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.SlideAnimationUtill

class EventsActivity : BaseActivity(), View.OnClickListener, RegionDialogFragmentSingle.SingleRegionSelectionInterface , CountryDialogFragment.countrySelectionInterface {
    var countryDialogFragment: CountryDialogFragment? = null

    var frameLayout: FrameLayout? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var regionDialogFragmentsingle: RegionDialogFragmentSingle? = null
    var manager: FragmentManager? = null
    var fragmentManager: FragmentManager? = null
    var customerBookingListModel: CustomerBookingListModel = CustomerBookingListModel();
    var TAG: String = BookAppointmentMassageActivity::class.java.simpleName
    var eventDetail: EventDetail = EventDetail()
    var businessDetailModel: VendorServiceDetailModel = VendorServiceDetailModel()
    var fieldmap: MutableMap<String, String> = mutableMapOf()
    var orderNo = String()
    var applyOfferModel = ApplyOfferModel()
    var isFromSearch: Boolean = false
    var searchModel = SearchModel()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        isFromSearch = intent.getBooleanExtra("isFromSearch", false)
        if (isFromSearch)
            searchModel = intent.getSerializableExtra(AppConstants.BUSINESS_DATA) as SearchModel
        AppLog.e(TAG, isFromSearch.toString() + searchModel.toString())
        setToolBar()
        init()
    }

    fun init() {
        frameLayout = findViewById(R.id.flContentnew) as FrameLayout

        if (isFromSearch) {
            customerBookingListModel.business_id = searchModel.busines_id
            customerBookingListModel.service_id = searchModel.service_id
            customerBookingListModel.business_name = searchModel.business_name
            setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else
        setDisplayFragment(1, resources.getString(R.string.events), false)
    }

    fun setToolBar() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.appoint)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }

    override fun onBackPressed() {
        handleBackPress()
    }

    fun setDisplayDialog(number: Int, title: String) {
        when (number) {
            7 -> showRegionSelectionDialogSingle(title)
            8 -> showCountrySelectionDialog()
        }
    }

    fun setDisplayFragment(number: Int, title: String, removeStack: Boolean) {
        setTitleBar(title)
        when (number) {
            1 -> replaceFragment(BookingFragmentOne.newInstance(), removeStack)
            2 -> replaceFragment(EventBusinessProfileFragment.newInstance(), removeStack)
            3 -> replaceFragment(EventDetailFragment.newInstance(), removeStack)
            4 -> replaceFragment(EventBookingFragment.newInstance(), removeStack)
            5 -> replaceFragment(EventBookingConfirmFragment.newInstance(), removeStack)
            6 -> replaceFragment(BookingFragmentConfirm.newInstance(), removeStack)
        }
    }

    private fun replaceFragment(fragmentClass: Fragment, removeStack: Boolean) {
        var fragment: Fragment? = null
        try {
            fragment = fragmentClass
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Insert the fragment by replacing any existing fragment
        fragmentManager = supportFragmentManager
        val mTransaction: android.support.v4.app.FragmentTransaction = fragmentManager!!.beginTransaction()
        mTransaction.addToBackStack(null)
        if (removeStack) {
            if (supportFragmentManager.fragments != null && supportFragmentManager.fragments.size > 1) {
                for (i in 1 until supportFragmentManager.fragments.size) {
                    supportFragmentManager.beginTransaction().remove(supportFragmentManager.fragments[i]).commit()
                }
            }
        }
        mTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left1, R.anim.slide_from_left, R.anim.slide_to_right).add(frameLayout!!.id, fragment!!).commit()
    }

    fun handleBackPress() {
        when {
            fragmentManager!!.findFragmentById(R.id.flContentnew) is BookingFragmentOne -> finish()
            fragmentManager!!.findFragmentById(R.id.flContentnew) is EventBusinessProfileFragment -> {
                if (!isFromSearch) {
                setTitleBar(resources.getString(R.string.events))
                fragmentManager!!.popBackStackImmediate()}else{
                    finish()
                }
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is EventDetailFragment -> {
                setTitleBar(resources.getString(R.string.business_profile))
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is EventBookingFragment -> {
                setTitleBar(resources.getString(R.string.event))
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is EventBookingConfirmFragment -> {
                applyOfferModel.order_price =""
                setTitleBar(resources.getString(R.string.booking))
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is BookingFragmentConfirm -> finish()
        }
        SlideAnimationUtill.slideBackAnimation(this)
    }


    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
    }

    private fun showRegionSelectionDialogSingle(from: String) {
        regionDialogFragmentsingle = RegionDialogFragmentSingle.newInstance(from, "all")
        manager = supportFragmentManager
        regionDialogFragmentsingle!!.show(manager, "My Dialog")
    }

    override fun regionSelectionInterface(bean: RegionModel, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        if (regionDialogFragmentsingle != null) {
            regionDialogFragmentsingle!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is BookingFragmentOne) fragment.updateSelectedRegion(bean)
            }
        }
    }

    private fun showCountrySelectionDialog() {
        countryDialogFragment = CountryDialogFragment.newInstance("all")
        manager = supportFragmentManager
        countryDialogFragment!!.show(manager, "My Dialog")
    }

    override fun selectedCountryUpdate(bean: CountryModel, fromWhere: String?) {
        AppUtils.hideSoftKeyboard(this)
        AppLog.e(TAG, bean.toString())
        if (countryDialogFragment != null) {
            countryDialogFragment!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is BookingFragmentOne) fragment.updateSelectedCountry(bean)
            }
        }
    }

    fun getcustomerBookingListModel(): CustomerBookingListModel {
        return customerBookingListModel
    }

    fun getbusinessDetailModel(): VendorServiceDetailModel {
        return businessDetailModel
    }

    fun eventDetail(): EventDetail {
        return eventDetail
    }

    public fun getFieldMap(): MutableMap<String, String> {
        return fieldmap
    }
    //Changes By Himanshu Starts

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1116) {
            if (resultCode == Activity.RESULT_OK) {

                val result = data?.getStringExtra("card_token") as String
                val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
                if (fragment != null && fragment.isVisible) {
                    if (fragment is EventBookingConfirmFragment) fragment.hitAPIForConfirm(result);
                }

            }
        }
    }
    //Changes By Himanshu Ends
}
