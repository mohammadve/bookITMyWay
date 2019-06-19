package com.virtual.customervendor.customer.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding.widget.RxTextView
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.adapter.SearchAdapterNew
import com.virtual.customervendor.customer.ui.dialogFragment.BusinessCategoryDialogFragment
import com.virtual.customervendor.customer.ui.dialogFragment.CountryDialogFragment
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.AllCategoryModel
import com.virtual.customervendor.model.CountryModel
import com.virtual.customervendor.model.SearchModel
import com.virtual.customervendor.model.response.SearchResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_search_customer.*
import java.util.concurrent.TimeUnit

class SearchActivity : BaseActivity(), View.OnClickListener, CountryDialogFragment.countrySelectionInterface, BusinessCategoryDialogFragment.bCategorySelectionInterface {
    override fun selectedCategoryUpdate(bean: AllCategoryModel, fromWhere: String?) {
        if (businessDialogFragment != null) {
            businessDialogFragment!!.dismiss()
            category.setText(bean.name)
            searchview.setText("")
            if (list.size > 0) {
                list.clear()
                searchAdapter?.notifyDataSetChanged()
                nodatafound.visibility = View.VISIBLE
            }
            apirequest.put("category_id", bean.category_id)
            apirequest.put("subcategory_id", bean.subcategory_id)
            AppUtils.hideSoftKeyboard(this)
        }
    }

    override fun selectedCountryUpdate(bean: CountryModel, fromWhere: String?) {

        if (countryDialogFragment != null) {
            countryDialogFragment!!.dismiss()
            country.setText(bean.name)
            searchview.setText("")
            if (list.size > 0) {
                list.clear()
                searchAdapter?.notifyDataSetChanged()
                nodatafound.visibility = View.VISIBLE
            }
            apirequest.put("country_code", bean.code!!)
            AppUtils.hideSoftKeyboard(this)
        }

    }


