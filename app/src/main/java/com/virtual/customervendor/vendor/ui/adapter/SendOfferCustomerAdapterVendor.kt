package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.virtual.customervendor.R
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.model.BusinessFollowerModel
import kotlinx.android.synthetic.main.activity_sendoffer_customer_vendor_item.view.*

class SendOfferCustomerAdapterVendor(val pagingListener: PagingListeners, val mContext: Context, val offermodel: ArrayList<BusinessFollowerModel>, val clickListener: (Int, Boolean) -> Unit) : RecyclerView.Adapter<SendOfferCustomerAdapterVendor.ResultItemViewHolder>() {

    var lisData: ArrayList<BusinessFollowerModel> = offermodel
    val filterlist: ArrayList<BusinessFollowerModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.activity_sendoffer_customer_vendor_item, parent, false)
        return ResultItemViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, position, lisData[position], clickListener)
        if (position == (lisData.size - 1)) {
            pagingListener.onFinishListener();
        }
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, pos: Int, offModel: BusinessFollowerModel, clickListener: (Int, Boolean) -> Unit) {
            itemView.simpleCheckedTextView.text = offModel.name
            if (offModel.checked!!) {
                itemView.simpleCheckedTextView.setChecked(true)
                itemView.simpleCheckedTextView.setCheckMarkDrawable(R.drawable.checkbox_sel)
            } else {
                itemView.simpleCheckedTextView.setChecked(false)
                itemView.simpleCheckedTextView.setCheckMarkDrawable(R.drawable.checkbox_unselect)
            }
            itemView.simpleCheckedTextView.setChecked(offModel.checked!!)
            Glide.with(mContext).load(offModel.avatar).apply(RequestOptions.circleCropTransform().placeholder(R.drawable.place_holder_list)).into(itemView.img_loc)

            itemView.setOnClickListener {
                if (itemView.simpleCheckedTextView.isChecked) {
                    itemView.simpleCheckedTextView.setChecked(false)
                    itemView.simpleCheckedTextView.setCheckMarkDrawable(R.drawable.checkbox_unselect)
                    clickListener(pos, false)
                } else {
                    itemView.simpleCheckedTextView.setChecked(true)
                    itemView.simpleCheckedTextView.setCheckMarkDrawable(R.drawable.checkbox_sel)
                    clickListener(pos, true)
                }
            }

        }
    }


}




