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

                } else {
                    var intent: Intent = Intent(this@SplashActivity, DashBoardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    SlideAnimationUtill.slideNextAnimation(this@SplashActivity)
                }
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        img.startAnimation(animZoomOut)
        app.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in))

    }


    private inner class ActivityRunnable : Runnable {
        override fun run() {


            if (SharedPreferenceManager.getAuthToken().equals("")) {
                var intent: Intent = Intent(this@SplashActivity, com.virtual.customervendor.vendor.ui.activity.LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(Intent(intent))
                SlideAnimationUtill.slideNextAnimation(this@SplashActivity)
                finish()
            } else {
                var intent: Intent = Intent(this@SplashActivity, DashBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(TimeManagerActivity.KEY_Multi_Slots,false);
                startActivity(intent)
                SlideAnimationUtill.slideNextAnimation(this@SplashActivity)
                finish()
            }
        }
    }

}