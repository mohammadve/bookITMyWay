package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.ClothingCategoryModel
import kotlinx.android.synthetic.main.fragment_store_subcategory_clothing.view.*
import java.util.*


class StoreClothSubCategoryAdapter(val mContext: Context, val offermodel: ArrayList<ClothingCategoryModel>) : RecyclerView.Adapter<StoreClothSubCategoryAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<ClothingCategoryModel> = offermodel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_store_subcategory_clothing, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position])
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mcontx: Context? = null
        var datetime: String? = null


        fun bind(mContext: Context, offModel: ClothingCategoryModel) {
            mcontx = mContext
            itemView.name.text = offModel.name

        }
    }

}






