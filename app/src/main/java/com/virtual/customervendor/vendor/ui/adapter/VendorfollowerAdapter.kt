package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.virtual.customervendor.R
import com.virtual.customervendor.R.id.ivUserPic
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.model.BusinessFollowerModel
import com.virtual.customervendor.model.FollowerModel
import kotlinx.android.synthetic.main.follower_list_item.view.*


class VendorfollowerAdapter(val pagingListener: PagingListeners, val mContext: Context, val offermodel: ArrayList<BusinessFollowerModel>) : RecyclerView.Adapter<VendorfollowerAdapter.ResultItemViewHolder>() {
    var lisData: ArrayList<BusinessFollowerModel> = offermodel
    val TAG: String = VendorfollowerAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.follower_list_item, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, position)
        if (position == (lisData.size - 1)) {
            pagingListener.onFinishListener();
        }
    }

    override fun getItemCount(): Int {
        return lisData.size
    }

    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, position: Int) {
            itemView.name.setText(lisData.get(position).name)
            if (lisData.get(position).avatar != null && !lisData.get(position).avatar.equals("")) {
//                Glide.with(mContext).load(lisData.get(position).avatar).into(ivUserPic)
            }
        }
    }
}




