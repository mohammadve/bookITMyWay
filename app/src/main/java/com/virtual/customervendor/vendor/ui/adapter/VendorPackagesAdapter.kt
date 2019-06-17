package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.PackageModel
import com.virtual.customervendor.utills.AppUtils
import kotlinx.android.synthetic.main.activity_packeages_item.view.*


class VendorPackagesAdapter(val mContext: Context, val offermodel: ArrayList<PackageModel>, val clickListener: (PackageModel) -> Unit) : RecyclerView.Adapter<VendorPackagesAdapter.ResultItemViewHolder>() {
    var lisData: ArrayList<PackageModel> = offermodel
    val TAG: String = VendorPackagesAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.activity_packeages_item, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, clickListener, position)
    }

    override fun getItemCount(): Int {
        return lisData.size
    }

    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, clickListener: (PackageModel) -> Unit, position: Int) {
            itemView.txtpack.setText(lisData.get(position).package_name)
            itemView.txtcost.setText(AppUtils.getRegisterRateWithSymbol(lisData.get(position).cost))

            var time = StringBuilder()
            if (AppUtils.getStatusBoolean(lisData.get(position).all_day)) {
                time.append(mContext.resources.getString(R.string.all_days))
            } else {
                if (AppUtils.getStatusBoolean(lisData.get(position).monday)) {
                    time.append(mContext.resources.getString(R.string.monday_m))
                }
                if (AppUtils.getStatusBoolean(lisData.get(position).tuesday)) {
                    if (time.toString().length > 0)
                        time.append(", " + mContext.resources.getString(R.string.tuesday_t))
                    else
                        time.append(mContext.resources.getString(R.string.tuesday_t))
                }
                if (AppUtils.getStatusBoolean(lisData.get(position).wednesday)) {
                    if (time.toString().length > 0)
                        time.append(", " + mContext.resources.getString(R.string.wednesday_w))
                    else {
                        time.append(mContext.resources.getString(R.string.wednesday_w))
                    }
                }
                if (AppUtils.getStatusBoolean(lisData.get(position).thursday)) {
                    if (time.toString().length > 0)
                        time.append(", " + mContext.resources.getString(R.string.thursday_t))
                    else {
                        time.append(mContext.resources.getString(R.string.thursday_t))
                    }
                }
                if (AppUtils.getStatusBoolean(lisData.get(position).friday)) {
                    if (time.toString().length > 0)
                        time.append(", " + mContext.resources.getString(R.string.friday_f))
                    else {
                        time.append(mContext.resources.getString(R.string.friday_f))
                    }
                }
                if (AppUtils.getStatusBoolean(lisData.get(position).saturday)) {
                    if (time.toString().length > 0)
                        time.append(", " + mContext.resources.getString(R.string.saturday_s))
                    else {
                        time.append(mContext.resources.getString(R.string.saturday_s))
                    }
                }
                if (AppUtils.getStatusBoolean(lisData.get(position).sunday)) {
                    if (time.toString().length > 0)
                        time.append(", " + mContext.resources.getString(R.string.sunday_t))
                    else {
                        time.append(mContext.resources.getString(R.string.sunday_t))
                    }
                }
            }

            itemView.datetime.setText(time)
            itemView.setOnClickListener { clickListener(lisData.get(position)) }

        }
    }
}




