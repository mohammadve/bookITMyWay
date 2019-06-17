package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.ItemPriceModel
import com.virtual.customervendor.utills.AppUtils
import kotlinx.android.synthetic.main.list_item_bodycare_servicemenu.view.*
import java.util.*

class BodyCareServiceMenuAdapter  (var context : Context, var restaurantList: ArrayList<ItemPriceModel>) : RecyclerView.Adapter<BodyCareServiceMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_bodycare_servicemenu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurantList.get(position))
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: ItemPriceModel) {
            itemView.tv_item_name.setText(restaurant.itemName)
            itemView.tv_quantity.setText(AppUtils.getRateWithSymbol(restaurant.itemPrice))
        }
    }


}