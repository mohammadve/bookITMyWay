package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.OfferModel
import kotlinx.android.synthetic.main.fragment_home_customer_item.view.*

class MyAccountadapterCustomer(val mContext: Context, val offermodel: ArrayList<OfferModel>) : RecyclerView.Adapter<MyAccountadapterCustomer.ResultItemViewHolder>() {

    var lisData: ArrayList<OfferModel> = offermodel
    val filterlist: ArrayList<OfferModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_myaccount_customer_item, parent, false)
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

        fun bind(mContext: Context, offModel: OfferModel) {
//                itemView.txt_title.text = offModel.name
//            itemView.txt_name.text = ""
//            itemView.txt_validty.text = ""

        }
    }


}




