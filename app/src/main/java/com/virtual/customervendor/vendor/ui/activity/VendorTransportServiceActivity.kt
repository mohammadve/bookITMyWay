package com.virtual.customervendor.vendor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppKeys
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.SlideAnimationUtill
import kotlinx.android.synthetic.main.activity_transport_service.*

class VendorTransportServiceActivity : BaseActivity(), View.OnClickListener {


    var datetime: String? = null
    var manager: FragmentManager? = null

    var fragmentManager: FragmentManager? = null
    var toolbar: Toolbar? = null
    var mTitle: TextView? = null
    var mTitleFrag: String? = null
    var category: String? = null
    var TAG: String? = VendorTransportServiceActivity::class.java.simpleName

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_taxi -> {
                callActivity(AppConstants.SUBCAT_TRANS_TAXI.toString(), VendorTaxiActivity())
            }
            R.id.btn_limos -> {
                callActivity(AppConstants.SUBCAT_TRANS_LIMO.toString(), VendorLimoActivity())
            }
            R.id.btn_tour -> {
                callActivity(AppConstants.SUBCAT_TRANS_TOURBUS.toString(), VendorTourBusActivity())
            }
            R.id.btn_sight -> {
                callActivity(AppConstants.SUBCAT_TRANS_SIGHTSEEING.toString(), VendorSightSeeingActivity())
            }
//            R.id.btn_other -> {
//                onBackPressed()
//            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun callActivity(keyValue: String, activity1: AppCompatActivity) {
        var intent: Intent = Intent(this, activity1.javaClass)
        intent.putExtra(AppKeys.KEY_SUBCATEGORY, keyValue)
        startActivity(intent)
        SlideAnimationUtill.slideNextAnimation(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_service)
        setToolBar()
        category = intent.getStringExtra(AppKeys.KEY_CATEGORY)
        AppLog.e(TAG, category)
        init()
    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as Toolbar
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        btn_taxi.setOnClickListener(this)
        btn_limos.setOnClickListener(this)
        btn_tour.setOnClickListener(this)
        btn_sight.setOnClickListener(this)
        btn_other.setOnClickListener(this)

    }


    fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.choose_sub_category)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }


}
