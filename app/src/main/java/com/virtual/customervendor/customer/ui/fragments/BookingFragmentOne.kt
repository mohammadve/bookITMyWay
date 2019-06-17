package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.*
import com.virtual.customervendor.customer.ui.adapter.CustomerBookingListAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CountryModel
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.model.SpecialityModel
import com.virtual.customervendor.model.response.CustomerDisplayListResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking.*

class BookingFragmentOne : Fragment(), View.OnClickListener {
    var TAG: String = BookingFragmentOne::class.java.name;
    var fieldMap: MutableMap<String, String> = mutableMapOf();
    var restaurants: ArrayList<CustomerBookingListModel> = ArrayList()
    var adapter: CustomerBookingListAdapter? = null;
    var countryModel: CountryModel? = null;
    var apiService: ApiInterface? = null


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.region -> {
                if (countryModel != null && countryModel?.code != "") {
                    if (activity is BookRideTaxiActivity)
                        (activity as BookRideTaxiActivity).setDisplayDialog(7, countryModel?.code!!)
                    else if (activity is BookRideLimoActivity)
                        (activity as BookRideLimoActivity).setDisplayDialog(7, countryModel?.code!!)
                    else if (activity is BookRideTourBusActivity)
                        (activity as BookRideTourBusActivity).setDisplayDialog(7, countryModel?.code!!)
                    else if (activity is BookRideSightSeenActivity)
                        (activity as BookRideSightSeenActivity).setDisplayDialog(7, countryModel?.code!!)
                    else if (activity is ParkingValetActivity)
                        (activity as ParkingValetActivity).setDisplayDialog(7, countryModel?.code!!)
                    else if (activity is BookAppointmentDoctorActivity)
                        (activity as BookAppointmentDoctorActivity).setDisplayDialog(7, countryModel?.code!!)
                    else if (activity is BookAppointmentHairActivity)
                        (activity as BookAppointmentHairActivity).setDisplayDialog(7, countryModel?.code!!)
                    else if (activity is BookAppointmentNailActivity)
                        (activity as BookAppointmentNailActivity).setDisplayDialog(7, countryModel?.code!!)
                    else if (activity is BookAppointmentMassageActivity)
                        (activity as BookAppointmentMassageActivity).setDisplayDialog(7, countryModel?.code!!)
                    else if (activity is EventsActivity)
                        (activity as EventsActivity).setDisplayDialog(7, countryModel?.code!!)
                } else {
                    UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.choose_country_first))
                }
            }
            R.id.country -> {
//                UiValidator.displayMsg(activity, "In Progress...")
                if (activity is BookRideTaxiActivity)
                    (activity as BookRideTaxiActivity).setDisplayDialog(8, "")
                else if (activity is BookRideLimoActivity)
                    (activity as BookRideLimoActivity).setDisplayDialog(8, "")
                else if (activity is BookRideTourBusActivity)
                    (activity as BookRideTourBusActivity).setDisplayDialog(8, "")
                else if (activity is BookRideSightSeenActivity)
                    (activity as BookRideSightSeenActivity).setDisplayDialog(8, "")
                else if (activity is ParkingValetActivity)
                    (activity as ParkingValetActivity).setDisplayDialog(8, "")
                else if (activity is BookAppointmentDoctorActivity)
                    (activity as BookAppointmentDoctorActivity).setDisplayDialog(9, "")
                else if (activity is BookAppointmentHairActivity)
                    (activity as BookAppointmentHairActivity).setDisplayDialog(8, "")
                else if (activity is BookAppointmentNailActivity)
                    (activity as BookAppointmentNailActivity).setDisplayDialog(8, "")
                else if (activity is BookAppointmentMassageActivity)
                    (activity as BookAppointmentMassageActivity).setDisplayDialog(8, "")
                else if (activity is EventsActivity)
                    (activity as EventsActivity).setDisplayDialog(8, "")
            }
            R.id.specility -> {
                if (activity is BookAppointmentDoctorActivity)
                    (activity as BookAppointmentDoctorActivity).setDisplayDialog(8, AppConstants.FROM_V_TAXI_SERVICE_AREA)
            }
        }
    }

    companion object {
        fun newInstance(): BookingFragmentOne {
            val fragment = BookingFragmentOne()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        region.setOnClickListener(this)
        specility.setOnClickListener(this)
        country.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)

        if (activity is BookRideTaxiActivity) {
            fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_TRANSPORTATION.toString())
            fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_TRANS_TAXI.toString())
            fieldMap.put(AppKeys.OFFSET, "0")
        } else if (activity is BookRideLimoActivity) {
            fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_TRANSPORTATION.toString())
            fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_TRANS_LIMO.toString())
            fieldMap.put(AppKeys.OFFSET, "0")
        } else if (activity is BookRideTourBusActivity) {
            fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_TRANSPORTATION.toString())
            fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_TRANS_TOURBUS.toString())
            fieldMap.put(AppKeys.OFFSET, "0")
        } else if (activity is BookRideSightSeenActivity) {
            fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_TRANSPORTATION.toString())
            fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_TRANS_SIGHTSEEING.toString())
            fieldMap.put(AppKeys.OFFSET, "0")
        } else if (activity is ParkingValetActivity) {
            fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_PARKING_VALET.toString())
            fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_PARKING_VALET.toString())
            fieldMap.put(AppKeys.OFFSET, "0")
        } else if (activity is BookAppointmentDoctorActivity) {
            fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_HEALTH_BODYCARE.toString())
            fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_HEALTH_DOCTOR.toString())
            fieldMap.put(AppKeys.OFFSET, "0")
