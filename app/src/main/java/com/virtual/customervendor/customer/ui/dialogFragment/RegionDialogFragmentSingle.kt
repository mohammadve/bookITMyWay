package com.virtual.customervendor.customer.ui.dialogFragment

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.adapter.RegionSelectionAdapterSingle
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.response.RegionResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class RegionDialogFragmentSingle : DialogFragment() {
    var regionSelectionAdaptersingle: RegionSelectionAdapterSingle? = null
    private var rv_countryList: RecyclerView? = null
    private var et_searchText: EditText? = null

    private var progress_select_region: ProgressBar? = null
    private var mActivity: Activity? = null
    private var mView: View? = null
    private var sregionSelectionInterface: SingleRegionSelectionInterface? = null
    private var fromWhere: String? = null
    private var TAG: String? = RegionDialogFragmentSingle::class.java.name
    var apiService: ApiInterface? = null


    companion object {
        fun newInstance(from: String, allregion: String): RegionDialogFragmentSingle {
            val args = Bundle()
            args.putString("name", from)
            args.putString("allregion", allregion)
//            args.putInt("value", value)
            val fragment = RegionDialogFragmentSingle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = arguments?.getString("name")
        val region = arguments?.getString("allregion")
        AppLog.e(TAG, name + "--" + region)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        mActivity = activity!!
        fetchRegion(name!!, region!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_vehicle_dialog_single, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
//        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)
        progress_select_region = mView!!.findViewById(R.id.progress_select_region) as ProgressBar

        return mView
    }


    private fun initializeUIComponent(regionResponse: ArrayList<RegionModel>, region: String) {
        rv_countryList = mView!!.findViewById(R.id.rv_countryList) as RecyclerView
        et_searchText = mView!!.findViewById(R.id.et_searchText) as EditText

        et_searchText?.requestFocus()
        if (region != null && region.equals("all")) {
            regionResponse.add(0, RegionModel("", "All Region", false))
        }
        regionSelectionAdaptersingle = RegionSelectionAdapterSingle(activity!!, regionResponse) { serviceModel ->
            //            var imm = mView?.getContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            if (imm.isActive())
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)


            sregionSelectionInterface!!.regionSelectionInterface(serviceModel, this!!.fromWhere)
        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        rv_countryList?.layoutManager = manager
        rv_countryList?.adapter = regionSelectionAdaptersingle
        performSearch()
    }


    private fun handleResults(regionResponse: RegionResponse, region: String) {
        isCancelable = true
        progress_select_region?.visibility = View.GONE
        if (regionResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            initializeUIComponent(regionResponse.data, region)
        } else {
//            UiValidator.displayMsgSnack(main_cord, context as DashBoardActivity, cityResponse.message)

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            sregionSelectionInterface = context as SingleRegionSelectionInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("Error in retrieving data. Please try again")
        }

    }

    private fun performSearch() {
        et_searchText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val searchText = editable.toString()
                regionSelectionAdaptersingle!!.filter.filter(searchText)
            }
        })
    }

    fun fetchRegion(regioncode: String, region: String) {
        if (AppUtils.isInternetConnected(activity)) {
            isCancelable = false
            progress_select_region?.visibility = View.VISIBLE
            apiService!!.getRegion(regioncode).subscribeOn(Schedulers.io())
//            apiService!!.getRegion("IN").subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<RegionResponse> {
                        override fun onSubscribe(d: Disposable) {

                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(regionResponse: RegionResponse) {
                            AppLog.e(TAG, regionResponse.toString())
                            handleResults(regionResponse, region)
                        }

                        override fun onError(e: Throwable) {
                            if (e != null) {
                                AppLog.e(TAG, e.message)
                            }
                            isCancelable = true
                            progress_select_region?.visibility = View.GONE
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsg(activity, getString(R.string.no_internet_connection))
        }
    }


    override fun onDetach() {
        sregionSelectionInterface = null
        super.onDetach()
    }

    interface SingleRegionSelectionInterface {
        fun regionSelectionInterface(bean: RegionModel, fromWhere: String?)
    }

}