package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.ItemPriceModel
import com.virtual.customervendor.utills.AppConstants
import kotlinx.android.synthetic.main.fragment_restaurant_fooditems_single_row.view.*
import java.util.*


class HealthAddServiceAdapter(val mContext: Context, val from: String, val offermodel: ArrayList<ItemPriceModel>) : RecyclerView.Adapter<HealthAddServiceAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<ItemPriceModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_health_service_single_row, parent, false)
        return ResultItemViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, from, lisData[position])
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mcontx: Context? = null
        var datetime: String? = null


        fun bind(mContext: Context, from: String, offModel: ItemPriceModel) {
            mcontx = mContext
            itemView.ed_itemname.setText(offModel.itemName)
            itemView.ed_price.setText(offModel.itemPrice)
            if (from == AppConstants.FROM_REVIEW) {
                itemView.img_cancel.visibility = View.GONE
                itemView.ed_itemname.setFocusable(false)
                itemView.ed_price.setFocusable(false)
            } else {
                itemView.ed_itemname.setFocusable(true)
                itemView.ed_price.setFocusable(true)
                itemView.img_cancel.visibility = View.VISIBLE
                itemView.img_cancel.setOnClickListener(View.OnClickListener {
                    lisData.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                })
            }
        }
    }

}






