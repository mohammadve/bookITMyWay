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
import com.virtual.customervendor.listener.PagingListeners
import com.virtual.customervendor.model.CustomerOrderModel
import com.virtual.customervendor.utills.AppConstants
import kotlinx.android.synthetic.main.fragment_orders_customer_item.view.*

class OrderadapterCustomer(val pagingListener: PagingListeners, val mContext: Context, val offermodel: ArrayList<CustomerOrderModel>, val clickListener: (CustomerOrderModel) -> Unit) : RecyclerView.Adapter<OrderadapterCustomer.ResultItemViewHolder>(), Filterable {
    override fun getFilter(): Filter {
        if (filterdata == null) {
            filterdata = FilterText()
        }
        return filterdata
    }

    var lisData: ArrayList<CustomerOrderModel> = offermodel
    val filterlist: ArrayList<CustomerOrderModel> = offermodel
    private var filterdata: FilterText = FilterText()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_orders_customer_item, parent, false)
        return ResultItemViewHolder(view, clickListener)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, lisData[position])
        if (position == (lisData.size - 1)) {
            pagingListener.onFinishListener();
        }
    }

    override fun getItemCount(): Int {
        return lisData.size
    }

    class ResultItemViewHolder(itemView: View, val clickListener: (CustomerOrderModel) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, offModel: CustomerOrderModel) {
            if (offModel.order_id != null) {
                itemView.visibility = View.VISIBLE
                itemView.ordernumber.text = offModel.order_id
                itemView.businessname.text = offModel.business_name

//                itemView.datetime.text = offModel.serviceinformation.book_date

                handleDate(offModel)

                if (offModel.complete_status == (AppConstants.ORDERSTATUS_COMPLETED)) {
                    itemView.orderstatus.text = AppConstants.STATUS_COMPLETED
                } else if (offModel.complete_status == (AppConstants.ORDERSTATUS_PENDING)) {
                    itemView.orderstatus.text = AppConstants.STATUS_PENDING
                }else if (offModel.complete_status == (AppConstants.ORDERSTATUS_CANCELLED)) {
                    itemView.orderstatus.text = AppConstants.STATUS_CANCELLED
                }
                itemView.setOnClickListener { clickListener(offModel) }
            } else {
                itemView.visibility = View.GONE
            }
        }


        fun handleDate(offModel: CustomerOrderModel) {
            when (offModel.category_id?.toInt()) {
                AppConstants.CAT_TRANSPORTATION -> {
                    when (offModel.subcategory_id?.toInt()) {
                        AppConstants.SUBCAT_TRANS_TAXI -> itemView.datetime.text = offModel.serviceinformation.from_date
                        AppConstants.SUBCAT_TRANS_LIMO -> itemView.datetime.text = offModel.serviceinformation.from_date
                        AppConstants.SUBCAT_TRANS_TOURBUS -> itemView.datetime.text = offModel.serviceinformation.from_date
                        AppConstants.SUBCAT_TRANS_SIGHTSEEING -> itemView.datetime.text = offModel.serviceinformation.booking_date
                    }
                }
                AppConstants.CAT_RESTAURANT_DINNIG -> {
                    when (offModel.subcategory_id?.toInt()) {
                        AppConstants.SUBCAT_RESTAURANT_DINNIG -> itemView.datetime.text = offModel.serviceinformation.book_date
                    }
                }
                AppConstants.CAT_HEALTH_BODYCARE -> {
                    when (offModel.subcategory_id?.toInt()) {
                        AppConstants.SUBCAT_HEALTH_DOCTOR -> itemView.datetime.text = offModel.serviceinformation.book_date
                        AppConstants.SUBCAT_HEALTH_HAIR -> itemView.datetime.text = offModel.serviceinformation.book_date
                        AppConstants.SUBCAT_HEALTH_NAIL -> itemView.datetime.text = offModel.serviceinformation.book_date
                        AppConstants.SUBCAT_HEALTH_PHYSIO -> itemView.datetime.text = offModel.serviceinformation.book_date
                    }
                }
                AppConstants.CAT_PARKING_VALET -> {
                    when (offModel.subcategory_id?.toInt()) {
                        AppConstants.SUBCAT_PARKING_VALET -> itemView.datetime.text = offModel.serviceinformation.from_date
                    }
                }
                AppConstants.CAT_EVENT_TICKET -> {
                    when (offModel.subcategory_id?.toInt()) {
                        AppConstants.SUBCAT_EVENT_TICKET -> itemView.datetime.text = offModel.serviceinformation.book_date
                    }
                }
                AppConstants.CAT_STORE_SELLER -> {
                    when (offModel.subcategory_id?.toInt()) {
                        AppConstants.SUBCAT_STORE_SELLER -> {
                            itemView.datetime.text = ""
                        }
                    }
                }

            }
        }

    }

    private inner class FilterText : Filter() {

        override fun convertResultToString(resultValue: Any): String? {
            return (resultValue as CustomerOrderModel).order_id
        }

        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
            val charString = constraint.toString()
            if (charString.isEmpty()) {
                lisData = filterlist
            } else {
                val filteredList = ArrayList<CustomerOrderModel>()
                for (bean in filterlist) {
                    if (bean.order_id?.toLowerCase()?.contains(charString)!! || bean.business_name?.toLowerCase()?.contains(charString)!! || bean.order_id?.toLowerCase()?.contains(charString)!!) {
                        filteredList.add(bean)
                    }
                }

                if (filteredList.size == 0) {
                    var noDataBean = CustomerOrderModel()
//                    noDataBean.cityname = "NODATA"
//                    noDataBean.cityid = ""
                    filteredList.add(noDataBean)
                }

                lisData = filteredList
            }

            val filterResults = Filter.FilterResults()
            filterResults.values = lisData
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            lisData = results.values as ArrayList<CustomerOrderModel>
            notifyDataSetChanged()
        }
    }


}




