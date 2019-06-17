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
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.BodyCareServiceMenuAdapter
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.*
import com.virtual.customervendor.model.request.VendorHealthServiceRequest
import com.virtual.customervendor.model.response.VendorServiceDetailResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_health_profile_edit_vendor.*
import java.io.File
import java.util.*


class VendorHealthProfileEditActivity : BaseActivity(), View.OnClickListener, ViewPagerItemClicked {
    var TAG: String = VendorHealthProfileEditActivity::class.java.simpleName
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
                if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_DOCTOR) {
                    callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorDoctorActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_DENTIST) {
//                    callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorDentistActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_HAIR) {
                    callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorHairActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_NAIL) {
                    callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorNailsActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_PHYSIO) {
                    callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorMassagePhysioActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_OTHER) {
                    callEditActivity(true, AppConstants.FRAGMENT_ONE, VendorMassagePhysioActivity())
                }
            }
            R.id.iv_service -> {
                if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_DOCTOR) {
                    callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorDoctorActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_DENTIST) {
//                    callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorDentistActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_HAIR) {
                    callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorHairActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_NAIL) {
                    callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorNailsActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_PHYSIO) {
                    callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorMassagePhysioActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_OTHER) {
                    callEditActivity(true, AppConstants.FRAGMENT_TWO, VendorMassagePhysioActivity())
                }
            }
            R.id.iv_servicemenu -> {
                if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_DOCTOR) {
                    callEditActivity(true, AppConstants.FRAGMENT_THREE, VendorDoctorActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_HAIR) {
                    callEditActivity(true, AppConstants.FRAGMENT_THREE, VendorHairActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_NAIL) {
                    callEditActivity(true, AppConstants.FRAGMENT_THREE, VendorNailsActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_PHYSIO) {
                    callEditActivity(true, AppConstants.FRAGMENT_THREE, VendorMassagePhysioActivity())
                } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_HEALTH_OTHER) {
                    callEditActivity(true, AppConstants.FRAGMENT_THREE, VendorMassagePhysioActivity())
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
        setContentView(R.layout.activity_health_profile_edit_vendor)
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
        iv_servicemenu.setOnClickListener(this)

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
            setRestaurantData(CachingManager.getVendorDoctorInfo())
            setResult(Activity.RESULT_OK)
        }
    }


    fun setRestaurantData(healthServiceRequest: VendorHealthServiceRequest) {
        ed_bname.setText(healthServiceRequest.business_name)
        ed_bcontact.setText(resources.getString(R.string.plus)+healthServiceRequest.dial_code + " " + healthServiceRequest.business_contactno)
        ed_email.setText(healthServiceRequest.business_email)
        ed_fees.setText(AppUtils.getRegisterRateWithSymbol(healthServiceRequest.fees_per_visit))
        ed_tax.setText(healthServiceRequest.business_tax)

        var time = StringBuilder()
        if (AppUtils.getStatusBoolean(healthServiceRequest.all_day)) {
            time.append(resources.getString(R.string.all_days))
        } else {
            if (AppUtils.getStatusBoolean(healthServiceRequest.mon)) {
                time.append(resources.getString(R.string.monday_m))
            }
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
        ed_region.setText(healthServiceRequest.business_region_id.regionname + ", " + healthServiceRequest.business_city_id.cityname)
        ed_address.setText(healthServiceRequest.business_address)
        var data = StringBuilder()
        for (regionModel: TimeSlotModel in healthServiceRequest.visiting_hours_slot) {
            if (data.length > 0) {
                data.append("\n")
            }
            data.append(regionModel.fromTime + " to " + regionModel.toTime)
        }
        ed_sertime.setText(data)
        ed_desc.setText(healthServiceRequest.description)
        ed_person.setText(healthServiceRequest.required_person_per_hr)
        ed_slottime.setText(healthServiceRequest.time_slot)

        if (healthServiceRequest.business_images != null && healthServiceRequest.business_images.size > 0) {
            initViewPager(healthServiceRequest.business_images)
        }

        if (healthServiceRequest.service_menu != null && healthServiceRequest.service_menu.size > 0) {
            txt_servicemenu.visibility = View.VISIBLE
            rv_timing.visibility = View.VISIBLE
            iv_servicemenu.visibility = View.VISIBLE
            createAdapterView(healthServiceRequest.service_menu)
        }
    }


    private fun createAdapterView(timeList: ArrayList<ItemPriceModel>) {
        var serviceAdapter = BodyCareServiceMenuAdapter(this,  timeList)
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_timing.layoutManager = manager
        rv_timing.adapter = (serviceAdapter)
    }

    private fun initViewPager(categories: ArrayList<BusinessImage>) {
        SCREEN_COUNT = categories.size
        homeSliderAdapter = HomeSliderAdapter(this@VendorHealthProfileEditActivity, categories, this, false)
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
            val colorsActive = ContextCompat.getDrawable(this@VendorHealthProfileEditActivity, R.drawable.active_home_view_pager_dot)
            val colorsInactive = ContextCompat.getDrawable(this@VendorHealthProfileEditActivity, R.drawable.non_active_home_view_pager_dot)
            layoutDots.removeAllViews()
            for (i in dots.indices) {
                dots[i] = ImageView(this@VendorHealthProfileEditActivity!!)
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

    override fun onPagerItemClicked(position: Int) {
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
                            UiValidator.displayMsgSnack(coordinator,this@VendorHealthProfileEditActivity,detailResponse.message)
                            AppLog.e(TAG, detailResponse.toString())
                            nest.visibility = View.VISIBLE
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
                AppConstants.CAT_HEALTH_BODYCARE -> {
                    when (SharedPreferenceManager.getSubcategoryId()) {
                        AppConstants.SUBCAT_HEALTH_DOCTOR -> handleHealthBodyCare(detailResponse.data)
                        AppConstants.SUBCAT_HEALTH_HAIR -> handleHealthBodyCare(detailResponse.data)
                        AppConstants.SUBCAT_HEALTH_NAIL -> handleHealthBodyCare(detailResponse.data)
                        AppConstants.SUBCAT_HEALTH_PHYSIO -> handleHealthBodyCare(detailResponse.data)
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

    private fun handleHealthBodyCare(detailModel: VendorServiceDetailModel) {
        var healthServiceRequest = VendorHealthServiceRequest()
        if (detailModel.businessData != null) {
            healthServiceRequest.business_name = detailModel.businessData.business_name
            healthServiceRequest.business_contactno = detailModel.businessData.contact_number
            healthServiceRequest.country_code = detailModel.businessData.country_code
            healthServiceRequest.dial_code = detailModel.businessData.dial_code
            healthServiceRequest.business_email = detailModel.businessData.email_id
            healthServiceRequest.business_city_id = detailModel.businessData.cities
            healthServiceRequest.business_region_id = detailModel.businessData.regions
            healthServiceRequest.business_address = detailModel.businessData.address
            healthServiceRequest.business_tax = detailModel.businessData.business_tax
            healthServiceRequest.business_category_id = detailModel.businessData.category_id.toString()
            healthServiceRequest.business_subcategory_id = detailModel.businessData.subcategory_id.toString()
            healthServiceRequest.business_id = detailModel.businessData.business_id
            healthServiceRequest.image_url = detailModel.businessData.business_image
            healthServiceRequest.business_images = detailModel.businessData.business_images
        }
        healthServiceRequest.fees_per_visit = detailModel.fees_per_visit
        healthServiceRequest.visiting_hours_slot = detailModel.visiting_hours_slot
        healthServiceRequest.service_id = detailModel.service_id
        healthServiceRequest.description = detailModel.description
        healthServiceRequest.required_person_per_hr = detailModel.required_person_per_hr
        healthServiceRequest.time_slot = detailModel.time_slot
        healthServiceRequest.all_day = detailModel.all_day
        healthServiceRequest.sun = detailModel.sunday
        healthServiceRequest.mon = detailModel.monday
        healthServiceRequest.tue = detailModel.tuesday
        healthServiceRequest.wed = detailModel.wednesday
        healthServiceRequest.thu = detailModel.thursday
        healthServiceRequest.fri = detailModel.friday
        healthServiceRequest.sat = detailModel.saturday
        healthServiceRequest.fri = detailModel.friday
        healthServiceRequest.service_menu = detailModel.service_menu

        healthServiceRequest.speciality = detailModel.speciality
        healthServiceRequest.business_specialit = detailModel.business_specialit

        CachingManager.setVendorDoctorInfo(healthServiceRequest)
        setRestaurantData(healthServiceRequest)
    }


}
