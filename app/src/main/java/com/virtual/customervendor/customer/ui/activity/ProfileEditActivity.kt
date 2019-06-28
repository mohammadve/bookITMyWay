package com.virtual.customervendor.customer.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.response.LoginResponse
import com.virtual.customervendor.model.response.UserProfilePicResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_myprofile_edit_customer.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileEditActivity : BaseActivity(), View.OnClickListener, CaptureImageUtils.ImageSelectionListener {
    override fun capturedImage(uri: Uri?, imageFile: File?) {
        if (imageFile != null) {
            this.imageFile = imageFile
//            Glide.with(this).load(File(imageFile.absolutePath)).apply(RequestOptions().placeholder(R.drawable.place_holder_round)).into(profile_img)
            AppUtill.loadImageRoundProfile(this, imageFile.absolutePath, profile_img)
            uploadUserPicOnServer(imageFile)
        }
    }

    private var captureImageUtils: CaptureImageUtils? = null
    var imageFile: File? = null
    var TAG: String = ProfileEditActivity::class.java.name
    var anInterface: ApiInterface? = null

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_next -> validateField()
            R.id.iv_back -> onBackPressed()

            R.id.profile_img -> captureImageUtils?.openSelectImageFromDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myprofile_edit_customer)
        anInterface = ApiClient.client.create(ApiInterface::class.java)
        init()
        setToolBar()
    }

    fun init() {
        captureImageUtils = CaptureImageUtils(this, this)
        btn_next.setOnClickListener(this)
        profile_img.setOnClickListener(this)

        ed_name.setText(SharedPreferenceManager.getCustomerName())
        ed_name.setSelection(ed_name.text!!.length)
        ed_email.setText(SharedPreferenceManager.getCustomerEmail())
        ed_mobno.setText(SharedPreferenceManager.getCustomerMobileNo())

        if (SharedPreferenceManager.getCustomerImage() != null && !SharedPreferenceManager.getCustomerImage().equals("")) {
//            Glide.with(this).load(File(SharedPreferenceManager.getCustomerImage())).apply(RequestOptions().placeholder(R.drawable.place_holder_round)).into(profile_img)
            AppUtill.loadImageRoundProfile(this, SharedPreferenceManager.getCustomerImage(), profile_img)
        }

        getCountryCode()
    }

    fun setToolBar() {
        val appbar: AppBarLayout = findViewById<AppBarLayout>(R.id.toolbar)
        val mTitle = appbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.edit_profile)
        val iv_back = appbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
    }

    fun validateField() {
//        var msg: String=""
//        var isValid: Boolean = true
//
        if (AppUtils.isInternetConnected(this)) {
            hitUpdate()
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        captureImageUtils?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            captureImageUtils?.onRequestPermission(requestCode, permissions, grantResults)
        }
    }

    private fun hitUpdate() {
        ProgressDialogLoader.progressDialogCreation(this@ProfileEditActivity, getString(R.string.please_wait))
        anInterface?.customerUpdateProfile("Bearer " + SharedPreferenceManager.getAuthToken(), ed_name.getText().toString())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : io.reactivex.Observer<LoginResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(response: LoginResponse) {
                        AppLog.e(TAG, response.toString())
                        if (response.status.equals(AppConstants.KEY_SUCCESS)) {
                            UiValidator.displayMsgSnack(coordinator, this@ProfileEditActivity, response.message)
                            SharedPreferenceManager.setCustomerName(ed_name.getText().toString())
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            UiValidator.displayMsgSnack(coordinator, this@ProfileEditActivity, response.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        ProgressDialogLoader.progressDialogDismiss()
                        if (e != null) {
                            AppLog.e(TAG, e.message)
                            UiValidator.displayMsgSnack(coordinator, this@ProfileEditActivity, e.message)
                        }
                    }

                    override fun onComplete() {

                    }
                })
    }


    private fun uploadUserPicOnServer(file: File) {
        ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        if (file != null) {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            // MultipartBody.Part is used to send also the actual file name
            val img = MultipartBody.Part.createFormData("avatar", file.name, requestFile)
//            val userId = RequestBody.create(MediaType.parse("multipart/form-data"), "" + "123456")

            anInterface?.uploadAvatarAfterLogin("Bearer " + SharedPreferenceManager.getAuthToken(),img)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : SingleObserver<UserProfilePicResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onSuccess(userImageUploadResponse: UserProfilePicResponse) {

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

    private fun handleResultsImage(userResponse: UserProfilePicResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
            SharedPreferenceManager.setCustomerImage(userResponse.data.avatar)
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            UiValidator.displayMsgSnack(coordinator, this, userResponse.message)
        }
    }

    fun getCountryCode() {
        val countrycodeResponse = SharedPreferenceManager.getRegisterCountryDetails()
        if (countrycodeResponse != null) {
            val codeInt: Int = countrycodeResponse.data.code!!.toInt() ?: 1
            ccp_login.setCountryForPhoneCode(codeInt)
        } else {
            ccp_login.resetToDefaultCountry()
        }
    }

}