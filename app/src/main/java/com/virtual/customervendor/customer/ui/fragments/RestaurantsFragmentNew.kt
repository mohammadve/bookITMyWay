package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.TableBookingActivity
import com.virtual.customervendor.customer.ui.adapter.CustomerBookingListAdapter
import com.virtual.customervendor.listener.RegionListner
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CountryModel
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.response.CustomerDisplayListResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tableboking_customer_new.*

class RestaurantsFragmentNew : Fragment(), View.OnClickListener, RegionListner {

    var TAG: String = RestaurantsFragmentNew::class.java.name;
    var restaurants: ArrayList<CustomerBookingListModel> = ArrayList()
    var adapter: CustomerBookingListAdapter? = null;
    var apiService: ApiInterface? = null
    var fieldMap: MutableMap<String, String> = mutableMapOf();
    var countryModel: CountryModel? = null;

    override fun region(regionModel: RegionModel?) {
        region.setText(regionModel?.regionname)
        getRestaurents(regionModel)
    }

    fun updateSelectedRegion(bean: RegionModel) {
        if (bean != null) {
            region.setText(bean.regionname)
            getRestaurents(bean)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.region -> {
                if (countryModel != null && countryModel?.code != "") {
                    (activity as TableBookingActivity).showRegionSelectionDialog(countryModel?.code!!)
                } else {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.choose_country_first))
                }
            }
            R.id.country -> (activity as TableBookingActivity).showCountrySelectionDialog()
        }

    }




    companion object {
        fun newInstance(): RestaurantsFragmentNew {
            val fragment = RestaurantsFragmentNew()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_tableboking_customer_new, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        region.setOnClickListener(this)
        country.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        (activity as TableBookingActivity).setRegionListner(this);
        getRestaurents(null)
        initView()
    }

    fun updateSelectedCountry(bean: CountryModel) {
        if (bean != null) {
            country.setText(bean.name)
            region.setText("")
            countryModel = bean
            fieldMap.put(AppKeys.COUNTRY_CODE, bean.code!!)
            getRestaurents(null)
        }

    }


    fun initView() {

        adapter = CustomerBookingListAdapter(activity!!, restaurants) { restaurant ->
            (activity as TableBookingActivity).customerBookingListModel = restaurant
            (activity as TableBookingActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        }
        recyclerView.layoutManager = LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    fun getRestaurents(regionModel: RegionModel?) {
        fieldMap.clear()
        if (regionModel != null) {
            fieldMap.put(AppKeys.KEY_REGIONID, regionModel?.regionid!!)
        }
        if (countryModel != null) {
            fieldMap.put(AppKeys.COUNTRY_CODE, countryModel?.code!!)
        }
        fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_RESTAURANT_DINNIG.toString())
        fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_RESTAURANT_DINNIG.toString())
        fieldMap.put(AppKeys.OFFSET, "0")

        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))

            apiService!!.getRestaurentsList("Bearer " + SharedPreferenceManager.getAuthToken(), fieldMap)!!.subscribeOn(Schedulers.io())
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
                                initView()
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, regionResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                AppLog.e(TAG, e.toString())
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }
    }

}