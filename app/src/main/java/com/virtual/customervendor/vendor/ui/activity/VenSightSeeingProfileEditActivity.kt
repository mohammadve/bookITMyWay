package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
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
import com.virtual.customervendor.model.LocationTimeModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.request.Ven_Sight_Seeing_Service_Request
import com.virtual.customervendor.model.response.VendorServiceDetailResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sightseeing_profile_edit_vendor.*
import java.io.File
import java.util.*

class VenSightSeeingProfileEditActivity : BaseActivity(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var TAG: String = VenSightSeeingProfileEditActivity::class.java.simpleName
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
                var intent: Intent = Intent(this, VendorSightSeeingActivity::class.java)
                intent.putExtra(AppConstants.FROM_EDIT, true)
                intent.putExtra(AppConstants.FRAGMENT_NUMBER, AppConstants.FRAGMENT_ONE)
                startActivityForResult(intent, 112)
                SlideAnimationUtill.slideNextAnimation(this)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sightseeing_profile_edit_vendor)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        businessDetail = intent.extras.getSerializable(AppConstants.BUSINESS_DATA) as BusinessDetail
        AppLog.e(TAG, businessDetail.toString())
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
            setRestaurantData(CachingManager.getVendorSightSeenInfo())
            setResult(Activity.RESULT_OK)
        }
    }


    fun setRestaurantData(sight_Seeing_Service_Request: Ven_Sight_Seeing_Service_Request) {
        ed_bname.setText(sight_Seeing_Service_Request.business_name)
        ed_bcontact.setText(resources.getString(R.string.plus)+sight_Seeing_Service_Request.dial_code + " " + sight_Seeing_Service_Request.business_contactno)
        ed_email.setText(sight_Seeing_Service_Request.business_email)
        ed_tax.setText(sight_Seeing_Service_Request.business_tax)

        ed_region.setText(sight_Seeing_Service_Request.business_region_id.regionname)
        ed_city.setText(sight_Seeing_Service_Request.business_city_id.cityname)
        ed_address.setText(sight_Seeing_Service_Request.business_address)

        ed_desc.setText(sight_Seeing_Service_Request.description)

        if (sight_Seeing_Service_Request.business_images != null && sight_Seeing_Service_Request.business_images.size > 0) {
            initViewPager(sight_Seeing_Service_Request.business_images)
        }
    }

    private fun initViewPager(categories: ArrayList<BusinessImage>) {
        SCREEN_COUNT = categories.size
        homeSliderAdapter = HomeSliderAdapter(this@VenSightSeeingProfileEditActivity, categories, this, false)
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
            val colorsActive = ContextCompat.getDrawable(this@VenSightSeeingProfileEditActivity, R.drawable.active_home_view_pager_dot)
            val colorsInactive = ContextCompat.getDrawable(this@VenSightSeeingProfileEditActivity, R.drawable.non_active_home_view_pager_dot)
            layoutDots.removeAllViews()
            for (i in dots.indices) {
                dots[i] = ImageView(this@VenSightSeeingProfileEditActivity!!)
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
                        AppConstants.SUBCAT_TRANS_SIGHTSEEING -> handleSightSeeing(detailResponse.data)
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

    private fun handleSightSeeing(detailModel: VendorServiceDetailModel) {
        var sightRequest = Ven_Sight_Seeing_Service_Request()
        if (detailModel.businessData != null) {
            sightRequest.business_name = detailModel.businessData.business_name
            sightRequest.business_contactno = detailModel.businessData.contact_number
            sightRequest.country_code = detailModel.businessData.country_code
            sightRequest.dial_code = detailModel.businessData.dial_code
            sightRequest.business_email = detailModel.businessData.email_id
            sightRequest.business_id = detailModel.businessData.business_id.toString()
            sightRequest.business_city_id = detailModel.businessData.cities
            sightRequest.business_region_id = detailModel.businessData.regions
            sightRequest.business_address = detailModel.businessData.address
            sightRequest.business_tax = detailModel.businessData.business_tax
            sightRequest.business_category_id = detailModel.businessData.category_id.toString()
            sightRequest.business_subcategory_id = detailModel.businessData.subcategory_id.toString()
            sightRequest.image_url = detailModel.businessData.business_image
            sightRequest.business_images = detailModel.businessData.business_images
        }
        sightRequest.service_id = detailModel.service_id
        sightRequest.total_tour_vehicle = detailModel.total_tour_vehicle
        sightRequest.avg_seat_per_vehicle = detailModel.avg_seat_per_vehicle
        sightRequest.cost = detailModel.cost
        sightRequest.service_region_ids = detailModel.region_ids
        sightRequest.description = detailModel.description
        sightRequest.all_day = detailModel.all_day
        sightRequest.sun = detailModel.sunday
        sightRequest.mon = detailModel.monday
        sightRequest.tue = detailModel.tuesday
        sightRequest.wed = detailModel.wednesday
        sightRequest.thu = detailModel.thursday
        sightRequest.fri = detailModel.friday
        sightRequest.sat = detailModel.saturday
        sightRequest.startinfo = LocationTimeModel(detailModel.start_location!!, detailModel.start_time!!)
        sightRequest.endinfo = LocationTimeModel(detailModel.end_location!!, detailModel.end_time!!)
        sightRequest.sight_seens = detailModel.sight_seens

        CachingManager.setVendorSightSeenInfo(sightRequest)
        setRestaurantData(sightRequest)
    }

}
