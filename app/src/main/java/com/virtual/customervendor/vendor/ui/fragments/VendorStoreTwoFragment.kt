package com.virtual.customervendor.vendor.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.StoreCategoryModel
import com.virtual.customervendor.model.StoreItemLocationModel
import com.virtual.customervendor.model.request.VendorStoreServiceRequest
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorLimoActivity
import com.virtual.customervendor.vendor.ui.activity.VendorStoreActivity
import com.virtual.customervendor.vendor.ui.activity.VendorTaxiActivity
import com.virtual.customervendor.vendor.ui.activity.VendorTourBusActivity
import com.virtual.customervendor.vendor.ui.adapter.StoreClothTypeAdapter
import com.virtual.customervendor.vendor.ui.adapter.StoreDeliverLocationAdapter
import kotlinx.android.synthetic.main.fragment_store_two_vendor.*
import kotlinx.android.synthetic.main.fragment_store_two_vendor.btn_next
import kotlinx.android.synthetic.main.fragment_store_two_vendor.ed_desc
import kotlinx.android.synthetic.main.fragment_store_two_vendor.nest
import kotlinx.android.synthetic.main.fragment_store_two_vendor.til_desc
import kotlinx.android.synthetic.main.fragment_taxi_two_vendor.*

class VendorStoreTwoFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView!!.id) {
            R.id.chk_alldays -> {
                handleAlldays(isChecked)
            }
            R.id.chk_monday -> {
                handleDays(isChecked)
            }
            R.id.chk_tuesday -> {
                handleDays(isChecked)
            }
            R.id.chk_wednesday -> {
                handleDays(isChecked)
            }
            R.id.chk_thursday -> {
                handleDays(isChecked)
            }
            R.id.chk_friday -> {
                handleDays(isChecked)
            }
            R.id.chk_saturday -> {
                handleDays(isChecked)
            }
            R.id.chk_sunday -> {
                handleDays(isChecked)
            }
            R.id.chk_24time -> {
                handleTime24(isChecked)
            }
        }
    }

    var datetime: String? = null
    var TAG: String = VendorStoreTwoFragment::class.java.simpleName
    var vendorStoreRequest = VendorStoreServiceRequest()

    var stadiumList: ArrayList<StoreItemLocationModel> = ArrayList()
    var storeClothTypeList: ArrayList<StoreItemLocationModel> = ArrayList()
    var arenaList: ArrayList<StoreItemLocationModel> = ArrayList()
    var otherList: ArrayList<StoreItemLocationModel> = ArrayList()

    var storeAddStadiumAdapter: StoreDeliverLocationAdapter? = null
    var storecClothTypeAdapter: StoreClothTypeAdapter? = null


    var storeAddArenaAdapter: StoreDeliverLocationAdapter? = null
    var storeAddOtherAdapter: StoreDeliverLocationAdapter? = null
    var manager: RecyclerView.LayoutManager? = null
    var dataId = StringBuilder()
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.ed_starttime -> {
                if (chk_24time.isChecked()) chk_24time.isChecked = false

                AppUtils.getTimeNew(ed_starttime, activity as AppCompatActivity?)

            }
            R.id.ed_closingtime -> {
                if (chk_24time.isChecked()) chk_24time.isChecked = false
                AppUtils.getTimeNew(ed_closingtime, activity as AppCompatActivity?)
            }
            R.id.btn_next -> {
                validateField()
            }

            R.id.addstadium -> performAddStadium()
            R.id.addClothType -> performAddClothsType()
            R.id.addarena -> performAddArena()
            R.id.addother -> performAddOther()

            R.id.ed_store -> {
                if (context is VendorStoreActivity) {
                    (context as VendorStoreActivity).setDisplayDialog(8, AppConstants.FROM_V_TAXI_CITY, "")
                }
            }
            R.id.edtxt_service_area -> {

                edtxt_service_area.requestFocus()
                if (context is VendorStoreActivity) {
                    if (vendorStoreRequest.business_region_id.regionid != null && !vendorStoreRequest.business_region_id.regionid.equals("")) {

                        AppLog.e("@@@",""+vendorStoreRequest.business_region_id.regionid);

                        (context as VendorStoreActivity).setDisplayDialog(9, AppConstants.FROM_V_TAXI_CITY, "" + vendorStoreRequest.business_region_id.regionid)
                    } else {
                        UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_region))
                    }
                }


            }

        }
    }

    companion object {
        fun newInstance(from: String): VendorStoreTwoFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorStoreTwoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_store_two_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vendorStoreRequest = (context as VendorStoreActivity).getFieldPojo()
        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        ed_store.setOnClickListener(this)
        edtxt_service_area.setOnClickListener(this)
        ed_starttime.setOnClickListener(this)
        ed_closingtime.setOnClickListener(this)
        chk_alldays.setOnCheckedChangeListener(this)
        chk_monday.setOnCheckedChangeListener(this)
        chk_tuesday.setOnCheckedChangeListener(this)
        chk_wednesday.setOnCheckedChangeListener(this)
        chk_thursday.setOnCheckedChangeListener(this)
        chk_friday.setOnCheckedChangeListener(this)
        chk_saturday.setOnCheckedChangeListener(this)
        chk_sunday.setOnCheckedChangeListener(this)
        chk_24time.setOnCheckedChangeListener(this)

        createAdapterClothsType()
        createAdapterStadium()
        createAdapterArena()
        createAdapterOther()

        addstadium.setOnClickListener(this)
        addClothType.setOnClickListener(this)
        addarena.setOnClickListener(this)
        addother.setOnClickListener(this)
        manageFromEdit()
    }

    fun handleAlldays(isChecked: Boolean) {
        if (isChecked) {
            if (chk_monday.isChecked()) chk_monday.isChecked = false
            if (chk_tuesday.isChecked()) chk_tuesday.isChecked = false
            if (chk_wednesday.isChecked()) chk_wednesday.isChecked = false
            if (chk_thursday.isChecked()) chk_thursday.isChecked = false
            if (chk_friday.isChecked()) chk_friday.isChecked = false
            if (chk_saturday.isChecked()) chk_saturday.isChecked = false
            if (chk_sunday.isChecked()) chk_sunday.isChecked = false
        }
    }

    fun handleDays(isChecked: Boolean) {
        if (isChecked) {
            if (chk_alldays.isChecked()) chk_alldays.isChecked = false
        }
    }

    fun handleTime24(isChecked: Boolean) {
        if (isChecked) {
            ed_starttime.setText("")
            ed_closingtime.setText("")
        }
    }

    private fun putAllDataToFieldMap() {
        vendorStoreRequest.all_day = AppUtils.getStatusString(chk_alldays.isChecked)
        vendorStoreRequest.mon = AppUtils.getStatusString(chk_monday.isChecked)
        vendorStoreRequest.tue = AppUtils.getStatusString(chk_tuesday.isChecked)
        vendorStoreRequest.wed = AppUtils.getStatusString(chk_wednesday.isChecked)
        vendorStoreRequest.thu = AppUtils.getStatusString(chk_thursday.isChecked)
        vendorStoreRequest.fri = AppUtils.getStatusString(chk_friday.isChecked)
        vendorStoreRequest.sat = AppUtils.getStatusString(chk_saturday.isChecked)
        vendorStoreRequest.sun = AppUtils.getStatusString(chk_sunday.isChecked)
        vendorStoreRequest.is_24_hours_open = AppUtils.getStatusString(chk_24time.isChecked)
        vendorStoreRequest.start_time = ed_starttime.text.toString()
        vendorStoreRequest.close_time = ed_closingtime.text.toString()
        vendorStoreRequest.description = ed_desc.text.toString()
        vendorStoreRequest.stadium_address = stadiumList
        vendorStoreRequest.arena_address = arenaList
        vendorStoreRequest.other_address = otherList
    }

    private fun getfilledData() {
        try {
            if (vendorStoreRequest != null) {
                ed_store.setText(vendorStoreRequest.storecategory)
                if (AppUtils.getStatusBoolean(vendorStoreRequest.all_day)) {
                    chk_alldays.isChecked = true
                } else {
                    chk_monday.isChecked = AppUtils.getStatusBoolean(vendorStoreRequest.mon)
                    chk_tuesday.isChecked = AppUtils.getStatusBoolean(vendorStoreRequest.tue)
                    chk_wednesday.isChecked = AppUtils.getStatusBoolean(vendorStoreRequest.wed)
                    chk_thursday.isChecked = AppUtils.getStatusBoolean(vendorStoreRequest.thu)
                    chk_friday.isChecked = AppUtils.getStatusBoolean(vendorStoreRequest.fri)
                    chk_saturday.isChecked = AppUtils.getStatusBoolean(vendorStoreRequest.sat)
                    chk_sunday.isChecked = AppUtils.getStatusBoolean(vendorStoreRequest.sun)
                }

                if (AppUtils.getStatusBoolean(vendorStoreRequest.is_24_hours_open)) {
                    chk_24time.isChecked = AppUtils.getStatusBoolean(vendorStoreRequest.is_24_hours_open)
                } else {
                    ed_starttime.setText(vendorStoreRequest.start_time)
                    ed_closingtime.setText(vendorStoreRequest.close_time)
                }
                ed_desc.setText(vendorStoreRequest.description)

                if (vendorStoreRequest.stadium_address.size > 0)
                    stadiumList.addAll(vendorStoreRequest.stadium_address)
                else {
                    stadiumList.add(StoreItemLocationModel())
                }

                if (vendorStoreRequest.arena_address.size > 0)
                    arenaList = vendorStoreRequest.arena_address
                else {
                    arenaList.add(StoreItemLocationModel())
                }

                if (vendorStoreRequest.other_address.size > 0)
                    otherList = vendorStoreRequest.other_address
                else {
                    otherList.add(StoreItemLocationModel())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun validateField() {

        if (ed_store.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_storecategory, getString(R.string.field_required))
            return
        }
        if (til_storecategory.isErrorEnabled()) {
            UiValidator.disableValidationError(til_storecategory)
        }

        if (!(chk_alldays.isChecked || chk_monday.isChecked || chk_tuesday.isChecked || chk_wednesday.isChecked || chk_thursday.isChecked || chk_friday.isChecked || chk_saturday.isChecked || chk_sunday.isChecked)) {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_days_of_service))
            return
        }
        if (!(chk_24time.isChecked || (!ed_starttime.text.toString().isEmpty() && !ed_closingtime.text.toString().isEmpty()))) {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.bussines_hours))
            return
        }
        if (!chk_24time.isChecked) {
            if (!AppUtill.compareTime(ed_starttime.text.toString(), ed_closingtime.text.toString())) {
                UiValidator.displayMsgSnack(nest, activity, getString(R.string.choose_valid_time_slot))
                return
            }
        }
        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }

        getListStadium()
        getListArena()
        getListOther()

        UiValidator.hideSoftKeyboard(context as AppCompatActivity)
        if (activity is VendorStoreActivity) {
            putAllDataToFieldMap()
            if ((activity as VendorStoreActivity).isFromedit()) {
                (activity as VendorStoreActivity).hitApiEdit(vendorStoreRequest)
            } else {
                (activity as VendorStoreActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.review_your_bussiness), false)
            }
        }
    }

    fun getListStadium() {
        var storelistnew = ArrayList<StoreItemLocationModel>()
        for (i in 0 until stadiumList.size) {
            if (!stadiumList.get(i).location_name.equals("")) {
                storelistnew.add(stadiumList.get(i))
            }
        }
        vendorStoreRequest?.stadium_address?.clear()
        vendorStoreRequest?.stadium_address?.addAll(storelistnew)
        AppLog.e(TAG, stadiumList.toString() + "----" + storelistnew.toString() + "---" + vendorStoreRequest?.stadium_address.toString())
    }

    fun getListArena() {
        var storelistnew = ArrayList<StoreItemLocationModel>()
        for (i in 0 until arenaList.size) {
            if (!arenaList.get(i).location_name.equals("")) {
                storelistnew.add(arenaList.get(i))
            }
        }
        vendorStoreRequest?.arena_address?.clear()
        vendorStoreRequest?.arena_address?.addAll(storelistnew)
        AppLog.e(TAG, arenaList.toString() + "----" + storelistnew.toString() + "---" + vendorStoreRequest?.arena_address.toString())
    }

    fun getListOther() {
        var storelistnew = ArrayList<StoreItemLocationModel>()
        for (i in 0 until otherList.size) {
            if (!otherList.get(i).location_name.equals("")) {
                storelistnew.add(otherList.get(i))
            }
        }
        vendorStoreRequest?.other_address?.clear()
        vendorStoreRequest?.other_address?.addAll(storelistnew)
        AppLog.e(TAG, otherList.toString() + "----" + storelistnew.toString() + "---" + vendorStoreRequest?.other_address.toString())
    }

    fun updateSelectedServiceArea(bean: ArrayList<CityModel>) {
        val data = StringBuilder()

        val filledList: ArrayList<CityModel> = ArrayList()

        for (temp in bean) {
            if (temp.isSelected) {
                filledList.add(temp)
                if (data.length > 0) {
                    data.append(", ")
                    dataId.append(", ")
                }
                data.append(temp.cityname)
                dataId.append(temp.cityid)
            }
        }


        edtxt_service_area.setText(data.toString())
    }
    fun updateSelectedServiceArea(bean: StoreCategoryModel) {
        if (bean != null) {
            vendorStoreRequest.store_category_id = bean.id
            vendorStoreRequest.storecategory = bean.category_name
            ed_store.setText(bean.category_name)
        }
    }

    fun manageFromEdit() {
        if (activity is VendorStoreActivity) {
            if ((activity as VendorStoreActivity).isFromedit()) {
                btn_next.setText((activity as VendorStoreActivity).getString(R.string.save))
            }
        }
    }

    private fun createAdapterStadium() {
        storeAddStadiumAdapter = StoreDeliverLocationAdapter(activity!!, AppConstants.FROM_ADDDATA, stadiumList)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_stadium.layoutManager = manager
        rv_stadium.adapter = (storeAddStadiumAdapter)
    }


    private fun createAdapterClothsType() {
        storecClothTypeAdapter = StoreClothTypeAdapter(activity!!, AppConstants.FROM_ADDDATA, storeClothTypeList)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_cloths_type.layoutManager = manager
        rv_cloths_type.adapter = (storecClothTypeAdapter)
    }

    private fun createAdapterArena() {
        storeAddArenaAdapter = StoreDeliverLocationAdapter(activity!!, AppConstants.FROM_ADDDATA, arenaList)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_arena.layoutManager = manager
        rv_arena.adapter = (storeAddArenaAdapter)
    }

    private fun createAdapterOther() {
        storeAddOtherAdapter = StoreDeliverLocationAdapter(activity!!, AppConstants.FROM_ADDDATA, otherList)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_other.layoutManager = manager
        rv_other.adapter = (storeAddOtherAdapter)
    }

    fun performAddClothsType() {
        storeClothTypeList.add(StoreItemLocationModel())
        storecClothTypeAdapter?.notifyItemInserted(storeClothTypeList.size - 1)
        rv_cloths_type.invalidate()
        AppLog.e(TAG + "RECYCLERVIEW CHILD COUNT ", storeClothTypeList.toString())
    }

    fun performAddStadium() {
        stadiumList.add(StoreItemLocationModel())
        storeAddStadiumAdapter?.notifyItemInserted(stadiumList.size - 1)
        rv_stadium.invalidate()
        AppLog.e(TAG + "RECYCLERVIEW CHILD COUNT ", stadiumList.toString())
    }

    fun performAddArena() {
        arenaList.add(StoreItemLocationModel())
        storeAddArenaAdapter?.notifyItemInserted(arenaList.size - 1)
        rv_arena.invalidate()
        AppLog.e(TAG + "RECYCLERVIEW CHILD COUNT ", arenaList.toString())
    }

    fun performAddOther() {
        otherList.add(StoreItemLocationModel())
        storeAddOtherAdapter?.notifyItemInserted(otherList.size - 1)
        rv_arena.invalidate()
        AppLog.e(TAG + "RECYCLERVIEW CHILD COUNT ", otherList.toString())
    }
}