package com.virtual.customervendor.customer.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.RestaurantDetail
import com.virtual.customervendor.model.RestaurantMenu
import com.virtual.customervendor.model.response.RestaurantDetailResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RestaurantActivity : BaseActivity(), View.OnClickListener {
    var TAG:String =RestaurantActivity::class.java.simpleName
//    var adapter:RestaurantMenuAdapter?=null
    var apiService: ApiInterface? = null
    var restaurantMenu:ArrayList<RestaurantMenu> = ArrayList()
    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.btn_next->{
            }

//            R.id.btnBookTable->{
//                val intent = Intent(this, BookingInfoActivity::class.java)
//                startActivity(intent)
//            }
//
//            R.id.btnFollow->{
//            }

            R.id.iv_back->{
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_restaurant_customer)

        setToolBar()

        apiService = ApiClient.client.create(ApiInterface::class.java)

//        getRestaurantDetail()
    }

    fun setToolBar(){
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.restaurant)
        val iv_back = toolbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
//        btnBookTable?.setOnClickListener(this)
//        btnFollow?.setOnClickListener(this)
    }

//    fun getRestaurantDetail(){
//        var restaurant: Restaurant? = getIntent().getSerializableExtra("restaurant") as? Restaurant
//        var requestValues:MutableMap<String,String> = hashMapOf()
//        requestValues.put("business_id",restaurant?.business_id!!)
//        requestValues.put("service_id",restaurant?.service_id!!)
//
//        apiService!!.getRestaurentInfo("Bearer " + SharedPreferenceManager.getAuthToken(),requestValues)!!.subscribeOn(Schedulers.io())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(object :Observer<RestaurantDetailResponse>{
//                    override fun onError(e: Throwable) {
//                        Log.d(TAG,"onError "+e.message)
//                    }
//
//                    override fun onNext(t: RestaurantDetailResponse) {
//                        Log.d(TAG,t.data.toString())
//                        restaurantMenu=t.data.menuitemlist
//                        setBusinessData(t.data)
//                    }
//
//                    override fun onComplete() {
//                        Log.d(TAG,"onComplete")
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                        Log.d(TAG,"onSubscribe")
//                    }
//                })
//    }


    fun setBusinessData(detailRestaurant: RestaurantDetail){

//        this@RestaurantActivity.runOnUiThread(java.lang.Runnable {
//            txtBusinessName.setText(detailRestaurant.business_name)
//            txtEmail.setText(detailRestaurant.business_email_id)
//            txtContactNumber.setText(detailRestaurant.contact_number)
//            txtAddressLine?.setText(detailRestaurant.address)
//            txtCity.setText(detailRestaurant.cityname)
//            txtDescription?.setText(detailRestaurant.description)
//            txtOpenTime.setText(getOpenDetails(detailRestaurant))
//            var imgBuisness=findViewById(R.id.imgBusiness) as ImageView
//            Glide.with(this).load(detailRestaurant.business_image).into(imgBuisness)
//            populateMenu()
//        })

    }

    fun getOpenDetails(detailRestaurant: RestaurantDetail): String {
        var openDetail:String =""
        if(detailRestaurant.all_day.equals("1"))
            openDetail=openDetail+"All Days "
        else{
            if(detailRestaurant.monday.equals("1"))
                openDetail=openDetail+"Monday "

            if(detailRestaurant.tuesday.equals("1"))
                openDetail=openDetail+"Tuesday "

            if(detailRestaurant.wednesday.equals("1"))
                openDetail=openDetail+"Wednesday "

            if(detailRestaurant.thursday.equals("1"))
                openDetail=openDetail+"Thursday "

            if(detailRestaurant.friday.equals("1"))
                openDetail=openDetail+"Friday "

            if(detailRestaurant.saturday.equals("1"))
                openDetail=openDetail+"Saturday "

            if(detailRestaurant.sunday.equals("1"))
                openDetail=openDetail+"Sunday "
        }
        openDetail=openDetail+" "+ detailRestaurant.start_time +" to "+detailRestaurant.close_time
        return openDetail
    }
    fun populateMenu(){
//        adapter= RestaurantMenuAdapter(restaurantMenu);
//        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
//        recyclerView.adapter= adapter
    }
}