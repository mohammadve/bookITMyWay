package com.virtual.customervendor.vendor.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.virtual.customervendor.R;
import com.virtual.customervendor.model.DayAviliability;

import java.util.ArrayList;

public class ViewTimeSlotsAdapter extends RecyclerView.Adapter<ViewTimeSlotsAdapter.ViewHolder>  {
private ArrayList<DayAviliability> aviliabilities;

    public ViewTimeSlotsAdapter(ArrayList<DayAviliability> aviliabilities) {
        this.aviliabilities = aviliabilities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_slots,parent,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return aviliabilities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
