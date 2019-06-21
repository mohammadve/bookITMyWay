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
import android.widget.CompoundButton;
import com.virtual.customervendor.R;
import com.virtual.customervendor.model.DayAviliability;
import com.virtual.customervendor.utills.UiValidator;

import java.util.ArrayList;

public class TimeManagerAdapter extends RecyclerView.Adapter<TimeManagerAdapter.ViewHolder> implements SlotsAdapter.SameSlotsObserver {
    private ArrayList<DayAviliability> aviliabilities;
    private boolean isMultiSlots,isSameSlots;

    public TimeManagerAdapter(ArrayList<DayAviliability> aviliabilities, boolean isMultiSlots, boolean isSameSlots) {
        this.aviliabilities = aviliabilities;
        this.isMultiSlots = isMultiSlots;
        this.isSameSlots = isSameSlots;
    }

    public void setSameSlots(boolean sameSlots) {
        isSameSlots = sameSlots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_manage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final DayAviliability aviliability=aviliabilities.get(position);
        if(isMultiSlots)
            holder.btnAddMore.setVisibility(View.VISIBLE);
        else
            holder.btnAddMore.setVisibility(View.GONE);

        holder.cbDay.setChecked(aviliability.isSeleted());
        holder.cbDay.setText(aviliability.getName());

        final SlotsAdapter adapter=new SlotsAdapter(aviliabilities.get(position).getSlots(),isSameSlots,this);
        LinearLayoutManager manager=new LinearLayoutManager(holder.itemView.getContext(),LinearLayoutManager.VERTICAL,false);
        holder.recyclerView.setLayoutManager(manager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(adapter);

//        if(aviliability.isSeleted()){
//            holder.recyclerView.setVisibility(View.VISIBLE);
//            if(isMultiSlots)
//                holder.btnAddMore.setVisibility(View.VISIBLE);
//            else
//                holder.btnAddMore.setVisibility(View.GONE);
//        }else {
//            holder.btnAddMore.setVisibility(View.GONE);
//            holder.recyclerView.setVisibility(View.GONE);
//        }
        toggleViews(aviliability.isSeleted(),holder);

        holder.cbDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isSameSlots && !isChecked)
                    isChecked=true;
                holder.cbDay.setChecked(isChecked);
                aviliability.setSeleted(isChecked);
                toggleViews(isChecked,holder);
//                if(isChecked){
//                    if(isMultiSlots)
//                        holder.btnAddMore.setVisibility(View.VISIBLE);
//                    else
//                        holder.btnAddMore.setVisibility(View.GONE);
//
//                    holder.recyclerView.setVisibility(View.VISIBLE);
//                }else {
//                    holder.btnAddMore.setVisibility(View.GONE);
//                    holder.recyclerView.setVisibility(View.GONE);
//                }
            }
        });
        holder.btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index=aviliability.getSlots().size()-1;
                if(aviliability.getSlots().get(index).getStartTime()!="" && aviliability.getSlots().get(index).getStopTime()!="") {
                    aviliability.addSlot("", "");
                    if (isSameSlots)
                        notifyDataSetChanged();
                    else
                        adapter.notifyDataSetChanged();
                }else
                    UiValidator.displayMsg(holder.itemView.getContext(),"Please select slot first!");
            }
        });


    }
    private void toggleViews(boolean isShow, ViewHolder holder){
        if(isShow){
            if(isMultiSlots)
                holder.btnAddMore.setVisibility(View.VISIBLE);
            else
                holder.btnAddMore.setVisibility(View.GONE);

            holder.recyclerView.setVisibility(View.VISIBLE);
        }else {
            holder.btnAddMore.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return aviliabilities.size();
    }

    @Override
    public void notifyAllDays() {
        notifyDataSetChanged();
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
