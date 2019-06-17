package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.StoreItemLocationModel
import com.virtual.customervendor.utills.AppConstants
import kotlinx.android.synthetic.main.fragment_storedeliver_review_single_row.view.*
import java.util.*


class StoreDeliverLocationReviewAdapter(val mContext: Context, val from: String, val offermodel: ArrayList<StoreItemLocationModel>) : RecyclerView.Adapter<StoreDeliverLocationReviewAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<StoreItemLocationModel> = offermodel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_storedeliver_review_single_row, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, from, lisData[position])
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(mContext: Context, from: String, offModel: StoreItemLocationModel) {

            itemView.ed_itemname.setText(offModel.location_name)
            if (from == AppConstants.FROM_REVIEW) {
                itemView.ed_itemname.setFocusable(false)
            } else {
                itemView.ed_itemname.setFocusable(true)
            }
        }
    }

}