//            specility.visibility = View.VISIBLE
        } else if (activity is BookAppointmentHairActivity) {
            fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_HEALTH_BODYCARE.toString())
            fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_HEALTH_HAIR.toString())
            fieldMap.put(AppKeys.OFFSET, "0")
        } else if (activity is BookAppointmentNailActivity) {
            fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_HEALTH_BODYCARE.toString())
            fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_HEALTH_NAIL.toString())
            fieldMap.put(AppKeys.OFFSET, "0")
        } else if (activity is BookAppointmentMassageActivity) {
            fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_HEALTH_BODYCARE.toString())
            fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_HEALTH_PHYSIO.toString())
            fieldMap.put(AppKeys.OFFSET, "0")
        } else if (activity is EventsActivity) {
            fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_EVENT_TICKET.toString())
            fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_EVENT_TICKET.toString())
            fieldMap.put(AppKeys.OFFSET, "0")
        }

        getListItem()
    }

    fun initView() {


        adapter = CustomerBookingListAdapter(activity!!, restaurants) { cust ->
            callActivity(cust)
        }
        recyclerView.layoutManager = LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    fun getListItem() {
        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            AppLog.e(TAG, fieldMap.toString())
            apiService!!.getRestaurentsList("Bearer " + SharedPreferenceManager.getAuthToken(), fieldMap).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CustomerDisplayListResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(regionResponse: CustomerDisplayListResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, regionResponse.toString())
                            if (regionResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                                restaurants.clear()
                                if (regionResponse.data.size > 0) {
                                    nodatafound.visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                    restaurants.addAll(regionResponse.data)
                                    adapter?.notifyDataSetChanged()
                                } else {
                                    nodatafound.visibility = View.VISIBLE
                                    recyclerView.visibility = View.GONE
                                }
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, regionResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null)
                                AppLog.e(TAG, e.message)
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }

        initView()

    }

    fun updateSelectedRegion(bean: RegionModel) {
        if (bean != null) {
            region.setText(bean.regionname)
            fieldMap.put(AppKeys.KEY_REGIONID, bean.regionid!!)
            getListItem()
        }
    }

    fun updateSelectedCountry(bean: CountryModel) {
        if (bean != null) {
            country.setText(bean.name)
            region.setText("")
            fieldMap.put(AppKeys.KEY_REGIONID, "")
            countryModel = bean
            fieldMap.put(AppKeys.COUNTRY_CODE, bean.code!!)
            getListItem()
        }
    }

    fun callActivity(dataModel: CustomerBookingListModel) {
        if (activity is BookRideTaxiActivity) {
            (activity as BookRideTaxiActivity).customerBookingListModel = dataModel
            (activity as BookRideTaxiActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else if (activity is BookRideLimoActivity) {
            (activity as BookRideLimoActivity).customerBookingListModel = dataModel
            (activity as BookRideLimoActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else if (activity is BookRideTourBusActivity) {
            (activity as BookRideTourBusActivity).customerBookingListModel = dataModel
            (activity as BookRideTourBusActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else if (activity is BookRideSightSeenActivity) {
            (activity as BookRideSightSeenActivity).customerBookingListModel = dataModel
            (activity as BookRideSightSeenActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else if (activity is ParkingValetActivity) {
            (activity as ParkingValetActivity).parkingInfo = dataModel
            (activity as ParkingValetActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else if (activity is BookAppointmentDoctorActivity) {
            (activity as BookAppointmentDoctorActivity).customerBookingListModel = dataModel
            (activity as BookAppointmentDoctorActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else if (activity is BookAppointmentHairActivity) {
            (activity as BookAppointmentHairActivity).customerBookingListModel = dataModel
            (activity as BookAppointmentHairActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else if (activity is BookAppointmentNailActivity) {
            (activity as BookAppointmentNailActivity).customerBookingListModel = dataModel
            (activity as BookAppointmentNailActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else if (activity is BookAppointmentMassageActivity) {
            (activity as BookAppointmentMassageActivity).customerBookingListModel = dataModel
            (activity as BookAppointmentMassageActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        } else if (activity is EventsActivity) {
            (activity as EventsActivity).customerBookingListModel = dataModel
            (activity as EventsActivity).setDisplayFragment(2, resources.getString(R.string.business_profile), false)
        }
    }

    fun updateSelectedSpeciality(bean: SpecialityModel) {
        if (bean != null) {
            specility.setText(bean.name)
            fieldMap.put(AppKeys.KEY_SPECIALITYID, bean.id!!)
            getListItem()
        }
    }
}