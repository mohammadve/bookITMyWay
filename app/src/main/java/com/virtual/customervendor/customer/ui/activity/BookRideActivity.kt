package com.virtual.customervendor.customer.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.utills.SlideAnimationUtill
import kotlinx.android.synthetic.main.activity_book_ride.*

class BookRideActivity : BaseActivity(), View.OnClickListener {
    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_taxi -> {
                callActivity(BookRideTaxiActivity())
            }
            R.id.btn_limos -> {
                callActivity(BookRideLimoActivity())
            }
            R.id.btn_tour -> {
                callActivity(BookRideTourBusActivity())
            }
            R.id.btn_sight -> {
                callActivity(BookRideSightSeenActivity())
            }
            R.id.btn_other -> {
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_ride)
        setToolBar()
        init()
    }

    fun init() {
        btn_taxi.setOnClickListener(this)
        btn_limos.setOnClickListener(this)
        btn_tour.setOnClickListener(this)
        btn_sight.setOnClickListener(this)
        btn_other.setOnClickListener(this)
    }

    fun setToolBar() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.book_ride)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun callActivity(/*keyValue: String,*/ activity1: AppCompatActivity) {
        var intent: Intent = Intent(this, activity1.javaClass)
//        intent.putExtra(AppKeys.KEY_SUBCATEGORY, keyValue)
        startActivity(intent)
        SlideAnimationUtill.slideNextAnimation(this)
    }
}
