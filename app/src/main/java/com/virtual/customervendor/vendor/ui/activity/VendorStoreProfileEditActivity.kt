package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessDetail
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.StoreItemLocationModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.request.VendorStoreServiceRequest
import com.virtual.customervendor.model.response.VendorServiceDetailResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.StoreDeliverLocationReviewAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_store_profile_edit_vendor.*
import java.io.File
import java.util.*

class VendorStoreProfileEditActivity : BaseActivity(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var TAG: String = VendorStoreProfileEditActivity::class.java.simpleName
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    private var captureImageUtils: CaptureImageUtils? = null
    var imageFile: File? = null
    var homeSliderAdapter: HomeSliderAdapter? = null

    var apiInterface: ApiInterface? = null
    var businessDetail = BusinessDetail()

    var stadiumList: ArrayList<StoreItemLocationModel> = ArrayList()
    var arenaList: ArrayList<StoreItemLocationModel> = ArrayList()
    var otherList: ArrayList<StoreItemLocationModel> = ArrayList()

    var storeAddStadiumAdapter: StoreDeliverLocationReviewAdapter? = null
    var storeAddArenaAdapter: StoreDeliverLocationReviewAdapter? = null
    var storeAddOtherAdapter: StoreDeliverLocationReviewAdapter? = null

    var manager: RecyclerView.LayoutManager? = null


    var SCREEN_COUNT: Int = 0
    var timer = Timer()
    var mTimerTask: TimerTask? = null
    val handler = Handler()
    private var pagePosition = 0

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_business -> {
                callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorStoreActivity())
            }
            R.id.iv_service -> {
                callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorStoreActivity())
            }
            R.id.iv_profile -> {
                captureImageUtils?.openSelectImageFromDialog()
            }
        }
    }

    fun callEditActivity(isfromEdit: Boolean, fragmentnumber: Int, activity1: AppCompatActivity) {
        var intent: Intent = Intent(this, activity1.javaClass)
        intent.putExtra(AppConstants.FROM_EDIT, isfromEdit)
        intent.putExtra(AppConstants.FRAGMENT_NUMBER, fragmentnumber)
        startActivityForResult(intent, 112)
        SlideAnimationUtill.slideNextAnimation(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_profile_edit_vendor)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        businessDetail = intent.extras.getSerializable(AppConstants.BUSINESS_DATA) as BusinessDetail
        AppLog.e(TAG, businessDetail.toString())
//        getRestaurantData()
        hitApi(businessDetail)
        init()
    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.business_profile)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        iv_business.setOnClickListener(this)
        iv_service.setOnClickListener(this)

        swipeToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if (AppUtils.isInternetConnected(this))
                hitApi(businessDetail)
            else
                swipeToRefresh.setRefreshing(false)
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 112) {
            setRestaurantData(CachingManager.getVendorStoreInfo())
            setResult(Activity.RESULT_OK)
        }
    }

    fun setRestaurantData(healthServiceRequest: VendorStoreServiceRequest) {
        ed_bname.setText(healthServiceRequest.business_name)
        ed_bcontact.setText(resources.getString(R.string.plus)+healthServiceRequest.dial_code + " " + healthServiceRequest.business_contactno)
        ed_email.setText(healthServiceRequest.business_email)

        ed_tax.setText(healthServiceRequest.business_tax)

        var time = StringBuilder()
        if (AppUtils.getStatusBoolean(healthServiceRequest.all_day)) {
            time.append(resources.getString(R.string.all_days))
        } else {
            if (AppUtils.getStatusBoolean(healthServiceRequest.mon)) time.append(resources.getString(R.string.monday_m))

            if (AppUtils.getStatusBoolean(healthServiceRequest.tue)) {
                time.append(", " + resources.getString(R.string.tuesday_t))
            }
            if (AppUtils.getStatusBoolean(healthServiceRequest.wed)) {
                time.append(", " + resources.getString(R.string.wednesday_w))
            }
            if (AppUtils.getStatusBoolean(healthServiceRequest.thu)) {
                time.append(", " + resources.getString(R.string.thursday_t))
            }
            if (AppUtils.getStatusBoolean(healthServiceRequest.fri)) {
                time.append(", " + resources.getString(R.string.friday_f))
            }
            if (AppUtils.getStatusBoolean(healthServiceRequest.sat)) {
                time.append(", " + resources.getString(R.string.saturday_s))
            }
            if (AppUtils.getStatusBoolean(healthServiceRequest.sun)) {
                time.append(", " + resources.getString(R.string.sunday_t))
            }
        }
        ed_day.setText(time)
        if (AppUtils.getStatusBoolean(healthServiceRequest.is_24_hours_open)) {
            ed_sertime.setText(resources.getString(R.string._24_hrs))
        } else {
            ed_sertime.setText(healthServiceRequest.start_time + " - " + healthServiceRequest.close_time)
        }

        ed_region.setText(healthServiceRequest.business_region_id.regionname)
        ed_city.setText(healthServiceRequest.business_city_id.cityname)
        ed_address.setText(healthServiceRequest.business_address)
        ed_desc.setText(healthServiceRequest.description)


        if (healthServiceRequest.stadium_address != null && healthServiceRequest.stadium_address.size > 0) {
            stadiumList = healthServiceRequest.stadium_address
            txt_stadium.visibility = View.VISIBLE
            rv_stadium.visibility = View.VISIBLE
            createAdapterStadium()
        }

        if (healthServiceRequest.arena_address != null && healthServiceRequest.arena_address.size > 0) {
            arenaList = healthServiceRequest.arena_address
            txt_arena.visibility = View.VISIBLE
            rv_arena.visibility = View.VISIBLE
            createAdapterArena()
        }

        if (healthServiceRequest.other_address != null && healthServiceRequest.other_address.size > 0) {
            otherList = healthServiceRequest.other_address
            txt_other.visibility = View.VISIBLE
            rv_other.visibility = View.VISIBLE
            createAdapterOther()
        }

        if (healthServiceRequest.business_images != null && healthServiceRequest.business_images.size > 0) {
            initViewPager(healthServiceRequest.business_images)
        }
    }

    private fun createAdapterStadium() {
        storeAddStadiumAdapter = StoreDeliverLocationReviewAdapter(this@VendorStoreProfileEditActivity, AppConstants.FROM_ADDDATA, stadiumList)
        manager = LinearLayoutManager(this@VendorStoreProfileEditActivity, LinearLayoutManager.VERTICAL, false)
        rv_stadium.layoutManager = manager
        rv_stadium.adapter = (storeAddStadiumAdapter)
    }

    private fun createAdapterArena() {
        storeAddArenaAdapter = StoreDeliverLocationReviewAdapter(this@VendorStoreProfileEditActivity, AppConstants.FROM_ADDDATA, arenaList)
        manager = LinearLayoutManager(this@VendorStoreProfileEditActivity, LinearLayoutManager.VERTICAL, false)
        rv_arena.layoutManager = manager
        rv_arena.adapter = (storeAddArenaAdapter)
    }

    private fun createAdapterOther() {
        storeAddOtherAdapter = StoreDeliverLocationReviewAdapter(this@VendorStoreProfileEditActivity, AppConstants.FROM_ADDDATA, otherList)
        manager = LinearLayoutManager(this@VendorStoreProfileEditActivity, LinearLayoutManager.VERTICAL, false)
        rv_other.layoutManager = manager
        rv_other.adapter = (storeAddOtherAdapter)
    }

    private fun initViewPager(categories: ArrayList<BusinessImage>) {
        SCREEN_COUNT = categories.size
        homeSliderAdapter = HomeSliderAdapter(this@VendorStoreProfileEditActivity, categories, this, false)
        viewPager?.setAdapter(homeSliderAdapter)
        addBottomDots(0)
        startViewPagerAutoScrolling()

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                addBottomDots(position)
//                setNames(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun addBottomDots(currentPage: Int) {
        if (layoutDots != null) {
            val dots = arrayOfNulls<ImageView>(SCREEN_COUNT)
            val colorsActive = ContextCompat.getDrawable(this@VendorStoreProfileEditActivity, R.drawable.active_home_view_pager_dot)
            val colorsInactive = ContextCompat.getDrawable(this@VendorStoreProfileEditActivity, R.drawable.non_active_home_view_pager_dot)
            layoutDots.removeAllViews()
            for (i in dots.indices) {
                dots[i] = ImageView(this@VendorStoreProfileEditActivity!!)
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(5, 5, 5, 5)
                dots[i]!!.setImageDrawable(colorsInactive)
                layoutDots.addView(dots[i], params)
            }
            if (dots.size > 0)
                dots[currentPage]!!.setImageDrawable(colorsActive)
        }
    }

    private fun startViewPagerAutoScrolling() {
        mTimerTask = object : TimerTask() {
            override fun run() {
                handler.post(Runnable {
                    if (pagePosition == SCREEN_COUNT) {
                        pagePosition = 0
//                        if (mTimerTask != null)
//                            mTimerTask!!.cancel()
                    } else {
                        pagePosition = pagePosition + 1
                    }
                    if (viewPager != null)
                        viewPager?.setCurrentItem(pagePosition, true)
                })
            }
        }

        // public void schedule (TimerTask task, long delay, long period)
        timer.schedule(mTimerTask, AppConstants.BANNER_SCROLL_DELAY, 6000)  //
    }

    fun hitApi(businessDetail: BusinessDetail) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            SharedPreferenceManager.setCategoryId(businessDetail.category_id!!)
            SharedPreferenceManager.setSubcategoryId(businessDetail.subcategory_id!!)
            apiInterface?.getBusinessServiceDetail("Bearer " + SharedPreferenceManager.getAuthToken(), businessDetail.category_id!!, businessDetail.subcategory_id!!, businessDetail.service_id!!, businessDetail.business_id!!)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<VendorServiceDetailResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: VendorServiceDetailResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            handleResults(detailResponse)
                            swipeToRefresh.setRefreshing(false)
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                AppLog.e(TAG, e?.message)
                            swipeToRefresh.setRefreshing(false)
                        }

                        override fun onComplete() {}
                    })
        } else {
            retry.setText(resources.getString(R.string.no_internet_connection))
            retry.visibility = View.VISIBLE
            nest.visibility = View.GONE
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
            swipeToRefresh.setRefreshing(false)
        }
    }

    private fun handleResults(detailResponse: VendorServiceDetailResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        AppLog.e(TAG, detailResponse.data.toString())
        if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            retry.visibility = View.GONE
            nest.visibility = View.VISIBLE
            SharedPreferenceManager.setSubcategoryId(detailResponse.data.businessData.subcategory_id!!)
            SharedPreferenceManager.setCategoryId(detailResponse.data.businessData.category_id!!)
            SharedPreferenceManager.setBussinessName(detailResponse.data.businessData.business_name)
//            SharedPreferenceManager.setVendorImage(detailResponse.data.businessData.business_images.get(0).url)
            SharedPreferenceManager.setBussinessID("" + detailResponse.data.businessData.business_id)
            when (SharedPreferenceManager.getCategoryId()) {
                AppConstants.CAT_STORE_SELLER -> {
                    when (SharedPreferenceManager.getSubcategoryId()) {
                        AppConstants.SUBCAT_STORE_SELLER -> handleStoreSeller(detailResponse.data)
                    }
                }
            }

        } else {
            retry.visibility = View.VISIBLE
            nest.visibility = View.GONE
            UiValidator.displayMsgSnack(coordinator, this, detailResponse.message)
            AppLog.e(TAG, detailResponse.message)

        }
    }

    private fun handleStoreSeller(detailModel: VendorServiceDetailModel) {
        var parkingRequest = VendorStoreServiceRequest()
        if (detailModel.businessData != null) {
            parkingRequest.business_id = detailModel.businessData.business_id
            parkingRequest.business_name = detailModel.businessData.business_name
            parkingRequest.business_contactno = detailModel.businessData.contact_number
            parkingRequest.country_code = detailModel.businessData.country_code
            parkingRequest.dial_code = detailModel.businessData.dial_code
            parkingRequest.business_email = detailModel.businessData.email_id
            parkingRequest.business_city_id = detailModel.businessData.cities
            parkingRequest.business_region_id = detailModel.businessData.regions
            parkingRequest.business_address = detailModel.businessData.address
            parkingRequest.business_tax = detailModel.businessData.business_tax
            parkingRequest.business_category_id = detailModel.businessData.category_id.toString()
            parkingRequest.business_subcategory_id = detailModel.businessData.subcategory_id.toString()
            parkingRequest.image_url = detailModel.businessData.business_image
            parkingRequest.business_images = detailModel.businessData.business_images
        }
        parkingRequest.service_id = detailModel.service_id
        parkingRequest.store_category_id = detailModel.store_category_id
        parkingRequest.storecategory = detailModel.storecategory
        parkingRequest.description = detailModel.description
        parkingRequest.all_day = detailModel.all_day
        parkingRequest.sun = detailModel.sunday
        parkingRequest.mon = detailModel.monday
        parkingRequest.tue = detailModel.tuesday
        parkingRequest.wed = detailModel.wednesday
        parkingRequest.thu = detailModel.thursday
        parkingRequest.fri = detailModel.friday
        parkingRequest.sat = detailModel.saturday
        parkingRequest.is_24_hours_open = detailModel.is_24_hours_open
        parkingRequest.start_time = detailModel.start_time
        parkingRequest.close_time = detailModel.close_time
        parkingRequest.stadium_address = detailModel.stadium_address
        parkingRequest.arena_address = detailModel.arena_address
        parkingRequest.other_address = detailModel.other_address

        CachingManager.setVendorStoreInfo(parkingRequest)
        setRestaurantData(parkingRequest)
    }

}
