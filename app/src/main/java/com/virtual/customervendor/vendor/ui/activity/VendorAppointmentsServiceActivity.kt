package com.virtual.customervendor.vendor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppKeys
import com.virtual.customervendor.utills.SlideAnimationUtill
import kotlinx.android.synthetic.main.activity_appointments_service_vendor.*

class VendorAppointmentsServiceActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.iv_back -> onBackPressed()
            R.id.btn_doctor -> callActivity(AppConstants.SUBCAT_HEALTH_DOCTOR.toString(), VendorDoctorActivity())
            R.id.btn_hair -> callActivity(AppConstants.SUBCAT_HEALTH_HAIR.toString(), VendorHairActivity())
            R.id.btn_nails -> callActivity(AppConstants.SUBCAT_HEALTH_NAIL.toString(), VendorNailsActivity())
            R.id.btn_phy -> callActivity(AppConstants.SUBCAT_HEALTH_PHYSIO.toString(), VendorMassagePhysioActivity())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments_service_vendor)
        setToolBar()
        init()
    }

    fun callActivity(keyValue: String, activity1: AppCompatActivity) {
        var intent: Intent = Intent(this, activity1.javaClass)
        intent.putExtra(AppKeys.KEY_SUBCATEGORY, keyValue)
        startActivity(intent)
        SlideAnimationUtill.slideNextAnimation(this)
    }


    fun init() {
        btn_doctor.setOnClickListener(this)
        btn_hair.setOnClickListener(this)
        btn_nails.setOnClickListener(this)
        btn_phy.setOnClickListener(this)
        btn_other.setOnClickListener(this)
    }


    fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.choose_sub_category)
        iv_back.setOnClickListener(this)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }
}
