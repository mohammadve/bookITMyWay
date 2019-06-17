package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.activity.TableBookingActivity
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.RestaurantPriceModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.vendor.ui.adapter.RestaurantItemsViewAdapter
import kotlinx.android.synthetic.main.fragment_booking_service_menu_restaurant.*
import java.util.*

class RestaurantBookingMenuSevice : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_next -> {
                if (activity is TableBookingActivity)
                    (activity as TableBookingActivity).setDisplayFragment(4, resources.getString(R.string.booking_information), false)
            }

        }
    }

    var detailResponse: VendorServiceDetailModel = VendorServiceDetailModel()

    var TAG: String = RestaurantBookingMenuSevice::class.java.name;
    var apiService: ApiInterface? = null
    var customerBookingListModel = CustomerBookingListModel()
    var restaurantAddItemsAdapterFood: RestaurantItemsViewAdapter? = null
    var restaurantAddItemsAdapterDrink: RestaurantItemsViewAdapter? = null
    var restaurantAddItemsAdapterDessert: RestaurantItemsViewAdapter? = null
    var restaurantAddItemsAdapterApptizer: RestaurantItemsViewAdapter? = null
    var foodList: java.util.ArrayList<RestaurantPriceModel> = java.util.ArrayList()
    var drinkList: java.util.ArrayList<RestaurantPriceModel> = java.util.ArrayList()
    var dessertList: java.util.ArrayList<RestaurantPriceModel> = java.util.ArrayList()
    var apptizerList: java.util.ArrayList<RestaurantPriceModel> = java.util.ArrayList()

    var homeSliderAdapter: HomeSliderAdapter? = null
    var SCREEN_COUNT: Int = 0
    var timer = Timer()
    var mTimerTask: TimerTask? = null
    val handler = Handler()
    private var pagePosition = 0


    companion object {
        fun newInstance(): RestaurantBookingMenuSevice {
            val fragment = RestaurantBookingMenuSevice()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_service_menu_restaurant, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)

        if (activity is TableBookingActivity) {
            detailResponse = (activity as TableBookingActivity).detailResponse
            setRestaurantData(detailResponse)
        }

    }


    fun setRestaurantData(serviceModel: VendorServiceDetailModel) {

        if (serviceModel.food_menu.size > 0) {
            nodatafound.visibility = View.GONE
            foodList = serviceModel.food_menu
            createAdapterFoodMenu()
        } else {
            foodmenu.visibility = View.GONE
        }
        if (serviceModel.drink_menu.size > 0) {
            nodatafound.visibility = View.GONE
            drinkList = serviceModel.drink_menu
            createAdapterDrinkMenu()

        } else {
            drinkmenu.visibility = View.GONE
        }
        if (serviceModel.dessert_menu.size > 0) {
            nodatafound.visibility = View.GONE
            dessertList = serviceModel.dessert_menu
            createAdapterDessertMenu()
        } else {
            dessertmenu.visibility = View.GONE
        }
        if (serviceModel.appetizers_menu.size > 0) {
            nodatafound.visibility = View.GONE
            apptizerList = serviceModel.appetizers_menu
            createAdapterApptizer()
        } else {
            apptizers.visibility = View.GONE
        }

    }


    private fun createAdapterFoodMenu() {
        restaurantAddItemsAdapterFood = RestaurantItemsViewAdapter(activity!!, foodList, AppConstants.CURRENT_COUNTRY) {}
        val manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        rv_food.layoutManager = manager
        rv_food.adapter = (restaurantAddItemsAdapterFood)
        rv_food.setNestedScrollingEnabled(false)
    }

    private fun createAdapterDrinkMenu() {
        restaurantAddItemsAdapterDrink = RestaurantItemsViewAdapter(activity!!, drinkList, AppConstants.CURRENT_COUNTRY) {}
        val manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        rv_drink.layoutManager = manager
        rv_drink.adapter = (restaurantAddItemsAdapterDrink)
        rv_drink.setNestedScrollingEnabled(false)
    }

    private fun createAdapterDessertMenu() {
        restaurantAddItemsAdapterDessert = RestaurantItemsViewAdapter(activity!!, dessertList, AppConstants.CURRENT_COUNTRY) {}
        val manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        rv_dessert.layoutManager = manager
        rv_dessert.adapter = (restaurantAddItemsAdapterDessert)
        rv_dessert.setNestedScrollingEnabled(false)
    }

    private fun createAdapterApptizer() {
        restaurantAddItemsAdapterApptizer = RestaurantItemsViewAdapter(activity!!, apptizerList, AppConstants.CURRENT_COUNTRY) {}
        val manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        rv_apptizer.layoutManager = manager
        rv_apptizer.adapter = (restaurantAddItemsAdapterApptizer)
        rv_apptizer.setNestedScrollingEnabled(false)
    }


}