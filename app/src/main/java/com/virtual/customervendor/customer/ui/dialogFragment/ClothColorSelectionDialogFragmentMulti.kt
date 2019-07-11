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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.adapter.ClothSelectionSelectionAdapterMulti
import com.virtual.customervendor.customview.CustomEditText
import com.virtual.customervendor.model.response.ClothColorResponse
import com.virtual.customervendor.model.response.StoreClothColorModel
import com.virtual.customervendor.model.response.StoreClothSizeModel
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class ClothColorSelectionDialogFragmentMulti : DialogFragment() {
    var regionSelectionAdapterMulti: ClothSelectionSelectionAdapterMulti? = null
    private var rv_countryList: RecyclerView? = null
    private var et_searchText: EditText? = null
    private var tv_ok_btn: CustomEditText? = null
    private var progress_select_region: ProgressBar? = null

    private var mActivity: Activity? = null
    private var mView: View? = null
    private var serviceModelList = ArrayList<StoreClothColorModel>()
    private var mregionSelectionInterface: MultiColorSelectionInterface? = null
    private var fromWhere: String? = null
    private var TAG: String? = ClothColorSelectionDialogFragmentMulti::class.java.name
    var apiService: ApiInterface? = null
    var JsonSizeArr : String?=""
    var JsonSelectedArr : String?=""


    companion object {
        fun newInstance(from: String,JsonSizeArr: String,JsonSelectedArr: String): ClothColorSelectionDialogFragmentMulti {
            val args = Bundle()
            args.putString("name", from)
            args.putString("JsonSizeArr", JsonSizeArr)
            args.putString("JsonSelectedArr", JsonSelectedArr)
//            args.putInt("value", value)
            val fragment = ClothColorSelectionDialogFragmentMulti()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = arguments?.getString("name")
        AppLog.e(TAG, name)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        mActivity = activity
        fromWhere=name

        JsonSizeArr = arguments?.getString("JsonSizeArr")
        JsonSelectedArr = arguments?.getString("JsonSelectedArr")


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_vehicle_dialog_multi, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
//        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)
        progress_select_region = mView!!.findViewById(R.id.progress_select_region) as ProgressBar


        val listType = object : TypeToken<ArrayList<StoreClothColorModel>>() {

        }.type

        val regionResponse: ArrayList<StoreClothColorModel> = Gson().fromJson(JsonSizeArr, listType)
        if (regionResponse != null&&regionResponse.size>0) {
            initializeUIComponent(regionResponse)
            progress_select_region?.visibility = View.GONE
            rv_countryList!!.visibility=View.VISIBLE
        }else{
            fetchRegion()

        }


        return mView
    }


    private fun initializeUIComponent(regionResponse: ArrayList<StoreClothColorModel>) {
        rv_countryList = mView!!.findViewById(R.id.rv_countryList) as RecyclerView
        et_searchText = mView!!.findViewById(R.id.et_searchText) as EditText
        tv_ok_btn = mView!!.findViewById(R.id.tv_ok_btn) as CustomEditText
//        progress_select_region = mView!!.findViewById(R.id.progress_select_region) as ProgressBar

        tv_ok_btn!!.setOnClickListener { mregionSelectionInterface!!.doneColor(serviceModelList, this!!.fromWhere) }
        regionSelectionAdapterMulti = ClothSelectionSelectionAdapterMulti(activity!!, regionResponse) { serviceModel ->
            serviceModelList = serviceModel

        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        rv_countryList?.layoutManager = manager
        rv_countryList?.adapter = regionSelectionAdapterMulti
        performSearch()

    }


    private fun handleResults(regionResponse: ClothColorResponse) {
        isCancelable = true
        progress_select_region?.visibility = View.GONE
        if (regionResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            initializeUIComponent(regionResponse.data)
        } else {
//            UiValidator.displayMsgSnack(main_cord, context as DashBoardActivity, cityResponse.message)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mregionSelectionInterface = context as MultiColorSelectionInterface
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
                regionSelectionAdapterMulti!!.filter.filter(searchText)
            }
        })
    }

    fun fetchRegion() {
        if (AppUtils.isInternetConnected(activity)) {
            isCancelable = false
            progress_select_region?.visibility = View.VISIBLE
            apiService!!.getClothColor().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ClothColorResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(regionResponse: ClothColorResponse) {
                            AppLog.e(TAG, regionResponse.toString())



                            val listType = object : TypeToken<ArrayList<StoreClothColorModel>>() {

                            }.type

                            val regionSelectedResponse: ArrayList<StoreClothColorModel> = Gson().fromJson(JsonSelectedArr, listType)

                            var i = 0
                            var j = 0
                            while (i < regionSelectedResponse.size) {

                                var isPresent = false
                                var p = 0
                                while (j < regionResponse.data.size) {
                                    if (regionSelectedResponse[i].name.equals(regionResponse.data[j].name)) {
                                        isPresent = true
                                        p = j
                                        break

                                    }
                                    j++


                                }

                                if (isPresent) {
                                    regionResponse.data.set(p,regionSelectedResponse[i])
                                    regionResponse.data[p].isSelected = true
                                }


                                i++
                            }



                            handleResults(regionResponse)
                        }

                        override fun onError(e: Throwable) {
                            handleError(e)

                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsg(activity, getString(R.string.no_internet_connection))
        }
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

    interface MultiColorSelectionInterface {
        fun doneColor(bean: ArrayList<StoreClothColorModel>, fromWhere: String?)
    }

}