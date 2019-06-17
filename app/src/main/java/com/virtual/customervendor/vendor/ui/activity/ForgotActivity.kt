package com.virtual.customervendor.vendor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.model.response.ForgotResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forgot_vendor.*

class ForgotActivity : BaseActivity(), View.OnClickListener {

    var apiInterface: ApiInterface? = null
    val TAG = ForgotActivity::class.java.name
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
        setContentView(R.layout.activity_forgot_vendor)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()
        setToolBar()
    }

    fun init() {
        btn_send.setOnClickListener(this)
    }

    fun validateField() {
        UiValidator.hideSoftKeyboard(this)
        if (ed_mobile.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_mobile, getString(R.string.rgs_sign_up_empty_email))
            return
        }
        if (!UiValidator.isValidEmail(ed_mobile.getText().toString())) {
            UiValidator.setValidationError(til_mobile, getString(R.string.rgs_sign_up_invalid_email))
            return
        }
        if (til_mobile.isErrorEnabled()) {
            UiValidator.disableValidationError(til_mobile)
        }
        hitForgot()
    }

    fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.forgot_password1)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }


    fun callForgot() {
        var intent: Intent = Intent(this, OtpVerificationActivity::class.java)
        intent.putExtra(AppConstants.FROM_FORGOT, true)
        intent.putExtra(AppKeys.MOBILE_NO, ed_mobile.getText().toString())
        startActivity(intent)
        SlideAnimationUtill.slideNextAnimation(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    private fun hitForgot() {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        apiInterface?.userForgot(ed_mobile.getText().toString())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : io.reactivex.Observer<ForgotResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(forgotResponse: ForgotResponse) {
                        handleResults(forgotResponse)
                        AppLog.e(TAG, forgotResponse.toString())
                    }

                    override fun onError(e: Throwable) {
                        handleError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }

    private fun handleResults(forgotResponse: ForgotResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        UiValidator.displayMsgSnack(cordinate, this, forgotResponse.message)
        if (forgotResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            callForgot()
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null) {
            AppLog.e(TAG, t.message)
            UiValidator.displayMsgSnack(cordinate, this, t.message)
        }
    }

}