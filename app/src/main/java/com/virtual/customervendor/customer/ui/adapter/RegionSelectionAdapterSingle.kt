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
import com.virtual.customervendor.model.OfferModel
import com.virtual.customervendor.model.RegionModel
import kotlinx.android.synthetic.main.fragment_dialog_customer_item.view.*

class RegionSelectionAdapterSingle(val mContext: Context, val offermodel: ArrayList<RegionModel>, val clickListener: (RegionModel) -> Unit) : RecyclerView.Adapter<RegionSelectionAdapterSingle.ResultItemViewHolder>(), Filterable {
    private var filterdata: FilterText = FilterText()

    var lisData: ArrayList<RegionModel> = offermodel
    var filterlist: ArrayList<RegionModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dialog_customer_item, parent, false)
        return ResultItemViewHolder(view, clickListener)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext/*, lisData*/, position)
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

    inner class ResultItemViewHolder(itemView: View, val clickListener: (RegionModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context/*, serviceModelList: ArrayList<RegionModel>*/, position: Int) {
            if (!lisData.get(position).regionname.equals("NODATA")) {
                itemView.tv_noCountrySearch.visibility = View.GONE
                itemView.txt_title.visibility = View.VISIBLE
                itemView.txt_title.text = lisData.get(position).regionname

                itemView.setOnClickListener {
                    clickListener(lisData.get(position))
                }
            } else {
                itemView.tv_noCountrySearch.visibility = View.VISIBLE
                itemView.txt_title.visibility = View.GONE
                itemView.setClickable(false)
            }


//            }
        }
    }

    private inner class FilterText : Filter() {

        override fun convertResultToString(resultValue: Any): CharSequence {
            return (resultValue as RegionModel).regionname!!
        }

        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {

            val charString = constraint.toString()

            if (charString.isEmpty()) {
                lisData = filterlist
            } else {

                val filteredList = ArrayList<RegionModel>()

                for (bean in filterlist) {

                    if (bean.regionname?.toLowerCase()?.contains(charString)!! || bean.regionname?.toLowerCase()?.contains(charString)!! || bean.regionname?.toLowerCase()?.contains(charString)!!) {
                        filteredList.add(bean)
                    }
                }

                if (filteredList.size == 0) {
                    var noDataBean = RegionModel()
                    noDataBean.regionname = "NODATA"
                    noDataBean.regionid = ""
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
            lisData = results.values as ArrayList<RegionModel>
            notifyDataSetChanged()
        }
    }

}




