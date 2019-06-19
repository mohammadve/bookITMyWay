package com.virtual.customervendor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.virtual.customervendor.R;
import com.virtual.customervendor.customview.CustomEditText;
import com.virtual.customervendor.listener.TimeListener;
import com.virtual.customervendor.model.DayAviliability;
import com.virtual.customervendor.utills.AppUtils;
import com.virtual.customervendor.utills.UiValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Context context=holder.itemView.getContext();
        final DayAviliability.TimeSlot timeSlot=slots.get(position);
        holder.edtStartTime.setText(timeSlot.getStartTime());
        holder.edtClosingTime.setText(timeSlot.getStopTime());

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slots.remove(timeSlot);
                notifyDataSetChanged();
            }
        });

        holder.edtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.getTimeNew(context, new TimeListener() {
                    @Override
                    public void onTimeSelect(String time) {
                        timeSlot.setStartTime(time);
                        if(isValidSlot(timeSlot,context))
                            if(slots.size()>1) {
                                if(!AppUtils.validateTimeSlot(slots, timeSlot, true)){
                                    time="";
                                    timeSlot.setStartTime(time);
                                    UiValidator.displayMsg(context,"Please select a valid slot");
                                }
                            }
                        holder.edtStartTime.setText(time);
                    }
                });
            }
        });
        holder.edtClosingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.getTimeNew(holder.itemView.getContext(), new TimeListener() {
                    @Override
                    public void onTimeSelect(String time) {
                        timeSlot.setStopTime(time);
                        if(isValidSlot(timeSlot,context))
                            if(slots.size()>1) {
                                if(!AppUtils.validateTimeSlot(slots, timeSlot, false)) {
                                    time = "";
                                    timeSlot.setStopTime(time);
                                    UiValidator.displayMsg(context,"Please select a valid slot");
                                }
                            }
                        holder.edtClosingTime.setText(time);
                    }
                });
            }
        });
    }
    private boolean isValidSlot(DayAviliability.TimeSlot timeSlot , Context context){// check weather start time is not greater then stop time
        SimpleDateFormat format=new SimpleDateFormat("hh:mm");
        if(timeSlot.getStartTime().length()>1 && timeSlot.getStopTime().length()>1){
            try {
                Date dateTimeStart=format.parse(timeSlot.getStartTime());
                Date dateTimeStop=format.parse(timeSlot.getStopTime());
                if(dateTimeStart.compareTo(dateTimeStop)>0 ||dateTimeStart.compareTo(dateTimeStop)==0) {
                    UiValidator.displayMsg(context,"Please select a valid slot");
                    timeSlot.setStopTime("");
                    timeSlot.setStartTime("");
                    notifyDataSetChanged();
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CustomEditText edtStartTime,edtClosingTime;
        private ImageView imgRemove;
        public ViewHolder(View itemView) {
            super(itemView);
            edtStartTime=itemView.findViewById(R.id.edtStartTime);
            edtClosingTime=itemView.findViewById(R.id.edtClosingTime);
            imgRemove=itemView.findViewById(R.id.imgRemove);
        }
    }

}
