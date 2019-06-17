package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.model.ItemPriceStoreModel
import kotlinx.android.synthetic.main.fragment_store_subcategory_single_row.view.*
import java.util.*


class StoreSubCategoryAdapter(val mContext: Context, val offermodel: ArrayList<ItemPriceStoreModel>, val clickListener: (ItemPriceStoreModel) -> Unit) : RecyclerView.Adapter<StoreSubCategoryAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<ItemPriceStoreModel> = offermodel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_store_subcategory_single_row, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position], clickListener)
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mcontx: Context? = null
        var datetime: String? = null


        fun bind(mContext: Context, offModel: ItemPriceStoreModel, clickListener: (ItemPriceStoreModel) -> Unit) {
            mcontx = mContext
            itemView.name.text = offModel.item_name

            if (offModel.item_image.size > 0)
                AppUtill.loadImageList(mContext, offModel.item_image.get(0), itemView.ivUserPic)
            itemView.setOnClickListener { clickListener(offModel) }

        }
    }

}






