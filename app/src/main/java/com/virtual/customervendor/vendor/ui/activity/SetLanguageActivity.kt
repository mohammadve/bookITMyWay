package com.virtual.customervendor.vendor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.MyApp
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.activity.DashBoardActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.LanguageModel
import com.virtual.customervendor.model.response.LanguageResponse
import com.virtual.customervendor.model.response.CommonResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.SelectLanguageAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_otpverification_vendor.*
import kotlinx.android.synthetic.main.activity_service_vendor.*

class SetLanguageActivity : BaseActivity(), View.OnClickListener {

    var selectLanguageAdapter: SelectLanguageAdapter? = null
    var list: ArrayList<LanguageModel> = java.util.ArrayList()
    var offerModel1: LanguageModel? = null
    val TAG: String = SetLanguageActivity::class.java.simpleName
    var apiService: ApiInterface? = null
    var isCoomingFromDash: Boolean = false
    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_next1 -> {
                setlanguageApi(offerModel1?.code!!)
            }
            R.id.iv_back -> onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCoomingFromDash = intent.getBooleanExtra("is_coomingFrom_Dash", false);
        setContentView(R.layout.activity_service_vendor)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        setToolBar()
        createAdapterView()
        languageApi()
    }


    fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.choose_preferred_lang)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        btn_next1.setOnClickListener(this)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }


    private fun createAdapterView() {


        AppLog.e(TAG, list.toString())
        selectLanguageAdapter = SelectLanguageAdapter(this, list) { offerModel ->
            callNext(offerModel)
        }
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_selectlang.layoutManager = manager
        rv_selectlang.adapter = (selectLanguageAdapter)
    }

    fun callNext(offerModel: LanguageModel) {

        offerModel1 = offerModel;


    }

    private fun languageApi() {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService?.getLanguage()?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<LanguageResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(userResponse: LanguageResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, userResponse.toString())
                            if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
                                list.addAll(userResponse.data)
                                for (item: LanguageModel in list) {
                                    if (item.code.equals(SharedPreferenceManager.getUserLanguage())) {
                                        item.checked = true
                                        offerModel1 = item;
                                    }
                                }
                                selectLanguageAdapter?.notifyDataSetChanged()
                            } else {
                                UiValidator.displayMsgSnack(coordinator, this@SetLanguageActivity, userResponse.message)
                            }

                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {

                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cord, this, getString(R.string.no_internet_connection))
        }
    }

    private fun setlanguageApi(code: String) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiService?.setLanguage("Bearer " + SharedPreferenceManager.getAuthToken(), code)?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<CommonResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(userResponse: CommonResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, userResponse.toString())
                            if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
                                SharedPreferenceManager.setUserLanguage(code)
                                //   AppUtils.updateLanguageResources(applicationContext, code)
                                MyApp.localeManager.setNewLocale(this@SetLanguageActivity, code)


                                if (isCoomingFromDash) {
                                    val intent = Intent(this@SetLanguageActivity, DashBoardActivity::class.java)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                    SlideAnimationUtill.slideNextAnimation(this@SetLanguageActivity)

                                    finish()
                                } else {
                                    startActivity(Intent(this@SetLanguageActivity, DashBoardActivity::class.java))
                                    finish()
                                    SlideAnimationUtill.slideNextAnimation(this@SetLanguageActivity)

                                }
                            }

                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {
                        }
                    })

        } else {
            UiValidator.displayMsgSnack(cord, this, getString(R.string.no_internet_connection))
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t.message)
    }

}