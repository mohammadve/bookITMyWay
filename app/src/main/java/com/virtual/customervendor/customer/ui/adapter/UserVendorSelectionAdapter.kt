package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.virtual.customervendor.R
import com.virtual.customervendor.model.BusinessDetail
import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.OfferModel
import kotlinx.android.synthetic.main.fragment_home_customer_item.view.*

class UserVendorSelectionAdapter(val mContext: Context, val businessList: ArrayList<BusinessDetail>, val clickListener: (BusinessDetail) -> Unit)
    : RecyclerView.Adapter<UserVendorSelectionAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<BusinessDetail> = businessList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dialog_user_vendor_item, parent, false)
        return ResultItemViewHolder(view, clickListener)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position])
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    class ResultItemViewHolder(itemView: View, val clickListener: (BusinessDetail) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, businessDetail: BusinessDetail) {
            itemView.txt_title.text = businessDetail.business_name
            itemView.setOnClickListener { clickListener(businessDetail) }
        }
    }


}




