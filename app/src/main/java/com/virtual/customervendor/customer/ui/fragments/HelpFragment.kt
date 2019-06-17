package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.response.BusinessOrderDetailResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.fragments.IssueDialogFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_help_customer.*


class HelpFragment : Fragment(), View.OnClickListener {
    private var stringList = ArrayList<String>()
    var apiService: ApiInterface? = null
    val TAG: String = HelpFragment::class.java.simpleName

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btn_send_help -> {
                UiValidator.hideSoftKeyboard(activity as AppCompatActivity)
                if (tv_issue.text.equals("Select Issue")) {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.please_select_issue))
                } else if (et_issue_des.text.isEmpty()) {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.please_enter_issue_description))
                } else {
                    if (AppUtils.isInternetConnected(activity)) {
                        submithelpApi(tv_issue.text.toString(), et_issue_des.text.toString())
                    } else {
                        UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
                    }
                }

            }

            R.id.tv_issue -> {
                showVendorSelectionDialog()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_help_customer, container, false)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_send_help.setOnClickListener(this)
        tv_issue.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        initView(view)
    }

    fun initView(view: View) {
        stringList.add("Adding service")
        stringList.add("Adding bussiness")
        stringList.add("Adding vendor")
        stringList.add("Taxi or other service Booking")
        stringList.add("Login or signup")
        stringList.add("Other")
    }


    private var userSwitchFragment: IssueDialogFragment? = null
    var manager: FragmentManager? = null
    private fun showVendorSelectionDialog() {
        userSwitchFragment = IssueDialogFragment.newInstance(stringList)
        manager = childFragmentManager
        userSwitchFragment!!.show(manager, "My Dialog")
    }

    fun updateText(selectedText: String) {
        tv_issue.text = selectedText
    }

    fun submithelpApi(title: String, message: String) {
        ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))
        apiService?.submitHelpQuery("Bearer " + SharedPreferenceManager.getAuthToken(), title, message)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<BusinessOrderDetailResponse> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(detailResponse: BusinessOrderDetailResponse) {
                        AppLog.e(TAG, detailResponse.toString())
                        ProgressDialogLoader.progressDialogDismiss()
                        if (detailResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                            UiValidator.displayMsgSnack(cons, activity, detailResponse.message)
                            handleResult()
                        } else {
                            UiValidator.displayMsgSnack(cons, activity, detailResponse.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        ProgressDialogLoader.progressDialogDismiss()

                        if (e != null)
                            AppLog.e(TAG, e?.message)
                    }

                    override fun onComplete() {

                    }
                })
    }

    fun handleResult() {
        tv_issue.text = ""
        et_issue_des.setText("")
    }

}