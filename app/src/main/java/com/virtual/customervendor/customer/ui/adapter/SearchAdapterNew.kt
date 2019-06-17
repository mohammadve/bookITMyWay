package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.SearchModel
import kotlinx.android.synthetic.main.fragment_dialog_customer_serach.view.*
import java.util.*

class SearchAdapterNew(var context: Context, var restaurantList: ArrayList<SearchModel>, val clickListener: (SearchModel) -> Unit) : RecyclerView.Adapter<SearchAdapterNew.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_dialog_customer_serach, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurantList.get(position), clickListener)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: SearchModel, clickListener: (SearchModel) -> Unit) {
            itemView.txt_title.setText(restaurant.business_name)
            itemView.setOnClickListener {
                clickListener(restaurant)
            }
        }
    }
}