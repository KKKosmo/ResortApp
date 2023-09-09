package com.resort.resortapp.Models;

public enum Rooms{
    ALL_ROOMS,
    ROOM_J,
    ROOM_G,
    ATTIC,
    KUBO_1,
    KUBO_2;

    static
    public final Rooms[] values = values();

    public Rooms prev() {
        return values[(ordinal() - 1  + values.length) % values.length];
    }

    public Rooms next() {
        return values[(ordinal() + 1) % values.length];
    }
    public String getDisplayName() {
        return this.name().replace("_", " ");
    }
}