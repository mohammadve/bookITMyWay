package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.ItemPriceStoreModel
import com.virtual.customervendor.utills.AppLog
import kotlinx.android.synthetic.main.fragment_storeitem_editrow.view.*
import java.util.*

class StoreUpdateItemsAdapter(val mContext: Context, val from: String, val offermodel: ArrayList<ItemPriceStoreModel>, val clickListener: UpdateItems) : RecyclerView.Adapter<StoreUpdateItemsAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<ItemPriceStoreModel> = offermodel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_storeitem_editrow, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, from, lisData[position], clickListener)
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            itemView.img_update.visibility = View.VISIBLE
            itemView.img_cancel.visibility = View.GONE
        }

        var mcontx: Context? = null


        fun bind(mContext: Context, from: String, offModel: ItemPriceStoreModel, clickListener: UpdateItems) {
            mcontx = mContext
            itemView.ed_itemname.setText(offModel.item_name)
            itemView.ed_price.setText(offModel.item_price)
            itemView.ed_itemname.setFocusable(true)
            itemView.ed_price.setFocusable(true)
            itemView.img_cancel.visibility = View.VISIBLE

            itemView.ed_price.setOnClickListener(View.OnClickListener {
                itemView.img_cancel.visibility = View.GONE
                itemView.img_update.visibility = View.VISIBLE
            })
            itemView.ed_itemname.setOnClickListener(View.OnClickListener {
                itemView.img_cancel.visibility = View.GONE
                itemView.img_update.visibility = View.VISIBLE
            })

            itemView.img_cancel.setOnClickListener(View.OnClickListener {
                AppLog.e("tagtag", "img_cancel")
                clickListener.deleteItem(offModel,adapterPosition)
            })
            itemView.img_update.setOnClickListener(View.OnClickListener {
                AppLog.e("tagtag", "img_update")
                clickListener.updateItem(offModel,adapterPosition)
            })
        }
    }


    public interface UpdateItems {
        fun deleteItem(itempriceModel: ItemPriceStoreModel, adapterPosition: Int)
        fun updateItem(itempriceModel: ItemPriceStoreModel, adapterPosition: Int)
    }

}






