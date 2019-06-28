package com.virtual.customervendor.customer.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.dialogFragment.CountryDialogFragment
import com.virtual.customervendor.customer.ui.dialogFragment.RegionDialogFragmentSingle
import com.virtual.customervendor.customer.ui.dialogFragment.TimeDialogFragment
import com.virtual.customervendor.customer.ui.fragments.*
import com.virtual.customervendor.listener.RegionListner
import com.virtual.customervendor.model.*
import com.virtual.customervendor.model.request.CustomerTimeSlotRequest
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.SlideAnimationUtill

class TableBookingActivity : BaseActivity(), View.OnClickListener, RegionDialogFragmentSingle.SingleRegionSelectionInterface, TimeDialogFragment.timeSelectionInterface, CountryDialogFragment.countrySelectionInterface {
    var countryDialogFragment: CountryDialogFragment? = null


    var listner: RegionListner? = null;
    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var manager: FragmentManager? = null
    var TAG: String = TableBookingActivity::class.java.simpleName
    var detailResponse: VendorServiceDetailModel = VendorServiceDetailModel()
    var fieldmap: MutableMap<String, String> = mutableMapOf()

    var customerBookingListModel: CustomerBookingListModel = CustomerBookingListModel()
    var orderNo = String()
    var timeDialogFragment: TimeDialogFragment? = null
    var customerTimeSlotRequest: CustomerTimeSlotRequest = CustomerTimeSlotRequest()
    var applyOfferModel = ApplyOfferModel()

    var regionDialogFragmentsingle: RegionDialogFragmentSingle? = null

    var isFromSearch: Boolean = false
    var searchModel = SearchModel()
    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    var toolbar: Toolbar? = null
    var mTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_booking)

        isFromSearch = intent.getBooleanExtra("isFromSearch", false)
        if (isFromSearch)
            searchModel = intent.getSerializableExtra(AppConstants.BUSINESS_DATA) as SearchModel
        AppLog.e(TAG, isFromSearch.toString() + searchModel.toString())
        init()
    }

    fun init() {
        frameLayout = findViewById<FrameLayout>(R.id.flContentnew)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)

        if (isFromSearch) {
            customerBookingListModel.business_id = searchModel.busines_id
            customerBookingListModel.service_id = searchModel.service_id
            customerBookingListModel.business_name = searchModel.business_name
            setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else
            setDisplayFragment(1, resources.getString(R.string.book_table), false)

    }

    public fun getFieldMap(): MutableMap<String, String> {
        return fieldmap
    }

    fun setRegionListner(listner: RegionListner) {
        this.listner = listner
    }

    fun showRegionSelectionDialog(from: String) {
        regionDialogFragmentsingle = RegionDialogFragmentSingle.newInstance(from, "all")
        manager = supportFragmentManager
        regionDialogFragmentsingle!!.show(manager, "My Dialog")
    }

    fun showTimeSelectionDialog(date: String) {
        customerTimeSlotRequest.business_id = customerBookingListModel.business_id
        customerTimeSlotRequest.service_id = customerBookingListModel.service_id
        customerTimeSlotRequest.category_id = AppConstants.CAT_RESTAURANT_DINNIG.toString()
        customerTimeSlotRequest.subcategory_id = AppConstants.SUBCAT_RESTAURANT_DINNIG.toString()
        customerTimeSlotRequest.date = date

        timeDialogFragment = TimeDialogFragment.newInstance(customerTimeSlotRequest)
        manager = supportFragmentManager
        timeDialogFragment!!.show(manager, "My Dialog")
    }

    fun showCountrySelectionDialog() {
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
                if (fragment is RestaurantsFragmentNew) fragment.updateSelectedCountry(bean)
            }
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


    fun setDisplayFragment(number: Int, title: String, removeStack: Boolean) {
        setTitleBar(title)
        when (number) {
            1 -> replaceFragment(RestaurantsFragmentNew.newInstance(), removeStack)
            2 -> replaceFragment(RestaurantBookingFragmentSeviceDetail.newInstance(), removeStack)
            3 -> replaceFragment(RestaurantBookingMenuSevice.newInstance(), removeStack)
            4 -> replaceFragment(RestaurantBookingInfromationFragment.newInstance(), removeStack)
            5 -> replaceFragment(RestaurantBookingFragmentFinal.newInstance(), removeStack)
            6 -> replaceFragment(BookingFragmentConfirm.newInstance(), removeStack)
        }
    }

    override fun onBackPressed() {
        handleBackPress()
    }

    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
    }

    fun handleBackPress() {
        when {
            fragmentManager!!.findFragmentById(R.id.flContentnew) is RestaurantsFragmentNew -> {
                finish()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is RestaurantBookingFragmentSeviceDetail -> {
                if (!isFromSearch) {
                    setTitleBar(resources.getString(R.string.book_table))
                    fragmentManager!!.popBackStackImmediate()
                } else {
                    finish()
                }
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is RestaurantBookingMenuSevice -> {
                setTitleBar(resources.getString(R.string.business_profile))
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is RestaurantBookingInfromationFragment -> {
                setTitleBar(resources.getString(R.string.menu))
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is RestaurantBookingFragmentFinal -> {
                applyOfferModel.order_price =""
                setTitleBar(resources.getString(R.string.booking_information))
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is BookingFragmentConfirm -> {
                finish()
            }

        }
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun getcustomerBookingListModel(): CustomerBookingListModel {
        return customerBookingListModel
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1111) {
            if (resultCode == Activity.RESULT_OK) {

                val result = data?.getStringExtra("card_token") as String
                //  setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)
                val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
                if (fragment != null && fragment.isVisible) {
                    if (fragment is RestaurantBookingFragmentFinal) fragment.hitAPIForConfirm(result);
                }
            }
        }
    }

    override fun selectedTimeUpdate(bean: CustomerTimeModel, fromWhere: String?, cityResponse: ArrayList<CustomerTimeModel>) {
        AppLog.e(TAG, bean.toString())
        if (timeDialogFragment != null) {
            timeDialogFragment!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is RestaurantBookingInfromationFragment) fragment.updateSelectedTime(bean)
            }
        }
    }

    override fun regionSelectionInterface(bean: RegionModel, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        if (regionDialogFragmentsingle != null) {
            regionDialogFragmentsingle!!.dismiss()
            val fragment = manager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is RestaurantsFragmentNew) fragment.updateSelectedRegion(bean)
            }
        }

    }

}
