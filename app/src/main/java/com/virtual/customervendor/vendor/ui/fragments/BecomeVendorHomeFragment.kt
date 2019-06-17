package com.virtual.customervendor.vendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppKeys
import com.virtual.customervendor.utills.SlideAnimationUtill
import com.virtual.customervendor.vendor.ui.activity.BecomeVendorActivity
import kotlinx.android.synthetic.main.fragment_become_vendor_home.*

class BecomeVendorHomeFragment : Fragment(), View.OnClickListener {
    var TAG: String = BecomeVendorHomeFragment::class.java.name

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_next -> {
                callActivity(AppConstants.CAT_RESTAURANT_DINNIG.toString(), BecomeVendorActivity())
            }
        }
    }

    fun callActivity(keyValue: String, activity1: AppCompatActivity) {
        var intent: Intent = Intent(context, activity1.javaClass)
        intent.putExtra(AppKeys.KEY_CATEGORY, keyValue)
        (activity as AppCompatActivity).startActivity(intent)
        SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_become_vendor_home, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
    }




}