package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.ItemPriceStoreModel
import com.virtual.customervendor.utills.AppLog
import kotlinx.android.synthetic.main.list_item_store_confirm_order.view.*
import java.util.ArrayList

class StoreItemsConfirmOrderAdapter(var context: Context, var restaurantList: ArrayList<ItemPriceStoreModel>) : RecyclerView.Adapter<StoreItemsConfirmOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_store_confirm_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurantList.get(position))
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: ItemPriceStoreModel) {
            AppLog.e("CHECK DATA", restaurant.toString())
            itemView.tv_item_name.setText(restaurant.item_name)
            itemView.tv_quantity.setText(restaurant.quantity.toString())
            var price = (restaurant.item_price.toDouble() * restaurant.quantity).toString()
            itemView.tv_price.setText(price)
            if (!restaurant.add_on_one.isEmpty()) {
                itemView.tv_addone.visibility = View.VISIBLE
                itemView.tv_addone.text = restaurant.add_on_one
            }else{
                itemView.tv_addone.visibility = View.GONE
            }
            if (!restaurant.add_on_two.isEmpty()) {
                itemView.tv_addtwo.text = restaurant.add_on_two
                itemView.tv_addtwo.visibility = View.VISIBLE
            }else{
                itemView.tv_addtwo.visibility = View.GONE
            }
        }
    }


}