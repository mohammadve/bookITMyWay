package com.virtual.customervendor.vendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.TimeManagerActivity
import com.virtual.customervendor.model.*
import com.virtual.customervendor.model.request.VendorStoreServiceRequest
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorStoreActivity
import com.virtual.customervendor.vendor.ui.adapter.StoreClothTypeAdapter
import com.virtual.customervendor.vendor.ui.adapter.StoreDeliverLocationAdapter
import kotlinx.android.synthetic.main.fragment_store_two_vendor.*

class VendorStoreTwoFragment : Fragment(), View.OnClickListener, TextWatcher {
    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        if (vendorStoreRequest.store_category_id == AppConstants.STORE_CAT_SEAT_SERVICE) {

            til_storeSubcategory.visibility = View.GONE
            addClothType.visibility = View.GONE
            til_Service_area.visibility = View.GONE

            txtDays.visibility = View.VISIBLE

            txt_stadium.visibility = View.VISIBLE
            rv_stadium.visibility = View.VISIBLE
            addstadium.visibility = View.VISIBLE

            txt_arena.visibility = View.VISIBLE
            rv_arena.visibility = View.VISIBLE
            addarena.visibility = View.VISIBLE

            txt_others.visibility = View.VISIBLE
            rv_other.visibility = View.VISIBLE
            addother.visibility = View.VISIBLE


        } else if (vendorStoreRequest.store_category_id == AppConstants.STORE_CAT_CLOTHING) {

            til_storeSubcategory.visibility = View.VISIBLE
            addClothType.visibility = View.VISIBLE
            rv_cloths_type.visibility = View.VISIBLE
            til_Service_area.visibility = View.VISIBLE

            txtDays.visibility = View.GONE

            txt_stadium.visibility = View.GONE
            rv_stadium.visibility = View.GONE
            addstadium.visibility = View.GONE

            txt_arena.visibility = View.GONE
            rv_arena.visibility = View.GONE
            addarena.visibility = View.GONE

            txt_others.visibility = View.GONE
            rv_other.visibility = View.GONE
            addother.visibility = View.GONE


        } else if (vendorStoreRequest.store_category_id == AppConstants.STORE_CAT_CUSTOM) {

            til_storeSubcategory.visibility = View.GONE
            addClothType.visibility = View.VISIBLE
            rv_cloths_type.visibility = View.VISIBLE
            til_Service_area.visibility = View.VISIBLE

            txtDays.visibility = View.GONE

            txt_stadium.visibility = View.GONE
            rv_stadium.visibility = View.GONE
            addstadium.visibility = View.GONE

            txt_arena.visibility = View.GONE
            rv_arena.visibility = View.GONE
            addarena.visibility = View.GONE

            txt_others.visibility = View.GONE
            rv_other.visibility = View.GONE
            addother.visibility = View.GONE
        }
    }


    var datetime: String? = null
    var TAG: String = VendorStoreTwoFragment::class.java.simpleName
    var vendorStoreRequest = VendorStoreServiceRequest()

    var stadiumList: ArrayList<StoreItemLocationModel> = ArrayList()
    var storeClothTypeList: ArrayList<CustomSubCategoryModel> = ArrayList()
    var arenaList: ArrayList<StoreItemLocationModel> = ArrayList()
    var otherList: ArrayList<StoreItemLocationModel> = ArrayList()

    var storeAddStadiumAdapter: StoreDeliverLocationAdapter? = null
    var storecClothTypeAdapter: StoreClothTypeAdapter? = null


    var storeAddArenaAdapter: StoreDeliverLocationAdapter? = null
    var storeAddOtherAdapter: StoreDeliverLocationAdapter? = null
    var manager: RecyclerView.LayoutManager? = null
    var dataId = StringBuilder()


    var dateTime: ArrayList<DayAviliability> = java.util.ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_store_two_vendor, container, false)
        return mView
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.txtDays -> {
                var intent = Intent(activity, TimeManagerActivity::class.java)
                intent.putExtra(TimeManagerActivity.KEY_Multi_Slots, false)
                if (vendorStoreRequest.dateTime.size > 0)
                    intent.putExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST, vendorStoreRequest.dateTime)
                startActivityForResult(intent, TimeManagerActivity.REQUEST_CODE)

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

            R.id.ed_storesub -> {
                if (context is VendorStoreActivity) {
                    (context as VendorStoreActivity).setDisplayDialog(10, AppConstants.FROM_V_TAXI_CITY, "")
                }
            }
            R.id.edtxt_service_area -> {

                edtxt_service_area.requestFocus()
                if (context is VendorStoreActivity) {
                    if (vendorStoreRequest.business_region_id.regionid != null && !vendorStoreRequest.business_region_id.regionid.equals("")) {

                        AppLog.e("@@@", "" + vendorStoreRequest.business_region_id.regionid);

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vendorStoreRequest = (context as VendorStoreActivity).getFieldPojo()
        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        ed_store.setOnClickListener(this)
        ed_storesub.setOnClickListener(this)
        edtxt_service_area.setOnClickListener(this)
        txtDays.setOnClickListener(this)

        ed_store.addTextChangedListener(this)

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

    private fun putAllDataToFieldMap() {
        vendorStoreRequest.description = ed_desc.text.toString()
        vendorStoreRequest.stadium_address = stadiumList
        vendorStoreRequest.arena_address = arenaList
        vendorStoreRequest.other_address = otherList
//        vendorStoreRequest.store_customSubCategory = storeClothTypeList
    }

    private fun getfilledData() {
        try {
            if (vendorStoreRequest != null) {
                ed_store.setText(vendorStoreRequest.storecategory)

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

                if (vendorStoreRequest.store_category_id == AppConstants.STORE_CAT_SEAT_SERVICE) {
                    til_storeSubcategory.visibility = View.GONE
                    addClothType.visibility = View.GONE
                    til_Service_area.visibility = View.GONE

                    txtDays.visibility = View.VISIBLE

                    txt_stadium.visibility = View.VISIBLE
                    rv_stadium.visibility = View.VISIBLE
                    addstadium.visibility = View.VISIBLE

                    txt_arena.visibility = View.VISIBLE
                    rv_arena.visibility = View.VISIBLE
                    addarena.visibility = View.VISIBLE

                    txt_others.visibility = View.VISIBLE
                    rv_other.visibility = View.VISIBLE
                    addother.visibility = View.VISIBLE


                } else if (vendorStoreRequest.store_category_id== AppConstants.STORE_CAT_CLOTHING) {

                    til_storeSubcategory.visibility = View.VISIBLE
                    addClothType.visibility = View.VISIBLE
                    rv_cloths_type.visibility = View.VISIBLE
                    til_Service_area.visibility = View.VISIBLE

                    txtDays.visibility = View.GONE

                    txt_stadium.visibility = View.GONE
                    rv_stadium.visibility = View.GONE
                    addstadium.visibility = View.GONE

                    txt_arena.visibility = View.GONE
                    rv_arena.visibility = View.GONE
                    addarena.visibility = View.GONE

                    txt_others.visibility = View.GONE
                    rv_other.visibility = View.GONE
                    addother.visibility = View.GONE

                    val data = StringBuilder()

                    val filledList: ArrayList<ClothingCategoryModel> = ArrayList()
                    var subcatId: ArrayList<Int> = ArrayList()

                    for (temp in vendorStoreRequest.store_subcategory_list) {
                            filledList.add(temp)
                            if (data.length > 0) {
                                data.append(", ")
                            }
                            data.append(temp.name)
                            subcatId.add(temp.id!!.toInt())
                    }
                    vendorStoreRequest.store_subcategory_ids = subcatId
                    vendorStoreRequest.store_subcategory = filledList

                    ed_storesub.setText(data.toString())


                } else if (vendorStoreRequest.store_category_id== AppConstants.STORE_CAT_CUSTOM) {

                    til_storeSubcategory.visibility = View.GONE
                    addClothType.visibility = View.VISIBLE
                    rv_cloths_type.visibility = View.VISIBLE
                    til_Service_area.visibility = View.VISIBLE

                    txtDays.visibility = View.GONE

                    txt_stadium.visibility = View.GONE
                    rv_stadium.visibility = View.GONE
                    addstadium.visibility = View.GONE

                    txt_arena.visibility = View.GONE
                    rv_arena.visibility = View.GONE
                    addarena.visibility = View.GONE

                    txt_others.visibility = View.GONE
                    rv_other.visibility = View.GONE
                    addother.visibility = View.GONE


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

        if (vendorStoreRequest.store_category_id == AppConstants.STORE_CAT_SEAT_SERVICE) {
            if (!isValidTimeSlots()) {
                UiValidator.displayMsg(context, "Please enter a valid time slots")
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
        if ((vendorStoreRequest.store_category_id == AppConstants.STORE_CAT_CLOTHING) || (vendorStoreRequest.store_category_id == AppConstants.STORE_CAT_CUSTOM)) {
            getListCustomField()
        }

        if (vendorStoreRequest.store_category_id == AppConstants.STORE_CAT_SEAT_SERVICE) {
            getListStadium()
            getListArena()
            getListOther()
        }

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


    fun getListCustomField() {
        var customFiled = ArrayList<String>()
        for (i in 0 until storeClothTypeList.size) {
            if (!storeClothTypeList.get(i).name.equals("")) {
                customFiled.add(storeClothTypeList.get(i).name!!)
            }
        }
        vendorStoreRequest?.store_customSubCategory?.clear()
        vendorStoreRequest?.store_customSubCategory?.addAll(customFiled)
        AppLog.e(TAG, storeClothTypeList.toString() + "----" + customFiled.toString() + "---" + vendorStoreRequest?.store_customSubCategory.toString())
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

        vendorStoreRequest.service_region_ids = filledList
        edtxt_service_area.setText(data.toString())
    }


    fun updateClothing(bean: ArrayList<ClothingCategoryModel>) {
        val data = StringBuilder()

        val filledList: ArrayList<ClothingCategoryModel> = ArrayList()
        var subcatId: ArrayList<Int> = ArrayList()

        for (temp in bean) {
            if (temp.isSelected!!) {
                filledList.add(temp)
                if (data.length > 0) {
                    data.append(", ")

                }
                data.append(temp.name)
                subcatId.add(temp.id!!.toInt())
            }
        }
        vendorStoreRequest.store_subcategory_ids = subcatId
        vendorStoreRequest.store_subcategory = filledList

        ed_storesub.setText(data.toString())
    }

    fun updateSelectedServiceArea(bean: StoreCategoryModel) {
        if (bean != null) {
            vendorStoreRequest.store_category_id = bean.id
            vendorStoreRequest.storecategory = bean.category_name
            ed_store.setText(bean.category_name)


            if (bean.id == AppConstants.STORE_CAT_SEAT_SERVICE) {
                til_storeSubcategory.visibility = View.GONE
                addClothType.visibility = View.GONE
                til_Service_area.visibility = View.GONE

                txtDays.visibility = View.VISIBLE

                txt_stadium.visibility = View.VISIBLE
                rv_stadium.visibility = View.VISIBLE
                addstadium.visibility = View.VISIBLE

                txt_arena.visibility = View.VISIBLE
                rv_arena.visibility = View.VISIBLE
                addarena.visibility = View.VISIBLE

                txt_others.visibility = View.VISIBLE
                rv_other.visibility = View.VISIBLE
                addother.visibility = View.VISIBLE


            } else if (bean.id == AppConstants.STORE_CAT_CLOTHING) {

                til_storeSubcategory.visibility = View.VISIBLE
                addClothType.visibility = View.VISIBLE
                rv_cloths_type.visibility = View.VISIBLE
                til_Service_area.visibility = View.VISIBLE

                txtDays.visibility = View.GONE

                txt_stadium.visibility = View.GONE
                rv_stadium.visibility = View.GONE
                addstadium.visibility = View.GONE

                txt_arena.visibility = View.GONE
                rv_arena.visibility = View.GONE
                addarena.visibility = View.GONE

                txt_others.visibility = View.GONE
                rv_other.visibility = View.GONE
                addother.visibility = View.GONE


            } else if (bean.id == AppConstants.STORE_CAT_CUSTOM) {

                til_storeSubcategory.visibility = View.GONE
                addClothType.visibility = View.VISIBLE
                rv_cloths_type.visibility = View.VISIBLE
                til_Service_area.visibility = View.VISIBLE

                txtDays.visibility = View.GONE

                txt_stadium.visibility = View.GONE
                rv_stadium.visibility = View.GONE
                addstadium.visibility = View.GONE

                txt_arena.visibility = View.GONE
                rv_arena.visibility = View.GONE
                addarena.visibility = View.GONE

                txt_others.visibility = View.GONE
                rv_other.visibility = View.GONE
                addother.visibility = View.GONE
            }
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
        storeClothTypeList.add(CustomSubCategoryModel())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TimeManagerActivity.REQUEST_CODE && resultCode == TimeManagerActivity.RESULT_CODE) {
            dateTime = data?.getSerializableExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST) as ArrayList<DayAviliability>
            vendorStoreRequest.dateTime = dateTime
            vendorStoreRequest.all_day = data?.getIntExtra(TimeManagerActivity.KEY_ALL_DAY_SAME, 0).toString()
        }

    }

    private fun isValidTimeSlots(): Boolean {
        for (i in 0..vendorStoreRequest.dateTime.size) {
            if (vendorStoreRequest.dateTime[i].isSeleted && vendorStoreRequest.dateTime[i].slots.size > 0)
                if (vendorStoreRequest.dateTime[i].slots[0].startTime.length > 0 && vendorStoreRequest.dateTime[i].slots[0].stopTime.length > 0)
                    return true
        }
        return false
    }
}