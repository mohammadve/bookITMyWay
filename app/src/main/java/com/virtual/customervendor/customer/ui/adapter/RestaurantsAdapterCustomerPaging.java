package com.virtual.customervendor.customer.ui.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.virtual.customervendor.R;
import com.virtual.customervendor.customer.ui.activity.RestaurantActivity;
import com.virtual.customervendor.customview.CustomTextView;
import com.virtual.customervendor.model.CustomerBookingListModel;

import java.util.ArrayList;

public class RestaurantsAdapterCustomerPaging extends PagedListAdapter<CustomerBookingListModel, RestaurantsAdapterCustomerPaging.ViewHolder> {
    private Context context;

    public RestaurantsAdapterCustomerPaging() {
        super(DIFF_CALLBACK);
//        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customerbookinglist_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        CustomerBookingListModel customerBooking=getItem(position);
        holder.txtDesc.setText(customerBooking.getDescription());
        holder.txtBusiness.setText(customerBooking.getBusiness_name());
        holder.txtCity.setText(customerBooking.getCityname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.itemView.getContext(), RestaurantActivity.class);
//                intent.putExtra("",customerBooking);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    private static DiffUtil.ItemCallback<CustomerBookingListModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CustomerBookingListModel>() {
                @Override
                public boolean areItemsTheSame(CustomerBookingListModel oldItem, CustomerBookingListModel newItem) {
                    return oldItem.getBusiness_id().equals(newItem.getBusiness_id());
                }

                @Override
                public boolean areContentsTheSame(CustomerBookingListModel oldItem, CustomerBookingListModel newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class ViewHolder extends RecyclerView.ViewHolder{
        CustomTextView txtBusiness,txtDesc,txtCity;
        ImageView imgBusiness;
        public ViewHolder(View itemView) {
            super(itemView);
            txtBusiness=itemView.findViewById(R.id.txtBusiness);
//            txtDesc=itemView.findViewById(R.id.txtDesc);
//            txtCity=itemView.findViewById(R.id.txtCity);
            imgBusiness=itemView.findViewById(R.id.imgBusiness);
        }
    }
}
