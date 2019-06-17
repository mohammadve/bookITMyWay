package com.virtual.customervendor.vendor.ui.fragments

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.EditText
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.adapter.IssueSelectionAdapter
import com.virtual.customervendor.utills.AppLog


class IssueDialogFragment : DialogFragment() {
    private var rv_countryList: RecyclerView? = null

    var selectionAdapter: IssueSelectionAdapter? = null
    private var et_searchText: EditText? = null
    private var mView: View? = null
    private var uservendorInterface: issueSelectionInterface? = null
    private var fromWhere: String? = null
    private var TAG: String? = IssueDialogFragment::class.java.name
    private var listStrings = ArrayList<String>()
    private var mActivity: Activity? = null


    companion object {

        fun newInstance(listString: ArrayList<String>): IssueDialogFragment {
            var listStrings = listString
            val args = Bundle()
            args.putSerializable("List", listStrings)
            val fragment = IssueDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listStrings = arguments?.getSerializable("List") as ArrayList<String>
        mActivity = activity
        AppLog.e(TAG, listStrings.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_issue_dialog, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)

        initializeUIComponent(mView!!)
        return mView
    }


    private fun initializeUIComponent(mView: View) {
        rv_countryList = mView.findViewById(R.id.rv_countryList) as RecyclerView
        et_searchText = mView.findViewById(R.id.et_searchText) as EditText

        selectionAdapter = IssueSelectionAdapter(activity!!, listStrings) { offerModel ->
            uservendorInterface!!.selectedIssueUpdate(offerModel, this.fromWhere)
            dismiss()
        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_countryList ?.layoutManager = manager
        rv_countryList?.adapter = selectionAdapter

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            uservendorInterface = context as issueSelectionInterface
        } catch (e: Exception) {
           e.printStackTrace()
        }
    }


    override fun onDetach() {
        uservendorInterface = null
        super.onDetach()
    }

    interface issueSelectionInterface {
        fun selectedIssueUpdate(bean: String, fromWhere: String?)
    }
}