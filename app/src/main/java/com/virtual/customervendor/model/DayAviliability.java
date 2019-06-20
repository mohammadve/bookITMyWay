package com.virtual.customervendor.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DayAviliability implements Serializable {
    private String name;
    private boolean isSeleted;
    private ArrayList<TimeSlot> slots;

    public DayAviliability(String name, boolean isSeleted) {
        this.name = name;
        this.isSeleted = isSeleted;
    }
    public DayAviliability(String name, boolean isSeleted,ArrayList<TimeSlot> slots) {
        this.name = name;
        this.isSeleted = isSeleted;
        this.slots = slots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }

    public ArrayList<TimeSlot> getSlots() {
        if(slots==null)
            slots=new ArrayList<>();
        if(slots.size()<=0)
            slots.add(new TimeSlot("",""));
        return slots;
    }
    public void addSlot(String startTime,String stopTime){
        slots.add(new TimeSlot(startTime,stopTime));
    }

    public void setSlots(ArrayList<TimeSlot> slots) {
        this.slots = slots;
    }

    public class TimeSlot implements Serializable{

        private String startTime,stopTime;

        public TimeSlot(String startTime, String stopTime) {
            this.startTime = startTime;
            this.stopTime = stopTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStopTime() {
            return stopTime;
        }

        public void setStopTime(String stopTime) {
            this.stopTime = stopTime;
        }
    }
}
