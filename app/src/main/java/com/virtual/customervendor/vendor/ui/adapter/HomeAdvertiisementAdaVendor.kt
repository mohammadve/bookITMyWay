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
import com.virtual.customervendor.R.id.img_loc
import com.virtual.customervendor.model.OfferModel
import com.virtual.customervendor.model.VenderOffersModel
import kotlinx.android.synthetic.main.fragment_advertisement_vendor_item.view.*
import kotlinx.android.synthetic.main.fragment_booking_service_detail.*
import kotlinx.android.synthetic.main.fragment_customer_event_detail.view.*

class HomeAdvertiisementAdaVendor
(val mContext: Context, val offermodel: ArrayList<VenderOffersModel>, val clickListener: (VenderOffersModel) -> Unit) : RecyclerView.Adapter<HomeAdvertiisementAdaVendor.ResultItemViewHolder>() {

    var lisData: ArrayList<VenderOffersModel> = offermodel
    val filterlist: ArrayList<VenderOffersModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_advertisement_vendor_item, parent, false)
        return ResultItemViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position], clickListener)
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, offModel: VenderOffersModel, clickListener: (VenderOffersModel) -> Unit) {
            itemView.txt_title.text = offModel.title
            itemView.txt_desc.text = offModel.description
            itemView.txt_from.text = offModel.startdate
            itemView.txt_to.text = offModel.enddate
            itemView.txt_coupon.text = offModel.coupon
            itemView.txt_discount.text = offModel.price

            if (offModel.offer_images.size > 0) {
                Glide.with(mContext).load(offModel.offer_images.get(0).media).apply(RequestOptions.circleCropTransform()).into(itemView.img_loc)
            }
            itemView.setOnClickListener { clickListener(offModel) }

        }
    }


}




