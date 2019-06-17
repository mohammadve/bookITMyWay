package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.model.RestaurantPriceModel
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppUtils
import kotlinx.android.synthetic.main.fragment_restaurant_itemview_row.view.*


class RestaurantItemsViewAdapter(val mContext: Context, val offermodel: ArrayList<RestaurantPriceModel>, val fromWhere: String, val clickListener: (RestaurantPriceModel) -> Unit) : RecyclerView.Adapter<RestaurantItemsViewAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<RestaurantPriceModel> = offermodel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_restaurant_itemview_row, parent, false)
        return ResultItemViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position], fromWhere, clickListener)
    }


    override fun getItemCount(): Int {
        return lisData.size
    }

    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mcontx: Context? = null
        var datetime: String? = null
        fun bind(mContext: Context, offModel: RestaurantPriceModel, fromWhere: String, clickListener: (RestaurantPriceModel) -> Unit) {
            mcontx = mContext
            itemView.ed_itemname.setText(offModel.item_name)

            itemView.ed_price.setText(AppUtils.getRegisterRateWithSymbol(offModel.item_price))

            if (offModel.item_image != null && offModel.item_image.size > 0)
                AppUtill.loadImageList(mcontx!!, offModel.item_image.get(0), itemView.imgBusiness)

            if (fromWhere.equals(AppConstants.ACTION_ADD)) {
                itemView.imgcancel.visibility = View.VISIBLE
                itemView.imgcancel.setOnClickListener { clickListener(offModel) }
            } else {
                itemView.imgcancel.visibility = View.GONE
            }
        }

    }
}






