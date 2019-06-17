//package com.virtual.customervendor.customer.ui.fragments
//
//import android.arch.lifecycle.Observer
//import android.arch.lifecycle.ViewModelProviders
//import android.arch.paging.PagedList
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.widget.LinearLayoutManager
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.virtual.customervendor.R
//import com.virtual.customervendor.customer.ui.activity.TableBookingActivity
//import com.virtual.customervendor.customer.ui.adapter.RestaurantsAdapterCustomerPaging
//import com.virtual.customervendor.listener.RegionReceiver
//import com.virtual.customervendor.model.CustomerBookingListModel
//import com.virtual.customervendor.model.RegionModel
//import com.virtual.customervendor.networks.ApiClient
//import com.virtual.customervendor.networks.ApiInterface
//import com.virtual.customervendor.viewmodel.RestaurantViewModel
//import kotlinx.android.synthetic.main.fragment_tableboking_customer_new.*
//
//
//class RestaurantsFragmentPaging : Fragment(), View.OnClickListener, RegionReceiver.RegionObserver {
//    var TAG :String = RestaurantsFragmentPaging::class.java.name;
//    var request: MutableMap<String, String> = mutableMapOf();
//    var adapter: RestaurantsAdapterCustomerPaging?=null;
//    var datetime: StringBuilder? = null
//    var apiService: ApiInterface? = null
//    var receiver: RegionReceiver? =null
//    override fun onReceiveRegion(region: RegionModel?) {
////        getRestaurents(region)
//    }
//
//    override fun onClick(v: View?) {
//        (activity as TableBookingActivity).showRegionSelectionDialog("Regions")
//    }
//
//    companion object {
//        fun newInstance(): RestaurantsFragmentPaging {
//            val fragment = RestaurantsFragmentPaging()
//            return fragment
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        var mView = inflater.inflate(R.layout.fragment_tableboking_customer_new, container, false)
//        return mView
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        txtSelectRegion.setOnClickListener(this)
//
//        apiService = ApiClient.getClient().create(ApiInterface::class.java)
//
//        receiver= RegionReceiver.registerReceiver(this, activity)
//
////        getRestaurents(null)
//        initView()
//        //getting our ItemViewModel
//        val itemViewModel = ViewModelProviders.of(this).get(RestaurantViewModel::class.java!!)
//
//
////        observing the itemPagedList from view model
//
//        itemViewModel.itemPagedList.observe(this, object : Observer<PagedList<CustomerBookingListModel>> {
//           override fun onChanged(items: PagedList<CustomerBookingListModel>?) {
//                //in case of any changes
//                //submitting the items to adapter
//                adapter?.submitList(items)
//            }
//        })
//
//
//    }
//
//
//    fun initView(  ) {
//        adapter= RestaurantsAdapterCustomerPaging();
//        recyclerView.layoutManager= LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
//        recyclerView.adapter= adapter
//
//    }
//
////    fun getRestaurents(regionModel: RegionModel?) {
////
////        apiService!!.getRestaurentsList("Bearer " + SharedPreferenceManager.getAuthToken(), regionModel?.regionid)!!.subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(object : Observer<ApiResponseRestaurant> {
////                    override fun onSubscribe(d: Disposable) {
////                        AppLog.e(TAG, "onSubscribe")
////                    }
////
////                    override fun onNext(apiResponseRestaurant: ApiResponseRestaurant) {
////                        AppLog.e(TAG, apiResponseRestaurant.toString())
////                        restaurants=apiResponseRestaurant.data
////                        initView()
////                    }
////
////                    override fun onError(e: Throwable) {
////                        AppLog.e(TAG, e.message)
////                    }
////
////                    override fun onComplete() {
////                        AppLog.e(TAG, "onComplete: ")
////                    }
////                })
////
////    }
//
//    fun updateSelectedRegion(bean: RegionModel) {
//        if (bean != null) {
////            txtSelectRegion.setText(bean.regionname)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        RegionReceiver.unRegisterReceiver(receiver, activity)
//    }
//
//}