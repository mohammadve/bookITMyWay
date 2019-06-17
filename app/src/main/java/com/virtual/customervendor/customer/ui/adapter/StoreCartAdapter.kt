//package com.virtual.customervendor.customer.ui.adapter
//
//import android.content.Context
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.virtual.customervendor.R
//import com.virtual.customervendor.model.StoreCartModel
//import com.virtual.customervendor.utill.AppUtils
//import kotlinx.android.synthetic.main.list_item_cart.view.*
//import java.util.*
//
//class StoreCartAdapter  (var context : Context, var restaurantList: ArrayList<StoreCartModel>,val clickListener: (StoreCartModel) -> Unit) : RecyclerView.Adapter<StoreCartAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_cart, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(restaurantList.get(position),position,clickListener)
//    }
//
//    override fun getItemCount(): Int {
//        return restaurantList.size
//    }
//
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(restaurant: StoreCartModel,pos:Int,clickListener: (StoreCartModel) -> Unit) {
//            itemView.tv_item_name.setText(restaurant.item_name)
//            itemView.tv_price.setText(AppUtils.getRateWithSymbol(""+restaurant.price!!.toInt() * restaurant.quantity!!.toInt()))
//            itemView.tv_quantity.text= restaurant.quantity
//            itemView.plus_img.setOnClickListener {
//                itemView.tv_quantity.setText(""+(restaurant.quantity?.toInt()!!.plus(1)))
////                (context as PurchaseItemsActivity).addedItemsList.get(pos).quantity=""+(restaurant.quantity?.toInt()!!.plus(1))
//                itemView.tv_price.setText(AppUtils.getRateWithSymbol(""+(restaurant.price!!.toInt() * restaurant.quantity!!.toInt())))
//
//                clickListener(restaurant)
//            }
//
//            itemView.minus_img.setOnClickListener {
//                if(restaurant.quantity?.toInt()!= 1) {
//                    itemView.tv_quantity.setText("" + (restaurant.quantity?.toInt()!!.minus(1)))
////                    (context as PurchaseItemsActivity).addedItemsList.get(pos).quantity = "" + (restaurant.quantity?.toInt()!!.minus(1))
//                    itemView.tv_price.setText(AppUtils.getRateWithSymbol(""+restaurant.price!!.toInt() * restaurant.quantity!!.toInt()))
//
//                    clickListener(restaurant)
//                }
//            }
//        }
//    }
//}