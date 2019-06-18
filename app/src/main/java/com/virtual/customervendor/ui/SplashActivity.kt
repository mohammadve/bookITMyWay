package com.virtual.customervendor.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.DashBoardActivity
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CountryCodeModel
import com.virtual.customervendor.model.response.CountrycodeResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import com.virtual.customervendor.commonActivity.TimeManagerActivity


class SplashActivity : AppCompatActivity() {
    private val TAG = SplashActivity::class.java.name
    private var ISAPIHit: Boolean = false

    lateinit var handler: Handler
    lateinit var runnable: Runnable
    var apiInterface: ApiInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
//        getCountryCode();

        AppUtils.updateLanguageResources(applicationContext, SharedPreferenceManager.getUserLanguage())

        val animZoomOut = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in)


        animZoomOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                if (SharedPreferenceManager.getAuthToken().equals("")) {
                    var intent: Intent = Intent(this@SplashActivity, com.virtual.customervendor.vendor.ui.activity.LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(Intent(intent))
                    SlideAnimationUtill.slideNextAnimation(this@SplashActivity)
                    finish()
                } else {
                    var intent: Intent = Intent(this@SplashActivity, TimeManagerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    SlideAnimationUtill.slideNextAnimation(this@SplashActivity)
                    finish()
                }
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        img.startAnimation(animZoomOut)
        app.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in))

    }


    override fun onResume() {
        super.onResume()
//        handler = Handler()
//        runnable = ActivityRunnable()
//        handler.postDelayed(runnable, 1000)
    }

    override fun onPause() {
        super.onPause()
//        handler.removeCallbacks(runnable)
    }

    private inner class ActivityRunnable : Runnable {
        override fun run() {

//            if (ISAPIHit) {
            if (SharedPreferenceManager.getAuthToken().equals("")) {
                var intent: Intent = Intent(this@SplashActivity, com.virtual.customervendor.vendor.ui.activity.LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(Intent(intent))
                SlideAnimationUtill.slideNextAnimation(this@SplashActivity)
                finish()
            } else {
                var intent: Intent = Intent(this@SplashActivity, DashBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                SlideAnimationUtill.slideNextAnimation(this@SplashActivity)
                finish()
            }
//            } else {
//                handler.postDelayed(runnable, 1000)
//            }
        }
    }


    fun getCountryCode() {


        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface!!.getCountryCode()!!.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CountrycodeResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(userResponse: CountrycodeResponse) {
                            AppLog.e(TAG, userResponse.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (userResponse.status!!.equals(AppConstants.KEY_SUCCESS)) {

//                               /*testing canada*/
                                var model = CountryCodeModel()
                                model.code = "1"
                                model.countryCode = "CA"
                                model.name = "CANADA"
                                CachingManager.setCurrentCountry(model)
//                                /*testing*/


//                                CachingManager.setCurrentCountry(userResponse.data)
                                AppLog.e("@@JAVA CLASS", "-----" + CachingManager.getCurrentCountry())
                                ISAPIHit = true
                            } else {
                                UiValidator.displayMsgSnack(cordinate, this@SplashActivity, userResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cordinate, this, getString(R.string.no_internet_connection))
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null) {
            UiValidator.displayMsgSnack(cordinate, this, t.message)
            AppLog.e(TAG, t?.message)
        }
    }

}