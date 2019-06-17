package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.response.CountrycodeResponse
import com.virtual.customervendor.model.response.UserProfilePicResponse
import com.virtual.customervendor.model.response.UserResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_signup_vendor.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SignUpActivity : BaseActivity(), View.OnClickListener, CaptureImageUtils.ImageSelectionListener {
    var isFromEdit: Boolean = false
    var TAG: String = SignUpActivity::class.java.name
    var apiService: ApiInterface? = null
    var request: MutableMap<String, String> = mutableMapOf()
    private var captureImageUtils: CaptureImageUtils? = null
    var imageFile: File? = null
    var userResponse: UserResponse? = null
    var cn_code = "US"

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_next -> {
                validateField()
            }
            R.id.signin -> {
                var intent: Intent = Intent(this, com.virtual.customervendor.vendor.ui.activity.LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(Intent(intent))
                SlideAnimationUtill.slideNextAnimation(this)
            }
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.rl_profileImage -> {
                captureImageUtils?.openSelectImageFromDialog()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_vendor)
        isFromEdit = intent.getBooleanExtra(AppConstants.FROM_EDIT, false)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        AppLog.e(TAG, isFromEdit.toString())
        init()
        setToolBar()
        getCountryCode()
//
//        val string = Stream.of(STRINGS,STRINGS1).flatMap { l->l.stream() }.collect(Collectors.toList())
//        Stream.concat(STRINGS.stream(),STRINGS1.stream()).distinct()
    }

    fun init() {
        captureImageUtils = CaptureImageUtils(this, this)
        setTextColor()
        btn_next.setOnClickListener(this)
        signin.setOnClickListener(this)
        rl_profileImage.setOnClickListener(this)
        if (isFromEdit) {
            chk_term.visibility = View.GONE
            signin.visibility = View.GONE
        }
    }

    fun setToolBar() {
        val appbar: AppBarLayout = findViewById<AppBarLayout>(R.id.toolbar)
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = appbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.Sign_Up)
        val iv_back = appbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }

    fun validateField() {

        if (ed_name.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_name, getString(R.string.rgs_sign_in_empty_username_text))
            return
        }
        if (!UiValidator.isValidUserName(ed_name.getText().toString())) {
            UiValidator.setValidationError(til_name, getString(R.string.rgs_sign_up_invalid_username))
            return
        }
        if (til_name.isErrorEnabled()) {
            UiValidator.disableValidationError(til_name)
        }
