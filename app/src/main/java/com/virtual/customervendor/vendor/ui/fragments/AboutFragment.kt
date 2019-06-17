package com.virtual.customervendor.vendor.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.virtual.customervendor.R
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.ProgressDialogLoader
import kotlinx.android.synthetic.main.fragment_about_vendor.*

class AboutFragment : Fragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when(v!!.id){
//            R.id.btn_edit-> startActivity(Intent(activity, ProfileEditActivity::class.java))
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_about_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(  view: View) {
        webView.getSettings().setJavaScriptEnabled(true)


        ProgressDialogLoader.progressDialogCreation(activity, activity!!.resources.getString(R.string.please_wait))
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
        webView.loadUrl(AppConstants.ABOUT_US_URL);
    }

}