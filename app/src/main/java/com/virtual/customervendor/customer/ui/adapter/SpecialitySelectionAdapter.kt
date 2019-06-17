package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.model.AppointmentReasonModel
import kotlinx.android.synthetic.main.fragment_dialog_customer_item.view.*

class SpecialitySelectionAdapter (val mContext: Context, val offermodel: ArrayList<AppointmentReasonModel>, val clickListener: (AppointmentReasonModel) -> Unit) : RecyclerView.Adapter<SpecialitySelectionAdapter.ResultItemViewHolder>()/*, Filterable*/ {
//    private var filterdata: FilterText = FilterText()

    var lisData: ArrayList<AppointmentReasonModel> = offermodel
    var filterlist: ArrayList<AppointmentReasonModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dialog_customer_item, parent, false)
        return ResultItemViewHolder(view, clickListener)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData, position)
    }


    override fun getItemCount(): Int {
        return lisData.size
    }

//    override fun getFilter(): Filter {
//        if (filterdata == null) {
//            filterdata = FilterText()
//        }
//        return filterdata
//    }

    class ResultItemViewHolder(itemView: View, val clickListener: (AppointmentReasonModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, serviceModelList: ArrayList<AppointmentReasonModel>, position: Int) {
            if (!serviceModelList.get(position).reason.equals("NODATA")) {
                itemView.txt_title.text = serviceModelList.get(position).reason
            itemView.setOnClickListener {
                clickListener(serviceModelList.get(position))}

        }
    }

//    private inner class FilterText : Filter() {
//
//        override fun convertResultToString(resultValue: Any): CharSequence {
//            return (resultValue as OfferModel).name
//        }
//
//        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
//
//            val charString = constraint.toString()
//
//            if (charString.isEmpty()) {
//                lisData = filterlist
//            } else {
//
//                val filteredList = ArrayList<AppointmentReasonModel>()
//
//                for (bean in filterlist) {
//
//                    if (bean.name?.toLowerCase()?.contains(charString)!! || bean.name?.toLowerCase()?.contains(charString)!! || bean.name?.toLowerCase()?.contains(charString)!!) {
//                        filteredList.add(bean)
//                    }
//                }
//
//                if (filteredList.size == 0) {
//                    var noDataBean = AppointmentReasonModel()
//                    noDataBean.name = "NODATA"
//                    noDataBean.name = ""
//                    noDataBean.isSelected = false
//                    filteredList.add(noDataBean)
//                }
//
//                lisData = filteredList
//            }
//
//            val filterResults = Filter.FilterResults()
//            filterResults.values = lisData
//            return filterResults
//        }
//
//        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
//            lisData = results.values as ArrayList<AppointmentReasonModel>
//            notifyDataSetChanged()
//        }
    }

}