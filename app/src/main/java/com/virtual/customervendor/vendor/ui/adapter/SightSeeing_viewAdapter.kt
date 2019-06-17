package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.LocationTimeModel
import kotlinx.android.synthetic.main.fragment_sight_seeing_viewrow.view.*
import java.util.*


class SightSeeing_viewAdapter(val mContext: Context ,val offermodel: ArrayList<LocationTimeModel>) : RecyclerView.Adapter<SightSeeing_viewAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<LocationTimeModel> = offermodel
    val filterlist: ArrayList<LocationTimeModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_sight_seeing_viewrow, parent, false)
        return ResultItemViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position])
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mcontx: Context? = null
        var datetime: String? = null
        fun bind(mContext: Context, offModel: LocationTimeModel) {
            mcontx = mContext
            itemView.ed_startloc.setText(offModel.location.toString())
            itemView.ed_sighttime.setText(offModel.time.toString())
        }
    }

}






