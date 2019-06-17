package com.virtual.customervendor.vendor.ui.activity

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.utills.SlideAnimationUtill
import com.virtual.customervendor.utills.UiValidator
import kotlinx.android.synthetic.main.checkin_checkout_vendor_activity.*

class VendorCheckInCheckOutActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_create -> {
                UiValidator.displayMsgSnack(coordinator,this,"Under Development")
            }
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkin_checkout_vendor_activity)
        init()
        setToolBar()
    }

    fun init() {
        btn_create.setOnClickListener(this)
    }


    fun setToolBar() {
        val appbar: AppBarLayout = findViewById<AppBarLayout>(R.id.toolbar)
        val mTitle = appbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.appoint)
        val iv_back = appbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

}