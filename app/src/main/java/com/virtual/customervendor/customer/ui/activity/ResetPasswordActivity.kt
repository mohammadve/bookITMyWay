package com.virtual.customervendor.customer.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.SlideAnimationUtill
import com.virtual.customervendor.utills.UiValidator
import kotlinx.android.synthetic.main.activity_resetpassword_vendor.*

class ResetPasswordActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_send -> {
                validateField()
            }
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpassword_customer)
        init()
        setToolBar()
    }

    fun init() {
        btn_send.setOnClickListener(this)
    }

    fun validateField() {
        var msg: String = ""
        var isValid: Boolean = false

        if (ed_newpass.text.toString().isEmpty()) {
            msg = "Password Required"
            UiValidator.setEditTextErrorBg(ed_newpass,this)
            isValid = false
        } else if (!(ed_newpass.text.toString().length > 7)) {
            msg = "Please fill at least 8 character"
            UiValidator.setEditTextErrorBg(ed_newpass,this)
            isValid = false
        } else {
            UiValidator.setEditTextBg(ed_newpass,this)
            isValid = true
        }
        if (isValid) {
            if (ed_conpass.text.toString().isEmpty()) {
                msg = "Password Required"
                UiValidator.setEditTextErrorBg(ed_conpass,this)
                isValid = false
            } else if (!(ed_conpass.text.toString().length > 7)) {
                msg = "Please fill at least 8 character"
                UiValidator.setEditTextErrorBg(ed_conpass,this)
                isValid = false
            } else if (!(ed_conpass.text.toString().equals(ed_newpass.text.toString()))) {
                msg = "Password do not match"
                UiValidator.setEditTextErrorBg(ed_conpass,this)
                isValid = false
            } else {
                UiValidator.setEditTextBg(ed_conpass,this)
                isValid = true
            }
        }

        UiValidator.hideSoftKeyboard(this)
        if (isValid) {
            if (AppUtils.isInternetConnected(this)) {
                callResetPass()
            } else {
                UiValidator.displayMsgSnack(cordinate, this, getString(R.string.no_internet_connection))
            }
        } else {
            UiValidator.displayMsgSnack(cordinate, this, msg)
        }
    }

    fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.resetpasswrd)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }


    fun callResetPass() {
        var intent: Intent = Intent(this, DashBoardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        SlideAnimationUtill.slideNextAnimation(this)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }
}