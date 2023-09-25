package com.resort.resortapp.Models;

import java.time.LocalDateTime;

public class EditHistoryModel {

    private int editId;
    private int bookingId;
    private String time;
    private String changes;
    private String user;

    public EditHistoryModel(int editId, int bookingId, String time, String changes, String user) {
        this.editId = editId;
        this.bookingId = bookingId;
        this.time = time;
        this.changes = changes;
        this.user = user;
    }

    public int getEditId() {
        return editId;
    }

    public void setEditId(int editId) {
        this.editId = editId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
