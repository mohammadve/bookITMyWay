package com.virtual.customervendor.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.virtual.customervendor.R;
import com.virtual.customervendor.customview.CustomEditText;
import com.virtual.customervendor.model.DayAviliability;

import java.util.ArrayList;

public class SlotsAdapter extends RecyclerView.Adapter<SlotsAdapter.ViewHolder>{
    private ArrayList<DayAviliability.TimeSlot> slots;
    public SlotsAdapter(ArrayList<DayAviliability.TimeSlot> slots) {
        this.slots = slots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slots,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CustomEditText edtStartTime,edtClosingTime;
        public ViewHolder(View itemView) {
            super(itemView);
            edtStartTime=itemView.findViewById(R.id.edtStartTime);
            edtClosingTime=itemView.findViewById(R.id.edtClosingTime);
        }
    }

}
