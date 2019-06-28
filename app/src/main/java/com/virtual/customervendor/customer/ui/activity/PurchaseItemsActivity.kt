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
import com.virtual.customervendor.customer.ui.dialogFragment.StoreListFragment
import com.virtual.customervendor.customer.ui.dialogFragment.StoreServiceAreaFragment
import com.virtual.customervendor.customer.ui.fragments.*
import com.virtual.customervendor.model.*
import com.virtual.customervendor.utills.*
import kotlinx.android.synthetic.main.activity_purchase_items_customer.*

class PurchaseItemsActivity : BaseActivity(), View.OnClickListener, RegionDialogFragmentSingle.SingleRegionSelectionInterface, StoreListFragment.categorySelectionInterface,
        CountryDialogFragment.countrySelectionInterface, StoreServiceAreaFragment.StoreServiceInterface {


    var countryDialogFragment: CountryDialogFragment? = null

    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var toolbar: Toolbar? = null
    var fram_cart: FrameLayout? = null
    var tv_cart_value: TextView? = null
    var mTitle: TextView? = null
    var titleData: String? = null
    var regionDialogFragmentsingle: RegionDialogFragmentSingle? = null
    var storeServiceAreaFragment: StoreServiceAreaFragment? = null
    var categoryListFragment: StoreListFragment? = null
    var businessDetailModel: VendorServiceDetailModel = VendorServiceDetailModel()
    var customerBookingListModel: CustomerBookingListModel = CustomerBookingListModel();
    var fieldmap: MutableMap<String, String> = mutableMapOf()
    var applyOfferModel = ApplyOfferModel()
    var productName = String()
    var categoryName = String()
    var productCategoryModel = ProductCategoryModel()
    var itemPriceModel = ItemPriceStoreModel()
    var addedItemsListNew: ArrayList<ItemPriceStoreModel> = ArrayList<ItemPriceStoreModel>()
    var TAG: String = PurchaseItemsActivity::class.java.name;
    var isFromSearch: Boolean = false
    var searchModel = SearchModel()
    var dataList = ArrayList<StoreItemLocationModel>()

    var manager: FragmentManager? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_notification -> {
                AppLog.e(TAG, addedItemsListNew.toString())
                if (addedItemsListNew.size > 0) {
                    setDisplayFragment(6, resources.getString(R.string.cart), false)
                } else {
                    UiValidator.displayMsgSnack(coordinator, this, resources.getString(R.string.cart_empty))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_items_customer)

        isFromSearch = intent.getBooleanExtra("isFromSearch", false)
        if (isFromSearch)
            searchModel = intent.getSerializableExtra(AppConstants.BUSINESS_DATA) as SearchModel
        AppLog.e(TAG, isFromSearch.toString() + searchModel.toString())
        initView()
    }


    fun initView() {
        frameLayout = findViewById(R.id.flContentnew) as FrameLayout
        toolbar = findViewById(R.id.toolbar) as Toolbar
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        fram_cart = toolbar!!.findViewById(R.id.fram_cart) as FrameLayout
        var iv_notification = toolbar!!.findViewById(R.id.iv_notification) as ImageView
        tv_cart_value = toolbar!!.findViewById(R.id.tv_cart_value) as TextView
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        iv_notification.setOnClickListener(this)

        if (isFromSearch) {
            customerBookingListModel.business_id = searchModel.busines_id
            customerBookingListModel.service_id = searchModel.service_id
            customerBookingListModel.business_name = searchModel.business_name
            setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else
            setDisplayFragment(1, resources.getString(R.string.stores), false)


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
        mTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left1, R.anim.slide_from_left, R.anim.slide_to_right).replace(frameLayout!!.id, fragment!!).commit()
    }

    fun setDisplayFragment(number: Int, title: String, removeStack: Boolean) {
        setTitleBar(title)
        setCartVisible(false)
        when (number) {
            1 -> replaceFragment(StoreListingFragment.newInstance(), removeStack)
            2 -> replaceFragment(BookingFragmentSeviceDetail.newInstance(), removeStack)
            3 -> replaceFragment(StoreItemsListingFragment.newInstance(), removeStack)
            4 -> replaceFragment(StoreProductListingFragment.newInstance(), removeStack)
            5 -> replaceFragment(StoreProductDetailFragment.newInstance(), removeStack)
            6 -> replaceFragment(StoreCartFragment.newInstance(), removeStack)
            7 -> replaceFragment(StoreDeliveryInformationFragment.newInstance(), removeStack)
            8 -> replaceFragment(StoreConfirmOrderFragment.newInstance(), removeStack)
            9 -> replaceFragment(BookingFragmentConfirm.newInstance(), removeStack)
        }

    }

    override fun onBackPressed() {
        handleBackPress()
    }

    fun setDisplayDialog(number: Int, title: String) {
        when (number) {
            7 -> showRegionSelectionDialogSingle(title)
            8 -> showCategorySelectionDialog(title)
            9 -> showCountrySelectionDialog()
        }
    }

    fun setDisplayDialogServiceArea(number: Int, title: String) {
        when (number) {

            7 -> showStoreServiceArea(title)

        }
    }

    fun setTitleBar(titleName: String) {
        mTitle!!.text = titleName
    }

    fun handleBackPress() {
        when {
            fragmentManager!!.findFragmentById(R.id.flContentnew) is StoreListingFragment -> finish()
            fragmentManager!!.findFragmentById(R.id.flContentnew) is BookingFragmentSeviceDetail -> {
                if (!isFromSearch) {
                    setTitleBar(resources.getString(R.string.stores))
                    fragmentManager!!.popBackStackImmediate()
                } else finish()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is StoreItemsListingFragment -> {
                setTitleBar(resources.getString(R.string.business_profile))

                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is StoreProductListingFragment -> {
                setTitleBar(resources.getString(R.string.store_items))
                setCartVisible(false)
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is StoreProductDetailFragment -> {
                setTitleBar(categoryName)
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is StoreCartFragment -> {

                setTitleBar(productName)
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is StoreDeliveryInformationFragment -> {
                applyOfferModel.order_price = ""
                setTitleBar(resources.getString(R.string.cart))
                fragmentManager!!.popBackStackImmediate()
            }
            fragmentManager!!.findFragmentById(R.id.flContentnew) is StoreConfirmOrderFragment -> {
                setTitleBar(resources.getString(R.string.delivery_information))
                fragmentManager!!.popBackStackImmediate()
            }

            fragmentManager!!.findFragmentById(R.id.flContentnew) is BookingFragmentConfirm -> finish()
        }
        SlideAnimationUtill.slideBackAnimation(this)
    }

    private fun showRegionSelectionDialogSingle(from: String) {
        regionDialogFragmentsingle = RegionDialogFragmentSingle.newInstance(from, "all")
        fragmentManager = supportFragmentManager
        regionDialogFragmentsingle!!.show(fragmentManager, "My Dialog")
    }

    private fun showStoreServiceArea(from: String) {
        dataList.clear()
        dataList.addAll(businessDetailModel.stadium_address)
        dataList.addAll(businessDetailModel.arena_address)
        dataList.addAll(businessDetailModel.other_address)
        storeServiceAreaFragment = StoreServiceAreaFragment.newInstance(dataList)
        fragmentManager = supportFragmentManager
        storeServiceAreaFragment!!.show(fragmentManager, "My Dialog")
    }

    private fun showCategorySelectionDialog(from: String) {
        categoryListFragment = StoreListFragment.newInstance(from)
        fragmentManager = supportFragmentManager
        categoryListFragment!!.show(fragmentManager, "My Dialog")
    }


    override fun storeSelectionInterface(bean: StoreItemLocationModel) {
        storeServiceAreaFragment!!.dismiss()
        val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
        if (fragment != null && fragment.isVisible) {
            if (fragment is StoreDeliveryInformationFragment) fragment.updateSelectedRegion(bean)
        }
    }

    override fun regionSelectionInterface(bean: RegionModel, fromWhere: String?) {
        if (regionDialogFragmentsingle != null) {
            regionDialogFragmentsingle!!.dismiss()
            val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is StoreListingFragment) fragment.updateSelectedRegion(bean)
            }
        }
    }

    override fun catSelectionInterface(bean: StoreCategoryModel, fromWhere: String?) {
        if (categoryListFragment != null) {
            categoryListFragment!!.dismiss()
            val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
            if (fragment != null && fragment.isVisible) {
                if (fragment is StoreListingFragment) fragment.updateSelectedCategory(bean)
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
                if (fragment is StoreListingFragment) fragment.updateSelectedCountry(bean)
            }
        }
    }

    fun getbusinessDetailModel(): VendorServiceDetailModel {
        return businessDetailModel
    }

    fun getcustomerBookingListModel(): CustomerBookingListModel {
        return customerBookingListModel
    }

    fun setCartVisible(value: Boolean) {
        if (value)
            fram_cart!!.visibility = View.VISIBLE
        else
            fram_cart!!.visibility = View.GONE
    }

    fun setCartValueVisible(value: Boolean) {
        if (addedItemsListNew.size > 0) {
            if (value)
                tv_cart_value!!.visibility = View.VISIBLE
            else
                tv_cart_value!!.visibility = View.GONE

            tv_cart_value!!.text = addedItemsListNew.size.toString()
        }
    }

    fun getAddedCartItems(): ArrayList<ItemPriceStoreModel> {
        return addedItemsListNew
    }

    fun getFieldMap(): MutableMap<String, String> {
        return fieldmap
    }
    //Changes By Himanshu Starts

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1115) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getStringExtra("card_token") as String
                val fragment = fragmentManager!!.findFragmentById(R.id.flContentnew)
                if (fragment != null && fragment.isVisible) {
                    if (fragment is StoreConfirmOrderFragment) fragment.hitAPIForConfirm(result);
                }
            }
        }
    }
    //Changes By Himanshu Ends
}