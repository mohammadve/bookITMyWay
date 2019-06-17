package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.PurchaseItemsActivity
import com.virtual.customervendor.customer.ui.adapter.CustomerBookingListAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CountryModel
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.StoreCategoryModel
import com.virtual.customervendor.model.response.CustomerDisplayListResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_store_listing.*

class StoreListingFragment : Fragment(), View.OnClickListener {

    var TAG: String = StoreListingFragment::class.java.name;
    var fieldMap: MutableMap<String, String> = mutableMapOf();
    var restaurants: ArrayList<CustomerBookingListModel> = ArrayList()
    var adapter: CustomerBookingListAdapter? = null;
    var apiService: ApiInterface? = null

    var countryModel: CountryModel? = null;

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.region -> {
                if (countryModel != null && countryModel?.code != "") {
                    (activity as PurchaseItemsActivity).setDisplayDialog(7, countryModel?.code!!)
                } else {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.choose_country_first))
                }
            }
            R.id.category -> {
                (activity as PurchaseItemsActivity).setDisplayDialog(8, AppConstants.FROM_V_TAXI_SERVICE_AREA)
            }
            R.id.country -> {
                (activity as PurchaseItemsActivity).setDisplayDialog(9, "")
            }
            R.id.iv_clearText -> region.setText("")
        }
    }

    companion object {
        fun newInstance(): StoreListingFragment {
            val fragment = StoreListingFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_store_listing, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        region.setOnClickListener(this)
        country.setOnClickListener(this)
        category.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_STORE_SELLER.toString())
        fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_STORE_SELLER.toString())
        fieldMap.put(AppKeys.OFFSET, "0")
        getListItem()
    }

    fun updateSelectedCountry(bean: CountryModel) {
        if (bean != null) {
            country.setText(bean.name)
            countryModel = bean
            region.setText("")
            fieldMap.put(AppKeys.KEY_REGIONID,"")
            fieldMap.put(AppKeys.COUNTRY_CODE, bean.code!!)
            getListItem()
        }
    }

    fun initView() {
        adapter = CustomerBookingListAdapter(activity!!, restaurants) { cust ->
            callActivity(cust)
        }
        recyclerView.layoutManager = LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    fun getListItem() {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            apiService!!.getRestaurentsList("Bearer " + SharedPreferenceManager.getAuthToken(), fieldMap).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CustomerDisplayListResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(regionResponse: CustomerDisplayListResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, regionResponse.toString())
                            if (regionResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                restaurants.clear()

                                if (regionResponse.data.size > 0) {
                                    nodatafound.visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                    restaurants.addAll(regionResponse.data)
                                    adapter?.notifyDataSetChanged()
                                } else {
                                    nodatafound.visibility = View.VISIBLE
                                    recyclerView.visibility = View.GONE
                                }
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, regionResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                AppLog.e(TAG, e.message)
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }

        initView()

    }

    fun updateSelectedRegion(bean: RegionModel) {
        if (bean != null) {
            region.setText(bean.regionname)
            fieldMap.put(AppKeys.KEY_REGIONID, bean.regionid!!)
            getListItem()
        }
    }

    fun updateSelectedCategory(bean: StoreCategoryModel) {
        if (bean != null) {
            category.setText(bean.category_name)

            fieldMap.put(AppKeys.KEY_STORE_ID, bean.id!!)
            getListItem()
        }
    }

    fun callActivity(dataModel: CustomerBookingListModel) {

        (activity as PurchaseItemsActivity).customerBookingListModel = dataModel
        (activity as PurchaseItemsActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
    }
}