//
//        if (ed_code.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_code, getString(R.string.field_required))
//            return
//        }
//
//        if (til_code.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_code)
//        }

        if (ed_mobno.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_mobno, getString(R.string.rgs_sign_in_empty_mobile_text))
            return
        }
        if (!UiValidator.isValidPhoneNumber(ed_mobno.getText().toString())) {
            UiValidator.setValidationError(til_mobno, getString(R.string.rgs_sign_up_invalid_mobileno))
            return
        }
        if (til_mobno.isErrorEnabled()) {
            UiValidator.disableValidationError(til_mobno)
        }

        if (ed_email.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_email, getString(R.string.rgs_sign_up_empty_email))
            return
        }
        if (!UiValidator.isValidEmail(ed_email.getText().toString())) {
            UiValidator.setValidationError(til_email, getString(R.string.rgs_sign_up_invalid_email))
            return
        }
        if (til_email.isErrorEnabled()) {
            UiValidator.disableValidationError(til_email)
        }

        if (ed_pass.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_pass, getString(R.string.sign_in_empty_password_text))
            return
        }
        if (!UiValidator.isValidPassword(ed_pass.getText().toString())) {
            UiValidator.setValidationError(til_pass, getString(R.string.sign_up_invalid_password))
            return
        }
        if (til_pass.isErrorEnabled()) {
            UiValidator.disableValidationError(til_pass)
        }

        if (!chk_term.isChecked) {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.rgs_terms_validations))
            return
        }

        UiValidator.hideSoftKeyboard(this)
        if (AppUtils.isInternetConnected(this)) {
            registerUser()
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }

    }

    fun registerUser() {
        request.put(AppKeys.USER_TYPE, AppConstants.USER_TYPE_CUSTOMER)
        request.put(AppKeys.USER_NAME, ed_name.text.toString())
        request.put(AppKeys.USER_PASSWORD, ed_pass.text.toString())
        request.put(AppKeys.MOBILE_NO, ed_mobno.text.toString())
        request.put(AppKeys.USER_EMAIL, ed_email.text.toString())
        //
        request.put(AppKeys.COUNTRY_CODE, ccp_login.selectedCountryCode)
        /*change surender*/
//        request.put(AppKeys.CN_CODE, cn_code)

        request.put(AppKeys.CN_CODE, ccp_login.selectedCountryNameCode)


        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService!!.userRegistration(request)!!.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<UserResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(userResponse: UserResponse) {
                            handleResults(userResponse)
                            AppLog.e(TAG, userResponse.toString())
                        }

                        override fun onError(e: Throwable) {

                            handleError(e)
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }


    fun getCountryCode() {
        ccp_login.resetToDefaultCountry()
    }


    private fun handleResults(userResponse: UserResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            this.userResponse = userResponse
            CachingManager.setUserSignupDetail(userResponse.data)


            var countrycodeResponse = CountrycodeResponse()
            countrycodeResponse.data?.code = userResponse.data.country_code
            countrycodeResponse.data?.countryCode = userResponse.data.cn_code
            var countryRes = Gson().toJson(countrycodeResponse, CountrycodeResponse::class.java)

            SharedPreferenceManager.setRegisterCountryDetails(countryRes)
            SharedPreferenceManager.setUserLanguage("en")
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
            if (imageFile != null) {
                uploadUserPicOnServer(imageFile!!)
            } else {
                callOtpScreen()
            }
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
        }
    }

    private fun handleError(e: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (e != null) {
            AppLog.e(TAG, e?.message)
            UiValidator.displayMsgSnack(coordinator, this, e.message)
        }
    }


    fun setTextColor() {
        val colorText = "I agree with the" + " <font color='#022E8D'>terms &amp; conditions</font>"
        val colorText1 = "Alreay have an account? <u><font color='#022E8D'>Login</font></u>"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            chk_term.text = Html.fromHtml(colorText, Html.FROM_HTML_MODE_LEGACY)
            signin.text = Html.fromHtml(colorText1, Html.FROM_HTML_MODE_LEGACY)
        } else {
            chk_term.text = Html.fromHtml(colorText)
            signin.text = Html.fromHtml(colorText1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
//            btn_done.visibility = View.VISIBLE
        } else {
            captureImageUtils!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            captureImageUtils?.onRequestPermission(requestCode, permissions, grantResults)
        }
    }

    override fun capturedImage(uri: Uri?, imageFile: File?) {
        this.imageFile = imageFile
        Glide.with(this).load(File(imageFile?.absolutePath)).apply(RequestOptions.circleCropTransform()).into(profile_img)
    }

    fun callOtpScreen() {
        if (!isFromEdit) {
            startActivity(Intent(this, OtpVerificationActivity::class.java))
            SlideAnimationUtill.slideNextAnimation(this
            )
        }
    }


    private fun uploadUserPicOnServer(file: File) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        if (file != null) {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            // MultipartBody.Part is used to send also the actual file name
            val img = MultipartBody.Part.createFormData("avatar", file.name, requestFile)
//            val userId = RequestBody.create(MediaType.parse("multipart/form-data"), "" + "123456")
            val anInterface = ApiClient.client.create(ApiInterface::class.java)
            anInterface.updateUserProfileImage(img).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<UserProfilePicResponse> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onSuccess(userImageUploadResponse: UserProfilePicResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            handleResultsImage(userImageUploadResponse)
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                AppLog.e(TAG, "--Image Upload Error:" + e.message)
                        }
                    })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    private fun handleResultsImage(userResponse: UserProfilePicResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            AppLog.e(TAG, "--Image onSuccess:" + userResponse.message)
            var userData = CachingManager.getUserSignupDetail()
            userData.imageUrl = userResponse.data.avatar
            CachingManager.setUserSignupDetail(userData)
            AppLog.e(TAG, "--Image onSuccess:" + userData.toString())
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
        }
        callOtpScreen()
    }

}


