package com.virtual.customervendor.vendor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.utills.SlideAnimationUtill
import kotlinx.android.synthetic.main.activity_application_status.*

class ApplicationStatusActivity : BaseActivity(), View.OnClickListener {

    var TAG: String = ApplicationStatusActivity::class.java.name
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_gotit -> {
                var intent: Intent = Intent(this, DashBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                SlideAnimationUtill.slideNextAnimation(this)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_status)
        init()
        setToolBar()
    }

    fun init() {
        btn_gotit.setOnClickListener(this)
    }

    fun setToolBar() {
        val appbar: AppBarLayout = findViewById<AppBarLayout>(R.id.toolbar)
        val mTitle = appbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.application_status)
        val iv_back = appbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }
}


