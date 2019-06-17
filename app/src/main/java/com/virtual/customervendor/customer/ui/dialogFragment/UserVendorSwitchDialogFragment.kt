package com.virtual.customervendor.customer.ui.dialogFragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.adapter.UserVendorSelectionAdapter
import com.virtual.customervendor.customview.CustomTextView
import com.virtual.customervendor.model.BusinessDetail
import com.virtual.customervendor.utills.AppLog


class UserVendorSwitchDialogFragment : DialogFragment() {
    var selectionAdapter: UserVendorSelectionAdapter? = null
    private var rv_countryList: RecyclerView? = null
    var txt_anotherbus: CustomTextView? = null
    //    private var et_searchText: EditText? = null
    private var mView: View? = null
    private var uservendorInterface: vendorSelectionInterface? = null
    private var fromWhere: String? = null
    private var TAG: String? = UserVendorSwitchDialogFragment::class.java.name
    private var listBusiness = ArrayList<BusinessDetail>()
    private var mActivity: Activity? = null


    companion object {
        fun newInstance(listbusiness: ArrayList<BusinessDetail>): UserVendorSwitchDialogFragment {
            var listBusiness = listbusiness
            val args = Bundle()
            args.putSerializable("List", listBusiness)
            val fragment = UserVendorSwitchDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listBusiness = arguments?.getSerializable("List") as ArrayList<BusinessDetail>
        mActivity = activity
        AppLog.e(TAG, listBusiness.toString())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_user_vendor_switch_dialog, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)
        initializeUIComponent(mView!!, listBusiness)
        return mView
    }


    private fun initializeUIComponent(mView: View, businessList: ArrayList<BusinessDetail>) {
        rv_countryList = mView.findViewById(R.id.rv_countryList) as RecyclerView
        txt_anotherbus = mView.findViewById(R.id.txt_anotherbus) as CustomTextView

        if (businessList != null && businessList.size < 5) {
            txt_anotherbus?.visibility = View.GONE
        } else {
            txt_anotherbus?.visibility = View.GONE
        }

        txt_anotherbus?.setOnClickListener(View.OnClickListener { uservendorInterface!!.addAnotherBusiness() })

        selectionAdapter = UserVendorSelectionAdapter(activity!!, businessList) { offerModel ->
            uservendorInterface!!.selectedVendorUpdate(offerModel, this.fromWhere)
        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_countryList?.layoutManager = manager
        rv_countryList?.adapter = selectionAdapter
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            uservendorInterface = context as vendorSelectionInterface
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDetach() {
        uservendorInterface = null
        super.onDetach()
    }

    interface vendorSelectionInterface {
        fun selectedVendorUpdate(bean: BusinessDetail, fromWhere: String?)
        fun addAnotherBusiness()
    }
}