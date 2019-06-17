package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.activity.TableBookingActivity
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.*
import com.virtual.customervendor.model.response.FollowUnfollowResponse
import com.virtual.customervendor.model.response.VendorServiceDetailResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking_service_detail_restaurant.*
import java.util.*

class RestaurantBookingFragmentSeviceDetail : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_next -> {
                if (activity is TableBookingActivity)
                    (activity as TableBookingActivity).setDisplayFragment(3, resources.getString(R.string.menu), false)
            }
            R.id.btn_follow_unfollow -> {
                if (btn_follow_unfollow.tag.equals(resources.getString(R.string.follow))) {
                    btn_follow_unfollow.tag = resources.getString(R.string.unfollow)
                    btn_follow_unfollow.text = resources.getString(R.string.unfollow)
                    followUnfollowApi("1", customerBookingListModel.business_id!!)
                } else {
                    btn_follow_unfollow.tag = resources.getString(R.string.follow)
                    btn_follow_unfollow.text = resources.getString(R.string.follow)
                    followUnfollowApi("0", customerBookingListModel.business_id!!)
                }
            }
        }
    }

    var TAG: String = RestaurantBookingFragmentSeviceDetail::class.java.name;
    var apiService: ApiInterface? = null
    var customerBookingListModel = CustomerBookingListModel()

    var homeSliderAdapter: HomeSliderAdapter? = null
    var SCREEN_COUNT: Int = 0
    var timer = Timer()
    var mTimerTask: TimerTask? = null
    val handler = Handler()
    private var pagePosition = 0


    companion object {
        fun newInstance(): RestaurantBookingFragmentSeviceDetail {
            val fragment = RestaurantBookingFragmentSeviceDetail()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_service_detail_restaurant, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)
        btn_follow_unfollow.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)

        if (activity is TableBookingActivity) {
            customerBookingListModel = (activity as TableBookingActivity).getcustomerBookingListModel()
            getRestaurents(AppConstants.CAT_RESTAURANT_DINNIG, AppConstants.SUBCAT_RESTAURANT_DINNIG, customerBookingListModel?.business_id!!, customerBookingListModel?.service_id!!)
        }
    }


    fun getRestaurents(category: Int, subcategory: Int, business_id: String, service_id: String) {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            apiService?.getBusinessServiceDetail("Bearer " + SharedPreferenceManager.getAuthToken(), category, subcategory, service_id.toInt(), business_id.toInt())
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<VendorServiceDetailResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: VendorServiceDetailResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            handleResults(detailResponse)
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {}
                    })
        } else {
            cons.visibility = View.GONE
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.no_internet_connection))
        }

    }

    private fun handleResults(detailResponse: VendorServiceDetailResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            SharedPreferenceManager.setSubcategoryId(detailResponse.data.businessData.subcategory_id!!)
            SharedPreferenceManager.setCategoryId(detailResponse.data.businessData.category_id!!)
            (activity as TableBookingActivity).detailResponse = detailResponse.data
            setRestaurantData(detailResponse.data)
        } else {
            cons.visibility = View.GONE
            UiValidator.displayMsgSnack(cons, activity, detailResponse.message)
            AppLog.e(TAG, detailResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        cons.visibility = View.GONE
        if (t != null)
            UiValidator.displayMsgSnack(cons, activity, t.message)
    }

    fun setRestaurantData(serviceModel: VendorServiceDetailModel) {

        if (serviceModel.businessData.business_images != null && serviceModel.businessData.business_images.size > 0) {
            initViewPager(serviceModel.businessData.business_images)
        }

        ed_bname.setText(serviceModel.businessData.business_name)
        ed_bcontact.setText("+" + serviceModel.businessData.dial_code + " " + serviceModel.businessData.contact_number)
        ed_email.setText(serviceModel.businessData.email_id)
        ed_address.setText(serviceModel.businessData.address)
        ed_city.setText(serviceModel.businessData.cities.cityname)
        ed_region.setText(serviceModel.businessData.regions.regionname)
        ed_table.setText(serviceModel.total_table)
        ed_person.setText(serviceModel.seat_per_table)
        ed_avcost.setText(AppUtils.getRateWithSymbol(serviceModel.cost_per_guest))

        ed_slot.setText(serviceModel.time_slot)


        if (serviceModel.businessData.followBusiness.equals("1")) {
            btn_follow_unfollow.text = resources.getString(R.string.unfollow)
            btn_follow_unfollow.tag = resources.getString(R.string.unfollow)
        } else {
            btn_follow_unfollow.text = resources.getString(R.string.follow)
            btn_follow_unfollow.tag = resources.getString(R.string.follow)
        }
        var time = StringBuilder()
        if (AppUtils.getStatusBoolean(serviceModel.all_day)) {
            time.append(resources.getString(R.string.all_days))
        } else {
            if (AppUtils.getStatusBoolean(serviceModel.monday)) {
                time.append(resources.getString(R.string.monday_m))
            }
            if (AppUtils.getStatusBoolean(serviceModel.tuesday)) {
                time.append(", " + resources.getString(R.string.tuesday_t))
            }
            if (AppUtils.getStatusBoolean(serviceModel.wednesday)) {
                time.append(", " + resources.getString(R.string.wednesday_w))
            }
            if (AppUtils.getStatusBoolean(serviceModel.thursday)) {
                time.append(", " + resources.getString(R.string.thursday_t))
            }
            if (AppUtils.getStatusBoolean(serviceModel.friday)) {
                time.append(", " + resources.getString(R.string.friday_f))
            }
            if (AppUtils.getStatusBoolean(serviceModel.saturday)) {
                time.append(", " + resources.getString(R.string.saturday_s))
            }
            if (AppUtils.getStatusBoolean(serviceModel.sunday)) {
                time.append(", " + resources.getString(R.string.sunday_t))
            }
        }

        ed_day.setText(time)
        if (AppUtils.getStatusBoolean(serviceModel.is_24_hours_open)) {
            ed_sertime.setText(resources.getString(R.string._24_hrs))
//            time.append(" | " + resources.getString(R.string._24_hrs))
        } else {
            ed_sertime.setText(serviceModel.start_time + " - " + serviceModel.close_time)
        }


        ed_desc.setText(serviceModel.description)
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

    fun followUnfollowApi(status: String, business_id: String) {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            apiService?.folloUnfollow("Bearer " + SharedPreferenceManager.getAuthToken(), business_id, status)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<FollowUnfollowResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: FollowUnfollowResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                UiValidator.displayMsgSnack(nest, activity, detailResponse.message)
                            } else UiValidator.displayMsgSnack(nest, activity, detailResponse.message)
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.no_internet_connection))
        }
    }


}