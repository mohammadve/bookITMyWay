package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.activity.*
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.*
import com.virtual.customervendor.model.response.FollowUnfollowResponse
import com.virtual.customervendor.model.response.VendorServiceDetailResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.StoreDeliverLocationReviewAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking_service_detail.*
import java.util.*

class BookingFragmentSeviceDetail : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_next -> {
                if (activity is BookRideTaxiActivity) {
                    (activity as BookRideTaxiActivity).serviceDetailModel = vendorServiceDetailModel
                    (activity as BookRideTaxiActivity).setDisplayFragment(3, resources.getString(R.string.booking_information), false)
                } else if (activity is BookRideLimoActivity) {
                    (activity as BookRideLimoActivity).serviceDetailModel = vendorServiceDetailModel
                    (activity as BookRideLimoActivity).setDisplayFragment(3, resources.getString(R.string.booking_information), false)
                } else if (activity is BookRideTourBusActivity) {
                    (activity as BookRideTourBusActivity).serviceDetailModel = vendorServiceDetailModel
                    (activity as BookRideTourBusActivity).setDisplayFragment(3, resources.getString(R.string.booking_information), false)
                } else if (activity is BookAppointmentDoctorActivity) {
                    (activity as BookAppointmentDoctorActivity).businessDetailModel = vendorServiceDetailModel
                    (activity as BookAppointmentDoctorActivity).setDisplayFragment(3, resources.getString(R.string.booking_information), false)
                } else if (activity is PurchaseItemsActivity) {
                    (activity as PurchaseItemsActivity).businessDetailModel = vendorServiceDetailModel
                    (activity as PurchaseItemsActivity).setDisplayFragment(3, resources.getString(R.string.store_items), false)
                }
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

    var TAG: String = BookingFragmentSeviceDetail::class.java.name;
    var apiService: ApiInterface? = null
    var customerBookingListModel = CustomerBookingListModel()
    var vendorServiceDetailModel = VendorServiceDetailModel()

    var homeSliderAdapter: HomeSliderAdapter? = null
    var SCREEN_COUNT: Int = 0
    var timer = Timer()
    var mTimerTask: TimerTask? = null
    val handler = Handler()
    private var pagePosition = 0


    var stadiumList: ArrayList<StoreItemLocationModel> = ArrayList()
    var arenaList: ArrayList<StoreItemLocationModel> = ArrayList()
    var otherList: ArrayList<StoreItemLocationModel> = ArrayList()

    var storeAddStadiumAdapter: StoreDeliverLocationReviewAdapter? = null
    var storeAddArenaAdapter: StoreDeliverLocationReviewAdapter? = null
    var storeAddOtherAdapter: StoreDeliverLocationReviewAdapter? = null

    var manager: RecyclerView.LayoutManager? = null


    companion object {
        fun newInstance(): BookingFragmentSeviceDetail {
            val fragment = BookingFragmentSeviceDetail()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_service_detail, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)
        btn_follow_unfollow.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)

        if (activity is BookRideTaxiActivity) {
            customerBookingListModel = (activity as BookRideTaxiActivity).getcustomerBookingListModel()
            getRestaurents(AppConstants.CAT_TRANSPORTATION, AppConstants.SUBCAT_TRANS_TAXI, customerBookingListModel.business_id!!, customerBookingListModel.service_id!!)
        } else if (activity is BookRideLimoActivity) {
            customerBookingListModel = (activity as BookRideLimoActivity).getcustomerBookingListModel()
            getRestaurents(AppConstants.CAT_TRANSPORTATION, AppConstants.SUBCAT_TRANS_LIMO, customerBookingListModel.business_id!!, customerBookingListModel.service_id!!)
        } else if (activity is BookRideTourBusActivity) {
            customerBookingListModel = (activity as BookRideTourBusActivity).getcustomerBookingListModel()
            getRestaurents(AppConstants.CAT_TRANSPORTATION, AppConstants.SUBCAT_TRANS_TOURBUS, customerBookingListModel.business_id!!, customerBookingListModel.service_id!!)
        } else if (activity is BookAppointmentDoctorActivity) {
            customerBookingListModel = (activity as BookAppointmentDoctorActivity).getcustomerBookingListModel()
            getRestaurents(AppConstants.CAT_HEALTH_BODYCARE, AppConstants.SUBCAT_HEALTH_DOCTOR, customerBookingListModel.business_id!!, customerBookingListModel.service_id!!)
        } else if (activity is PurchaseItemsActivity) {
            customerBookingListModel = (activity as PurchaseItemsActivity).getcustomerBookingListModel()
            getRestaurents(AppConstants.CAT_STORE_SELLER, AppConstants.SUBCAT_STORE_SELLER, customerBookingListModel.business_id!!, customerBookingListModel.service_id!!)
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

                        override fun onComplete() {

                        }
                    })
        } else {
            cons.visibility = View.GONE
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.no_internet_connection))
        }

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
                            UiValidator.displayMsgSnack(nest, activity, detailResponse.message)
