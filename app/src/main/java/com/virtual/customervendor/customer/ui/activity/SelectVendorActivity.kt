package com.virtual.customervendor.customer.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.adapter.VendoradapterCustomer
import com.virtual.customervendor.model.OfferModel
import kotlinx.android.synthetic.main.fragment_myaccount_customer.*

class SelectVendorActivity : BaseActivity(), View.OnClickListener {
    lateinit var vendoradapterCustomer: VendoradapterCustomer
    val list:ArrayList<OfferModel> = java.util.ArrayList()
    override fun onClick(v: View?) {

        when (v!!.id) {
//            R.id.iv_notification -> {
//                setResult(Activity.RESULT_OK, Intent().setData(Uri.parse("final")))
//                finish()
//            }
            R.id.iv_back -> {
                onBackPressed()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectvendor_customer)
        init()
        setToolBar()
    }

    fun init() {
      createAdapterView()
    }

    fun setToolBar(){
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.select_vendor)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)

//        val iv_notification = toolbar.findViewById(R.id.iv_notification) as ImageView
//        iv_notification.visibility=View.VISIBLE
//        iv_notification.setImageDrawable(resources.getDrawable(R.drawable.ic_save))
//        iv_notification.setOnClickListener(this)
    }


    private fun createAdapterView() {
        list.add(OfferModel("vendor1","1"))
        list.add(OfferModel("vendor1","1"))
        list.add(OfferModel("vendor1","1"))
        list.add(OfferModel("vendor1","1"))
        list.add(OfferModel("vendor1","1"))

        vendoradapterCustomer = VendoradapterCustomer(this, list)
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_list.layoutManager = manager
        rv_list.adapter=(vendoradapterCustomer)
    }


}