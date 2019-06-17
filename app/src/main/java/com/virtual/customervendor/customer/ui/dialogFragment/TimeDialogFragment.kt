package com.virtual.customervendor.customer.ui.dialogFragment

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ProgressBar
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.adapter.TimeSelectionAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CustomerTimeModel
import com.virtual.customervendor.model.request.CustomerTimeSlotRequest
import com.virtual.customervendor.model.response.CustomerTimeSlotResponse
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


class TimeDialogFragment : DialogFragment(), View.OnClickListener {
    var timeSelectionAdapter: TimeSelectionAdapter? = null
    private var rv_countryList: RecyclerView? = null
    private var mActivity: Activity? = null
    private var mView: View? = null
    private var mCitySelectionInterface: timeSelectionInterface? = null
    private var fromWhere: String? = null
    private var TAG: String? = TimeDialogFragment::class.java.name
    var apiService: ApiInterface? = null
    private var progress_select_region: ProgressBar? = null


    companion object {

        fun newInstance(customerBookingListModel: CustomerTimeSlotRequest): TimeDialogFragment {
            val args = Bundle()
            args.putSerializable("FROM", customerBookingListModel)

//            args.putInt("value", value)
            val fragment = TimeDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_searchView -> {
//                performSearch()
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        var Measuredwidth = 0
//        var Measuredheight = 0
//        val size = Point()
//        val w = activity?.getWindowManager()
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            w?.getDefaultDisplay()?.getSize(size)
//            Measuredwidth = size.x
//            Measuredheight = size.y
//        } else {
//            val d = w?.getDefaultDisplay()
//            Measuredwidth = d!!.getWidth()
//            Measuredheight = d!!.getHeight()
//        }
//        val window = dialog.window
//        window!!.setLayout(Measuredwidth - 20, (Measuredheight * 2) / 3)
//        window.setGravity(Gravity.CENTER)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val request = arguments?.getSerializable("FROM") as CustomerTimeSlotRequest

        AppLog.e(TAG, request.toString())
        mActivity = activity as Activity?
        apiService = ApiClient.client.create(ApiInterface::class.java)
        getTimeSlot(request)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_time_dialog, container, false)
        progress_select_region = mView!!.findViewById(R.id.progress_select_region) as ProgressBar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        rl_searchView.setOnClickListener(this)
    }


    private fun initializeUIComponent(mView: View, cityResponse: ArrayList<CustomerTimeModel>) {
        rv_countryList = mView.findViewById(R.id.rv_countryList) as RecyclerView

        timeSelectionAdapter = TimeSelectionAdapter(activity!!, cityResponse) { offerModel ->
            mCitySelectionInterface!!.selectedTimeUpdate(offerModel, this!!.fromWhere)
        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_countryList?.layoutManager = manager
        rv_countryList?.adapter = timeSelectionAdapter
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCitySelectionInterface = context as timeSelectionInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("Error in retrieving data. Please try again")
        }
    }


    fun getTimeSlot(request: CustomerTimeSlotRequest) {
        if (AppUtils.isInternetConnected(activity)) {
            apiService?.getServiceTimeSLOT("Bearer " + SharedPreferenceManager.getAuthToken(), request)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Observer<CustomerTimeSlotResponse> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onNext(detailResponse: CustomerTimeSlotResponse) {
                            AppLog.e(TAG, detailResponse.toString())
                            handleResults(detailResponse)
                        }

                        override fun onError(e: Throwable) {
                            progress_select_region?.visibility = View.GONE
                            if (e != null)
                            AppLog.e(TAG, e.message)
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsg(activity, getString(R.string.no_internet_connection))
        }

    }


    private fun handleResults(cityResponse: CustomerTimeSlotResponse) {
        progress_select_region?.visibility = View.GONE
        if (cityResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            initializeUIComponent(mView!!, cityResponse.data)
        } else {
//            UiValidator.displayMsgSnack(main_cord, context as DashBoardActivity, cityResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        AppLog.e(TAG, "UserDetails Registration Error" + t.toString())
    }


    override fun onDetach() {
        mCitySelectionInterface = null
        super.onDetach()
    }

    interface timeSelectionInterface {
        fun selectedTimeUpdate(bean: CustomerTimeModel, fromWhere: String?)
    }
}