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
import com.virtual.customervendor.utills.AppConstants
import kotlinx.android.synthetic.main.fragment_storeitem_single_row.view.*
import java.util.*


class StoreAddItemsAdapter(val mContext: Context, val from: String, val offermodel: ArrayList<ItemPriceStoreModel>) : RecyclerView.Adapter<StoreAddItemsAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<ItemPriceStoreModel> = offermodel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_storeitem_single_row, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, from, lisData[position])
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mcontx: Context? = null
        var datetime: String? = null


        fun bind(mContext: Context, from: String, offModel: ItemPriceStoreModel) {
            mcontx = mContext
            itemView.ed_itemname.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    if (s.length != 0)
                        offModel.item_name = itemView.ed_itemname.text.toString()
                }
            })
            itemView.ed_price.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    if (s.length != 0)
                        offModel.item_price = itemView.ed_price.text.toString()
                }
            })
//            itemView.ed_itemname.
            itemView.ed_itemname.setText(offModel.item_name)
            itemView.ed_price.setText(offModel.item_price)
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






