package com.resort.resortapp.Models;

import java.util.ArrayList;
import java.util.List;

public class EditHistoryModel {

    String editId, bookingId, time, changes, user;


    public EditHistoryModel(String editId, String bookingId, String time, String changes, String user) {
        this.editId = editId;
        this.bookingId = bookingId;
        this.time = time;
        this.changes = changes;
        this.user = user;
    }

}
