package com.virtual.customervendor.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DayAviliability implements Serializable {
    private String day;
    private boolean dayValue;
    private ArrayList<TimeSlot> timeSlot;

    public DayAviliability(String name, boolean isSeleted) {
        this.day = name;
        this.dayValue = isSeleted;
    }
    public DayAviliability(String name, boolean isSeleted,ArrayList<TimeSlot> slots) {
        this.day = name;
        this.dayValue = isSeleted;
        this.timeSlot = slots;
    }

    public String getName() {
        return day;
    }

    public void setName(String name) {
        this.day = name;
    }

    public boolean isSeleted() {
        return dayValue;
    }

    public void setSeleted(boolean seleted) {
        dayValue = seleted;
    }

    public ArrayList<TimeSlot> getSlots() {
        if(timeSlot ==null)
            timeSlot =new ArrayList<>();
        if(timeSlot.size()<=0)
            timeSlot.add(new TimeSlot("",""));
        return timeSlot;
    }
    public void addSlot(String startTime,String stopTime){
        timeSlot.add(new TimeSlot(startTime,stopTime));
    }

    public void setSlots(ArrayList<TimeSlot> slots) {
        this.timeSlot = slots;
    }

    public class TimeSlot implements Serializable{

        private String start_time,end_time;
        public TimeSlot(String startTime, String stopTime) {
            this.start_time = startTime;
            this.end_time = stopTime;
        }

        public String getStartTime() {
            if(start_time==null)
                start_time="";
            return start_time;
        }

        public void setStartTime(String startTime) {
            this.start_time = startTime;
        }

        public String getStopTime() {
            if(end_time==null)
                end_time="";
            return end_time;
        }

        public void setStopTime(String stopTime) {
            this.end_time = stopTime;
        }

        @Override
        public String toString() {
            return "{ end_time "+ end_time+" start_time" +start_time+"}";
        }
    }

    @Override
    public String toString() {
        return "{ day ="+day+", dayValue="+dayValue+", timeSlot ="+timeSlot.toString()+"},";
    }
}
