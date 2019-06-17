package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.CustomerOrderDetailActivity
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.model.CustomerOrderModel
import com.virtual.customervendor.model.NotificationModel
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.SlideAnimationUtill
import com.virtual.customervendor.vendor.ui.activity.VendorOrderDetailActivity
import kotlinx.android.synthetic.main.fragment_notification_customer_item.view.*

class NotificationadapterCustomer(val pagingListener: PagingListeners, val mContext: Context, val offermodel: ArrayList<NotificationModel>, val clickListener: (NotificationModel) -> Unit) : RecyclerView.Adapter<NotificationadapterCustomer.ResultItemViewHolder>() {

    var lisData: ArrayList<NotificationModel> = offermodel
    val filterlist: ArrayList<NotificationModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_notification_customer_item, parent, false)
        return ResultItemViewHolder(view, clickListener)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position])
        if (position == (lisData.size - 1)) {
            pagingListener.onFinishListener();
        }
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    inner class ResultItemViewHolder(itemView: View, val clickListener: (NotificationModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, offModel: NotificationModel) {
            itemView.txt_title.text = offModel.title
            itemView.txt_name.text = offModel.text
            itemView.txt_time.text = offModel.created_time
            itemView.txt_date.text = offModel.created_date
            if (offModel.type.equals("neworder", true) || offModel.type.equals("newvendororder", true)) {
                itemView.iv_show_details_notifiction.visibility = View.VISIBLE
            } else {
                itemView.iv_show_details_notifiction.visibility = View.GONE

            }

            /*Surender changes*/
//            itemView.iv_show_details_notifiction.setOnClickListener {
            itemView.setOnClickListener {
                if (offModel.type.equals("neworder", true)) {
                    var orderModel = CustomerOrderModel()
                    orderModel.order_id = offModel.objectid
                    var intent: Intent = Intent(mContext, CustomerOrderDetailActivity::class.java)
                    var bundle = Bundle()
                    bundle.putSerializable(AppConstants.OREDER_DATA, orderModel)
                    intent.putExtras(bundle)
                    intent.putExtra("isComming_from_notification", true);
                    mContext.startActivity(intent)
                    SlideAnimationUtill.slideNextAnimation(mContext as AppCompatActivity)


                } else if (offModel.type.equals("newvendororder", true)) {
                    var orderdetailModel = CustomerOrderModel()
                    orderdetailModel.order_id = offModel.objectid
                    var intent: Intent = Intent(mContext!!, VendorOrderDetailActivity::class.java)
                    var bundle = Bundle()
                    bundle.putSerializable(AppConstants.OREDER_DATA, orderdetailModel)
                    intent.putExtras(bundle)
                    intent.putExtra("isComming_from_notification", true);
                    mContext.startActivity(intent)
                    SlideAnimationUtill.slideNextAnimation(mContext as AppCompatActivity)
                }


            }
//            itemView.setOnClickListener {
//                clickListener(offModel)
//            }
        }
    }

}




