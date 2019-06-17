//package com.virtual.customervendor.vendor.ui.fragments
//
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.LinearLayoutManager
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.virtual.customervendor.R
//import com.virtual.customervendor.model.RestaurantPriceModel
//import com.virtual.customervendor.model.RestaurantPriceModel
//import com.virtual.customervendor.model.request.VendorRestaurantServiceModel
//import com.virtual.customervendor.utill.AppConstants
//import com.virtual.customervendor.utill.AppLog
//import com.virtual.customervendor.utill.UiValidator
//import com.virtual.customervendor.vendor.ui.activity.VendorRestaurantActivity
//import com.virtual.customervendor.vendor.ui.adapter.RestaurantAddItemsAdapter
//import kotlinx.android.synthetic.main.fragment_restaurant_three_vendor.*
//import java.util.*
//
//class VendorRestaurantThreeFragment : Fragment(), View.OnClickListener {
//
//    var datetime: String? = null
//    var TAG: String = VendorRestaurantThreeFragment::class.java.simpleName
//    var restaurantAddItemsAdapterFood: RestaurantAddItemsAdapter? = null
//    var restaurantAddItemsAdapterDrink: RestaurantAddItemsAdapter? = null
//    var restaurantAddItemsAdapterDessert: RestaurantAddItemsAdapter? = null
//    var foodList: ArrayList<RestaurantPriceModel> = ArrayList()
//    var drinkList: ArrayList<RestaurantPriceModel> = ArrayList()
//    var dessertList: ArrayList<RestaurantPriceModel> = ArrayList()
//    var vendorRestaurantServiceModel = VendorRestaurantServiceModel()
//
//    override fun onClick(v: View?) {
//        when (v!!.id) {
//            R.id.iv_back -> {
//                activity?.onBackPressed()
//            }
//            R.id.btn_next -> {
//                validateField()
//            }
//            R.id.addmore_food -> {
//                performAddFood()
//            }
//            R.id.addmore_drink -> {
//                performAddDrink()
//            }
//            R.id.addmore_dessert -> {
//                performAddDessert()
//            }
//        }
//    }
//
//    companion object {
//        fun newInstance(from: String): VendorRestaurantThreeFragment {
//            val args = Bundle()
//            args.putString("name", from)
////            args.putInt("value", value)
//            val fragment = VendorRestaurantThreeFragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        var mView = inflater.inflate(R.layout.fragment_restaurant_three_vendor, container, false)
//        return mView
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        vendorRestaurantServiceModel = (context as VendorRestaurantActivity).getFieldPojo()
//        getfilledData()
//        initView(view)
//    }
//
//    fun initView(view: View) {
//        btn_next.setOnClickListener(this)
//        createAdapterFoodMenu()
//        createAdapterDrinkMenu()
//        createAdapterDessertMenu()
//        addmore_food.setOnClickListener(this)
//        addmore_drink.setOnClickListener(this)
//        addmore_dessert.setOnClickListener(this)
//        ed_desc.setText(vendorRestaurantServiceModel.description)
//        manageFromEdit()
//    }
//
//    private fun getfilledData() {
//        try {
//            if (vendorRestaurantServiceModel.food_menu.size != 0)
//                foodList = vendorRestaurantServiceModel.food_menu
//            else {
//                foodList.add(RestaurantPriceModel())
//                foodList.add(RestaurantPriceModel())
//            }
//            if (vendorRestaurantServiceModel.drink_menu.size != 0)
//                drinkList = vendorRestaurantServiceModel.drink_menu
//            else {
//                drinkList.add(RestaurantPriceModel())
//                drinkList.add(RestaurantPriceModel())
//            }
//
//            if (vendorRestaurantServiceModel.dessert_menu.size != 0)
//                dessertList = vendorRestaurantServiceModel.dessert_menu
//            else {
//                dessertList.add(RestaurantPriceModel())
//                dessertList.add(RestaurantPriceModel())
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun createAdapterFoodMenu() {
//        restaurantAddItemsAdapterFood = RestaurantAddItemsAdapter(activity!!, AppConstants.FROM_ADDDATA, foodList)
//        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        rv_food.layoutManager = manager
//        rv_food.adapter = (restaurantAddItemsAdapterFood)
//        rv_food.isNestedScrollingEnabled = false
//    }
//
//    private fun createAdapterDrinkMenu() {
//        restaurantAddItemsAdapterDrink = RestaurantAddItemsAdapter(activity!!, AppConstants.FROM_ADDDATA, drinkList)
//        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        rv_drink.layoutManager = manager
//        rv_drink.adapter = (restaurantAddItemsAdapterDrink)
//        rv_drink.isNestedScrollingEnabled = false
//    }
//
//    private fun createAdapterDessertMenu() {
//        restaurantAddItemsAdapterDessert = RestaurantAddItemsAdapter(activity!!, AppConstants.FROM_ADDDATA, dessertList)
//        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        rv_dessert.layoutManager = manager
//        rv_dessert.adapter = (restaurantAddItemsAdapterDessert)
//        rv_dessert.isNestedScrollingEnabled = false
//    }
//
//    fun validateField() {
//        if (ed_desc.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
//            return
//        }
//        if (til_desc.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_desc)
//        }
//
//        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
//        if (activity is VendorRestaurantActivity) {
//            getListFood()
//            getListDrink()
//            getListDessert()
//            vendorRestaurantServiceModel.description = ed_desc.text.toString()
//            if ((activity as VendorRestaurantActivity).isFromedit()) {
//                (activity as VendorRestaurantActivity).hitApiEdit(vendorRestaurantServiceModel)
//            } else {
//                (activity as VendorRestaurantActivity).setDisplayFragment(4, activity!!.resources.getString(R.string.review_your_bussiness), false)
//            }
//        }
//    }
//
//    fun performAddFood() {
//        foodList.add(RestaurantPriceModel())
//        restaurantAddItemsAdapterFood!!.notifyItemInserted(foodList.size - 1)
//        rv_food.invalidate()
//    }
//
//    fun performAddDrink() {
//        drinkList.add(RestaurantPriceModel())
//        restaurantAddItemsAdapterDrink!!.notifyItemInserted(drinkList.size - 1)
//        rv_drink.invalidate()
//    }
//
//    fun performAddDessert() {
//        dessertList.add(RestaurantPriceModel())
//        restaurantAddItemsAdapterDessert!!.notifyItemInserted(dessertList.size - 1)
//        rv_dessert.invalidate()
//    }
//
//    fun getListFood() {
//        var foodListnew = ArrayList<RestaurantPriceModel>()
//        for (i in 0 until foodList.size) {
//            if (!foodList.get(i).itemName.isEmpty() && !foodList.get(i).itemPrice.isEmpty()) {
//                foodListnew.add(foodList.get(i))
//            }
//        }
//        vendorRestaurantServiceModel.food_menu.clear()
//        vendorRestaurantServiceModel.food_menu.addAll(foodListnew)
//
//        AppLog.e(TAG, vendorRestaurantServiceModel.food_menu.size.toString())
//    }
//
//    fun getListDrink() {
//        var drinkListnew = ArrayList<RestaurantPriceModel>()
//
//        for (i in 0 until drinkList.size) {
//            if (!drinkList.get(i).itemName.isEmpty() && !drinkList.get(i).itemPrice.isEmpty()) {
//                drinkListnew.add(drinkList.get(i))
//            }
//        }
//        vendorRestaurantServiceModel.drink_menu.clear()
//        vendorRestaurantServiceModel.drink_menu.addAll(drinkListnew)
//
//        AppLog.e(TAG, vendorRestaurantServiceModel.drink_menu.size.toString())
//    }
//
//    fun getListDessert() {
//        var dessertListnew = ArrayList<RestaurantPriceModel>()
//        for (i in 0 until dessertList.size) {
//            if (!dessertList.get(i).itemName.isEmpty() && !dessertList.get(i).itemPrice.isEmpty()) {
//                dessertListnew.add(dessertList.get(i))
//            }
//        }
//        vendorRestaurantServiceModel.dessert_menu.clear()
//        vendorRestaurantServiceModel.dessert_menu.addAll(dessertListnew)
//
//        AppLog.e(TAG, vendorRestaurantServiceModel.dessert_menu.size.toString())
//    }
//
//    fun manageFromEdit() {
//        if (activity is VendorRestaurantActivity) {
//            if ((activity as VendorRestaurantActivity).isFromedit()) {
//                btn_next.setText((activity as VendorRestaurantActivity).getString(R.string.save))
//            }
//        }
//    }
//}