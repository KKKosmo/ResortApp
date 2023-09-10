package com.resort.resortapp.Models;

import static com.resort.resortapp.Models.DayModel.*;

public class RoomedDateModel {

    int gridSlot;
    boolean withinMonth;
    int gridDate;

    public RoomedDateModel(int i, int j){
        gridSlot = (7*i)+(j+1);
        gridDate = gridSlot - dateOffset;
        withinMonth = gridSlot > dateOffset && gridDate <= monthMaxDate;
    }


    public int getGridSlot() {
        return gridSlot;
    }

    public void setGridSlot(int gridSlot) {
        this.gridSlot = gridSlot;
    }

    public boolean isWithinMonth() {
        return withinMonth;
    }

    public void setWithinMonth(boolean withinMonth) {
        this.withinMonth = withinMonth;
    }

    public int getGridDate() {
        return gridDate;
    }

    public void setGridDate(int gridDate) {
        this.gridDate = gridDate;
    }
}