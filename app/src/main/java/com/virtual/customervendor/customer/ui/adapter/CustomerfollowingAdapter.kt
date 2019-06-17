package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.virtual.customervendor.R
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.model.BusinessFollowingModel
import kotlinx.android.synthetic.main.customer_following_list_item.view.*


class CustomerfollowingAdapter(val pagingListener: PagingListeners, val mContext: Context, val offermodel: ArrayList<BusinessFollowingModel>, val clickListener: (BusinessFollowingModel,Int) -> Unit) : RecyclerView.Adapter<CustomerfollowingAdapter.ResultItemViewHolder>() {
    var lisData: ArrayList<BusinessFollowingModel> = offermodel
    val TAG: String = CustomerfollowingAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.customer_following_list_item, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, position,clickListener)
        if (position == (lisData.size - 1)) {
            pagingListener.onFinishListener();
        }
    }

    override fun getItemCount(): Int {
        return lisData.size
    }

    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, position: Int,  clickListener: (BusinessFollowingModel,Int) -> Unit) {
            itemView.name.setText(lisData.get(position).business_name)
            itemView.btn_follow_unfollow.setOnClickListener { clickListener(lisData.get(position),position) }
            if (lisData.get(position).business_image != null && !lisData.get(position).business_image.equals("")) {
                Glide.with(mContext).load(lisData.get(position).business_image).into(itemView.ivUserPic)
            }
        }
    }
}



