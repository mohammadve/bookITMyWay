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
import kotlinx.android.synthetic.main.activity_book_appointment.*

class BookAppointmentsActivity : BaseActivity(), View.OnClickListener {
    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_doctor -> {
                callActivity(BookAppointmentDoctorActivity())
            }
            R.id.btn_hair -> {
                callActivity(BookAppointmentHairActivity())
            }
            R.id.btn_nails -> {
                callActivity(BookAppointmentNailActivity())
            }
            R.id.btn_phy -> {
                callActivity(BookAppointmentMassageActivity())
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)
        setToolBar()
        init()
    }

    fun init() {
        btn_doctor.setOnClickListener(this)
//        btn_dentist.setOnClickListener(this)
        btn_hair.setOnClickListener(this)
        btn_nails.setOnClickListener(this)
        btn_phy.setOnClickListener(this)
        btn_other.setOnClickListener(this)
    }

    fun setToolBar() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.appoint)
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
