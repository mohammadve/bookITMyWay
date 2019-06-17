package com.virtual.customervendor.vendor.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R

class MyProfileFragment : Fragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when(v!!.id){
//            R.id.btn_edit-> startActivity(Intent(activity, ProfileEditActivity::class.java))
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_myprofile_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(  view: View) {
//        btn_edit.setOnClickListener(this)
    }
}