package com.virtual.customervendor.vendor.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.LocationTimeModel
import com.virtual.customervendor.model.PackageModel
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.vendor.ui.activity.VendorPackageDetailActivity
import com.virtual.customervendor.vendor.ui.adapter.SightSeeing_viewAdapter
import kotlinx.android.synthetic.main.fragment_package_detail_vendor.*
import java.io.File

class VendorPackageDetailFragment : Fragment(), View.OnClickListener {

    var imageFile: File? = null
    var TAG: String = VendorPackageDetailFragment::class.java.name
    var eventRequest = PackageModel()
    var apiInterface: ApiInterface? = null
    var seenList: ArrayList<LocationTimeModel> = ArrayList()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> activity?.onBackPressed()
        }
    }

    companion object {
        fun newInstance(): VendorPackageDetailFragment {
            val fragment = VendorPackageDetailFragment()
            return fragment
        }
    }

    private fun setData() {
        try {
            tv_event_name.setText(eventRequest.package_name)
            tv_fee.setText(AppUtils.getRegisterRateWithSymbol(eventRequest.cost) + " /" + resources.getString(R.string.person))
            tv_vehicle.setText(eventRequest.total_tour_vehicle)
            tv_avg_no_seats_value.setText(eventRequest.avg_seat_per_vehicle)

            var time = StringBuilder()
            if (AppUtils.getStatusBoolean(eventRequest.all_day)) {
                time.append(resources.getString(R.string.all_days))
            } else {
                if (AppUtils.getStatusBoolean(eventRequest.monday)) {
                    time.append(resources.getString(R.string.monday_m))
                }
                if (AppUtils.getStatusBoolean(eventRequest.tuesday)) {
                    time.append(", " + resources.getString(R.string.tuesday_t))
                }
                if (AppUtils.getStatusBoolean(eventRequest.wednesday)) {
                    time.append(", " + resources.getString(R.string.wednesday_w))
                }
                if (AppUtils.getStatusBoolean(eventRequest.thursday)) {
                    time.append(", " + resources.getString(R.string.thursday_t))
                }
                if (AppUtils.getStatusBoolean(eventRequest.friday)) {
                    time.append(", " + resources.getString(R.string.friday_f))
                }
                if (AppUtils.getStatusBoolean(eventRequest.saturday)) {
                    time.append(", " + resources.getString(R.string.saturday_s))
                }
                if (AppUtils.getStatusBoolean(eventRequest.sunday)) {
                    time.append(", " + resources.getString(R.string.sunday_t))
                }
            }

            seenList.add(LocationTimeModel(eventRequest.start_location!!, eventRequest.start_time!!))
            seenList.addAll(eventRequest.sight_seens)
            seenList.add(LocationTimeModel(eventRequest.end_location!!, eventRequest.end_time!!))

            createAdapterView()

            tv_day.setText(time)
            tv_des.setText(eventRequest.description)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_package_detail_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        eventRequest = (context as VendorPackageDetailActivity).getEventFieldPojo()
        setData()
    }

    private fun createAdapterView() {
        var seightSeeing_sight_V = SightSeeing_viewAdapter(activity!!, seenList)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_packages.layoutManager = manager
        rv_packages.adapter = (seightSeeing_sight_V)
    }

}