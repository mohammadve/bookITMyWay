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
import com.virtual.customervendor.model.LocationTimeModel
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppUtils
import kotlinx.android.synthetic.main.fragment_sight_seeing_addsight_single_row.view.*
import java.util.*


class SeightSeeing_sight_V(val mContext: Context, val from: String, val offermodel: ArrayList<LocationTimeModel>) : RecyclerView.Adapter<SeightSeeing_sight_V.ResultItemViewHolder>() {

    var lisData: ArrayList<LocationTimeModel> = offermodel
    val filterlist: ArrayList<LocationTimeModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_sight_seeing_addsight_single_row, parent, false)
        return ResultItemViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, from, lisData[position])
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mcontx: Context? = null
        var datetime: String? = null

        override fun onClick(v: View?) {
            AppUtils.getTimeNew(itemView.ed_sighttime, mcontx as AppCompatActivity?)
        }

        fun bind(mContext: Context, from: String, offModel: LocationTimeModel) {
            mcontx = mContext
            itemView.ed_startloc.setText(offModel.location.toString())
            itemView.ed_sighttime.setText(offModel.time.toString())
            if (from == AppConstants.FROM_REVIEW) {
                itemView.ed_startloc.setFocusable(false)
                itemView.ed_sighttime.setFocusable(false)
            } else {
                itemView.ed_startloc.setFocusable(true)
                itemView.ed_sighttime.setFocusable(true)
                itemView.ed_sighttime.setOnClickListener(this)
            }

        }
    }

}






