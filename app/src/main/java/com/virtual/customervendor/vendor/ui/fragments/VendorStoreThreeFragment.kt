//package com.virtual.customervendor.vendor.ui.fragments
//
//import android.os.Bundle
//import android.support.constraint.ConstraintLayout
//import android.support.v4.app.Fragment
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.virtual.customervendor.R
//import com.virtual.customervendor.customview.CustomEditText
//import com.virtual.customervendor.model.ItemPriceStoreModel
//import com.virtual.customervendor.model.request.VendorStoreServiceRequest
//import com.virtual.customervendor.utill.AppConstants
//import com.virtual.customervendor.utill.AppLog
//import com.virtual.customervendor.utill.UiValidator
//import com.virtual.customervendor.vendor.ui.activity.VendorStoreActivity
//import com.virtual.customervendor.vendor.ui.adapter.StoreAddItemsAdapter
//import kotlinx.android.synthetic.main.fragment_store_three_vendor.*
//import kotlin.collections.ArrayList
//
//class VendorStoreThreeFragment : Fragment(), View.OnClickListener {
//
//    var datetime: String? = null
//    var TAG: String = VendorStoreThreeFragment::class.java.simpleName
//    var storeAddItemsAdapter: StoreAddItemsAdapter? = null
//    var store_items: ArrayList<ItemPriceStoreModel> = ArrayList()
//    var vendorstoreRequest: VendorStoreServiceRequest? = null
//    var manager: RecyclerView.LayoutManager? = null
//
//    override fun onClick(v: View?) {
//        when (v!!.id) {
//            R.id.iv_back -> {
//                activity?.onBackPressed()
//            }
//            R.id.btn_next -> {
//
//                validateField()
//            }
//            R.id.addmore -> {
//                performAddFood()
//            }
//
//        }
//    }
//
//    companion object {
//        fun newInstance(from: String): VendorStoreThreeFragment {
//            val args = Bundle()
//            args.putString("name", from)
////            args.putInt("value", value)
//            val fragment = VendorStoreThreeFragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        var mView = inflater.inflate(R.layout.fragment_store_three_vendor, container, false)
//        return mView
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        vendorstoreRequest = (context as VendorStoreActivity).getFieldPojo()
//        getfilledData()
//        initView(view)
//    }
//
//    fun initView(view: View) {
//        btn_next.setOnClickListener(this)
//        createAdapterFoodMenu()
//        addmore.setOnClickListener(this)
//        manageFromEdit()
//    }
//
//    private fun getfilledData() {
//        try {
//            if (vendorstoreRequest?.store_items?.size != 0) {
//                store_items = ArrayList()
//                store_items.addAll(vendorstoreRequest!!.store_items)
//            } else {
//                store_items.add(ItemPriceStoreModel())
//                store_items.add(ItemPriceStoreModel())
//            }
//
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun createAdapterFoodMenu() {
//        storeAddItemsAdapter = StoreAddItemsAdapter(activity!!, AppConstants.FROM_ADDDATA, store_items)
//        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        rv_food_three.layoutManager = manager
//        rv_food_three.adapter = (storeAddItemsAdapter)
//    }
//
//
//    fun validateField() {
//        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
//        if (activity is VendorStoreActivity) {
//            getListFood()
//            if ((activity as VendorStoreActivity).isFromedit()) {
////                (activity as VendorStoreActivity).hitApiEdit(vendorstoreRequest)
//            } else {
//                if (vendorstoreRequest?.store_items!!.size > 0) {
//                    (activity as VendorStoreActivity).setDisplayFragment(4, activity!!.resources.getString(R.string.review_your_bussiness), false)
//                } else {
//                    UiValidator.displayMsgSnack(coordinator, activity, resources.getString(R.string.please_select_atleast_one_option))
//                }
//            }
//        }
//    }
//
//    fun performAddFood() {
//        store_items.add(ItemPriceStoreModel())
//        storeAddItemsAdapter?.notifyItemInserted(store_items.size - 1)
//        rv_food_three.invalidate()
//        AppLog.e(TAG + "RECYCLERVIEW CHILD COUNT ", store_items.toString())
//    }
//
//
//    fun getListFood() {
//        var storelistnew = ArrayList<ItemPriceStoreModel>()
//        for (i in 0 until store_items.size) {
//            if (!store_items.get(i).item_name.equals("") && !store_items.get(i).item_price.equals("")) {
//                storelistnew.add(store_items.get(i))
//            }
//        }
//        vendorstoreRequest?.store_items?.clear()
//        vendorstoreRequest?.store_items?.addAll(storelistnew)
//        AppLog.e(TAG + " LIST FOOD WALI LIST KA SIZE HAI YE", "" + store_items.toString() + "----" + storelistnew.toString() + "---" + vendorstoreRequest?.store_items.toString())
//    }
//
//
//    fun manageFromEdit() {
//        if (activity is VendorStoreActivity) {
//            if ((activity as VendorStoreActivity).isFromedit()) {
//                btn_next.setText((activity as VendorStoreActivity).getString(R.string.save))
//            }
//        }
//    }
//}