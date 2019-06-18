package com.virtual.customervendor.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.virtual.customervendor.R;
import com.virtual.customervendor.model.DayAviliability;

import java.util.ArrayList;

public class TimeManagerAdapter extends RecyclerView.Adapter<TimeManagerAdapter.ViewHolder> {
    private ArrayList<DayAviliability> aviliabilities;
    private SlotsListener listener;
    private boolean isMultiSlots;

    public TimeManagerAdapter(ArrayList<DayAviliability> aviliabilities, SlotsListener listener, boolean isMultiSlots) {
        this.aviliabilities = aviliabilities;
        this.listener = listener;
        this.isMultiSlots = isMultiSlots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_manage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayAviliability aviliability=aviliabilities.get(position);
        if(isMultiSlots)
            holder.btnAddMore.setVisibility(View.GONE);

        holder.cbDay.setChecked(aviliability.isSeleted());
        holder.cbDay.setText(aviliability.getName());

        SlotsAdapter adapter=new SlotsAdapter(aviliabilities.get(position).getSlots());
        LinearLayoutManager manager=new LinearLayoutManager(holder.itemView.getContext(),LinearLayoutManager.VERTICAL,false);
        holder.recyclerView.setLayoutManager(manager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return aviliabilities.size();
    }

    public interface SlotsListener{
        void onSlotSelection();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private AppCompatCheckBox cbDay;
        private RecyclerView recyclerView;
        private Button btnAddMore;
        public ViewHolder(View itemView) {
            super(itemView);
            cbDay=itemView.findViewById(R.id.cbDay);
            recyclerView=itemView.findViewById(R.id.recyclerView);
            btnAddMore=itemView.findViewById(R.id.btnAddMore);
        }
    }
}
