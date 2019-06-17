package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.virtual.customervendor.R
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.utills.AppUtils
import kotlinx.android.synthetic.main.fragment_event_item_vendor.view.*


class VendorEventsAdapter(val mContext: Context, val listeventDetail: ArrayList<EventDetail>, val clickListener: (EventDetail) -> Unit) : RecyclerView.Adapter<VendorEventsAdapter.ResultItemViewHolder>() {
    var lisData: ArrayList<EventDetail> = listeventDetail
    val TAG: String = VendorEventsAdapter::class.java.simpleName
    private val lastSelectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_event_item_vendor, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, clickListener, position)
    }

    override fun getItemCount(): Int {
        return lisData.size
    }

    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, clickListener: (EventDetail) -> Unit, position: Int) {
            itemView.ed_eventname.setText(lisData.get(position).name)
            itemView.ed_price.setText(AppUtils.getRegisterRateWithSymbol(lisData.get(position).ticket_price))
            itemView.ed_datetime.setText(lisData.get(position).start_date + " - " + lisData.get(position).end_date)
            if (lisData.get(position).event_images!!.size > 0) {
                Glide.with(mContext).load(lisData.get(position).event_images!!.get(0).url).into(itemView.img_upload)
            }
            itemView.setOnClickListener(View.OnClickListener { clickListener(lisData.get(position)) })
        }
    }
}




