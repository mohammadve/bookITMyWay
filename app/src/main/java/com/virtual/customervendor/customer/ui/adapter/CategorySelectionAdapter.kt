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
import com.virtual.customervendor.model.StoreCategoryModel
import kotlinx.android.synthetic.main.fragment_dialog_customer_item.view.*

class CategorySelectionAdapter(val mContext: Context, val offermodel: ArrayList<StoreCategoryModel>, val clickListener: (StoreCategoryModel) -> Unit) : RecyclerView.Adapter<CategorySelectionAdapter.ResultItemViewHolder>(), Filterable {
    private var filterdata: FilterText = FilterText()

    var lisData: ArrayList<StoreCategoryModel> = offermodel
    var filterlist: ArrayList<StoreCategoryModel> = offermodel


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

    override fun getFilter(): Filter {
        if (filterdata == null) {
            filterdata = FilterText()
        }
        return filterdata
    }

    class ResultItemViewHolder(itemView: View, val clickListener: (StoreCategoryModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(mContext: Context, serviceModelList: ArrayList<StoreCategoryModel>, position: Int) {
            if (!serviceModelList.get(position).category_name.equals("NODATA")) {
//                itemView.msg.visibility = View.GONE
                itemView.txt_title.visibility = View.VISIBLE

                itemView.txt_title.text = serviceModelList.get(position).category_name
//            itemView.txt_name.text = ""
//            itemView.txt_validty.text = ""

//                itemView.txt_getnow.setOnClickListener(View.OnClickListener { })
//                itemView.img_share.setOnClickListener(View.OnClickListener { })
            } else {
//                itemView.msg.visibility = View.VISIBLE
                itemView.txt_title.visibility = View.GONE
            }
            itemView.setOnClickListener {
                //                itemView.txt_title.setOnCheckedChangeListener { buttonView, isChecked -> serviceModelList.get(position).isSelected = isChecked
                if (!serviceModelList.get(position).category_name.equals("NODATA")) {
                    clickListener(serviceModelList.get(position))
                }
            }

//            }
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

                val filteredList = ArrayList<StoreCategoryModel>()

                for (bean in filterlist) {

                    if (bean.category_name?.toLowerCase()?.contains(charString)!!) {
                        filteredList.add(bean)
                    }
                }

                if (filteredList.size == 0) {
                    var noDataBean = StoreCategoryModel()
                    noDataBean.category_name = "NODATA"
                    noDataBean.id = ""
                    filteredList.add(noDataBean)
                }

                lisData = filteredList
            }

            val filterResults = Filter.FilterResults()
            filterResults.values = lisData
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            lisData = results.values as ArrayList<StoreCategoryModel>
            notifyDataSetChanged()
        }
    }

}




