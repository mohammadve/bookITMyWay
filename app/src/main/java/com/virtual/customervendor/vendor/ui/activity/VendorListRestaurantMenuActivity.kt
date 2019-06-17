package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessDetail
import com.virtual.customervendor.model.RestaurantPriceModel
import com.virtual.customervendor.model.response.RestaurantMenuListingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.RestaurantItemsViewAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_restaurant_menu_vendor.*

class VendorListRestaurantMenuActivity : BaseActivity(), View.OnClickListener {

    var resAdapterFood: RestaurantItemsViewAdapter? = null
    var resAdapterDrink: RestaurantItemsViewAdapter? = null
    var resAdapterDessert: RestaurantItemsViewAdapter? = null
    var resAdapterApptizers: RestaurantItemsViewAdapter? = null

    var foodList: ArrayList<RestaurantPriceModel> = ArrayList()
    var drinkList: ArrayList<RestaurantPriceModel> = ArrayList()
    var dessertList: ArrayList<RestaurantPriceModel> = ArrayList()
    var apptizersList: ArrayList<RestaurantPriceModel> = ArrayList()

    var TAG: String = VendorListRestaurantMenuActivity::class.java.simpleName
    var fragmentManager: FragmentManager? = null
    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    var apiInterface: ApiInterface? = null
    var businessDetail = BusinessDetail()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.addmore_food -> {
                val intent: Intent = Intent(this, VendorAddRestaurantItemActivity::class.java)
                intent.putExtra("from", AppConstants.FROM_FOOD)
                startActivityForResult(intent, 112)
                SlideAnimationUtill.slideNextAnimation(this)
            }
            R.id.addmore_drink -> {
                val intent: Intent = Intent(this, VendorAddRestaurantItemActivity::class.java)
                intent.putExtra("from", AppConstants.FROM_DRINK)
                startActivityForResult(intent, 112)
                SlideAnimationUtill.slideNextAnimation(this)
            }
            R.id.addmore_dessert -> {
                val intent: Intent = Intent(this, VendorAddRestaurantItemActivity::class.java)
                intent.putExtra("from", AppConstants.FROM_DESSERT)
                startActivityForResult(intent, 112)
                SlideAnimationUtill.slideNextAnimation(this)
            }
            R.id.addmore_appetizer -> {
                val intent: Intent = Intent(this, VendorAddRestaurantItemActivity::class.java)
                intent.putExtra("from", AppConstants.FROM_APPTIZERS)
                startActivityForResult(intent, 112)
                SlideAnimationUtill.slideNextAnimation(this)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_restaurant_menu_vendor)
        businessDetail = intent.extras.get(AppConstants.BUSINESS_DATA) as BusinessDetail
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        UiValidator.hideSoftKeyboard(this)
        init()
        hitApi()
    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.menu)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)

        addmore_food.setOnClickListener(this)
        addmore_drink.setOnClickListener(this)
        addmore_dessert.setOnClickListener(this)
        addmore_appetizer.setOnClickListener(this)

        createAdapterEvents()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }

    fun hitApi() {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.getRestaurantMenuList("Bearer " + SharedPreferenceManager.getAuthToken(), SharedPreferenceManager.getServiceId())
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<RestaurantMenuListingResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: RestaurantMenuListingResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            handleResults(detailResponse)
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }

    fun hitApiDelete(offerModel: RestaurantPriceModel) {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
            apiInterface?.deleteRestaurantItem("Bearer " + SharedPreferenceManager.getAuthToken(), offerModel.id)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<RestaurantMenuListingResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: RestaurantMenuListingResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, detailResponse.toString())
                            if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                hitApi()
                            } else {
                                UiValidator.displayMsgSnack(coordinator, this@VendorListRestaurantMenuActivity, detailResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)
                        }

                        override fun onComplete() {}
                    })
        } else {
            UiValidator.displayMsgSnack(coordinator, this, getString(R.string.no_internet_connection))
        }
    }


    private fun handleResults(restaurantmenu: RestaurantMenuListingResponse) {
        ProgressDialogLoader.progressDialogDismiss()
        if (restaurantmenu.status.equals(AppConstants.KEY_SUCCESS)) {
            if (restaurantmenu.data.food_menu != null && restaurantmenu.data.food_menu.size > 0) {
                foodList.clear()
                foodList.addAll(restaurantmenu.data.food_menu)
                resAdapterFood?.notifyDataSetChanged()
            }
            if (restaurantmenu.data.drink_menu != null && restaurantmenu.data.drink_menu.size > 0) {
                drinkList.clear()
                drinkList.addAll(restaurantmenu.data.drink_menu)
                resAdapterDrink?.notifyDataSetChanged()
            }
            if (restaurantmenu.data.dessert_menu != null && restaurantmenu.data.dessert_menu.size > 0) {
                dessertList.clear()
                dessertList.addAll(restaurantmenu.data.dessert_menu)
                resAdapterDessert?.notifyDataSetChanged()
            }
            if (restaurantmenu.data.appetizers_menu != null && restaurantmenu.data.appetizers_menu.size > 0) {
                apptizersList.clear()
                apptizersList.addAll(restaurantmenu.data.appetizers_menu)
                resAdapterApptizers?.notifyDataSetChanged()
            }
        } else {
            UiValidator.displayMsgSnack(coordinator, this, restaurantmenu.message)
        }
    }

    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        if (t != null)
            AppLog.e(TAG, t.message)
    }

    private fun createAdapterEvents() {
        resAdapterFood = RestaurantItemsViewAdapter(this, foodList, AppConstants.ACTION_ADD) { offerModel -> hitApiDelete(offerModel) }
        rv_food.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_food.adapter = resAdapterFood
        rv_food.isNestedScrollingEnabled = false

        resAdapterDrink = RestaurantItemsViewAdapter(this, drinkList, AppConstants.ACTION_ADD) { offerModel -> hitApiDelete(offerModel) }
        rv_drink.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_drink.adapter = resAdapterDrink
        rv_drink.isNestedScrollingEnabled = false

        resAdapterDessert = RestaurantItemsViewAdapter(this, dessertList, AppConstants.ACTION_ADD) { offerModel -> hitApiDelete(offerModel) }
        rv_dessert.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_dessert.adapter = resAdapterDessert
        rv_dessert.isNestedScrollingEnabled = false

        resAdapterApptizers = RestaurantItemsViewAdapter(this, apptizersList, AppConstants.ACTION_ADD) { offerModel -> hitApiDelete(offerModel) }
        rv_apptizer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_apptizer.adapter = resAdapterApptizers
        rv_apptizer.isNestedScrollingEnabled = false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 112 && resultCode == Activity.RESULT_OK) {
            hitApi()
        }
    }
}
