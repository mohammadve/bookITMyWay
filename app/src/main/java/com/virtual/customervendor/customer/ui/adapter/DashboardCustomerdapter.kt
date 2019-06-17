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
import kotlinx.android.synthetic.main.fragment_home_customer_item.view.*

class DashboardCustomerdapter(val mContext: Context, val offermodel: ArrayList<OfferModel>,val clickListener: (OfferModel) -> Unit) : RecyclerView.Adapter<DashboardCustomerdapter.ResultItemViewHolder>(), Filterable {
    private var filterdata: FilterText = FilterText()

    var lisData: ArrayList<OfferModel> = offermodel
    val filterlist: ArrayList<OfferModel> = offermodel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dialog_customer_item, parent, false)
        return ResultItemViewHolder(view,clickListener)
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

    class ResultItemViewHolder(itemView: View,val clickListener: (OfferModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, offModel: OfferModel) {
            if (!offModel.name.equals("NODATA")) {
//                itemView.msg.visibility = View.GONE
//                itemView.inner.visibility = View.VISIBLE
                itemView.txt_title.text = offModel.name
//            itemView.txt_name.text = ""
//            itemView.txt_validty.text = ""

//                itemView.txt_getnow.setOnClickListener(View.OnClickListener { })
//                itemView.img_share.setOnClickListener(View.OnClickListener { })
            } else {
//                itemView.msg.visibility = View.VISIBLE
//                itemView.inner.visibility = View.GONE
            }
            itemView.setOnClickListener{clickListener(offModel)}
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

                val filteredList = ArrayList<OfferModel>()

                for (bean in filterlist) {

                    if (bean.name.toLowerCase().contains(charString) || bean.name.toLowerCase().contains(charString) || bean.name.toLowerCase().contains(charString)) {
                        filteredList.add(bean)
                    }
                }

                if (filteredList.size == 0) {
                    var noDataBean = OfferModel()
                    noDataBean.name = "NODATA"
                    noDataBean.rollno = ""
                    filteredList.add(noDataBean)
                }

                lisData = filteredList
            }

            val filterResults = Filter.FilterResults()
            filterResults.values = lisData
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            lisData = results.values as ArrayList<OfferModel>
            notifyDataSetChanged()
        }
    }

}