    var searchAdapter: SearchAdapterNew? = null
    var list: ArrayList<SearchModel> = java.util.ArrayList()
    var apiService: ApiInterface? = null
    val TAG: String = SearchActivity::class.java.simpleName
    var apirequest: MutableMap<String, String> = mutableMapOf()
    var countryDialogFragment: CountryDialogFragment? = null
    var businessDialogFragment: BusinessCategoryDialogFragment? = null
    var manager: FragmentManager? = null


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.category -> {
                showBusinesscatSelectionDialog("")
            }
            R.id.country -> {
                showCountrySelectionDialog("")
            }
        }
    }

    private fun showCountrySelectionDialog(region_id: String) {
        countryDialogFragment = CountryDialogFragment.newInstance("")
        manager = supportFragmentManager
        countryDialogFragment!!.show(manager, "My Dialog")
    }

    private fun showBusinesscatSelectionDialog(region_id: String) {
        businessDialogFragment = BusinessCategoryDialogFragment.newInstance(region_id)
        manager = supportFragmentManager
        businessDialogFragment!!.show(manager, "My Dialog")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_customer)
        init()
        setToolBar()
    }

    fun init() {
        apiService = ApiClient.client.create(ApiInterface::class.java)

        searchAdapter = SearchAdapterNew(this@SearchActivity, list) { offerModel ->
            AppLog.e(TAG, offerModel.toString())
            callProfile(offerModel.category_id?.toInt()!!, offerModel.subcategory_id?.toInt()!!, offerModel)
            finish()
        }
        recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = (searchAdapter)

        getCustomerNotificationList()
    }


    fun setToolBar() {
        val appbar: AppBarLayout = findViewById<AppBarLayout>(R.id.toolbar)
        val mTitle = appbar.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle.text = getString(R.string.search)
        val iv_back = appbar.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        category.setOnClickListener(this)
        country.setOnClickListener(this)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        SlideAnimationUtill.slideBackAnimation(this)
    }


    fun getCustomerNotificationList() {
        if (AppUtils.isInternetConnected(this)) {
            val obs = RxTextView.textChanges(searchview)
                    .filter { charSequence -> charSequence.length > 0 }
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .map<String> { charSequence -> charSequence.toString() }
                obs.subscribe { string ->

                    runOnUiThread {
                        progress.visibility = View.VISIBLE
                        nodatafound.visibility = View.GONE
                    }


                ProgressDialogLoader.progressDialogCreation(this@SearchActivity, resources.getString(R.string.please_wait))
                apirequest.put("keyword", string)
                AppLog.e(TAG, "debounced $string")
                apiService?.searchBusiness("Bearer " + SharedPreferenceManager.getAuthToken(), apirequest)
                        ?.subscribeOn(io())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(object : Observer<SearchResponse> {
                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(detailResponse: SearchResponse) {
                                AppLog.e(TAG, detailResponse.toString())
                                runOnUiThread { progress.visibility = View.GONE }
                                ProgressDialogLoader.progressDialogDismiss()
                                if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                    if (detailResponse.data.size > 0) {
                                        nodatafound.visibility = View.GONE
                                        list.clear()
                                        list.addAll(detailResponse.data)
                                        searchAdapter?.notifyDataSetChanged()
                                    } else {
                                        list.clear()
                                        searchAdapter?.notifyDataSetChanged()
                                        nodatafound.visibility = View.VISIBLE
                                    }

                                } else {
                                    UiValidator.displayMsgSnack(cord, this@SearchActivity, detailResponse.message)
                                }
                            }

                            override fun onError(e: Throwable) {
                                runOnUiThread { progress.visibility = View.GONE }
                                ProgressDialogLoader.progressDialogDismiss()
                                if (e != null)
                                    AppLog.e(TAG, e?.message)
                            }

                            override fun onComplete() {}
                        })
            }
        } else {
            UiValidator.displayMsgSnack(cord, this, getString(R.string.no_internet_connection))
        }
    }

    fun callProfile(catergoty: Int, subcatergoty: Int, offerModel: SearchModel) {
        when (catergoty) {
            AppConstants.CAT_TRANSPORTATION -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_TRANS_TAXI -> callEditActivity(offerModel, BookRideTaxiActivity())
                    AppConstants.SUBCAT_TRANS_LIMO -> callEditActivity(offerModel, BookRideLimoActivity())
                    AppConstants.SUBCAT_TRANS_TOURBUS -> callEditActivity(offerModel, BookRideTourBusActivity())
                    AppConstants.SUBCAT_TRANS_SIGHTSEEING -> callEditActivity(offerModel, BookRideSightSeenActivity())
                }
            }
            AppConstants.CAT_RESTAURANT_DINNIG -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_RESTAURANT_DINNIG -> callEditActivity(offerModel, TableBookingActivity())
                }
            }
            AppConstants.CAT_HEALTH_BODYCARE -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_HEALTH_DOCTOR -> callEditActivity(offerModel, BookAppointmentDoctorActivity())
                    AppConstants.SUBCAT_HEALTH_HAIR -> callEditActivity(offerModel, BookAppointmentHairActivity())
                    AppConstants.SUBCAT_HEALTH_NAIL -> callEditActivity(offerModel, BookAppointmentNailActivity())
                    AppConstants.SUBCAT_HEALTH_PHYSIO -> callEditActivity(offerModel, BookAppointmentMassageActivity())
                }
            }
            AppConstants.CAT_PARKING_VALET -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_PARKING_VALET -> callEditActivity(offerModel, ParkingValetActivity())
                }
            }
            AppConstants.CAT_EVENT_TICKET -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_EVENT_TICKET -> callEditActivity(offerModel, EventsActivity())
                }
            }
            AppConstants.CAT_STORE_SELLER -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_STORE_SELLER -> callEditActivity(offerModel, PurchaseItemsActivity())
                }
            }
        }
    }

    fun callEditActivity(keyValue: SearchModel, activity1: AppCompatActivity) {
        var intent: Intent = Intent(this, activity1.javaClass)
        var bundle = Bundle()
        bundle.putSerializable(AppConstants.BUSINESS_DATA, keyValue)
        bundle.putBoolean("isFromSearch", true)
        intent.putExtras(bundle)
        startActivityForResult(intent, 222)
        SlideAnimationUtill.slideNextAnimation(this)
    }


}