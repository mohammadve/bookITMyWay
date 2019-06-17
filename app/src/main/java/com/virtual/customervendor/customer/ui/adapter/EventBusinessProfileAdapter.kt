package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.virtual.customervendor.R
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.EventDetail
import kotlinx.android.synthetic.main.customerbookinglist_row.view.*
import kotlinx.android.synthetic.main.list_item_event_business_profile.view.*
import java.util.ArrayList

class EventBusinessProfileAdapter (var context : Context, var restaurantList: ArrayList<EventDetail>, val clickListener: (EventDetail) -> Unit) : RecyclerView.Adapter<EventBusinessProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_event_business_profile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurantList.get(position), clickListener)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: EventDetail, clickListener: (EventDetail) -> Unit) {
            itemView.tv_event_name.setText(restaurant.name)

            itemView.setOnClickListener { clickListener(restaurant) }
        }
    }
}