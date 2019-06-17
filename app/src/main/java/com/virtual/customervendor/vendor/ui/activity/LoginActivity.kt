package com.virtual.customervendor.vendor.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.virtual.customervendor.MyApp
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.activity.DashBoardActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.request.LoginRequest
import com.virtual.customervendor.model.response.CountrycodeResponse
import com.virtual.customervendor.model.response.LoginResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login_vendor.*


class LoginActivity : BaseActivity(), View.OnClickListener {
    var apiInterface: ApiInterface? = null
    private val TAG = LoginActivity::class.java.name
    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_login -> {
                validateField()
            }
            R.id.btn_forgotpass -> {
                startActivity(Intent(this, ForgotActivity::class.java))
                SlideAnimationUtill.slideNextAnimation(this)
            }
            R.id.signup -> {
                startActivity(Intent(this, SignUpActivity::class.java))
                SlideAnimationUtill.slideNextAnimation(this)
            }
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_vendor)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        init()
        setToolBar()
        getCountryCode()
    }

    fun init() {
        btn_login.setOnClickListener(this)
        btn_forgotpass.setOnClickListener(this)
        signup.setOnClickListener(this)
        setTextColor()
//        ed_mobile.setText("9015401900")
//        ed_pass.setText("12345qQ@")
    }

    fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.login)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }

    fun validateField() {
//        if (ed_code.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_code, getString(R.string.field_required))
//            return
//        }
//        if (til_code.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_code)
//        }
        if (ed_mobile.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_mobile, getString(R.string.rgs_sign_in_empty_mobile_text))
            return
        }
        if (!UiValidator.isValidPhoneNumber(ed_mobile.getText().toString())) {
            UiValidator.setValidationError(til_mobile, getString(R.string.rgs_sign_up_invalid_mobileno))
            return
        }
        if (til_mobile.isErrorEnabled()) {
            UiValidator.disableValidationError(til_mobile)
        }

        if (ed_pass.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_pass, getString(R.string.sign_in_empty_password_text))
            return
        }
//        if (!UiValidator.isValidPassword(ed_pass.getText().toString())) {
//            UiValidator.setValidationError(til_pass, getString(R.string.sign_up_invalid_password))
//            return
//        }
        if (til_pass.isErrorEnabled()) {
            UiValidator.disableValidationError(til_pass)
        }

        UiValidator.hideSoftKeyboard(this)

        if (AppUtils.isInternetConnected(this)) {
            signIn()
        } else {
            UiValidator.displayMsgSnack(cordinate, this, getString(R.string.no_internet_connection))
        }

    }

    fun setTextColor() {
        val colorText = "Need an account?" + " <u><font color='#022E8D'>SignUp</font></u>"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            signup.text = Html.fromHtml(colorText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            signup.text = Html.fromHtml(colorText)
        }
    }


    private fun signIn() {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            var loginRequest = LoginRequest()
            loginRequest.mobile_no = ed_mobile.text.toString()
            loginRequest.password = ed_pass.text.toString()
            loginRequest.device_type = AppConstants.DEVICE_TYPE_ANDROID
            loginRequest.device_token = SharedPreferenceManager.getFCMToken()

            loginRequest.country_code = ccp_login.selectedCountryCode

            apiInterface?.userLogin(loginRequest)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<LoginResponse> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onNext(loginResponse: LoginResponse) {
                            AppLog.e(TAG, loginResponse.toString())
                            handleResults(loginResponse)
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(cordinate, this, getString(R.string.no_internet_connection))
        }
    }

    private fun handleResults(loginResponse: LoginResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (loginResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            SharedPreferenceManager.setAuthToken(loginResponse.success.token)
            SharedPreferenceManager.setCustomerName(loginResponse.success.user.name)
            SharedPreferenceManager.setCustomerImage(loginResponse.success.user.avatar)

            SharedPreferenceManager.setCustomerMobilNo(loginResponse.success.user.mobile_no)
            SharedPreferenceManager.setCustomerEmail(loginResponse.success.user.email)
            SharedPreferenceManager.setCustomerUserId(loginResponse.success.user.id.toString())

            var countrycodeResponse = CountrycodeResponse()
            countrycodeResponse.data?.code = loginResponse.success.user.country_code
            countrycodeResponse.data?.countryCode = loginResponse.success.user.cn_code
            var countryRes = Gson().toJson(countrycodeResponse, CountrycodeResponse::class.java)
            SharedPreferenceManager.setRegisterCountryDetails(countryRes)

            SharedPreferenceManager.setUserLanguage(loginResponse.success.user.laguage)
            //      AppUtils.updateLanguageResources(applicationContext, loginResponse.success.user.laguage)
            MyApp.localeManager.setNewLocale(this, loginResponse.success.user.laguage)
//            CachingManager.setUserDetail(loginResponse.success)
            launchHomeActivity()
        } else {
            UiValidator.displayMsgSnack(cordinate, this as LoginActivity, loginResponse.message)
        }
    }


    protected override fun attachBaseContext(base: Context) {
        super.attachBaseContext(MyApp.localeManager.setLocale(base))
        Log.d(TAG, "attachBaseContext")
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null) {
            UiValidator.displayMsgSnackError(cordinate, this, t.message)
            AppLog.e(TAG, t?.message)
        }
    }

    fun launchHomeActivity() {
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

    fun getCountryCode() {
            ccp_login.resetToDefaultCountry()
    }

}