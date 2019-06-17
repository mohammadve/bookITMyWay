package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.virtual.customervendor.R
import com.virtual.customervendor.model.LanguageModel
import kotlinx.android.synthetic.main.activity_select_lang_item.view.*
import java.lang.Exception


class SelectLanguageAdapter(val mContext: Context, val offermodel: ArrayList<LanguageModel>, val clickListener: (LanguageModel) -> Unit) : RecyclerView.Adapter<SelectLanguageAdapter.ResultItemViewHolder>() {
    var lisData: ArrayList<LanguageModel> = offermodel
    val TAG: String = SelectLanguageAdapter::class.java.simpleName
    private val lastSelectedPosition = -1
    var selectedpos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.activity_select_lang_item, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {

      //  holder.bind(mContext, clickListener, position)

        holder.rad_taxi.setOnCheckedChangeListener(null)

        holder.rad_taxi.setText(lisData.get(position).name)
        holder.rad_taxi.isChecked = lisData.get(position).checked!!
        if (lisData.get(position).checked!!) {
            selectedpos = position
        }
        holder.rad_taxi.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                 if (selectedpos != -1) {
                     lisData.get(selectedpos).checked = false
                 }
                 lisData.get(position).checked = true
                 selectedpos = position




                for (i in 0..lisData.size - 1) {
                    lisData.get(i).checked = false
                }


                lisData.get(position).checked = true


                Handler().post(object : Runnable {
                    override fun run() {
                        clickListener(lisData.get(position))
                        notifyDataSetChanged()
                    }
                })




            }
        }


    }

    override fun getItemCount(): Int {
        return lisData.size
    }

    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var rad_taxi: RadioButton

        init {

            rad_taxi = itemView.findViewById(R.id.rad_taxi)

        }


        fun bind(mContext: Context, clickListener: (LanguageModel) -> Unit, position: Int) {
        /*    rad_taxi.setText(lisData.get(position).name)
            rad_taxi.isChecked = lisData.get(position).checked!!
            if (lisData.get(position).checked!!) {
                selectedpos = position
            }
*/



/*            rad_taxi.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                     if (selectedpos != -1) {
                         lisData.get(selectedpos).checked = false
                     }
                     lisData.get(position).checked = true
                     selectedpos = position



                    *//*for (i in 0..lisData.size - 1) {
                        lisData.get(i).checked = false
                    }

*//*
                    lisData.get(position).checked = true


                    Handler().post(object : Runnable {
                        override fun run() {
                            clickListener(lisData.get(position))
                            notifyDataSetChanged()
                        }
                    })




                }
            }*/
        }
    }
}




