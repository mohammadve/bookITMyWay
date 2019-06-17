package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
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
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.request.VendorParkingRequest
import com.virtual.customervendor.model.response.VendorServiceDetailResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_parking_profile_edit_vendor.*
import java.io.File
import java.util.*

class VendorParkingProfileEditActivity : BaseActivity(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var TAG: String = VendorParkingProfileEditActivity::class.java.simpleName
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    private var captureImageUtils: CaptureImageUtils? = null
    var imageFile: File? = null
    var homeSliderAdapter: HomeSliderAdapter? = null
    var apiInterface: ApiInterface? = null
    var businessDetail = BusinessDetail()

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
                if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_PARKING_VALET) {
                    callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorParkingValetActivity())
                }
            }
            R.id.iv_service -> {
                if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_PARKING_VALET) {
                    callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorParkingValetActivity())
                }
            }
        }
    }

    fun callEditActivity(isfromEdit: Boolean, fragmentnumber: Int, activity1: AppCompatActivity) {
        var intent: Intent = Intent(this, activity1.javaClass)
        intent.putExtra(AppConstants.FROM_EDIT, isfromEdit)
        intent.putExtra(AppConstants.FRAGMENT_NUMBER, fragmentnumber)
        startActivityForResult(intent,112)
        SlideAnimationUtill.slideNextAnimation(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_profile_edit_vendor)
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
        mTitle!!.text = getString(R.string.bussiness_information)
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
            setParkingData(CachingManager.getVendorParkingInfo())
            setResult(Activity.RESULT_OK)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            captureImageUtils?.onRequestPermission(requestCode, permissions, grantResults)
        }
    }




    fun setParkingData(parkingRequest: VendorParkingRequest) {
        ed_bname.setText(parkingRequest.business_name)
        ed_bcontact.setText(resources.getString(R.string.plus)+parkingRequest.dial_code+" "+parkingRequest.business_contactno)
        ed_email.setText(parkingRequest.business_email)
        ed_tax.setText(parkingRequest.business_tax)

        var time = StringBuilder()
        if (AppUtils.getStatusBoolean(parkingRequest.all_day)) {
            time.append(resources.getString(R.string.all_days))
        } else {
            if (AppUtils.getStatusBoolean(parkingRequest.mon)) {
                time.append(resources.getString(R.string.monday_m))
            }
            if (AppUtils.getStatusBoolean(parkingRequest.tue)) {
                time.append(", " + resources.getString(R.string.tuesday_t))
            }
            if (AppUtils.getStatusBoolean(parkingRequest.wed)) {
                time.append(", " + resources.getString(R.string.wednesday_w))
            }
            if (AppUtils.getStatusBoolean(parkingRequest.thu)) {
                time.append(", " + resources.getString(R.string.thursday_t))
            }
            if (AppUtils.getStatusBoolean(parkingRequest.fri)) {
                time.append(", " + resources.getString(R.string.friday_f))
            }
            if (AppUtils.getStatusBoolean(parkingRequest.sat)) {
                time.append(", " + resources.getString(R.string.saturday_s))
            }
            if (AppUtils.getStatusBoolean(parkingRequest.sun)) {
                time.append(", " + resources.getString(R.string.sunday_t))
            }
        }

        ed_day.setText(time)

        if (AppUtils.getStatusBoolean(parkingRequest.is_24_hours_open)) {
            ed_sertime.setText(resources.getString(R.string._24_hrs))
        } else {
            ed_sertime.setText(parkingRequest.start_time + " - " + parkingRequest.close_time)
        }

        ed_region.setText(parkingRequest.business_region_id.regionname)
        ed_city.setText(parkingRequest.business_city_id.cityname)
        ed_address.setText(parkingRequest.business_address)
        ed_capacity.setText(parkingRequest.parking_capacity)
        ed_rate.setText(AppUtils.getRegisterRateWithSymbol(parkingRequest.parking_charges) )
        ed_desc.setText(parkingRequest.description)

        if (parkingRequest.business_images != null && parkingRequest.business_images.size > 0) {
            initViewPager(parkingRequest.business_images)
        }

    }

    private fun initViewPager(categories: ArrayList<BusinessImage>) {
        SCREEN_COUNT = categories.size
        homeSliderAdapter = HomeSliderAdapter(this@VendorParkingProfileEditActivity, categories, this, false)
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
            val colorsActive = ContextCompat.getDrawable(this@VendorParkingProfileEditActivity, R.drawable.active_home_view_pager_dot)
            val colorsInactive = ContextCompat.getDrawable(this@VendorParkingProfileEditActivity, R.drawable.non_active_home_view_pager_dot)
            layoutDots.removeAllViews()
            for (i in dots.indices) {
                dots[i] = ImageView(this@VendorParkingProfileEditActivity!!)
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
                AppConstants.CAT_PARKING_VALET -> {
                    when (SharedPreferenceManager.getSubcategoryId()) {
                        AppConstants.SUBCAT_PARKING_VALET -> handleParking(detailResponse.data)
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
    private fun handleParking(detailModel: VendorServiceDetailModel) {
        var parkingRequest = VendorParkingRequest()
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
        parkingRequest.parking_charges = detailModel.parking_charges
        parkingRequest.parking_capacity = detailModel.parking_capacity
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
        parkingRequest.fri = detailModel.friday

        CachingManager.setVendorParkingInfo(parkingRequest)

        setParkingData(parkingRequest)
    }

}
