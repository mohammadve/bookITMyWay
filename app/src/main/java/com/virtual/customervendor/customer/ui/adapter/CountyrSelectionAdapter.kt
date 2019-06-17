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
import com.virtual.customervendor.model.CountryModel
import com.virtual.customervendor.model.OfferModel
import kotlinx.android.synthetic.main.fragment_dialog_country_item.view.*

class CountyrSelectionAdapter(val mContext: Context, val CountryModellist: ArrayList<CountryModel>, val clickListener: (CountryModel) -> Unit) : RecyclerView.Adapter<CountyrSelectionAdapter.ResultItemViewHolder>(), Filterable {
    private var filterdata: FilterText = FilterText()

    var lisData: ArrayList<CountryModel> = CountryModellist
    val filterlist: ArrayList<CountryModel> = CountryModellist


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dialog_country_item, parent, false)
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

    class ResultItemViewHolder(itemView: View, val clickListener: (CountryModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, offModel: CountryModel) {
            if (!offModel.name.equals("NODATA")) {
                itemView.txt_title.text = offModel.name
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
                val filteredList = ArrayList<CountryModel>()
                for (bean in filterlist) {
                    if (bean.name?.toLowerCase()?.contains(charString)!! || bean.name?.toLowerCase()?.contains(charString)!! || bean.name?.toLowerCase()?.contains(charString)!!) {
                        filteredList.add(bean)
                    }
                }

                if (filteredList.size == 0) {
                    var noDataBean = CountryModel()
                    noDataBean.name = "NODATA"
                    noDataBean.code = ""
                    filteredList.add(noDataBean)
                }

                lisData = filteredList
            }

            val filterResults = Filter.FilterResults()
            filterResults.values = lisData
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            lisData = results.values as ArrayList<CountryModel>
            notifyDataSetChanged()
        }
    }

}




