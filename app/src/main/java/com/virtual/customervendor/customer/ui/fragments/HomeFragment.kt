package com.virtual.customervendor.customer.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClickedCustomer
import com.virtual.customervendor.customer.ui.activity.*
import com.virtual.customervendor.customer.ui.adapter.HomeSliderCustomerAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BannerModel
import com.virtual.customervendor.model.response.CustomerBannerListResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home_customer.*
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener, ViewPagerItemClickedCustomer {
    override fun onPagerItemClicked(position: BannerModel) {

//        if (position != null && !(position.link?.isEmpty()!!)) {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(position.link))
//            startActivity(intent)
//        } else {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
//            startActivity(intent)
//        }
    }

    var list: ArrayList<BannerModel> = java.util.ArrayList()
    var homeSliderAdapter: HomeSliderCustomerAdapter? = null
    var SCREEN_COUNT: Int = 0
    var timer = Timer()
    var mTimerTask: TimerTask? = null
    val handler = Handler()
    private var pagePosition = 0
    var dots: Array<ImageView>? = null
    var apiService: ApiInterface? = null
    val TAG: String = HomeFragment::class.java.simpleName


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_table -> {
                startActivity(Intent(activity, TableBookingActivity::class.java))
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
            R.id.btn_valet -> {
                startActivity(Intent(activity, ParkingValetActivity::class.java))
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
            R.id.btn_ride -> {
                startActivity(Intent(activity, BookRideActivity::class.java))
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
            R.id.btn_appoint -> {
                startActivity(Intent(activity, BookAppointmentsActivity::class.java))
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
            R.id.btn_purchase -> {
                startActivity(Intent(activity, PurchaseItemsActivity::class.java))
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
            R.id.btn_event -> {
                startActivity(Intent(activity, EventsActivity::class.java))
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
            R.id.img_upload2 -> bannerClick(list.get(1).link!!)
            R.id.img_upload3 -> bannerClick(list.get(2).link!!)
            R.id.img_upload4 -> bannerClick(list.get(3).link!!)
            R.id.img_upload5 -> bannerClick(list.get(4).link!!)
        }
    }

    private fun initViewPager(categories: ArrayList<BannerModel>) {
        SCREEN_COUNT = categories.size
        homeSliderAdapter = HomeSliderCustomerAdapter(activity, categories, this, false)
        homeViewPager?.setAdapter(homeSliderAdapter)
        addBottomDots(0)
        startViewPagerAutoScrolling()

        homeViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
            val colorsActive = ContextCompat.getDrawable(context!!, R.drawable.active_home_view_pager_dot)
            val colorsInactive = ContextCompat.getDrawable(context!!, R.drawable.non_active_home_view_pager_dot)
            layoutDots.removeAllViews()
            for (i in dots.indices) {
                dots[i] = ImageView(context!!)
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(5, 5, 5, 5)
                dots[i]!!.setImageDrawable(colorsInactive)
                layoutDots.addView(dots[i], params)
            }
            if (dots.size > 0)
                dots[currentPage]!!.setImageDrawable(colorsActive)
        }
    }

    /**
     * This method used to start auto switch pages in view pager
     */
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
                    if (homeViewPager != null)
                        homeViewPager?.setCurrentItem(pagePosition, true)
                })
            }
        }

        // public void schedule (TimerTask task, long delay, long period)
        timer.schedule(mTimerTask, AppConstants.BANNER_SCROLL_DELAY, 6000)  //
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_home_customer, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        getBannerList()

    }

    fun initView(view: View) {
        btn_table.setOnClickListener(this)
        btn_ride.setOnClickListener(this)
        btn_appoint.setOnClickListener(this)
        btn_purchase.setOnClickListener(this)
        btn_valet.setOnClickListener(this)
        btn_event.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
    }

    fun getBannerList() {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))
            apiService?.getCustomerBannerList("Bearer " + SharedPreferenceManager.getAuthToken())
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<CustomerBannerListResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: CustomerBannerListResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            handleResult(detailResponse)
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

    fun handleError(e: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (e != null)
            UiValidator.displayMsg(activity, e.message)
    }

    fun handleResult(detailResponse: CustomerBannerListResponse) {
        ProgressDialogLoader.progressDialogDismiss()

        if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            SharedPreferenceManager.setCustomerName(detailResponse.user_name)
            SharedPreferenceManager.setCustomerImage(detailResponse.profile_pic)

        try {
            (context as DashBoardActivity).updateProfile()
            initViewPager(detailResponse.data)
            list = detailResponse.data
            activity?.runOnUiThread {
                if (list.size > 1) {
                    if (img_upload2 != null) {
                        img_upload2.visibility = View.VISIBLE
                        AppUtill.loadImageHomeBanner(activity!!, list.get(1).url!!, img_upload2)
//                        Glide.with(activity!!).load(list.get(1).url).into(img_upload2)
                        img_upload2.setOnClickListener(this)
                    }
                }
                if (list.size > 2) {
                    img_upload3.visibility = View.VISIBLE
                    AppUtill.loadImageHomeBanner(activity!!, list.get(2).url!!, img_upload3)
//                    Glide.with(activity!!).load(list.get(2).url).into(img_upload3)
                    img_upload3.setOnClickListener(this)
                }
                if (list.size > 3) {
                    img_upload4.visibility = View.VISIBLE
                    AppUtill.loadImageHomeBanner(activity!!, list.get(3).url!!, img_upload4)
//                    Glide.with(activity!!).load(list.get(3).url).into(img_upload4)
                    img_upload4.setOnClickListener(this)
                }
                if (list.size > 4) {
                    img_upload5.visibility = View.VISIBLE
                    AppUtill.loadImageHomeBanner(activity!!, list.get(4).url!!, img_upload5)
//                    Glide.with(activity!!).load(list.get(4).url).into(img_upload5)
                    img_upload5.setOnClickListener(this)
                }
            }
        }catch (ex : Exception){

        }
        } else {
            UiValidator.displayMsg(activity, detailResponse.message)
        }

    }

    fun bannerClick(link: String) {
//        var intent = Intent()
//        if (!link.isEmpty()) {
//            intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
//        } else {
//            intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
//        }
//        startActivity(intent)
    }

}