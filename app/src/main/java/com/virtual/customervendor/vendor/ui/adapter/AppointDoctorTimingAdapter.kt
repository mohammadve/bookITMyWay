package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.TimeSlotModel
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import kotlinx.android.synthetic.main.fragment_appointment_visithours_single_row.view.*
import java.util.*


class AppointDoctorTimingAdapter(val mContext: Context, val offermodel: ArrayList<TimeSlotModel>) : RecyclerView.Adapter<AppointDoctorTimingAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<TimeSlotModel> = offermodel
    val filterlist: ArrayList<TimeSlotModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_appointment_visithours_single_row, parent, false)
        return ResultItemViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position])
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mcontx: Context? = null
        var datetime: String? = null

        override fun onClick(v: View?) {
            AppLog.e("CheckView", v!!.id.toString())
            when (v!!.id) {
//                R.id.ed_fromtime -> AppUtill.getTime(itemView.ed_fromtime, (mcontx as AppCompatActivity))
                R.id.ed_fromtime -> {

                    AppUtils.getTimeNew(itemView.ed_fromtime, mcontx as AppCompatActivity?)
//                    AppUtill.getTime(itemView.ed_fromtime, (mcontx as AppCompatActivity))
                }
                R.id.ed_totime -> {
                    AppUtils.getTimeNew(itemView.ed_totime, mcontx as AppCompatActivity?)
//                    AppUtill.getTime(itemView.ed_totime, (mcontx as AppCompatActivity))
                }
            }
        }

        fun bind(mContext: Context, offModel: TimeSlotModel) {
            mcontx = mContext
            itemView.txt_visithours.setText(mContext.resources.getString(R.string.bussines_hours) + " " + (adapterPosition + 1))
            itemView.ed_fromtime.setText(offModel.fromTime.toString())
            itemView.ed_totime.setText(offModel.toTime.toString())
            itemView.ed_fromtime.setOnClickListener(this)
            itemView.ed_totime.setOnClickListener(this)

        }
    }

}






