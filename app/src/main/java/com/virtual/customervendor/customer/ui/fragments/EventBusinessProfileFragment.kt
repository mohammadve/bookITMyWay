package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.activity.EventsActivity
import com.virtual.customervendor.customer.ui.adapter.EventBusinessProfileAdapter
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.response.EventListingResponse
import com.virtual.customervendor.model.response.FollowUnfollowResponse
import com.virtual.customervendor.model.response.VendorServiceDetailResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_event_business_profile.*
import java.util.*

class EventBusinessProfileFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_follow_unfollow -> {
                if (btn_follow_unfollow.tag.equals("follow")) {
                    btn_follow_unfollow.tag = "unfollow"
                    btn_follow_unfollow.text = resources.getString(R.string.unfollow)
                    followUnfollowApi("1", customerBookingListModel.business_id!!)
                } else {
                    btn_follow_unfollow.tag = "follow"
                    btn_follow_unfollow.text = resources.getString(R.string.follow)
                    followUnfollowApi("0", customerBookingListModel.business_id!!)
                }

            }
        }
    }

    var TAG: String = EventBusinessProfileFragment::class.java.name;
    var apiService: ApiInterface? = null
    var customerBookingListModel = CustomerBookingListModel()
    var vendorServiceDetailModel = VendorServiceDetailModel()
    var eventBusinessProfileAdapter: EventBusinessProfileAdapter? = null

    var homeSliderAdapter: HomeSliderAdapter? = null
    var SCREEN_COUNT: Int = 0
    var timer = Timer()
    var mTimerTask: TimerTask? = null
    val handler = Handler()
    private var pagePosition = 0

    companion object {
        fun newInstance(): EventBusinessProfileFragment {
            val fragment = EventBusinessProfileFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_event_business_profile, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_follow_unfollow.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        customerBookingListModel = (activity as EventsActivity).getcustomerBookingListModel()
        getBusinessDetail(AppConstants.CAT_HEALTH_BODYCARE, AppConstants.SUBCAT_HEALTH_OTHER, customerBookingListModel.business_id!!, customerBookingListModel.service_id!!)
        eventListApi();
    }


    fun getBusinessDetail(category: Int, subcategory: Int, business_id: String, service_id: String) {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            apiService?.getBusinessServiceDetail("Bearer " + SharedPreferenceManager.getAuthToken(), category, subcategory, service_id.toInt(), business_id.toInt())
                    ?.subscribeOn(io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<VendorServiceDetailResponse> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onNext(detailResponse: VendorServiceDetailResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            handleResults(detailResponse)
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            cons.visibility = View.GONE
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.no_internet_connection))
        }
    }

    fun eventListApi() {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))
            apiService?.getEventListing("Bearer " + SharedPreferenceManager.getAuthToken(), customerBookingListModel.business_id!!.toInt(),AppConstants.USER_CUSTOMER)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<EventListingResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: EventListingResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                createAdapterEvents(detailResponse.eventlisting)
                            } else {
                                UiValidator.displayMsgSnack(nest, activity, detailResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.no_internet_connection))
        }
    }

    fun followUnfollowApi(status: String, business_id: String) {
        ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
        apiService?.folloUnfollow("Bearer " + SharedPreferenceManager.getAuthToken(), business_id, status)
                ?.subscribeOn(io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<FollowUnfollowResponse> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(detailResponse: FollowUnfollowResponse) {
                        AppLog.e(TAG, detailResponse.toString())
                        ProgressDialogLoader.progressDialogDismiss()
                        if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                            UiValidator.displayMsgSnack(nest, activity, detailResponse.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        handleError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }

    private fun createAdapterEvents(eventlisting: ArrayList<EventDetail>) {
        eventBusinessProfileAdapter = EventBusinessProfileAdapter(activity!!, eventlisting) { event ->
            callActivity(event)
        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_event.layoutManager = manager
        rv_event.adapter = (eventBusinessProfileAdapter)
    }

    private fun handleResults(detailResponse: VendorServiceDetailResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            SharedPreferenceManager.setSubcategoryId(detailResponse.data.businessData.subcategory_id!!)
            SharedPreferenceManager.setCategoryId(detailResponse.data.businessData.category_id!!)
            vendorServiceDetailModel = detailResponse.data
            handleTaxi(detailResponse.data)
        } else {
            cons.visibility = View.GONE
            UiValidator.displayMsgSnack(nest, activity, detailResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        cons.visibility = View.GONE
        if (t != null)
            AppLog.e(TAG, t.message)
    }

    private fun handleTaxi(detailModel: VendorServiceDetailModel) {
        if (detailModel.businessData != null) {
            ed_bname.setText(detailModel.businessData.business_name)
            ed_bcontact.setText("+" + detailModel.businessData.dial_code + " " + detailModel.businessData.contact_number)
            ed_email.setText(detailModel.businessData.email_id)
            ed_region.setText(detailModel.businessData.regions.regionname)
            ed_city.setText(detailModel.businessData.cities.cityname)
            ed_address.setText(detailModel.businessData.address)
        }
        if (detailModel.businessData.followBusiness.equals("1")) {
            btn_follow_unfollow.text = "Unfollow"
            btn_follow_unfollow.tag = "unfollow"
        } else {
            btn_follow_unfollow.text = "Follow"
            btn_follow_unfollow.tag = "follow"
        }

        if (detailModel.businessData.business_images != null && detailModel.businessData.business_images.size > 0) {
            initViewPager(detailModel.businessData.business_images)
        }
    }

    fun callActivity(dataModel: EventDetail) {
        (activity as EventsActivity).businessDetailModel = vendorServiceDetailModel
        (activity as EventsActivity).eventDetail = dataModel
        (activity as EventsActivity).setDisplayFragment(3, resources.getString(R.string.event), false)
    }

    private fun initViewPager(categories: ArrayList<BusinessImage>) {
        SCREEN_COUNT = categories.size
        homeSliderAdapter = HomeSliderAdapter(activity, categories, this, false)
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
            val colorsActive = ContextCompat.getDrawable(activity!!, R.drawable.active_home_view_pager_dot)
            val colorsInactive = ContextCompat.getDrawable(activity!!, R.drawable.non_active_home_view_pager_dot)
            layoutDots.removeAllViews()
            for (i in dots.indices) {
                dots[i] = ImageView(activity!!)
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(5, 5, 5, 5)
                dots[i]!!.setImageDrawable(colorsInactive)
                layoutDots.addView(dots[i], params)
            }
            if (dots.size > 0)
                dots[currentPage]!!.setImageDrawable(colorsActive)

            if (dots.size > 1) {
                layoutDots.visibility = View.VISIBLE
            } else {
                layoutDots.visibility = View.GONE
            }
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

}