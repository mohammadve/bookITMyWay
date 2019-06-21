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
import com.virtual.customervendor.customer.ui.adapter.ServiceSelectionAdapterMulti
import com.virtual.customervendor.customview.CustomEditText
import com.virtual.customervendor.model.ItemPriceModel
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.AppLog


class ServiceSlectionDialogFragmentMulti : DialogFragment() {
    var serviceSelectionAdapterMulti: ServiceSelectionAdapterMulti? = null
    private var rv_countryList: RecyclerView? = null
    private var et_searchText: EditText? = null
    private var tv_ok_btn: CustomEditText? = null
    private var progress_select_region: ProgressBar? = null

    private var mActivity: Activity? = null
    private var mView: View? = null
    private var serviceModelList = ArrayList<ItemPriceModel>()
    private var mregionSelectionInterface: MultiServiceSelectionInterfaceService? = null
    private var fromWhere: String? = null
    private var TAG: String? = ServiceSlectionDialogFragmentMulti::class.java.name
    var apiService: ApiInterface? = null

    var servicemenu: ArrayList<ItemPriceModel> = ArrayList<ItemPriceModel>()

    companion object {
        fun newInstance(from: String, serviceMenu: ArrayList<ItemPriceModel>): ServiceSlectionDialogFragmentMulti {
            val args = Bundle()
            args.putString("name", from)
            args.putSerializable("serviceJson", serviceMenu)
//            args.putInt("value", value)
            val fragment = ServiceSlectionDialogFragmentMulti()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = arguments?.getString("name")
         servicemenu = arguments?.getSerializable("serviceJson") as ArrayList<ItemPriceModel>


        AppLog.e(TAG, name)
        mActivity = activity

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_service_selection_dialog_multi, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
//        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)
        progress_select_region = mView!!.findViewById(R.id.progress_select_region) as ProgressBar
        handleResults();
        return mView
    }


    private fun initializeUIComponent() {
        rv_countryList = mView!!.findViewById(R.id.rv_countryList) as RecyclerView
        et_searchText = mView!!.findViewById(R.id.et_searchText) as EditText
        tv_ok_btn = mView!!.findViewById(R.id.tv_ok_btn) as CustomEditText
//        progress_select_region = mView!!.findViewById(R.id.progress_select_region) as ProgressBar

        tv_ok_btn!!.setOnClickListener {


            val filledList: ArrayList<ItemPriceModel> = ArrayList()

            for (temp in serviceModelList) {
                if (temp.isSelected) {
                    filledList.add(temp)

                }
            }
            mregionSelectionInterface!!.done(filledList, this!!.fromWhere)
            dismiss()
        }
        serviceSelectionAdapterMulti = ServiceSelectionAdapterMulti(activity!!, servicemenu) { serviceModel ->
            serviceModelList = serviceModel


        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        rv_countryList?.layoutManager = manager
        rv_countryList?.adapter = serviceSelectionAdapterMulti
        performSearch()

    }


    private fun handleResults() {
        isCancelable = true
        progress_select_region?.visibility = View.GONE

        initializeUIComponent()

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mregionSelectionInterface = context as MultiServiceSelectionInterfaceService
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
                serviceSelectionAdapterMulti!!.filter.filter(searchText)
            }
        })
    }


    private fun handleError(t: Throwable) {
        isCancelable = true
        progress_select_region?.visibility = View.GONE
        if (t != null) {
            AppLog.e(TAG, "UserDetails Registration Error" + t.toString())
        }
    }

    override fun onDetach() {
        mregionSelectionInterface = null
        super.onDetach()
    }

    interface MultiServiceSelectionInterfaceService {
        fun done(bean: ArrayList<ItemPriceModel>, fromWhere: String?)
    }

}