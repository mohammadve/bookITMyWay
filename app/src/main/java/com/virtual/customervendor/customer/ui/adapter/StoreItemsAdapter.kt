package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.ItemPriceStoreModel

import kotlinx.android.synthetic.main.list_item_store_item.view.*
import java.util.ArrayList

class StoreItemsAdapter  (var context : Context, var restaurantList: ArrayList<ItemPriceStoreModel>, val clickListener: (ItemPriceStoreModel) -> Unit) : RecyclerView.Adapter<StoreItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_store_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurantList.get(position), clickListener)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: ItemPriceStoreModel, clickListener: (ItemPriceStoreModel) -> Unit) {
            itemView.tv_item_name.setText(restaurant.item_name)
            itemView.tv_price.setText(restaurant.item_price)
            itemView.tv_add_to_cart.setOnClickListener {
                if(itemView.tv_add_to_cart.getTag().equals("addtocart")) {
                    itemView.tv_add_to_cart.text = "Added"
                    itemView.tv_add_to_cart.tag="added"
                    clickListener(restaurant)
                }
            }
        }
    }
}