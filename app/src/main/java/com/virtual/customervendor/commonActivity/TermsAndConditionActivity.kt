package com.virtual.customervendor.commonActivity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.utills.AppConstants
import kotlinx.android.synthetic.main.activity_terms_and_condition.*
import android.widget.Toast
import android.webkit.WebView
import android.webkit.WebViewClient
import com.virtual.customervendor.utills.ProgressDialogLoader


class TermsAndConditionActivity : BaseActivity(), View.OnClickListener {
    var toolbar: Toolbar? = null
    var mTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_condition)
        init()
    }

    fun init() {

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        mTitle!!.text = getString(R.string.terms_conditions)

        webView.getSettings().setJavaScriptEnabled(true)


        ProgressDialogLoader.progressDialogCreation(this, resources.getString(R.string.please_wait))
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                ProgressDialogLoader.progressDialogDismiss()
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {

            }
        }
        webView.loadUrl(AppConstants.TERMS_CONDITIONS_URL);

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }
}
