package com.virtual.customervendor.vendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.DashBoardActivity
import com.virtual.customervendor.utills.SlideAnimationUtill
import kotlinx.android.synthetic.main.fragment_succesbooking_vendor.*

class SuccesBookingFragment : Fragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_home -> {
                var intent: Intent = Intent(context, DashBoardActivity::class.java)
                startActivity(intent)
                (activity as AppCompatActivity).finish()
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
        }
    }

    companion object {
        fun newInstance(): SuccesBookingFragment {
            val fragment = SuccesBookingFragment()
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_succesbooking_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(view: View) {
        btn_home.setOnClickListener(this)
    }

}