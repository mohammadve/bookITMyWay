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
import com.virtual.customervendor.model.*
import com.virtual.customervendor.model.request.Ven_Taxi_Service_Request
import com.virtual.customervendor.model.response.VendorServiceDetailResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_taxi_profile_edit_vendor.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class VenTaxiProfileEditActivity : BaseActivity(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }


    var TAG: String = VenTaxiProfileEditActivity::class.java.simpleName
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
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
                if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_TRANS_TAXI) {
                    callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorTaxiActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_TRANS_LIMO) {
                    callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorLimoActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_TRANS_TOURBUS) {
                    callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorTourBusActivity())
                }
            }
            R.id.iv_service -> {
                if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_TRANS_TAXI) {
                    callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorTaxiActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_TRANS_LIMO) {
                    callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorLimoActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_TRANS_TOURBUS) {
                    callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorTourBusActivity())
                }
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
        setContentView(R.layout.activity_taxi_profile_edit_vendor)
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
//        iv_profile.setOnClickListener(this)
        iv_business.setOnClickListener(this)
        iv_service.setOnClickListener(this)
        swipeToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if (AppUtils.isInternetConnected(this))
                hitApi(businessDetail)
            else
                swipeToRefresh.setRefreshing(false)

        })
    }

    fun setDaySlots(taxi_Service_Request: Ven_Taxi_Service_Request){
        var isAllDay: Boolean=AppUtils.getStatusBoolean(taxi_Service_Request.all_day)
        if(taxi_Service_Request.dateTime.size==0){
//            if(taxi_Service_Request.monday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Monday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.mon) ,taxi_Service_Request.monday_time))
//            if(taxi_Service_Request.tuesday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Tuesday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.tue) ,taxi_Service_Request.tuesday_time))
//            if(taxi_Service_Request.wednesday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Wednesday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.wed) ,taxi_Service_Request.wednesday_time))
//            if(taxi_Service_Request.thursday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Thursday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.thu) ,taxi_Service_Request.thursday_time))
//            if(taxi_Service_Request.friday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Friday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.fri) ,taxi_Service_Request.friday_time))
//            if(taxi_Service_Request.saturday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Saturday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.sat) ,taxi_Service_Request.saturday_time))
//            if(taxi_Service_Request.sunday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Sunday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.sun) ,taxi_Service_Request.sunday_time))
        }
        txtSlotsMon.setText(getSlots(taxi_Service_Request.monday_time))
        txtSlotsTue.setText(getSlots(taxi_Service_Request.tuesday_time))
        txtSlotsWed.setText(getSlots(taxi_Service_Request.wednesday_time))
        txtSlotsThu.setText(getSlots(taxi_Service_Request.thursday_time))
        txtSlotsFri.setText(getSlots(taxi_Service_Request.friday_time))
        txtSlotsSat.setText(getSlots(taxi_Service_Request.saturday_time))
        txtSlotsSun.setText(getSlots(taxi_Service_Request.sunday_time))
    }

    private fun getSlots(slots: ArrayList<DayAviliability.TimeSlot>): String {
        val builder = StringBuilder()

        for (timeSlots in slots) {
            if(timeSlots.startTime.length>0 && timeSlots.stopTime.length>0  )
                builder.append(timeSlots.startTime+" to "+timeSlots.stopTime+"\n")
        }
        var str:String=builder.toString()

        if(str.equals(""))
            str="none"
        else
            str=str.substring(0,str.length-1)

        return str
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 112) {
            setRestaurantData(CachingManager.getVendorTaxiInfo())
            setResult(Activity.RESULT_OK)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        }
    }


    fun setRestaurantData(taxi_Service_Request: Ven_Taxi_Service_Request) {
        ed_bname.setText(taxi_Service_Request.business_name)
        ed_bcontact.setText(resources.getString(R.string.plus) + taxi_Service_Request.dial_code + " " + taxi_Service_Request.business_contactno)
        ed_email.setText(taxi_Service_Request.business_email)
        ed_vehicle.setText(taxi_Service_Request.total_taxi)
        ed_vehiclecap.setText(taxi_Service_Request.avg_seat_per_taxi)
        ed_tax.setText(taxi_Service_Request.business_tax)

        var time = StringBuilder()
        if (AppUtils.getStatusBoolean(taxi_Service_Request.all_day)) {
            time.append(resources.getString(R.string.all_days))
        } else {
            if (AppUtils.getStatusBoolean(taxi_Service_Request.mon)) {
                time.append(resources.getString(R.string.monday_m))
            }
            if (AppUtils.getStatusBoolean(taxi_Service_Request.tue)) {
                time.append(", " + resources.getString(R.string.tuesday_t))
            }
            if (AppUtils.getStatusBoolean(taxi_Service_Request.wed)) {
                time.append(", " + resources.getString(R.string.wednesday_w))
            }
            if (AppUtils.getStatusBoolean(taxi_Service_Request.thu)) {
                time.append(", " + resources.getString(R.string.thursday_t))
            }
            if (AppUtils.getStatusBoolean(taxi_Service_Request.fri)) {
                time.append(", " + resources.getString(R.string.friday_f))
            }
            if (AppUtils.getStatusBoolean(taxi_Service_Request.sat)) {
                time.append(", " + resources.getString(R.string.saturday_s))
            }
            if (AppUtils.getStatusBoolean(taxi_Service_Request.sun)) {
                time.append(", " + resources.getString(R.string.sunday_t))
            }
        }
        ed_day.setText(time)

        if (AppUtils.getStatusBoolean(taxi_Service_Request.is_24_hours_open)) {
            ed_sertime.setText(resources.getString(R.string._24_hrs))
        } else {
            ed_sertime.setText(taxi_Service_Request.start_time + " - " + taxi_Service_Request.close_time)
        }

        ed_region.setText(taxi_Service_Request.business_region_id.regionname)
        ed_city.setText(taxi_Service_Request.business_city_id.cityname)
        ed_address.setText(taxi_Service_Request.business_address)
        var data = StringBuilder()
        for (regionModel: RegionModel in taxi_Service_Request.service_region_ids) {
            if (data.length > 0) {
                data.append(", ")
            }
            data.append(regionModel.regionname)
        }
        ed_serviceregion.setText(data)
        ed_rate.setText(AppUtils.getRegisterRateWithSymbol(taxi_Service_Request.rate_per_km))
        ed_desc.setText(taxi_Service_Request.description)


        if (taxi_Service_Request.business_images != null && taxi_Service_Request.business_images.size > 0) {
            initViewPager(taxi_Service_Request.business_images)
        }

        setDaySlots(taxi_Service_Request)

    }

    private fun initViewPager(categories: ArrayList<BusinessImage>) {
        SCREEN_COUNT = categories.size
        homeSliderAdapter = HomeSliderAdapter(this@VenTaxiProfileEditActivity, categories, this, false)
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
            val colorsActive = ContextCompat.getDrawable(this@VenTaxiProfileEditActivity, R.drawable.active_home_view_pager_dot)
            val colorsInactive = ContextCompat.getDrawable(this@VenTaxiProfileEditActivity, R.drawable.non_active_home_view_pager_dot)
            layoutDots.removeAllViews()
            for (i in dots.indices) {
                dots[i] = ImageView(this@VenTaxiProfileEditActivity!!)
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
                AppConstants.CAT_TRANSPORTATION -> {
                    when (SharedPreferenceManager.getSubcategoryId()) {
                        AppConstants.SUBCAT_TRANS_TAXI -> handleTaxi(detailResponse.data)
                        AppConstants.SUBCAT_TRANS_LIMO -> handleTaxi(detailResponse.data)
                        AppConstants.SUBCAT_TRANS_TOURBUS -> handleTaxi(detailResponse.data)
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

    private fun handleTaxi(detailModel: VendorServiceDetailModel) {
        var taxiinfo = Ven_Taxi_Service_Request()
        if (detailModel.businessData != null) {
            taxiinfo.business_name = detailModel.businessData.business_name
            taxiinfo.business_contactno = detailModel.businessData.contact_number
            taxiinfo.country_code = detailModel.businessData.country_code
            taxiinfo.dial_code = detailModel.businessData.dial_code
            taxiinfo.business_email = detailModel.businessData.email_id
            taxiinfo.business_id = detailModel.businessData.business_id.toString()
            taxiinfo.business_city_id = detailModel.businessData.cities
            taxiinfo.business_region_id = detailModel.businessData.regions
            taxiinfo.business_address = detailModel.businessData.address
            taxiinfo.business_tax = detailModel.businessData.business_tax
            taxiinfo.business_category_id = detailModel.businessData.category_id.toString()
            taxiinfo.business_subcategory_id = detailModel.businessData.subcategory_id.toString()
            taxiinfo.business_images = detailModel.businessData.business_images
        }
        taxiinfo.service_id = detailModel.service_id
        taxiinfo.total_taxi = detailModel.total_taxi
        taxiinfo.avg_seat_per_taxi = detailModel.avg_seat_per_taxi
        taxiinfo.rate_per_km = detailModel.rate_per_km
        taxiinfo.service_region_ids = detailModel.region_ids
        taxiinfo.description = detailModel.description
        taxiinfo.all_day = detailModel.all_day
        taxiinfo.sun = detailModel.sunday
        taxiinfo.mon = detailModel.monday
        taxiinfo.tue = detailModel.tuesday
        taxiinfo.wed = detailModel.wednesday
        taxiinfo.thu = detailModel.thursday
        taxiinfo.fri = detailModel.friday
        taxiinfo.sat = detailModel.saturday
        taxiinfo.is_24_hours_open = detailModel.is_24_hours_open
        taxiinfo.start_time = detailModel.start_time
        taxiinfo.close_time = detailModel.close_time
        taxiinfo.fri = detailModel.friday

        taxiinfo.monday_time.addAll(detailModel.monday_time)
        taxiinfo.tuesday_time.addAll(detailModel.tuesday_time)
        taxiinfo.wednesday_time.addAll(detailModel.wednesday_time)
        taxiinfo.thursday_time.addAll(detailModel.thursday_time)
        taxiinfo.friday_time.addAll(detailModel.friday_time)
        taxiinfo.saturday_time.addAll(detailModel.saturday_time)
        taxiinfo.sunday_time.addAll(detailModel.sunday_time)

        setRestaurantData(taxiinfo)

        CachingManager.setVendorTaxiInfo(taxiinfo)
    }

    override fun onResume() {
        super.onResume()
        swipeToRefresh.setRefreshing(true)
        hitApi(businessDetail)
    }


}
