package com.resort.resortapp.Models;

public class EditHistoryModel {

    private final int editId;
    private final int bookingId;
    private final String time;
    private final String changes;
    private final String user;

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

    public int getBookingId() {
        return bookingId;
    }

    public String getChanges() {
        return changes;
    }

    public String getUser() {
        return user;
    }

    public String getTime() {
        return time;
    }
}
