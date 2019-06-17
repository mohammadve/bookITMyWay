package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.BookRideSightSeenActivity
import com.virtual.customervendor.model.LocationTimeModel
import com.virtual.customervendor.model.PackageModel
import com.virtual.customervendor.model.RegionModel
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.vendor.ui.adapter.SightSeeing_viewAdapter
import kotlinx.android.synthetic.main.fragment_sight_seen_package_info.*

class SightSeenPackageInfo : Fragment(), View.OnClickListener {

    var TAG: String = SightSeenPackageInfo::class.java.name
    var apiService: ApiInterface? = null
    var packageDetail = PackageModel()
    var seenList: ArrayList<LocationTimeModel> = ArrayList()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_book_package -> {
                (activity as BookRideSightSeenActivity).setDisplayFragment(4, resources.getString(R.string.booking), false)
            }
        }
    }

    companion object {
        fun newInstance(): SightSeenPackageInfo {
            val fragment = SightSeenPackageInfo()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_sight_seen_package_info, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_book_package.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)
        packageDetail = (activity as BookRideSightSeenActivity).getSightSeenDetail()
        tv_event_name.text = packageDetail.package_name
        tv_fee.text = AppUtils.getRateWithSymbol(packageDetail.cost)
        tv_vehicle.text = packageDetail.total_tour_vehicle
        tv_avg_no_seats_value.text = packageDetail.avg_seat_per_vehicle


        var time = StringBuilder()
        if (AppUtils.getStatusBoolean(packageDetail.all_day)) {
            time.append(resources.getString(R.string.all_days))
        } else {
            if (AppUtils.getStatusBoolean(packageDetail.monday)) {
                time.append(resources.getString(R.string.monday_m))
            }
            if (AppUtils.getStatusBoolean(packageDetail.tuesday)) {
                time.append(", " + resources.getString(R.string.tuesday_t))
            }
            if (AppUtils.getStatusBoolean(packageDetail.wednesday)) {
                time.append(", " + resources.getString(R.string.wednesday_w))
            }
            if (AppUtils.getStatusBoolean(packageDetail.thursday)) {
                time.append(", " + resources.getString(R.string.thursday_t))
            }
            if (AppUtils.getStatusBoolean(packageDetail.friday)) {
                time.append(", " + resources.getString(R.string.friday_f))
            }
            if (AppUtils.getStatusBoolean(packageDetail.saturday)) {
                time.append(", " + resources.getString(R.string.saturday_s))
            }
            if (AppUtils.getStatusBoolean(packageDetail.sunday)) {
                time.append(", " + resources.getString(R.string.sunday_t))
            }
        }
        tv_day.setText(time)

        var data = StringBuilder()
        for (regionModel: RegionModel in packageDetail.service_region_ids) {
            if (data.length > 0) {
                data.append(", ")
            }
            data.append(regionModel.regionname)
        }
        seenList.clear()
        seenList.add(LocationTimeModel(packageDetail.start_location!!, packageDetail.start_time!!))
        seenList.addAll(packageDetail.sight_seens)
        seenList.add(LocationTimeModel(packageDetail.end_location!!, packageDetail.end_time!!))


        createAdapterView()

        tv_des.text = packageDetail.description
    }

    private fun createAdapterView() {
        var seightSeeing_sight_V = SightSeeing_viewAdapter(activity!!, seenList)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_packages.layoutManager = manager
        rv_packages.adapter = (seightSeeing_sight_V)
    }

}