package com.virtual.customervendor.vendor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.activity.DashBoardActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.response.ResetPassResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_resetpassword_vendor.*

class ResetPasswordActivity : BaseActivity(), View.OnClickListener {

    var apiService: ApiInterface? = null
    var userId = String()
    val TAG: String = ResetPasswordActivity::class.java.name

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_send -> {
                validateField()
//
            }
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpassword_vendor)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        userId = intent.getStringExtra(AppKeys.USER_ID)
        AppLog.e(TAG, "userId=" + userId)
        init()
        setToolBar()
    }

    fun init() {
        btn_send.setOnClickListener(this)
    }

    fun validateField() {

        if (ed_newpass.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_pass, getString(R.string.sign_in_empty_password_text))
            return
        }
        if (!UiValidator.isValidPassword(ed_newpass.getText().toString())) {
            UiValidator.setValidationError(til_pass, getString(R.string.sign_up_invalid_password))
            return
        }
        if (til_pass.isErrorEnabled()) {
            UiValidator.disableValidationError(til_pass)
        }

        if (ed_conpass.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_conpass, getString(R.string.sign_in_empty_password_text))
            return
        }
//        if (!UiValidator.isValidPassword(ed_conpass.getText().toString())) {
//            UiValidator.setValidationError(til_conpass, getString(R.string.sign_up_invalid_password))
//            return
//        }
        if (!ed_newpass.text.toString().equals(ed_conpass.text.toString())) {
            UiValidator.setValidationError(til_conpass, getString(R.string.rgs_password_not_match))
            return
        }
        if (til_conpass.isErrorEnabled()) {
            UiValidator.disableValidationError(til_conpass)
        }

        UiValidator.hideSoftKeyboard(this)
        if (AppUtils.isInternetConnected(this)) {
            hitResetPassword()
        } else {
            UiValidator.displayMsgSnack(cordinate, this, getString(R.string.no_internet_connection))
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

    private fun hitResetPassword() {
        var request: MutableMap<String, String> = mutableMapOf()
        request.put(AppKeys.USER_ID, userId)
        request.put(AppKeys.USER_PASSWORD, ed_newpass.text.toString())

        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        apiService?.resetPasword(request)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<ResetPassResponse> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(userResponse: ResetPassResponse) {
                        ProgressDialogLoader.progressDialogDismiss()
                        AppLog.e(TAG, userResponse.toString())
                        if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
                            UiValidator.displayMsgSnack(cordinate, this@ResetPasswordActivity, userResponse.message)
                            SharedPreferenceManager.setAuthToken(userResponse.token)
                            callResetPass()
                        } else {
                            UiValidator.displayMsgSnack(cordinate, this@ResetPasswordActivity, userResponse.message)
                        }

                    }

                    override fun onError(e: Throwable) {
                        handleError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t.message)
    }


}