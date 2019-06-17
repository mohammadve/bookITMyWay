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
import com.virtual.customervendor.customer.ui.adapter.StoreServiceAreaAdapter
import com.virtual.customervendor.model.StoreItemLocationModel
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.AppLog


class StoreServiceAreaFragment : DialogFragment() {
    var regionSelectionAdaptersingle: StoreServiceAreaAdapter? = null
    private var rv_countryList: RecyclerView? = null
    private var et_searchText: EditText? = null
    private var progress_select_region: ProgressBar? = null
    private var mActivity: Activity? = null
    private var mView: View? = null
    private var sregionSelectionInterface: StoreServiceInterface? = null
    private var TAG: String? = StoreServiceAreaFragment::class.java.name
    var apiService: ApiInterface? = null
    var datalist = ArrayList<StoreItemLocationModel>()


    companion object {
        fun newInstance(list: ArrayList<StoreItemLocationModel>): StoreServiceAreaFragment {
            val args = Bundle()
            args.putSerializable("datalist",list)
            val fragment = StoreServiceAreaFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datalist = arguments?.getSerializable("datalist") as ArrayList<StoreItemLocationModel>
        AppLog.e(TAG,  "--" + datalist.toString())
        apiService = ApiClient.client.create(ApiInterface::class.java)
        mActivity = activity

//        fetchRegion(name!!, region!!)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleResults(datalist)
    }


    private fun initializeUIComponent(regionResponse: ArrayList<StoreItemLocationModel>) {
        rv_countryList = mView!!.findViewById(R.id.rv_countryList) as RecyclerView
        et_searchText = mView!!.findViewById(R.id.et_searchText) as EditText

        et_searchText?.requestFocus()
        regionSelectionAdaptersingle = StoreServiceAreaAdapter(activity!!, regionResponse) { serviceModel ->

            sregionSelectionInterface!!.storeSelectionInterface(serviceModel)
        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        rv_countryList?.layoutManager = manager
        rv_countryList?.adapter = regionSelectionAdaptersingle
        performSearch()
    }


    private fun handleResults(list :ArrayList<StoreItemLocationModel>) {
        isCancelable = true
        progress_select_region?.visibility = View.GONE
            initializeUIComponent(list)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            sregionSelectionInterface = context as StoreServiceInterface
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

    override fun onDetach() {
        sregionSelectionInterface = null
        super.onDetach()
    }

    interface StoreServiceInterface {
        fun storeSelectionInterface(bean: StoreItemLocationModel)
    }

}