package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.utills.AppLog
import kotlinx.android.synthetic.main.fragment_dialog_user_vendor_item.view.*

class IssueSelectionAdapter(val mContext: Context, val stringList: ArrayList<String>, val clickListener: (String) -> Unit)
    : RecyclerView.Adapter<IssueSelectionAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<String> = stringList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dialog_user_vendor_item, parent, false)
        return ResultItemViewHolder(view, clickListener)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position])
    }


    override fun getItemCount(): Int {
        AppLog.e("@@@","Size"+lisData.size)
        return lisData.size
    }


    class ResultItemViewHolder(itemView: View, val clickListener: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, st: String) {
            itemView.txt_title.text = st
            itemView.setOnClickListener { clickListener(st) }
        }
    }


}




