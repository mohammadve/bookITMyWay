package com.virtual.customervendor.customer.ui.dialogFragment

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.adapter.CountyrSelectionAdapter
import com.virtual.customervendor.model.CountryModel
import com.virtual.customervendor.model.response.CountryResponse
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
import kotlinx.android.synthetic.main.fragment_country_dialog.*


class CountryDialogFragment : DialogFragment(), View.OnClickListener {
    var citySelectionAdapter: CountyrSelectionAdapter? = null
    private var rv_countryList: RecyclerView? = null
    private var et_searchText: EditText? = null
    private var progress_select_city: ProgressBar? = null

    private var mView: View? = null
    private var mCitySelectionInterface: countrySelectionInterface? = null
    private var fromWhere: String? = null
    private var TAG: String? = CountryDialogFragment::class.java.name
    var apiService: ApiInterface? = null


    companion object {

        fun newInstance(allCountry: String): CountryDialogFragment {
            val args = Bundle()
            args.putString("allcountry", allCountry)
//            args.putInt("value", value)
            val fragment = CountryDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_searchView -> {
                performSearch()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val country = arguments?.getString("allcountry")
        AppLog.e(TAG, country + "--")

        apiService = ApiClient.client.create(ApiInterface::class.java)

        fetchCountry(country!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_country_dialog, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)

        performSearch()
        return mView
    }

    override fun onViewCreated(mView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(mView, savedInstanceState)
        rl_searchView.setOnClickListener(this)
        rv_countryList = mView.findViewById(R.id.rv_countryList) as RecyclerView
        et_searchText = mView.findViewById(R.id.et_searchText) as EditText
        progress_select_city = mView.findViewById(R.id.progress_select_city) as ProgressBar
    }


    private fun initializeUIComponent(mView: View, cityResponse: ArrayList<CountryModel>, country: String) {

        if (country != null && country.equals("all")) {
            cityResponse.add(0, CountryModel("All Country", ""))
        }
        citySelectionAdapter = CountyrSelectionAdapter(activity!!, cityResponse) { offerModel ->
            var imm = et_searchText?.getContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive())
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            mCitySelectionInterface!!.selectedCountryUpdate(offerModel, this!!.fromWhere)
        }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_countryList?.layoutManager = manager
        rv_countryList?.adapter = citySelectionAdapter
        performSearch()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCitySelectionInterface = context as countrySelectionInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("Error in retrieving data. Please try again")
        }
    }

    fun fetchCountry(country: String) {
        if (AppUtils.isInternetConnected(activity)) {
            progress_select_city?.visibility = View.VISIBLE
            isCancelable = false
            apiService!!.getCountries().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CountryResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(cityResponse: CountryResponse) {
                            AppLog.e(TAG, cityResponse.toString())
                            handleResults(cityResponse, country)
                        }

                        override fun onError(e: Throwable) {
                            if (e != null) {
                                AppLog.e(TAG, "error" + e.message)
                            }
//                        handleError(e)
                            isCancelable = true
                            progress_select_city?.visibility = View.GONE

                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")


                        }
                    })
        } else {
            UiValidator.displayMsg(activity, getString(R.string.no_internet_connection))
        }
    }


    private fun handleResults(cityResponse: CountryResponse, country: String) {


        if (cityResponse.status.equals(AppConstants.KEY_SUCCESS)) {
            initializeUIComponent(mView!!, cityResponse.data, country)
            isCancelable = true
            progress_select_city?.visibility = View.GONE
            rv_countryList?.visibility = View.VISIBLE
        } else {
//            UiValidator.displayMsgSnack(main_cord, context as DashBoardActivity, cityResponse.message)
        }
    }

    private fun handleError(t: Throwable) {
        isCancelable = true
        progress_select_city?.visibility = View.GONE
        rv_countryList?.visibility = View.VISIBLE
        AppLog.e(TAG, "UserDetails Registration Error" + t.toString())
    }

    private fun performSearch() {
        et_searchText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val searchText = editable.toString()
                citySelectionAdapter!!.filter.filter(searchText)
            }
        })
    }

    override fun onDetach() {
        mCitySelectionInterface = null
        super.onDetach()
    }

    interface countrySelectionInterface {
        fun selectedCountryUpdate(bean: CountryModel, fromWhere: String?)
    }
}