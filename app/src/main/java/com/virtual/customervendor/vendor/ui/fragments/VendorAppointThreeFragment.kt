package com.virtual.customervendor.vendor.ui.fragments

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customview.CustomEditText
import com.virtual.customervendor.model.ItemPriceModel
import com.virtual.customervendor.model.request.VendorHealthServiceRequest
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.*
import com.virtual.customervendor.vendor.ui.adapter.HealthAddServiceAdapter
import kotlinx.android.synthetic.main.fragment_appoint_three_vendor.*

class VendorAppointThreeFragment : Fragment(), View.OnClickListener {
    var datetime: String? = null
    var manager: FragmentManager? = null
    val timeList: ArrayList<ItemPriceModel> = java.util.ArrayList()
    var count: Int = 0
    var TAG: String = VendorAppointThreeFragment::class.java.simpleName
    var hairServiceRequest = VendorHealthServiceRequest()
    var nailServiceRequest = VendorHealthServiceRequest()
    var massageServiceRequest = VendorHealthServiceRequest()
    var serviceAdapter: HealthAddServiceAdapter? = null


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_next -> {
                validateField()
            }
            R.id.addmore -> {
                performAddSight()
            }
        }
    }

    private fun getfilledData() {
        if (context is VendorHairActivity)
            hairServiceRequest = (context as VendorHairActivity).getHairFieldPojo()
        if (context is VendorNailsActivity) nailServiceRequest = (context as VendorNailsActivity).getNailFieldPojo()
        if (context is VendorMassagePhysioActivity) massageServiceRequest = (context as VendorMassagePhysioActivity).getMassageFieldPojo()

        try {
            if (activity is VendorHairActivity) {
                ed_desc.setText(hairServiceRequest.description)

                if (hairServiceRequest.service_menu.size > 0) {
                    timeList.addAll(hairServiceRequest.service_menu)
                    serviceAdapter = HealthAddServiceAdapter(activity!!, AppConstants.FROM_ADDDATA, timeList)
                    val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    rv_timing.layoutManager = manager
                    rv_timing.adapter = (serviceAdapter)
                } else {
                    createAdapterView()
                }
            } else if (activity is VendorNailsActivity) {
                ed_desc.setText(nailServiceRequest.description)

                if (nailServiceRequest.service_menu.size > 0) {
                    timeList.addAll(nailServiceRequest.service_menu)
                    serviceAdapter = HealthAddServiceAdapter(activity!!, AppConstants.FROM_ADDDATA, timeList)
                    val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    rv_timing.layoutManager = manager
                    rv_timing.adapter = (serviceAdapter)
                } else {
                    createAdapterView()
                }
            } else if (activity is VendorMassagePhysioActivity) {
                ed_desc.setText(massageServiceRequest.description)

                if (massageServiceRequest.service_menu.size > 0) {
                    timeList.addAll(massageServiceRequest.service_menu)
                    serviceAdapter = HealthAddServiceAdapter(activity!!, AppConstants.FROM_ADDDATA, timeList)
                    val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    rv_timing.layoutManager = manager
                    rv_timing.adapter = (serviceAdapter)
                } else {
                    createAdapterView()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance(from: String): VendorAppointThreeFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorAppointThreeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_appoint_three_vendor, container, false)
        return mView
    }

    fun performAddSight() {
        if (timeList.size < 20) {
            timeList.add(ItemPriceModel())
            serviceAdapter?.notifyItemInserted(timeList.size - 1)
            rv_timing.invalidate()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getfilledData()
        manageFromEdit()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        addmore.setOnClickListener(this)
    }


    private fun putAllDataToFieldMap(doctoreServiceRequest: VendorHealthServiceRequest) {
        try {
            doctoreServiceRequest.description = ed_desc.text.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun manageFromEdit() {
        if (activity is VendorDoctorActivity) {
            if ((activity as VendorDoctorActivity).isFromedit()) {
                btn_next.setText((activity as VendorDoctorActivity).getString(R.string.save))
            }
        } /*else if (activity is VendorDentistActivity) {
            if ((activity as VendorDentistActivity).isFromedit()) {
                btn_next.setText((activity as VendorDentistActivity).getString(R.string.save))
            }
        }*/ else if (activity is VendorMassagePhysioActivity) {
            if ((activity as VendorMassagePhysioActivity).isFromedit()) {
                btn_next.setText((activity as VendorMassagePhysioActivity).getString(R.string.save))
            }
        }
    }

    fun validateField() {
//        if (ed_desc.getText().toString().isEmpty()) {
//            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
//            return
//        }
//        if (til_desc.isErrorEnabled()) {
//            UiValidator.disableValidationError(til_desc)
//        }

        if (!getListSightSeen()) {
            UiValidator.displayMsgSnack(cons, activity, activity!!.resources.getString(R.string.choose_service_menu))
            return
        }

        UiValidator.hideSoftKeyboard(context as AppCompatActivity)

        if (activity is VendorHairActivity) {
            putAllDataToFieldMap(hairServiceRequest)
            if ((activity as VendorHairActivity).isFromedit()) {
                (activity as VendorHairActivity).hitApi(hairServiceRequest)
            } else {
                (activity as VendorHairActivity).setDisplayFragment(4, activity!!.resources.getString(R.string.review_your_bussiness), false)
            }
        } else if (activity is VendorNailsActivity) {
            putAllDataToFieldMap(nailServiceRequest)
            if ((activity as VendorNailsActivity).isFromedit()) {
                (activity as VendorNailsActivity).hitApi(nailServiceRequest)
            } else {
                (activity as VendorNailsActivity).setDisplayFragment(4, activity!!.resources.getString(R.string.review_your_bussiness), false)
            }
        } else if (activity is VendorMassagePhysioActivity) {
            putAllDataToFieldMap(massageServiceRequest)
            if ((activity as VendorMassagePhysioActivity).isFromedit()) {
                (activity as VendorMassagePhysioActivity).hitApi(massageServiceRequest)
            } else {
                (activity as VendorMassagePhysioActivity).setDisplayFragment(4, activity!!.resources.getString(R.string.review_your_bussiness), false)
            }
        }
    }

    private fun createAdapterView() {
        timeList.add(ItemPriceModel())
        serviceAdapter = HealthAddServiceAdapter(activity!!, AppConstants.FROM_ADDDATA, timeList)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_timing.layoutManager = manager
        rv_timing.adapter = (serviceAdapter)
    }

    fun getListSightSeen(): Boolean {
        AppLog.e(TAG, rv_timing.childCount.toString())
        var itemPriceModel: ItemPriceModel
        if (timeList != null) {
            timeList.clear()
        }
        for (i in 0 until rv_timing.childCount) {
            var data: ConstraintLayout = rv_timing.getChildAt(i) as ConstraintLayout
            if (!data.findViewById<CustomEditText>(R.id.ed_itemname).text.toString().isEmpty() && !(data.findViewById<CustomEditText>(R.id.ed_price).text.toString().isEmpty())) {
                itemPriceModel = ItemPriceModel()
                itemPriceModel.itemName = data.findViewById<CustomEditText>(R.id.ed_itemname).text.toString()
                itemPriceModel.itemPrice = data.findViewById<CustomEditText>(R.id.ed_price).text.toString()
                timeList.add(itemPriceModel)
            }
        }
        if (activity is VendorHairActivity)
            hairServiceRequest.service_menu = timeList
        else if (activity is VendorNailsActivity)
            nailServiceRequest.service_menu = timeList
        else if (activity is VendorMassagePhysioActivity)
            massageServiceRequest.service_menu = timeList

        if (timeList.size == 0) {
            return false
        } else {
            return true
        }
    }


}