package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.virtual.customervendor.R
import com.virtual.customervendor.model.StoreItemLocationModel
import com.virtual.customervendor.model.response.StoreClothColorModel
import com.virtual.customervendor.utills.AppConstants
import kotlinx.android.synthetic.main.store_cloth_color_quantity_single_row.view.*
import kotlinx.android.synthetic.main.store_cloth_type_single_row.view.*
import java.util.*


class StoreClothColorQuentityAdapter(val mContext: AppCompatActivity, val from: String, var offermodel: ArrayList<StoreClothColorModel>) : RecyclerView.Adapter<StoreClothColorQuentityAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<StoreClothColorModel> = offermodel
var needTochangeSpinnerValue= false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.store_cloth_color_quantity_single_row, parent, false)
        return ResultItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, from, lisData[position],position)
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mcontx: Context? = null
        var datetime: String? = null


        fun bind(mContext: Context, from: String, offModel: StoreClothColorModel, position: Int) {

            itemView.tv_color_name.text=offModel.name



            itemView.tv_quantity.setOnClickListener {
                itemView.quantity_Spinner.performClick()
                needTochangeSpinnerValue=true
            }
            itemView.remove_colors.setOnClickListener {
                lisData.removeAt(position)
                notifyDataSetChanged()
            }
            itemView.quantity_Spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if(needTochangeSpinnerValue){
                    itemView.tv_quantity.text =parent!!.getItemAtPosition(position).toString()}
                    needTochangeSpinnerValue=false
                }

            }


            if(!offModel.quantity.equals("")){
                itemView.tv_quantity.text=offModel.quantity
            }


            itemView.tv_quantity.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    offModel.quantity=itemView.tv_quantity.text.toString().trim()

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })

            if(!offModel.price.equals("")){
                itemView.tv_price.setText(offModel.price)
            }


            itemView.tv_price.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    offModel.price=itemView.tv_price.text.toString().trim()

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })


            if(!offModel.bookingPrice.equals("")){
                itemView.tv_price2.setText(offModel.bookingPrice)
            }
            itemView.tv_price2.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    offModel.bookingPrice=itemView.tv_price2.text.toString().trim()

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })


        }
    }

}






