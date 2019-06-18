package com.virtual.customervendor.model;

import java.util.ArrayList;

public class DayAviliability {
    private String name;
    private boolean isSeleted;
    private ArrayList<TimeSlot> slots=new ArrayList();

    public DayAviliability(String name, boolean isSeleted) {
        this.name = name;
        this.isSeleted = isSeleted;
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
        if(slots.size()<=0)
            slots.add(new TimeSlot(""," "," "));
        return slots;
    }

    public void setSlots(ArrayList<TimeSlot> slots) {
        this.slots = slots;
    }

    public class TimeSlot{

        private String startTime,stopTime,description;

        public TimeSlot(String startTime, String stopTime, String description) {
            this.startTime = startTime;
            this.stopTime = stopTime;
            this.description = description;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