//                        if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
//                            Toast.makeText(activity, detailResponse.message, Toast.LENGTH_SHORT).show();
//                        }
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
        if (t != null) {
            UiValidator.displayMsgSnack(nest, activity, t.message)
        }
    }

    private fun handleTaxi(detailModel: VendorServiceDetailModel) {
        if (detailModel != null && detailModel.businessData != null) {
            ed_bname.setText(detailModel.businessData.business_name)
            ed_bcontact.setText("+" + detailModel.businessData.dial_code + " " + detailModel.businessData.contact_number)
            ed_email.setText(detailModel.businessData.email_id)
            ed_region.setText(detailModel.businessData.regions.regionname)
            ed_address.setText(detailModel.businessData.address)
            ed_city.setText(detailModel.businessData.cities.cityname)
        }
        ed_desc.setText(detailModel.description)

        setDaySlots(detailModel)

        if (detailModel.businessData.followBusiness.equals("1")) {
            btn_follow_unfollow.tag = resources.getString(R.string.unfollow)
            btn_follow_unfollow.text = resources.getString(R.string.unfollow)
        } else {
            btn_follow_unfollow.tag = resources.getString(R.string.follow)
            btn_follow_unfollow.text = resources.getString(R.string.follow)
        }

        if (activity is BookAppointmentDoctorActivity) {
            slottime.visibility = View.VISIBLE
            serviceregion.visibility = View.GONE
            t_vehiclecap.setText(resources.getString(R.string.avg_person_per_slot))
            ed_vehiclecap.text = detailModel.required_person_per_hr

            t_rate.setText(resources.getString(R.string.fees_per_visit))
            ed_rate.text = AppUtils.getRateWithSymbol(detailModel.fees_per_visit)

            ed_slottime.text = detailModel.time_slot

        } else if (activity is PurchaseItemsActivity) {
            rate.visibility = View.GONE
            vehiclecap.visibility = View.GONE

            if (detailModel.stadium_address != null && detailModel.stadium_address.size > 0) {
                stadiumList = detailModel.stadium_address
                txt_stadium.visibility = View.VISIBLE
                rv_stadium.visibility = View.VISIBLE
                txt_servicearea.visibility = View.VISIBLE
                createAdapterStadium()
            }

            if (detailModel.arena_address != null && detailModel.arena_address.size > 0) {
                arenaList = detailModel.arena_address
                txt_arena.visibility = View.VISIBLE
                rv_arena.visibility = View.VISIBLE
                txt_servicearea.visibility = View.VISIBLE
                createAdapterArena()
            }

            if (detailModel.other_address != null && detailModel.other_address.size > 0) {
                otherList = detailModel.other_address
                txt_other.visibility = View.VISIBLE
                rv_other.visibility = View.VISIBLE
                txt_servicearea.visibility = View.VISIBLE
                createAdapterOther()
            }

        } else if (activity is BookRideTaxiActivity || activity is BookRideLimoActivity
                || activity is BookRideTourBusActivity) {
            serviceregion.visibility = View.VISIBLE
            vehicle.visibility = View.VISIBLE
            ed_vehicle.text = detailModel.total_taxi
            ed_vehiclecap.text = detailModel.avg_seat_per_taxi
            ed_rate.text = AppUtils.getRateWithSymbol(detailModel.rate_per_km)
        } else {
            ed_rate.text = AppUtils.getRateWithSymbol(detailModel.rate_per_km) + " " + resources.getString(R.string.per_km)
        }

        var time = StringBuilder()
        if (AppUtils.getStatusBoolean(detailModel.all_day)) {
            time.append(resources.getString(R.string.all_days))
        } else {
            if (AppUtils.getStatusBoolean(detailModel.monday)) {
                time.append(resources.getString(R.string.monday_m))
            }
            if (AppUtils.getStatusBoolean(detailModel.tuesday)) {
                time.append(", " + resources.getString(R.string.tuesday_t))
            }
            if (AppUtils.getStatusBoolean(detailModel.wednesday)) {
                time.append(", " + resources.getString(R.string.wednesday_w))
            }
            if (AppUtils.getStatusBoolean(detailModel.thursday)) {
                time.append(", " + resources.getString(R.string.thursday_t))
            }
            if (AppUtils.getStatusBoolean(detailModel.friday)) {
                time.append(", " + resources.getString(R.string.friday_f))
            }
            if (AppUtils.getStatusBoolean(detailModel.saturday)) {
                time.append(", " + resources.getString(R.string.saturday_s))
            }
            if (AppUtils.getStatusBoolean(detailModel.sunday)) {
                time.append(", " + resources.getString(R.string.sunday_t))
            }
        }
        ed_day.setText(time)

        if (activity is BookAppointmentDoctorActivity) {
            var data = StringBuilder()
            for (regionModel: TimeSlotModel in detailModel.visiting_hours_slot) {
                if (data.length > 0) {
                    data.append("\n")
                }
                data.append(regionModel.fromTime + " to " + regionModel.toTime)
            }
            ed_sertime.setText(data)
        } else {
            if (AppUtils.getStatusBoolean(detailModel.is_24_hours_open)) {
                ed_sertime.setText(resources.getString(R.string._24_hrs))
            } else {
                ed_sertime.setText(detailModel.start_time + " - " + detailModel.close_time)
            }
        }


        if (activity is PurchaseItemsActivity) {
            ed_serviceregion.visibility = View.GONE
            btn_next.text = resources.getString(R.string.go_to_store)
        } else {
            var data = StringBuilder()
            for (regionModel: RegionModel in detailModel.region_ids) {
                if (data.length > 0) {
                    data.append(", ")
                }
                data.append(regionModel.regionname)
            }
            ed_serviceregion.setText(data)
        }



        if (detailModel.businessData.business_images != null && detailModel.businessData.business_images.size > 0) {
            initViewPager(detailModel.businessData.business_images)
        }
    }


    fun setDaySlots(taxi_Service_Request: VendorServiceDetailModel){
        var isAllDay: Boolean=AppUtils.getStatusBoolean(taxi_Service_Request.all_day)
        if(taxi_Service_Request.dateTime.size==0){
//            if(taxi_Service_Request.monday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Monday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.monday) ,taxi_Service_Request.monday_time))
//            if(taxi_Service_Request.tuesday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Tuesday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.tuesday) ,taxi_Service_Request.tuesday_time))
//            if(taxi_Service_Request.wednesday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Wednesday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.wednesday) ,taxi_Service_Request.wednesday_time))
//            if(taxi_Service_Request.thursday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Thursday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.thursday) ,taxi_Service_Request.thursday_time))
//            if(taxi_Service_Request.friday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Friday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.friday) ,taxi_Service_Request.friday_time))
//            if(taxi_Service_Request.saturday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Saturday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.saturday) ,taxi_Service_Request.saturday_time))
//            if(taxi_Service_Request.sunday_time.size>0)
            taxi_Service_Request.dateTime.add(DayAviliability("Sunday",if(isAllDay) true else AppUtils.getStatusBoolean(taxi_Service_Request.sunday) ,taxi_Service_Request.sunday_time))
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

    private fun createAdapterStadium() {
        storeAddStadiumAdapter = StoreDeliverLocationReviewAdapter(activity!!, AppConstants.FROM_ADDDATA, stadiumList)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_stadium.layoutManager = manager
        rv_stadium.adapter = (storeAddStadiumAdapter)
    }

    private fun createAdapterArena() {
        storeAddArenaAdapter = StoreDeliverLocationReviewAdapter(activity!!, AppConstants.FROM_ADDDATA, arenaList)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_arena.layoutManager = manager
        rv_arena.adapter = (storeAddArenaAdapter)
    }

    private fun createAdapterOther() {
        storeAddOtherAdapter = StoreDeliverLocationReviewAdapter(activity!!, AppConstants.FROM_ADDDATA, otherList)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_other.layoutManager = manager
        rv_other.adapter = (storeAddOtherAdapter)
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