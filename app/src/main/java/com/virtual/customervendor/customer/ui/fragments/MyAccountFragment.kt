package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.adapter.MyAccountadapterCustomer
import com.virtual.customervendor.model.OfferModel
import kotlinx.android.synthetic.main.fragment_myaccount_customer.*

class MyAccountFragment : Fragment(), View.OnClickListener {

    lateinit var myAccountadapterCustomer: MyAccountadapterCustomer
    val list:ArrayList<OfferModel> = java.util.ArrayList()

    override fun onClick(v: View?) {
        when(v!!.id){
//            R.id.btn_restaurant-> startActivity(Intent(activity, RestaurantActivity::class.java))
//            R.id.btn_doctor->{startActivity(Intent(activity, DoctorAppointmentActivity::class.java))}
//            R.id.btn_purchase->{startActivity(Intent(activity, PurchaseItemsActivity::class.java))}
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_myaccount_customer, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(  view: View) {
        createAdapterView()
    }

    private fun createAdapterView() {
        list.add(OfferModel("Surender","1"))
        list.add(OfferModel("Data","1"))
        list.add(OfferModel("Tinder","1"))
        list.add(OfferModel("Dhoomal","1"))
        list.add(OfferModel("Parado","1"))

        myAccountadapterCustomer = MyAccountadapterCustomer(activity!!, list)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_list.layoutManager = manager
        rv_list.adapter=(myAccountadapterCustomer)
    }
}