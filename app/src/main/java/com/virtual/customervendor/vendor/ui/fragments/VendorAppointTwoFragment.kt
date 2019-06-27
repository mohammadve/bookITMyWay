package com.virtual.customervendor.vendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.TimeManagerActivity
import com.virtual.customervendor.customview.CustomEditText
import com.virtual.customervendor.model.AppointmentReasonModel
import com.virtual.customervendor.model.DayAviliability
import com.virtual.customervendor.model.SpecialityModel
import com.virtual.customervendor.model.TimeSlotModel
import com.virtual.customervendor.model.request.VendorHealthServiceRequest
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.activity.*
import com.virtual.customervendor.vendor.ui.adapter.AppointDoctorTimingAdapter
import kotlinx.android.synthetic.main.fragment_appoint_two_vendor.*

class VendorAppointTwoFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    var datetime: String? = null
    var manager: FragmentManager? = null
    var timeList: ArrayList<TimeSlotModel> = java.util.ArrayList()
    var count: Int = 0
    var TAG: String = VendorAppointTwoFragment::class.java.simpleName
    var venAppointDoctoreServiceRequest = VendorHealthServiceRequest()
    var dateTime: ArrayList<DayAviliability> = java.util.ArrayList()
    var hairServiceRequest = VendorHealthServiceRequest()
    var nailsServiceRequest = VendorHealthServiceRequest()
    var massageServiceRequest = VendorHealthServiceRequest()
    var appointDoctorTimingAdapter: AppointDoctorTimingAdapter? = null

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
        }
    }

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
            R.id.ed_specialty -> {
                if (activity is VendorDoctorActivity) {
                    (activity as VendorDoctorActivity).setDisplayDialog(9, AppConstants.FROM_V_TAXI_SERVICE_AREA, "")
                }
            }
            R.id.txtDays -> {
                var intent = Intent(activity, TimeManagerActivity::class.java)
                intent.putExtra(TimeManagerActivity.KEY_Multi_Slots, false)
                if (activity is VendorDoctorActivity) {
                    if (venAppointDoctoreServiceRequest.dateTime.size > 0)
                        intent.putExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST, venAppointDoctoreServiceRequest.dateTime)
                }else if(activity is VendorHairActivity){
                    if(hairServiceRequest.dateTime.size>0)
                        intent.putExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST,hairServiceRequest.dateTime)
                }else if(activity is VendorNailsActivity){
                    if(nailsServiceRequest.dateTime.size>0)
                        intent.putExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST,nailsServiceRequest.dateTime)
                }else if(activity is VendorMassagePhysioActivity){
                    if(massageServiceRequest.dateTime.size>0)
                        intent.putExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST,massageServiceRequest.dateTime)
                }
                startActivityForResult(intent, TimeManagerActivity.REQUEST_CODE)
            }

        }
    }

    private fun getfilledData() {
        if (context is VendorDoctorActivity)
            venAppointDoctoreServiceRequest = (context as VendorDoctorActivity).getDoctorFieldPojo()
        else if (context is VendorHairActivity)
            hairServiceRequest = (context as VendorHairActivity).getHairFieldPojo()
        else if (context is VendorNailsActivity)
            nailsServiceRequest = (context as VendorNailsActivity).getNailFieldPojo()
        else if (context is VendorMassagePhysioActivity)
            massageServiceRequest = (context as VendorMassagePhysioActivity).getMassageFieldPojo()

        try {
            if (activity is VendorDoctorActivity) {
                ed_fees.setText(venAppointDoctoreServiceRequest.fees_per_visit)
                chk_alldays.isChecked = AppUtils.getStatusBoolean(venAppointDoctoreServiceRequest.all_day)
//                chk_monday.isChecked = AppUtils.getStatusBoolean(venAppointDoctoreServiceRequest.mon)
//                chk_tuesday.isChecked = AppUtils.getStatusBoolean(venAppointDoctoreServiceRequest.tue)
//                chk_wednesday.isChecked = AppUtils.getStatusBoolean(venAppointDoctoreServiceRequest.wed)
//                chk_thursday.isChecked = AppUtils.getStatusBoolean(venAppointDoctoreServiceRequest.thu)
//                chk_friday.isChecked = AppUtils.getStatusBoolean(venAppointDoctoreServiceRequest.fri)
//                chk_saturday.isChecked = AppUtils.getStatusBoolean(venAppointDoctoreServiceRequest.sat)
//                chk_sunday.isChecked = AppUtils.getStatusBoolean(venAppointDoctoreServiceRequest.sun)
                ed_desc.setText(venAppointDoctoreServiceRequest.description)
                ed_patient.setText(venAppointDoctoreServiceRequest.required_person_per_hr)
                if (venAppointDoctoreServiceRequest.visiting_hours_slot.size > 0) {

                    timeList = venAppointDoctoreServiceRequest.visiting_hours_slot
                    appointDoctorTimingAdapter = AppointDoctorTimingAdapter(activity!!, timeList)
                    val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//                    rv_timing.layoutManager = manager
//                    rv_timing.adapter = (appointDoctorTimingAdapter)
//                    rv_timing.isNestedScrollingEnabled = false
                } else {
                    createAdapterView()
                }
                ed_specialty.setText(venAppointDoctoreServiceRequest.business_specialit.name)

                if (venAppointDoctoreServiceRequest.time_slot != null) {
                    if (venAppointDoctoreServiceRequest.time_slot.equals(resources.getString(R.string.fourfive))) {
                        radioButton1.isChecked = true
                    } else if (venAppointDoctoreServiceRequest.time_slot.equals(resources.getString(R.string.onezero))) {
                        radioButton2.isChecked = true
                    } else if (venAppointDoctoreServiceRequest.time_slot.equals(resources.getString(R.string.onethreezero))) {
                        radioButton3.isChecked = true
                    } else if (venAppointDoctoreServiceRequest.time_slot.equals(resources.getString(R.string.twozero))) {
                        radioButton4.isChecked = true
                    }
                }
            } else if (activity is VendorHairActivity) {
                til_speci.visibility = View.GONE
                ed_fees.setText(hairServiceRequest.fees_per_visit)
                chk_alldays.isChecked = AppUtils.getStatusBoolean(hairServiceRequest.all_day)
//                chk_monday.isChecked = AppUtils.getStatusBoolean(hairServiceRequest.mon)
//                chk_tuesday.isChecked = AppUtils.getStatusBoolean(hairServiceRequest.tue)
//                chk_wednesday.isChecked = AppUtils.getStatusBoolean(hairServiceRequest.wed)
//                chk_thursday.isChecked = AppUtils.getStatusBoolean(hairServiceRequest.thu)
//                chk_friday.isChecked = AppUtils.getStatusBoolean(hairServiceRequest.fri)
//                chk_saturday.isChecked = AppUtils.getStatusBoolean(hairServiceRequest.sat)
//                chk_sunday.isChecked = AppUtils.getStatusBoolean(hairServiceRequest.sun)
                ed_desc.setText(hairServiceRequest.description)
                ed_patient.setText(hairServiceRequest.required_person_per_hr)
                if (hairServiceRequest.visiting_hours_slot.size > 0) {
                    timeList = hairServiceRequest.visiting_hours_slot
                    appointDoctorTimingAdapter = AppointDoctorTimingAdapter(activity!!, timeList)
                    val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//                    rv_timing.layoutManager = manager
//                    rv_timing.adapter = (appointDoctorTimingAdapter)
//                    rv_timing.isNestedScrollingEnabled = false
                } else {
                    createAdapterView()
                }
                ed_specialty.setText(hairServiceRequest.business_specialit.name)

                if (hairServiceRequest.time_slot != null) {
                    if (hairServiceRequest.time_slot.equals(resources.getString(R.string.fourfive))) {
                        radioButton1.isChecked = true
                    } else if (hairServiceRequest.time_slot.equals(resources.getString(R.string.onezero))) {
                        radioButton2.isChecked = true
                    } else if (hairServiceRequest.time_slot.equals(resources.getString(R.string.onethreezero))) {
                        radioButton3.isChecked = true
                    } else if (hairServiceRequest.time_slot.equals(resources.getString(R.string.twozero))) {
                        radioButton4.isChecked = true
                    }
                }
            } else if (activity is VendorNailsActivity) {
                til_speci.visibility = View.GONE
                ed_fees.setText(nailsServiceRequest.fees_per_visit)
                chk_alldays.isChecked = AppUtils.getStatusBoolean(nailsServiceRequest.all_day)
//                chk_monday.isChecked = AppUtils.getStatusBoolean(nailsServiceRequest.mon)
//                chk_tuesday.isChecked = AppUtils.getStatusBoolean(nailsServiceRequest.tue)
//                chk_wednesday.isChecked = AppUtils.getStatusBoolean(nailsServiceRequest.wed)
//                chk_thursday.isChecked = AppUtils.getStatusBoolean(nailsServiceRequest.thu)
//                chk_friday.isChecked = AppUtils.getStatusBoolean(nailsServiceRequest.fri)
//                chk_saturday.isChecked = AppUtils.getStatusBoolean(nailsServiceRequest.sat)
//                chk_sunday.isChecked = AppUtils.getStatusBoolean(nailsServiceRequest.sun)
                ed_desc.setText(nailsServiceRequest.description)
                ed_patient.setText(nailsServiceRequest.required_person_per_hr)
                if (nailsServiceRequest.visiting_hours_slot.size > 0) {
                    timeList = nailsServiceRequest.visiting_hours_slot
                    appointDoctorTimingAdapter = AppointDoctorTimingAdapter(activity!!, timeList)
                    val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//                    rv_timing.layoutManager = manager
//                    rv_timing.adapter = (appointDoctorTimingAdapter)
//                    rv_timing.isNestedScrollingEnabled = false
                } else {
                    createAdapterView()
                }
                ed_specialty.setText(nailsServiceRequest.business_specialit.name)

                if (nailsServiceRequest.time_slot != null) {
                    if (nailsServiceRequest.time_slot.equals(resources.getString(R.string.fourfive))) {
                        radioButton1.isChecked = true
                    } else if (nailsServiceRequest.time_slot.equals(resources.getString(R.string.onezero))) {
                        radioButton2.isChecked = true
                    } else if (nailsServiceRequest.time_slot.equals(resources.getString(R.string.onethreezero))) {
                        radioButton3.isChecked = true
                    } else if (nailsServiceRequest.time_slot.equals(resources.getString(R.string.twozero))) {
                        radioButton4.isChecked = true
                    }
                }
            } else if (activity is VendorMassagePhysioActivity) {
                til_speci.visibility = View.GONE
                ed_fees.setText(massageServiceRequest.fees_per_visit)
                chk_alldays.isChecked = AppUtils.getStatusBoolean(massageServiceRequest.all_day)
//                chk_monday.isChecked = AppUtils.getStatusBoolean(massageServiceRequest.mon)
//                chk_tuesday.isChecked = AppUtils.getStatusBoolean(massageServiceRequest.tue)
//                chk_wednesday.isChecked = AppUtils.getStatusBoolean(massageServiceRequest.wed)
//                chk_thursday.isChecked = AppUtils.getStatusBoolean(massageServiceRequest.thu)
//                chk_friday.isChecked = AppUtils.getStatusBoolean(massageServiceRequest.fri)
//                chk_saturday.isChecked = AppUtils.getStatusBoolean(massageServiceRequest.sat)
//                chk_sunday.isChecked = AppUtils.getStatusBoolean(massageServiceRequest.sun)
                ed_desc.setText(massageServiceRequest.description)
                ed_patient.setText(massageServiceRequest.required_person_per_hr)
                if (massageServiceRequest.visiting_hours_slot.size > 0) {
                    timeList = massageServiceRequest.visiting_hours_slot
                    appointDoctorTimingAdapter = AppointDoctorTimingAdapter(activity!!, timeList)
                    val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//                    rv_timing.layoutManager = manager
//                    rv_timing.adapter = (appointDoctorTimingAdapter)
//                    rv_timing.isNestedScrollingEnabled = false
                } else {
                    createAdapterView()
                }
                ed_specialty.setText(massageServiceRequest.business_specialit.name)

                if (massageServiceRequest.time_slot != null) {
                    if (massageServiceRequest.time_slot.equals(resources.getString(R.string.fourfive))) {
                        radioButton1.isChecked = true
                    } else if (massageServiceRequest.time_slot.equals(resources.getString(R.string.onezero))) {
                        radioButton2.isChecked = true
                    } else if (massageServiceRequest.time_slot.equals(resources.getString(R.string.onethreezero))) {
                        radioButton3.isChecked = true
                    } else if (massageServiceRequest.time_slot.equals(resources.getString(R.string.twozero))) {
                        radioButton4.isChecked = true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance(from: String): VendorAppointTwoFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorAppointTwoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_appoint_two_vendor, container, false)
        return mView
    }

    fun performAddSight() {
        if (timeList.size < 5) {
            timeList.add(TimeSlotModel())
            appointDoctorTimingAdapter?.notifyItemInserted(timeList.size - 1)
//            rv_timing.invalidate()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        til_fees.hint = "" + til_fees.hint + "(" + AppUtils.getCurrencySymbol() + ")"

        if (activity is VendorDoctorActivity) {
            hint_avtimeslot.visibility = View.GONE
            cons_timeslot.visibility = View.GONE
        } else {
            hint_avtimeslot.visibility = View.VISIBLE
            cons_timeslot.visibility = View.VISIBLE
        }

        getfilledData()
        manageFromEdit()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        txtDays.setOnClickListener(this)
        chk_alldays.setOnCheckedChangeListener(this)
//        chk_monday.setOnCheckedChangeListener(this)
//        chk_tuesday.setOnCheckedChangeListener(this)
//        chk_wednesday.setOnCheckedChangeListener(this)
//        chk_thursday.setOnCheckedChangeListener(this)
//        chk_friday.setOnCheckedChangeListener(this)
//        chk_saturday.setOnCheckedChangeListener(this)
//        chk_sunday.setOnCheckedChangeListener(this)
//        addmore.setOnClickListener(this)
        ed_specialty.setOnClickListener(this)
    }


    fun handleAlldays(isChecked: Boolean) {
        if (isChecked) {
//            if (chk_monday.isChecked()) chk_monday.isChecked = false
//            if (chk_tuesday.isChecked()) chk_tuesday.isChecked = false
//            if (chk_wednesday.isChecked()) chk_wednesday.isChecked = false
//            if (chk_thursday.isChecked()) chk_thursday.isChecked = false
//            if (chk_friday.isChecked()) chk_friday.isChecked = false
//            if (chk_saturday.isChecked()) chk_saturday.isChecked = false
//            if (chk_sunday.isChecked()) chk_sunday.isChecked = false
        }
    }

    fun handleDays(isChecked: Boolean) {
        if (isChecked) {
            if (chk_alldays.isChecked()) chk_alldays.isChecked = false
        }
    }


    private fun putAllDataToFieldMap(doctoreServiceRequest: VendorHealthServiceRequest) {
        val rb = cons_timeslot.findViewById(cons_timeslot.getCheckedRadioButtonId()) as RadioButton
        try {
            doctoreServiceRequest.fees_per_visit = ed_fees.text.toString()
            doctoreServiceRequest.all_day = AppUtils.getStatusString(chk_alldays.isChecked)
//            doctoreServiceRequest.mon = AppUtils.getStatusString(chk_monday.isChecked)
//            doctoreServiceRequest.tue = AppUtils.getStatusString(chk_tuesday.isChecked)
//            doctoreServiceRequest.wed = AppUtils.getStatusString(chk_wednesday.isChecked)
//            doctoreServiceRequest.thu = AppUtils.getStatusString(chk_thursday.isChecked)
//            doctoreServiceRequest.fri = AppUtils.getStatusString(chk_friday.isChecked)
//            doctoreServiceRequest.sat = AppUtils.getStatusString(chk_saturday.isChecked)
//            doctoreServiceRequest.sun = AppUtils.getStatusString(chk_sunday.isChecked)
            doctoreServiceRequest.description = ed_desc.text.toString()
            doctoreServiceRequest.required_person_per_hr = ed_patient.text.toString()

            if (activity is VendorDoctorActivity) {
                doctoreServiceRequest.time_slot = "01:00"
            } else {
                doctoreServiceRequest.time_slot = rb.text.toString()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun manageFromEdit() {
        if (activity is VendorDoctorActivity) {
            if ((activity as VendorDoctorActivity).isFromedit()) {
                btn_next.setText((activity as VendorDoctorActivity).getString(R.string.save))
            }
        } else if (activity is VendorHairActivity) {
            if ((activity as VendorHairActivity).isFromedit()) {
                btn_next.setText((activity as VendorHairActivity).getString(R.string.save))
            }
        } else if (activity is VendorNailsActivity) {
            if ((activity as VendorNailsActivity).isFromedit()) {
                btn_next.setText((activity as VendorNailsActivity).getString(R.string.save))
            }
        } else if (activity is VendorMassagePhysioActivity) {
            if ((activity as VendorMassagePhysioActivity).isFromedit()) {
                btn_next.setText((activity as VendorMassagePhysioActivity).getString(R.string.save))
            }
        }
    }

    fun validateField() {
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)

        if (ed_fees.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_fees, getString(R.string.field_required))
            return
        }
        if (til_fees.isErrorEnabled()) {
            UiValidator.disableValidationError(til_fees)
        }

        if (ed_patient.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_patient, getString(R.string.field_required))
            return
        }
        if (til_patient.isErrorEnabled()) {
            UiValidator.disableValidationError(til_patient)
        }

//        if (!(chk_alldays.isChecked || chk_monday.isChecked || chk_tuesday.isChecked || chk_wednesday.isChecked || chk_thursday.isChecked || chk_friday.isChecked || chk_saturday.isChecked || chk_sunday.isChecked)) {
//            UiValidator.displayMsgSnack(nest, activity, getString(R.string.select_days_of_service))
//            return
//        }

        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }

        if (!getListSightSeen()) {
            UiValidator.displayMsgSnack(nest, activity, resources.getString(R.string.choose_valid_time_slot))
            return
        }


        if (activity is VendorDoctorActivity) {
            if(!isValidTimeSlots(venAppointDoctoreServiceRequest.dateTime)) {
                UiValidator.displayMsg(context, "Please enter a valid time slots")
                return
            }
            putAllDataToFieldMap(venAppointDoctoreServiceRequest)
            if ((activity as VendorDoctorActivity).isFromedit()) {
                (activity as VendorDoctorActivity).hitApiEdit(venAppointDoctoreServiceRequest)
            } else {
                (activity as VendorDoctorActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.review_your_bussiness), false)
            }
        } else if (activity is VendorHairActivity) {
            if(!isValidTimeSlots(hairServiceRequest.dateTime)) {
                UiValidator.displayMsg(context, "Please enter a valid time slots")
                return
            }

            putAllDataToFieldMap(hairServiceRequest)
            if ((activity as VendorHairActivity).isFromedit()) {
                (activity as VendorHairActivity).hitApi(hairServiceRequest)
            } else {
                (activity as VendorHairActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.set_your_menu), false)
            }
        } else if (activity is VendorNailsActivity) {
            if(!isValidTimeSlots(nailsServiceRequest.dateTime)) {
                UiValidator.displayMsg(context, "Please enter a valid time slots")
                return
            }

            putAllDataToFieldMap(nailsServiceRequest)

            if ((activity as VendorNailsActivity).isFromedit()) {
                (activity as VendorNailsActivity).hitApi(nailsServiceRequest)
            } else {
                (activity as VendorNailsActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.set_your_menu), false)
            }
        } else if (activity is VendorMassagePhysioActivity) {
            if(!isValidTimeSlots(massageServiceRequest.dateTime)) {
                UiValidator.displayMsg(context, "Please enter a valid time slots")
                return
            }

            putAllDataToFieldMap(massageServiceRequest)
            if ((activity as VendorMassagePhysioActivity).isFromedit()) {
                (activity as VendorMassagePhysioActivity).hitApi(massageServiceRequest)
            } else {
                (activity as VendorMassagePhysioActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.set_your_menu), false)
            }
        }
    }

    private fun createAdapterView() {
        timeList.add(TimeSlotModel())
        appointDoctorTimingAdapter = AppointDoctorTimingAdapter(activity!!, timeList)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        rv_timing.layoutManager = manager
//        rv_timing.adapter = (appointDoctorTimingAdapter)
//        rv_timing.isNestedScrollingEnabled = false
    }

    fun isValidTimeSlots(taxi_Service_Request: ArrayList<DayAviliability>): Boolean {
        for (aviliability in taxi_Service_Request){
            if(aviliability.isSeleted && aviliability.slots.size>0)
                if(aviliability.slots[0].startTime.length>0 && aviliability.slots[0].stopTime.length>0 )
                    return true
        }
        return false
    }

    fun getListSightSeen(): Boolean {

//        AppLog.e(TAG, rv_timing.childCount.toString())
//        var timeSlotModel: TimeSlotModel
//        timeList.clear()
//        for (i in 0 until rv_timing.childCount) {
//            var data: ConstraintLayout = rv_timing.getChildAt(i) as ConstraintLayout
//            if (!data.findViewById<CustomEditText>(R.id.ed_fromtime).text.toString().isEmpty()) {
//                timeSlotModel = TimeSlotModel()
//                timeSlotModel.fromTime = data.findViewById<CustomEditText>(R.id.ed_fromtime).text.toString()
//                timeSlotModel.toTime = data.findViewById<CustomEditText>(R.id.ed_totime).text.toString()
//                if (!AppUtill.compareTime(timeSlotModel.fromTime.toString(), timeSlotModel.toTime.toString()))
//                    return false
//
//                timeList.add(timeSlotModel)
//            }
//        }

        for (i in 0 until timeList.size - 1) {
            if (!AppUtill.compareTime(timeList.get(i).toTime!!, timeList.get(i + 1).fromTime!!))
                return false
        }
        if (activity is VendorDoctorActivity)
            venAppointDoctoreServiceRequest.visiting_hours_slot = timeList
        else if (activity is VendorHairActivity)
            hairServiceRequest.visiting_hours_slot = timeList
        else if (activity is VendorNailsActivity)
            nailsServiceRequest.visiting_hours_slot = timeList
        else if (activity is VendorMassagePhysioActivity)
            massageServiceRequest.visiting_hours_slot = timeList
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TimeManagerActivity.REQUEST_CODE && resultCode == TimeManagerActivity.RESULT_CODE) {
            dateTime = data?.getSerializableExtra(TimeManagerActivity.KEY_TIME_SLOTS_LIST) as ArrayList<DayAviliability>
            var isAllDay: String = data?.getIntExtra(TimeManagerActivity.KEY_ALL_DAY_SAME, 0).toString()
            if (activity is VendorDoctorActivity) {
                venAppointDoctoreServiceRequest.dateTime = dateTime
                venAppointDoctoreServiceRequest.all_day = isAllDay
            } else if (activity is VendorHairActivity) {
                hairServiceRequest.dateTime = dateTime
                hairServiceRequest.all_day = isAllDay
            } else if (activity is VendorNailsActivity) {
                nailsServiceRequest.dateTime = dateTime
                nailsServiceRequest.all_day = isAllDay
            } else if (activity is VendorMassagePhysioActivity)
                massageServiceRequest.dateTime = dateTime
            massageServiceRequest.all_day = isAllDay
        }

        fun updateSelectedSpeciality(bean: AppointmentReasonModel) {
            if (bean != null) {
                ed_specialty.setText(bean.reason)
//            if (activity is VendorDoctorActivity) {
//                venAppointDoctoreServiceRequest.speciality = bean.id
//                venAppointDoctoreServiceRequest.business_specialit = bean
//            }
//            fieldMap.put(AppKeys.KEY_SPECIALITYID, bean.id!!)
            }
        }
    }
}