package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.model.ItemPriceStoreModel
import kotlinx.android.synthetic.main.fragment_vendor_store_subcategory_single_row.view.*
import java.util.*


class VendorStoreSubCategoryAdapter(val mContext: Context, val offermodel: ArrayList<ItemPriceStoreModel>, val clickListener: UpdateItems) : RecyclerView.Adapter<VendorStoreSubCategoryAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<ItemPriceStoreModel> = offermodel
    var clickListener1 = clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_vendor_store_subcategory_single_row, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position], clickListener1)
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mcontx: Context? = null
        var datetime: String? = null


        fun bind(mContext: Context, offModel: ItemPriceStoreModel, clickListener: UpdateItems) {
            mcontx = mContext
            itemView.name.text = offModel.item_name

            if (offModel.item_image.size > 0)
                AppUtill.loadImageList(mContext, offModel.item_image.get(0), itemView.ivUserPic)
            itemView.ivedit.setOnClickListener { clickListener.updateItem(offModel) }
            itemView.iv_delete.setOnClickListener { clickListener.deleteItem(offModel) }

        }
    }


    public interface UpdateItems {
        fun deleteItem(itempriceModel: ItemPriceStoreModel)
        fun updateItem(itempriceModel: ItemPriceStoreModel)
    }

}






