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
import kotlinx.android.synthetic.main.fragment_dialog_customer_item.view.*

class CitySelectionAdapter(val mContext: Context, val cityModellist: ArrayList<CityModel>, val clickListener: (CityModel) -> Unit) : RecyclerView.Adapter<CitySelectionAdapter.ResultItemViewHolder>(), Filterable {
    private var filterdata: FilterText = FilterText()

    var lisData: ArrayList<CityModel> = cityModellist
    val filterlist: ArrayList<CityModel> = cityModellist


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dialog_customer_item, parent, false)
        return ResultItemViewHolder(view, clickListener)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position])
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

    class ResultItemViewHolder(itemView: View, val clickListener: (CityModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, offModel: CityModel) {
            if (!offModel.cityname.equals("NODATA")) {
                itemView.txt_title.text = offModel.cityname
                itemView.setOnClickListener { clickListener(offModel) }
            } else {
                itemView.txt_title.visibility = View.GONE
                itemView.tv_noCountrySearch.visibility = View.VISIBLE
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
                val filteredList = ArrayList<CityModel>()
                for (bean in filterlist) {
                    if (bean.cityname?.toLowerCase()?.contains(charString)!! || bean.cityname?.toLowerCase()?.contains(charString)!! || bean.cityname?.toLowerCase()?.contains(charString)!!) {
                        filteredList.add(bean)
                    }
                }

                if (filteredList.size == 0) {
                    var noDataBean = CityModel()
                    noDataBean.cityname = "NODATA"
                    noDataBean.cityid = ""
                    filteredList.add(noDataBean)
                }

                lisData = filteredList
            }

            val filterResults = Filter.FilterResults()
            filterResults.values = lisData
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            lisData = results.values as ArrayList<CityModel>
            notifyDataSetChanged()
        }
    }

}




