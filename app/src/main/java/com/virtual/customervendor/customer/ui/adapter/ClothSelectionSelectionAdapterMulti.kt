package com.virtual.customervendor.customer.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.virtual.customervendor.R
import com.virtual.customervendor.model.CityModel
import com.virtual.customervendor.model.OfferModel
import com.virtual.customervendor.model.response.StoreClothColorModel
import kotlinx.android.synthetic.main.fragment_dialog_customer_item_multi.view.*

class ClothSelectionSelectionAdapterMulti(val mContext: Context, val offermodel: ArrayList<StoreClothColorModel>, val clickListener: (ArrayList<StoreClothColorModel>) -> Unit) : RecyclerView.Adapter<ClothSelectionSelectionAdapterMulti.ResultItemViewHolder>(), Filterable {
    private var filterdata: FilterText = FilterText()

    var lisData: ArrayList<StoreClothColorModel> = offermodel
    var filterlist: ArrayList<StoreClothColorModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dialog_customer_item_multi, parent, false)
        return ResultItemViewHolder(view, clickListener)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData, position)
    }


    override fun getItemCount(): Int {
        return lisData.size
    }

    override fun getFilter(): Filter {
        if (filterdata == null) {
            filterdata = FilterText()
        }
        return filterdata
    }

    inner class ResultItemViewHolder(itemView: View, val clickListener: (ArrayList<StoreClothColorModel>) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, serviceModelList: ArrayList<StoreClothColorModel>, position: Int) {
            if (!serviceModelList.get(position).name.equals("NODATA")) {
                itemView.tv_noCountrySearch.visibility = View.GONE
                itemView.txt_title.visibility = View.VISIBLE
                itemView.txt_title.text = serviceModelList.get(position).name
                if (serviceModelList.get(position).isSelected) {
                    itemView.txt_title.isChecked = true
                }else {
                    itemView.txt_title.isChecked = false
                }
//
//

                itemView.txt_title.setOnClickListener {
                    serviceModelList.get(position).isSelected = !serviceModelList.get(position).isSelected
                    clickListener(serviceModelList)
                    notifyDataSetChanged()
                }
            } else {
                itemView.tv_noCountrySearch.visibility = View.VISIBLE
                itemView.txt_title.visibility = View.GONE
                itemView.setClickable(false)
            }
        }
    }

    private inner class FilterText : Filter() {

        override fun convertResultToString(resultValue: Any): CharSequence {
            return (resultValue as OfferModel).name
        }

        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {

            val charString = constraint.toString()

            if (charString.isEmpty()) {
                lisData = filterlist
            } else {

                val filteredList = ArrayList<StoreClothColorModel>()

                for (bean in filterlist) {
                    if(bean.isSelected){
                        filteredList.add(bean)
                    }

                    if (bean.name?.toLowerCase()?.contains(charString)!! || bean.name?.toLowerCase()?.contains(charString)!! || bean.name?.toLowerCase()?.contains(charString)!!) {
                        filteredList.add(bean)
                    }
                }

                if (filteredList.size == 0) {
                    var noDataBean = StoreClothColorModel()
                    noDataBean.name = "NODATA"
                    noDataBean.color_id= -1
                    noDataBean.isSelected = false
                    filteredList.add(noDataBean)
                }

                lisData = filteredList
            }

            val filterResults = Filter.FilterResults()
            filterResults.values = lisData
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            lisData = results.values as ArrayList<StoreClothColorModel>
            notifyDataSetChanged()
        }
    }

}




