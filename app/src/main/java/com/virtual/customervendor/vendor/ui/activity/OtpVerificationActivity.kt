package com.virtual.customervendor.vendor.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.telephony.SmsMessage
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.UserData
import com.virtual.customervendor.model.response.CountrycodeResponse
import com.virtual.customervendor.model.response.ForgotOtpVerifificationResponse
import com.virtual.customervendor.model.response.OtpVerificationResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_otpverification_vendor.*

class OtpVerificationActivity : BaseActivity(), View.OnClickListener {
    var isForgetPassword: Boolean = false
    var mobileno = String()
    var apiService: ApiInterface? = null
    val TAG: String = OtpVerificationActivity::class.java.name

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_next -> {
                validateField()
            }
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverification_vendor)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        isForgetPassword = intent.getBooleanExtra(AppConstants.FROM_FORGOT, false)
        if (isForgetPassword)
            mobileno = intent.getStringExtra(AppKeys.MOBILE_NO)
        AppLog.e(TAG, "Mobileno =" + mobileno + " isForgetPassword=" + isForgetPassword)
        init()
        setToolBar()

    }

    fun init() {
        btn_next.setOnClickListener(this)
    }

    fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.verification)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }

    fun validateField() {
        var msg: String? = null
        var otp: String? = null
        var isValid: Boolean? = null
        UiValidator.hideSoftKeyboard(this)
        if (ed_pin1.text.toString().length < 4) {
            msg = "Please fill otp"
            isValid = false
        } else {
            otp = ed_pin1.text.toString()
            isValid = true
        }
        if (isValid) {
            if (AppUtils.isInternetConnected(this)) {
                if (isForgetPassword) {
                    verifyUserForgot(otp!!)
                } else {
                    verifyUser(otp!!)
                }
            } else {
                UiValidator.displayMsgSnack(coordinator, applicationContext, getString(R.string.no_internet_connection))
            }
        } else {
            UiValidator.displayMsgSnack(coordinator, this, msg)
        }
    }

    public override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        startBroadcastListener()

    }

    override fun onPause() {
        super.onPause()
        stopBroadcastListener()

    }

    fun callNext() {
        if (isForgetPassword) {
            val intent = Intent(this@OtpVerificationActivity, ResetPasswordActivity::class.java)
            intent.putExtra(AppKeys.USER_EMAIL, SharedPreferenceManager.getUserEmail())
            startActivity(intent)
            SlideAnimationUtill.slideNextAnimation(this)
        } else {
            val intent = Intent(this@OtpVerificationActivity, SetLanguageActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            SlideAnimationUtill.slideNextAnimation(this)
        }
    }

    private fun verifyUser(otp: String) {
        val userData: UserData = CachingManager.getUserSignupDetail()
        var request: MutableMap<String, String> = mutableMapOf()
        request.put(AppKeys.SYSTEM_OTP, userData.systemotp)
        request.put(AppKeys.OTP, otp)
        request.put(AppKeys.USER_NAME, userData.name)
        request.put(AppKeys.USER_EMAIL, userData.email)
        request.put(AppKeys.COUNTRY_CODE, userData.country_code)
        request.put(AppKeys.CN_CODE, userData.cn_code)
        request.put(AppKeys.USER_TYPE, AppConstants.USER_TYPE_CUSTOMER)
        request.put(AppKeys.MOBILE_NO, userData.mobile_no)
        request.put(AppKeys.DEVICE_TYPE, AppConstants.DEVICE_TYPE_ANDROID)
        request.put(AppKeys.DEVICE_TOKEN, SharedPreferenceManager.getFCMToken())
        if (userData.imageUrl != null)
            request.put(AppKeys.USER_PROFILE_PIC, "" + userData.imageUrl!!)
        request.put(AppKeys.USER_PASSWORD, userData.password)

        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        apiService?.verifyOtp(request)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<OtpVerificationResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(userResponse: OtpVerificationResponse) {
                        handleResults(userResponse)
                        AppLog.e(TAG, userResponse.toString())
                    }

                    override fun onError(e: Throwable) {
                        handleError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }

    private fun verifyUserForgot(otp: String) {
        var request: MutableMap<String, String> = mutableMapOf()
        request.put(AppKeys.OTP, otp)
        request.put(AppKeys.MOBILE_NO, mobileno)

        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        apiService?.verifyOtpForgot(request)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<ForgotOtpVerifificationResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(userResponse: ForgotOtpVerifificationResponse) {
                        ProgressDialogLoader.progressDialogDismiss()
                        AppLog.e(TAG, userResponse.toString())
                        if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
                            val intent = Intent(this@OtpVerificationActivity, ResetPasswordActivity::class.java)
                            intent.putExtra(AppKeys.USER_ID, userResponse.user_id)
                            startActivity(intent)
                            SlideAnimationUtill.slideNextAnimation(this@OtpVerificationActivity)
                        } else {
                            UiValidator.displayMsgSnack(coordinator, this@OtpVerificationActivity, userResponse.message)
                        }

                    }

                    override fun onError(e: Throwable) {
                        handleError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }


    private fun handleResults(userResponse: OtpVerificationResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
            SharedPreferenceManager.setAuthToken(userResponse.data.token)
            SharedPreferenceManager.setCustomerName(userResponse.data.user.name)
            SharedPreferenceManager.setCustomerImage(userResponse.data.user.avatar)

            SharedPreferenceManager.setCustomerMobilNo(userResponse.data.user.mobile_no)
            SharedPreferenceManager.setCustomerEmail(userResponse.data.user.email)
            SharedPreferenceManager.setCustomerUserId(userResponse.data.user.id.toString())

            var countrycodeResponse = CountrycodeResponse()
            countrycodeResponse.data?.code = userResponse.data.user.country_code
            countrycodeResponse.data?.countryCode = userResponse.data.user.cn_code
            var countryRes = Gson().toJson(countrycodeResponse, CountrycodeResponse::class.java)

            SharedPreferenceManager.setRegisterCountryDetails(countryRes)
            callNext()
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t.message)
    }

    internal var smsReciever: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras
            try {
                if (bundle != null) {
                    val pdusObj = bundle.get("pdus") as Array<Any>
                    for (i in pdusObj.indices) {
                        val currentMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                        val message = currentMessage.displayMessageBody
                        if (message.contains("activate your account")) {
                            val numVerificationCode = message.replace("\\D+".toRegex(), "")
//                            hitApiToVerifyOtp(numVerificationCode)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SmsReceiver", "Exception smsReceiver$e")
            }
        }
    }

    internal fun startBroadcastListener() {
        val filter = IntentFilter()
        filter.addAction(android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        registerReceiver(smsReciever, filter)
    }

    internal fun stopBroadcastListener() {
        try {
            unregisterReceiver(smsReciever)
        } catch (e: Exception) {
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }
}