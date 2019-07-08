package com.virtual.customervendor.vendor.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.TermsAndConditionActivityVendor
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.model.*
import com.virtual.customervendor.model.request.VendorStoreServiceRequest
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.VendorStoreActivity
import com.virtual.customervendor.vendor.ui.adapter.StoreAddItemsAdapter
import com.virtual.customervendor.vendor.ui.adapter.StoreDeliverLocationReviewAdapter
import kotlinx.android.synthetic.main.fragment_store_review_vendor.*
import java.io.File
import java.util.*

class VendorStoreReviewFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var TAG: String = VendorStoreReviewFragment::class.java.name
    var vendorstoreRequest = VendorStoreServiceRequest()
    var imageFile: File? = null
    var homeSliderAdapter: HomeSliderAdapter? = null

    var stadiumList: ArrayList<StoreItemLocationModel> = ArrayList()
    var arenaList: ArrayList<StoreItemLocationModel> = ArrayList()
    var otherList: ArrayList<StoreItemLocationModel> = ArrayList()

    var storeAddStadiumAdapter: StoreDeliverLocationReviewAdapter? = null
    var storeAddArenaAdapter: StoreDeliverLocationReviewAdapter? = null
    var storeAddOtherAdapter: StoreDeliverLocationReviewAdapter? = null

    var manager: RecyclerView.LayoutManager? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_store_review_vendor, container, false)
        return mView
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                if (chk_term.isChecked) {
                    if (activity is VendorStoreActivity)
                        (activity as VendorStoreActivity).hitApi()
                } else {
                    UiValidator.displayMsgSnack(nest, activity!!, activity!!.resources.getString(R.string.check_tern))
                }
            }
            R.id.btn_edit -> {
                if (activity is VendorStoreActivity)
                    (activity as VendorStoreActivity).setDisplayFragment(1, activity!!.resources.getString(R.string.bussiness_information), true)
            }
            R.id.txt_chk -> {
                startActivity(Intent(activity, TermsAndConditionActivityVendor::class.java))
            }
        }
    }

    companion object {
        fun newInstance(from: String): VendorStoreReviewFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorStoreReviewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun putAllDataToFieldMap() {
//        if (activity is VendorStoreActivity)
//            store_items = vendorstoreRequest.store_items

        if (activity is VendorStoreActivity)
            try {
                setData(vendorstoreRequest)
                AppLog.e(TAG, vendorstoreRequest.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

    fun setData(vendorstoreRequest: VendorStoreServiceRequest) {
        ed_bname.setText(vendorstoreRequest.business_name)
        ed_bcontact.setText(vendorstoreRequest.dial_code + " " + vendorstoreRequest.business_contactno)
        ed_email.setText(vendorstoreRequest.business_email)
        ed_city.setText(vendorstoreRequest.business_city_id.cityname)
        ed_area.setText(vendorstoreRequest.business_region_id.regionname)
        ed_address.setText(vendorstoreRequest.business_address)

        if (vendorstoreRequest.business_tax != null && !vendorstoreRequest.business_tax.equals("")) {
            hint_tax.visibility = View.VISIBLE
            ed_tax.visibility = View.VISIBLE
            ed_tax.setText(vendorstoreRequest.business_tax)
        } else {
            hint_tax.visibility = View.GONE
            ed_tax.visibility = View.GONE
        }



        if (vendorstoreRequest.store_category_id == AppConstants.STORE_CAT_SEAT_SERVICE) {

            lytDaySlots.visibility = View.VISIBLE
            txt_days.visibility = View.VISIBLE
            txt_cussubcat.visibility = View.GONE
            ed_cussubcat.visibility = View.GONE
            ed_clothing.visibility = View.GONE
            txt_clothing.visibility = View.GONE

            if (vendorstoreRequest.stadium_address != null && vendorstoreRequest.stadium_address.size > 0) {
                stadiumList = vendorstoreRequest.stadium_address
                txt_stadium.visibility = View.VISIBLE
                rv_stadium.visibility = View.VISIBLE
                createAdapterStadium()
            }

            if (vendorstoreRequest.arena_address != null && vendorstoreRequest.arena_address.size > 0) {
                arenaList = vendorstoreRequest.arena_address
                txt_arena.visibility = View.VISIBLE
                rv_arena.visibility = View.VISIBLE
                createAdapterArena()
            }

            if (vendorstoreRequest.other_address != null && vendorstoreRequest.other_address.size > 0) {
                otherList = vendorstoreRequest.other_address
                ed_servicearea.visibility = View.GONE
                txt_other.visibility = View.VISIBLE
                rv_other.visibility = View.VISIBLE
                createAdapterOther()
            }

            txtSlotsMon.setText(getSlots(vendorstoreRequest.dateTime.get(0).slots))
            txtSlotsTue.setText(getSlots(vendorstoreRequest.dateTime.get(1).slots))
            txtSlotsWed.setText(getSlots(vendorstoreRequest.dateTime.get(2).slots))
            txtSlotsThu.setText(getSlots(vendorstoreRequest.dateTime.get(3).slots))
            txtSlotsFri.setText(getSlots(vendorstoreRequest.dateTime.get(4).slots))
            txtSlotsSat.setText(getSlots(vendorstoreRequest.dateTime.get(5).slots))
            txtSlotsSun.setText(getSlots(vendorstoreRequest.dateTime.get(6).slots))
        } else if (vendorstoreRequest.store_category_id == AppConstants.STORE_CAT_CLOTHING) {
            ed_servicearea.visibility = View.VISIBLE
            txt_cussubcat.visibility = View.VISIBLE
            ed_cussubcat.visibility = View.VISIBLE
            ed_clothing.visibility = View.VISIBLE
            txt_clothing.visibility = View.VISIBLE

            vendorstoreRequest.store_subcategory
            if (vendorstoreRequest.store_subcategory?.size!! > 0) {
                val data = StringBuilder()
                val filledList: ArrayList<ClothingCategoryModel> = ArrayList()


                for (temp in vendorstoreRequest.store_subcategory!!) {
                    if (temp.isSelected!!) {
                        filledList.add(temp)
                        if (data.length > 0) {
                            data.append(", ")

                        }
                        data.append(temp.name)
                    }
                }
                ed_clothing.setText(data.toString())
            }

            if (vendorstoreRequest.store_customSubCategory?.size!! > 0) {
                val datacustom = StringBuilder()


                for (temp in vendorstoreRequest.store_customSubCategory!!) {
                    if (datacustom.length > 0) {
                        datacustom.append(", ")
                    }
                    datacustom.append(temp)
                }
                ed_cussubcat.setText(datacustom.toString())
            }

            if (vendorstoreRequest.service_region_ids.size > 0) {
                val data = StringBuilder()
                for (temp in vendorstoreRequest.service_region_ids) {
                        if (data.length > 0) {
                            data.append(", ")
                        }
                        data.append(temp.cityname)
                }
                ed_servicearea.setText(data.toString())
            }

        } else if (vendorstoreRequest.store_category_id == AppConstants.STORE_CAT_CUSTOM) {
            ed_servicearea.visibility = View.VISIBLE
            txt_cussubcat.visibility = View.VISIBLE
            ed_cussubcat.visibility = View.VISIBLE
            ed_clothing.visibility = View.GONE
            txt_clothing.visibility = View.GONE

            if (vendorstoreRequest.store_customSubCategory?.size!! > 0) {
                val datacustom = StringBuilder()

                for (temp in vendorstoreRequest.store_customSubCategory!!) {
                        if (datacustom.length > 0) {
                            datacustom.append(", ")
                        }
                        datacustom.append(temp)
                }
                ed_cussubcat.setText(datacustom.toString())
            }

            if (vendorstoreRequest.service_region_ids.size > 0) {
                val data = StringBuilder()
                for (temp in vendorstoreRequest.service_region_ids) {
                    if (data.length > 0) {
                        data.append(", ")
                    }
                    data.append(temp.cityname)
                }
                ed_servicearea.setText(data.toString())
            }

        }


        initViewPager((activity as VendorStoreActivity).mResults, false)
        ed_desc.setText(vendorstoreRequest.description)
    }


    private fun getSlots(slots: ArrayList<DayAviliability.TimeSlot>): String {
        val builder = StringBuilder()

        for (timeSlots in slots) {
            if (timeSlots.startTime.length > 0 && timeSlots.stopTime.length > 0)
                builder.append(timeSlots.startTime + " to " + timeSlots.stopTime + "\n")
        }
        var str: String = builder.toString()

        if (str.equals(""))
            str = "none"
        else
            str = str.substring(0, str.length - 1)

        return str
    }


    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        homeSliderAdapter = HomeSliderAdapter(activity, mResults, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vendorstoreRequest = (context as VendorStoreActivity).getFieldPojo()
        if (activity is VendorStoreActivity) {
            imageFile = (context as VendorStoreActivity).imageFile
        }
        putAllDataToFieldMap()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        btn_edit.setOnClickListener(this)
        txt_chk.setOnClickListener(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }


    private fun createAdapterStadium() {
        storeAddStadiumAdapter = StoreDeliverLocationReviewAdapter(activity!!, AppConstants.FROM_ADDDATA, stadiumList)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_stadium.layoutManager = manager
        rv_stadium.adapter = (storeAddStadiumAdapter)
    }

    private fun createAdapterArena() {
        storeAddArenaAdapter = StoreDeliverLocationReviewAdapter(activity!!, AppConstants.FROM_ADDDATA, arenaList)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_arena.layoutManager = manager
        rv_arena.adapter = (storeAddArenaAdapter)
    }

    private fun createAdapterOther() {
        storeAddOtherAdapter = StoreDeliverLocationReviewAdapter(activity!!, AppConstants.FROM_ADDDATA, otherList)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_other.layoutManager = manager
        rv_other.adapter = (storeAddOtherAdapter)
    }

}