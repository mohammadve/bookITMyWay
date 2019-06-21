package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.CustomerTimeModel
import kotlinx.android.synthetic.main.fragment_dialog_time_selection_item.view.*

class TimeSelectionAdapter(val mContext: Context, val cityModellist: ArrayList<CustomerTimeModel>, val clickListener: (CustomerTimeModel) -> Unit) : RecyclerView.Adapter<TimeSelectionAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<CustomerTimeModel> = cityModellist
    val filterlist: ArrayList<CustomerTimeModel> = cityModellist


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dialog_time_selection_item, parent, false)
        return ResultItemViewHolder(view, clickListener)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position],position)
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    class ResultItemViewHolder(itemView: View, val clickListener: (CustomerTimeModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, offModel: CustomerTimeModel,pos: Int) {
            itemView.txt_title.text = offModel.slot
            itemView.txt_remain.text = offModel.remain.toString() +" / "+ offModel.totalseat
            if (offModel.status.equals("1")) {
                itemView.setClickable(true)
                itemView.setOnClickListener { clickListener(offModel) }
            } else {
                itemView.txt_title.setTextColor(mContext.resources.getColor(R.color.btn_bg_color_d8d8d8))
                itemView.setClickable(false)
                itemView.setFocusable(false)
            }
        }
    }


}




