package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.model.CustomerBookingListModel
import kotlinx.android.synthetic.main.customerbookinglist_row.view.*
import java.util.*


class CustomerBookingListAdapter(var context: Context, var restaurantList: ArrayList<CustomerBookingListModel>, val clickListener: (CustomerBookingListModel) -> Unit) : RecyclerView.Adapter<CustomerBookingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.customerbookinglist_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurantList.get(position), clickListener)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: CustomerBookingListModel, clickListener: (CustomerBookingListModel) -> Unit) {
            itemView.txtBusiness.setText(restaurant.business_name)
            itemView.txtregion.setText(restaurant.contact_number)
            itemView.txtcity.setText(restaurant.cityname)

            if (restaurant.business_image != null && !restaurant.business_image.equals("")) {
                AppUtill.loadImageList(context,restaurant.business_image!!,itemView.imgBusiness)
            }
            itemView.setOnClickListener { clickListener(restaurant) }
        }
    }
}